/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.exportimport.internal.background.task;

import com.liferay.exportimport.kernel.lar.ExportImportHelperUtil;
import com.liferay.exportimport.kernel.lar.ExportImportThreadLocal;
import com.liferay.exportimport.kernel.lar.MissingReferences;
import com.liferay.exportimport.kernel.lifecycle.ExportImportLifecycleManagerUtil;
import com.liferay.exportimport.kernel.lifecycle.constants.ExportImportLifecycleConstants;
import com.liferay.exportimport.kernel.model.ExportImportConfiguration;
import com.liferay.exportimport.kernel.service.ExportImportLocalServiceUtil;
import com.liferay.exportimport.kernel.staging.StagingUtil;
import com.liferay.portal.kernel.backgroundtask.BackgroundTask;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskExecutor;
import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.exception.NoSuchLayoutException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.model.LayoutConstants;
import com.liferay.portal.kernel.security.auth.HttpPrincipal;
import com.liferay.portal.kernel.service.LayoutLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portlet.exportimport.service.http.StagingServiceHttp;

import java.io.File;
import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Máté Thurzó
 */
public class LayoutRemoteStagingBackgroundTaskExecutor
	extends BaseStagingBackgroundTaskExecutor {

	public LayoutRemoteStagingBackgroundTaskExecutor() {
		setBackgroundTaskStatusMessageTranslator(
			new LayoutStagingBackgroundTaskStatusMessageTranslator());
	}

	@Override
	public BackgroundTaskExecutor clone() {
		LayoutRemoteStagingBackgroundTaskExecutor
			layoutRemoteStagingBackgroundTaskExecutor =
				new LayoutRemoteStagingBackgroundTaskExecutor();

		layoutRemoteStagingBackgroundTaskExecutor.
			setBackgroundTaskStatusMessageTranslator(
				getBackgroundTaskStatusMessageTranslator());
		layoutRemoteStagingBackgroundTaskExecutor.setIsolationLevel(
			getIsolationLevel());

		return layoutRemoteStagingBackgroundTaskExecutor;
	}

	@Override
	public BackgroundTaskResult execute(BackgroundTask backgroundTask) {
		ExportImportConfiguration exportImportConfiguration =
			getExportImportConfiguration(backgroundTask);

		clearBackgroundTaskStatus(backgroundTask);

		Thread currentThread = Thread.currentThread();

		ClassLoader contextClassLoader = currentThread.getContextClassLoader();

		File file = null;
		HttpPrincipal httpPrincipal = null;
		MissingReferences missingReferences = null;
		long stagingRequestId = 0L;

		try {
			currentThread.setContextClassLoader(
				PortalClassLoaderUtil.getClassLoader());

			ExportImportThreadLocal.setLayoutStagingInProcess(true);

			ExportImportLifecycleManagerUtil.fireExportImportLifecycleEvent(
				ExportImportLifecycleConstants.
					EVENT_PUBLICATION_LAYOUT_REMOTE_STARTED,
				ExportImportLifecycleConstants.
					PROCESS_FLAG_LAYOUT_STAGING_IN_PROCESS,
				String.valueOf(
					exportImportConfiguration.getExportImportConfigurationId()),
				exportImportConfiguration);

			Map<String, Serializable> settingsMap =
				exportImportConfiguration.getSettingsMap();

			long sourceGroupId = MapUtil.getLong(settingsMap, "sourceGroupId");
			boolean privateLayout = MapUtil.getBoolean(
				settingsMap, "privateLayout");

			initThreadLocals(sourceGroupId, privateLayout);

			Map<Long, Boolean> layoutIdMap =
				(Map<Long, Boolean>)settingsMap.get("layoutIdMap");
			long targetGroupId = MapUtil.getLong(settingsMap, "targetGroupId");

			Map<String, Serializable> taskContextMap =
				backgroundTask.getTaskContextMap();

			httpPrincipal = (HttpPrincipal)taskContextMap.get("httpPrincipal");

			file = exportLayoutsAsFile(
				exportImportConfiguration, layoutIdMap, targetGroupId,
				httpPrincipal);

			String checksum = FileUtil.getMD5Checksum(file);

			stagingRequestId = StagingServiceHttp.createStagingRequest(
				httpPrincipal, targetGroupId, checksum);

			StagingUtil.transferFileToRemoteLive(
				file, stagingRequestId, httpPrincipal);

			markBackgroundTask(
				backgroundTask.getBackgroundTaskId(), "exported");

			missingReferences = StagingServiceHttp.publishStagingRequest(
				httpPrincipal, stagingRequestId, exportImportConfiguration);

			deleteExportedChangesetEntries();

			ExportImportThreadLocal.setLayoutStagingInProcess(false);

			ExportImportLifecycleManagerUtil.fireExportImportLifecycleEvent(
				ExportImportLifecycleConstants.
					EVENT_PUBLICATION_LAYOUT_REMOTE_SUCCEEDED,
				ExportImportLifecycleConstants.
					PROCESS_FLAG_LAYOUT_STAGING_IN_PROCESS,
				String.valueOf(
					exportImportConfiguration.getExportImportConfigurationId()),
				exportImportConfiguration);

			ServiceContext serviceContext =
				ServiceContextThreadLocal.getServiceContext();

			ExportImportHelperUtil.processBackgroundTaskManifestSummary(
				serviceContext.getUserId(), sourceGroupId, backgroundTask,
				file);
		}
		catch (Throwable throwable) {
			ExportImportThreadLocal.setLayoutStagingInProcess(false);

			ExportImportLifecycleManagerUtil.fireExportImportLifecycleEvent(
				ExportImportLifecycleConstants.
					EVENT_PUBLICATION_LAYOUT_REMOTE_FAILED,
				ExportImportLifecycleConstants.
					PROCESS_FLAG_LAYOUT_STAGING_IN_PROCESS,
				String.valueOf(
					exportImportConfiguration.getExportImportConfigurationId()),
				exportImportConfiguration);

			deleteTempLarOnFailure(file);

			throw new SystemException(throwable);
		}
		finally {
			currentThread.setContextClassLoader(contextClassLoader);

			if ((stagingRequestId > 0) && (httpPrincipal != null)) {
				try {
					StagingServiceHttp.cleanUpStagingRequest(
						httpPrincipal, stagingRequestId);
				}
				catch (PortalException portalException) {
					if (_log.isWarnEnabled()) {
						_log.warn(
							"Unable to clean up the remote live site",
							portalException);
					}
				}
			}
		}

		deleteTempLarOnSuccess(file);

		return processMissingReferences(
			backgroundTask.getBackgroundTaskId(), missingReferences);
	}

	protected File exportLayoutsAsFile(
			ExportImportConfiguration exportImportConfiguration,
			Map<Long, Boolean> layoutIdMap, long remoteGroupId,
			HttpPrincipal httpPrincipal)
		throws PortalException {

		List<Layout> layouts = new ArrayList<>();

		if (layoutIdMap != null) {
			Set<Map.Entry<Long, Boolean>> entrySet = layoutIdMap.entrySet();

			for (Map.Entry<Long, Boolean> entry : entrySet) {
				long plid = GetterUtil.getLong(String.valueOf(entry.getKey()));

				Layout layout = null;

				try {
					layout =
						ExportImportHelperUtil.getLayoutOrCreateDummyRootLayout(
							plid);
				}
				catch (NoSuchLayoutException noSuchLayoutException) {

					// See LPS-36174

					if (_log.isDebugEnabled()) {
						_log.debug(
							noSuchLayoutException, noSuchLayoutException);
					}

					entrySet.remove(plid);

					continue;
				}

				if (!layouts.contains(layout)) {
					layouts.add(layout);
				}

				if (layout.getPlid() == LayoutConstants.DEFAULT_PLID) {
					continue;
				}

				List<Layout> parentLayouts = getMissingRemoteParentLayouts(
					httpPrincipal, layout, remoteGroupId);

				for (Layout parentLayout : parentLayouts) {
					if (!layouts.contains(parentLayout)) {
						layouts.add(parentLayout);
					}
				}

				boolean includeChildren = entry.getValue();

				if (includeChildren) {
					for (Layout childLayout : layout.getAllChildren()) {
						if (!layouts.contains(childLayout)) {
							layouts.add(childLayout);
						}
					}
				}
			}
		}

		long[] layoutIds = ExportImportHelperUtil.getLayoutIds(layouts);

		Map<String, Serializable> settingsMap =
			exportImportConfiguration.getSettingsMap();

		settingsMap.remove("layoutIdMap");

		settingsMap.put("layoutIds", layoutIds);

		return ExportImportLocalServiceUtil.exportLayoutsAsFile(
			exportImportConfiguration);
	}

	/**
	 * @see com.liferay.portal.lar.ExportImportHelperImpl#getMissingParentLayouts(
	 *      Layout, long)
	 */
	protected List<Layout> getMissingRemoteParentLayouts(
			HttpPrincipal httpPrincipal, Layout layout, long remoteGroupId)
		throws PortalException {

		List<Layout> missingRemoteParentLayouts = new ArrayList<>();

		long parentLayoutId = layout.getParentLayoutId();

		while (parentLayoutId > 0) {
			Layout parentLayout = LayoutLocalServiceUtil.getLayout(
				layout.getGroupId(), layout.isPrivateLayout(), parentLayoutId);

			if (StagingServiceHttp.hasRemoteLayout(
					httpPrincipal, parentLayout.getUuid(), remoteGroupId,
					parentLayout.isPrivateLayout())) {

				// If one parent is found, all others are assumed to exist

				break;
			}

			missingRemoteParentLayouts.add(parentLayout);

			parentLayoutId = parentLayout.getParentLayoutId();
		}

		return missingRemoteParentLayouts;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		LayoutRemoteStagingBackgroundTaskExecutor.class);

}