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

package com.liferay.external.reference.service;

import aQute.bnd.annotation.ProviderType;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import org.osgi.util.tracker.ServiceTracker;

/**
 * Provides the local service utility for EROrganization. This utility wraps
 * {@link com.liferay.external.reference.service.impl.EROrganizationLocalServiceImpl} and is the
 * primary access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author Brian Wing Shun Chan
 * @see EROrganizationLocalService
 * @see com.liferay.external.reference.service.base.EROrganizationLocalServiceBaseImpl
 * @see com.liferay.external.reference.service.impl.EROrganizationLocalServiceImpl
 * @generated
 */
@ProviderType
public class EROrganizationLocalServiceUtil {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to {@link com.liferay.external.reference.service.impl.EROrganizationLocalServiceImpl} and rerun ServiceBuilder to regenerate this class.
	 */
	public static com.liferay.portal.kernel.model.Organization addOrUpdateOrganization(
		String externalReferenceCode, long userId, long parentOrganizationId,
		String name, String type, long regionId, long countryId, long statusId,
		String comments, boolean site, boolean logo, byte[] logoBytes,
		com.liferay.portal.kernel.service.ServiceContext serviceContext)
		throws com.liferay.portal.kernel.exception.PortalException {
		return getService()
				   .addOrUpdateOrganization(externalReferenceCode, userId,
			parentOrganizationId, name, type, regionId, countryId, statusId,
			comments, site, logo, logoBytes, serviceContext);
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	public static String getOSGiServiceIdentifier() {
		return getService().getOSGiServiceIdentifier();
	}

	public static EROrganizationLocalService getService() {
		return _serviceTracker.getService();
	}

	private static ServiceTracker<EROrganizationLocalService, EROrganizationLocalService> _serviceTracker;

	static {
		Bundle bundle = FrameworkUtil.getBundle(EROrganizationLocalService.class);

		ServiceTracker<EROrganizationLocalService, EROrganizationLocalService> serviceTracker =
			new ServiceTracker<EROrganizationLocalService, EROrganizationLocalService>(bundle.getBundleContext(),
				EROrganizationLocalService.class, null);

		serviceTracker.open();

		_serviceTracker = serviceTracker;
	}
}