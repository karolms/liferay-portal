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

package com.liferay.portal.verify;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.permission.ResourceActionsUtil;
import com.liferay.portal.kernel.service.ResourceActionLocalServiceUtil;
import com.liferay.portal.kernel.util.LoggingTimer;

import java.util.List;

/**
 * @author Tobias Kaefer
 * @author Douglas Wong
 * @author Matthew Kong
 * @author Raymond Augé
 */
public class VerifyPermission extends VerifyProcess {

	protected void checkPermissions() throws Exception {
		try (LoggingTimer loggingTimer = new LoggingTimer()) {
			List<String> modelNames = ResourceActionsUtil.getModelNames();

			for (String modelName : modelNames) {
				List<String> actionIds =
					ResourceActionsUtil.getModelResourceActions(modelName);

				ResourceActionLocalServiceUtil.checkResourceActions(
					modelName, actionIds, true);
			}

			List<String> portletNames = ResourceActionsUtil.getPortletNames();

			for (String portletName : portletNames) {
				List<String> actionIds =
					ResourceActionsUtil.getPortletResourceActions(portletName);

				ResourceActionLocalServiceUtil.checkResourceActions(
					portletName, actionIds, true);
			}
		}
	}

	@Override
	protected void doVerify() throws Exception {
		checkPermissions();
	}

}