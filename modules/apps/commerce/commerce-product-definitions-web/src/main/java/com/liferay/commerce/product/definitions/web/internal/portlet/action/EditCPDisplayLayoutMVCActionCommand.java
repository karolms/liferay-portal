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

package com.liferay.commerce.product.definitions.web.internal.portlet.action;

import com.liferay.commerce.product.constants.CPPortletKeys;
import com.liferay.commerce.product.exception.CPDisplayLayoutEntryException;
import com.liferay.commerce.product.exception.CPDisplayLayoutLayoutUuidException;
import com.liferay.commerce.product.exception.NoSuchCPDefinitionException;
import com.liferay.commerce.product.exception.NoSuchCPDisplayLayoutException;
import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CPDisplayLayoutService;
import com.liferay.commerce.product.service.CommerceChannelService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.security.auth.PrincipalException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.StringUtil;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Alessio Antonio Rendina
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + CPPortletKeys.COMMERCE_CHANNELS,
		"mvc.command.name=editProductDisplayLayout"
	},
	service = MVCActionCommand.class
)
public class EditCPDisplayLayoutMVCActionCommand extends BaseMVCActionCommand {

	protected void deleteCPDisplayLayouts(ActionRequest actionRequest)
		throws Exception {

		long[] deleteCPDisplayLayoutIds = null;

		long cpDisplayLayoutId = ParamUtil.getLong(
			actionRequest, "cpDisplayLayoutId");

		if (cpDisplayLayoutId > 0) {
			deleteCPDisplayLayoutIds = new long[] {cpDisplayLayoutId};
		}
		else {
			deleteCPDisplayLayoutIds = StringUtil.split(
				ParamUtil.getString(actionRequest, "deleteCPDisplayLayoutIds"),
				0L);
		}

		for (long deleteCPDisplayLayoutId : deleteCPDisplayLayoutIds) {
			_cpDisplayLayoutService.deleteCPDisplayLayout(
				deleteCPDisplayLayoutId);
		}
	}

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		String cmd = ParamUtil.getString(actionRequest, Constants.CMD);

		try {
			if (cmd.equals(Constants.ADD) || cmd.equals(Constants.UPDATE)) {
				updateCPDisplayLayout(actionRequest);
			}
			else if (cmd.equals(Constants.DELETE)) {
				deleteCPDisplayLayouts(actionRequest);
			}
		}
		catch (Exception e) {
			if (e instanceof NoSuchCPDisplayLayoutException ||
				e instanceof PrincipalException) {

				SessionErrors.add(actionRequest, e.getClass());

				actionResponse.setRenderParameter("mvcPath", "/error.jsp");
			}
			else if (e instanceof CPDisplayLayoutEntryException ||
					 e instanceof CPDisplayLayoutLayoutUuidException ||
					 e instanceof NoSuchCPDefinitionException) {

				hideDefaultErrorMessage(actionRequest);

				SessionErrors.add(actionRequest, e.getClass());

				actionResponse.setRenderParameter(
					"mvcRenderCommandName", "editProductDisplayLayout");
			}
			else {
				_log.error(e, e);

				throw e;
			}
		}
	}

	protected void updateCPDisplayLayout(ActionRequest actionRequest)
		throws PortalException {

		long cpDisplayLayoutId = ParamUtil.getLong(
			actionRequest, "cpDisplayLayoutId");

		long classPK = ParamUtil.getLong(actionRequest, "classPK");
		String layoutUuid = ParamUtil.getString(actionRequest, "layoutUuid");

		if (cpDisplayLayoutId > 0) {
			_cpDisplayLayoutService.updateCPDisplayLayout(
				cpDisplayLayoutId, layoutUuid);
		}
		else {
			long commerceChannelId = ParamUtil.getLong(
				actionRequest, "commerceChannelId");

			CommerceChannel commerceChannel =
				_commerceChannelService.getCommerceChannel(commerceChannelId);

			_cpDisplayLayoutService.addCPDisplayLayout(
				_portal.getUserId(actionRequest),
				commerceChannel.getSiteGroupId(), CPDefinition.class, classPK,
				layoutUuid);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		EditCPDisplayLayoutMVCActionCommand.class);

	@Reference
	private CommerceChannelService _commerceChannelService;

	@Reference
	private CPDisplayLayoutService _cpDisplayLayoutService;

	@Reference
	private Portal _portal;

}