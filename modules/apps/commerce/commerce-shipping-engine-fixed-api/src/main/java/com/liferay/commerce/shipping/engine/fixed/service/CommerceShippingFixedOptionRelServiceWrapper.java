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

package com.liferay.commerce.shipping.engine.fixed.service;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link CommerceShippingFixedOptionRelService}.
 *
 * @author Alessio Antonio Rendina
 * @see CommerceShippingFixedOptionRelService
 * @generated
 */
public class CommerceShippingFixedOptionRelServiceWrapper
	implements CommerceShippingFixedOptionRelService,
			   ServiceWrapper<CommerceShippingFixedOptionRelService> {

	public CommerceShippingFixedOptionRelServiceWrapper(
		CommerceShippingFixedOptionRelService
			commerceShippingFixedOptionRelService) {

		_commerceShippingFixedOptionRelService =
			commerceShippingFixedOptionRelService;
	}

	@Override
	public com.liferay.commerce.shipping.engine.fixed.model.
		CommerceShippingFixedOptionRel addCommerceShippingFixedOptionRel(
				long userId, long groupId, long commerceShippingMethodId,
				long commerceShippingFixedOptionId,
				long commerceInventoryWarehouseId, long countryId,
				long regionId, String zip, double weightFrom, double weightTo,
				java.math.BigDecimal fixedPrice,
				java.math.BigDecimal rateUnitWeightPrice, double ratePercentage)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionRelService.
			addCommerceShippingFixedOptionRel(
				userId, groupId, commerceShippingMethodId,
				commerceShippingFixedOptionId, commerceInventoryWarehouseId,
				countryId, regionId, zip, weightFrom, weightTo, fixedPrice,
				rateUnitWeightPrice, ratePercentage);
	}

	/**
	 * @deprecated As of Athanasius (7.3.x)
	 */
	@Deprecated
	@Override
	public com.liferay.commerce.shipping.engine.fixed.model.
		CommerceShippingFixedOptionRel addCommerceShippingFixedOptionRel(
				long commerceShippingMethodId,
				long commerceShippingFixedOptionId,
				long commerceInventoryWarehouseId, long countryId,
				long regionId, String zip, double weightFrom, double weightTo,
				java.math.BigDecimal fixedPrice,
				java.math.BigDecimal rateUnitWeightPrice, double ratePercentage,
				com.liferay.portal.kernel.service.ServiceContext serviceContext)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionRelService.
			addCommerceShippingFixedOptionRel(
				commerceShippingMethodId, commerceShippingFixedOptionId,
				commerceInventoryWarehouseId, countryId, regionId, zip,
				weightFrom, weightTo, fixedPrice, rateUnitWeightPrice,
				ratePercentage, serviceContext);
	}

	@Override
	public void deleteCommerceShippingFixedOptionRel(
			long commerceShippingFixedOptionRelId)
		throws com.liferay.portal.kernel.exception.PortalException {

		_commerceShippingFixedOptionRelService.
			deleteCommerceShippingFixedOptionRel(
				commerceShippingFixedOptionRelId);
	}

	@Override
	public com.liferay.commerce.shipping.engine.fixed.model.
		CommerceShippingFixedOptionRel fetchCommerceShippingFixedOptionRel(
				long commerceShippingFixedOptionRelId)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionRelService.
			fetchCommerceShippingFixedOptionRel(
				commerceShippingFixedOptionRelId);
	}

	@Override
	public java.util.List
		<com.liferay.commerce.shipping.engine.fixed.model.
			CommerceShippingFixedOptionRel>
					getCommerceShippingMethodFixedOptionRels(
						long commerceShippingMethodId, int start, int end,
						com.liferay.portal.kernel.util.OrderByComparator
							<com.liferay.commerce.shipping.engine.fixed.model.
								CommerceShippingFixedOptionRel>
									orderByComparator)
				throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionRelService.
			getCommerceShippingMethodFixedOptionRels(
				commerceShippingMethodId, start, end, orderByComparator);
	}

	@Override
	public int getCommerceShippingMethodFixedOptionRelsCount(
			long commerceShippingMethodId)
		throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionRelService.
			getCommerceShippingMethodFixedOptionRelsCount(
				commerceShippingMethodId);
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return _commerceShippingFixedOptionRelService.
			getOSGiServiceIdentifier();
	}

	@Override
	public com.liferay.commerce.shipping.engine.fixed.model.
		CommerceShippingFixedOptionRel updateCommerceShippingFixedOptionRel(
				long commerceShippingFixedOptionRelId,
				long commerceInventoryWarehouseId, long countryId,
				long regionId, String zip, double weightFrom, double weightTo,
				java.math.BigDecimal fixedPrice,
				java.math.BigDecimal rateUnitWeightPrice, double ratePercentage)
			throws com.liferay.portal.kernel.exception.PortalException {

		return _commerceShippingFixedOptionRelService.
			updateCommerceShippingFixedOptionRel(
				commerceShippingFixedOptionRelId, commerceInventoryWarehouseId,
				countryId, regionId, zip, weightFrom, weightTo, fixedPrice,
				rateUnitWeightPrice, ratePercentage);
	}

	@Override
	public CommerceShippingFixedOptionRelService getWrappedService() {
		return _commerceShippingFixedOptionRelService;
	}

	@Override
	public void setWrappedService(
		CommerceShippingFixedOptionRelService
			commerceShippingFixedOptionRelService) {

		_commerceShippingFixedOptionRelService =
			commerceShippingFixedOptionRelService;
	}

	private CommerceShippingFixedOptionRelService
		_commerceShippingFixedOptionRelService;

}