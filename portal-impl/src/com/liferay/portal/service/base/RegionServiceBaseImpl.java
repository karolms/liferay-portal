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

package com.liferay.portal.service.base;

import com.liferay.portal.kernel.bean.BeanReference;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdate;
import com.liferay.portal.kernel.dao.jdbc.SqlUpdateFactoryUtil;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.Region;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.service.BaseServiceImpl;
import com.liferay.portal.kernel.service.RegionService;
import com.liferay.portal.kernel.service.persistence.AddressPersistence;
import com.liferay.portal.kernel.service.persistence.CountryPersistence;
import com.liferay.portal.kernel.service.persistence.OrganizationFinder;
import com.liferay.portal.kernel.service.persistence.OrganizationPersistence;
import com.liferay.portal.kernel.service.persistence.RegionLocalizationPersistence;
import com.liferay.portal.kernel.service.persistence.RegionPersistence;
import com.liferay.portal.kernel.service.persistence.UserFinder;
import com.liferay.portal.kernel.service.persistence.UserPersistence;
import com.liferay.portal.kernel.util.PortalUtil;

import javax.sql.DataSource;

/**
 * Provides the base implementation for the region remote service.
 *
 * <p>
 * This implementation exists only as a container for the default service methods generated by ServiceBuilder. All custom service methods should be put in {@link com.liferay.portal.service.impl.RegionServiceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.portal.service.impl.RegionServiceImpl
 * @generated
 */
public abstract class RegionServiceBaseImpl
	extends BaseServiceImpl implements IdentifiableOSGiService, RegionService {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Use <code>RegionService</code> via injection or a <code>org.osgi.util.tracker.ServiceTracker</code> or use <code>com.liferay.portal.kernel.service.RegionServiceUtil</code>.
	 */

	/**
	 * Returns the region local service.
	 *
	 * @return the region local service
	 */
	public com.liferay.portal.kernel.service.RegionLocalService
		getRegionLocalService() {

		return regionLocalService;
	}

	/**
	 * Sets the region local service.
	 *
	 * @param regionLocalService the region local service
	 */
	public void setRegionLocalService(
		com.liferay.portal.kernel.service.RegionLocalService
			regionLocalService) {

		this.regionLocalService = regionLocalService;
	}

	/**
	 * Returns the region remote service.
	 *
	 * @return the region remote service
	 */
	public RegionService getRegionService() {
		return regionService;
	}

	/**
	 * Sets the region remote service.
	 *
	 * @param regionService the region remote service
	 */
	public void setRegionService(RegionService regionService) {
		this.regionService = regionService;
	}

	/**
	 * Returns the region persistence.
	 *
	 * @return the region persistence
	 */
	public RegionPersistence getRegionPersistence() {
		return regionPersistence;
	}

	/**
	 * Sets the region persistence.
	 *
	 * @param regionPersistence the region persistence
	 */
	public void setRegionPersistence(RegionPersistence regionPersistence) {
		this.regionPersistence = regionPersistence;
	}

	/**
	 * Returns the counter local service.
	 *
	 * @return the counter local service
	 */
	public com.liferay.counter.kernel.service.CounterLocalService
		getCounterLocalService() {

		return counterLocalService;
	}

	/**
	 * Sets the counter local service.
	 *
	 * @param counterLocalService the counter local service
	 */
	public void setCounterLocalService(
		com.liferay.counter.kernel.service.CounterLocalService
			counterLocalService) {

		this.counterLocalService = counterLocalService;
	}

	/**
	 * Returns the address local service.
	 *
	 * @return the address local service
	 */
	public com.liferay.portal.kernel.service.AddressLocalService
		getAddressLocalService() {

		return addressLocalService;
	}

	/**
	 * Sets the address local service.
	 *
	 * @param addressLocalService the address local service
	 */
	public void setAddressLocalService(
		com.liferay.portal.kernel.service.AddressLocalService
			addressLocalService) {

		this.addressLocalService = addressLocalService;
	}

	/**
	 * Returns the address remote service.
	 *
	 * @return the address remote service
	 */
	public com.liferay.portal.kernel.service.AddressService
		getAddressService() {

		return addressService;
	}

	/**
	 * Sets the address remote service.
	 *
	 * @param addressService the address remote service
	 */
	public void setAddressService(
		com.liferay.portal.kernel.service.AddressService addressService) {

		this.addressService = addressService;
	}

	/**
	 * Returns the address persistence.
	 *
	 * @return the address persistence
	 */
	public AddressPersistence getAddressPersistence() {
		return addressPersistence;
	}

	/**
	 * Sets the address persistence.
	 *
	 * @param addressPersistence the address persistence
	 */
	public void setAddressPersistence(AddressPersistence addressPersistence) {
		this.addressPersistence = addressPersistence;
	}

	/**
	 * Returns the country local service.
	 *
	 * @return the country local service
	 */
	public com.liferay.portal.kernel.service.CountryLocalService
		getCountryLocalService() {

		return countryLocalService;
	}

	/**
	 * Sets the country local service.
	 *
	 * @param countryLocalService the country local service
	 */
	public void setCountryLocalService(
		com.liferay.portal.kernel.service.CountryLocalService
			countryLocalService) {

		this.countryLocalService = countryLocalService;
	}

	/**
	 * Returns the country remote service.
	 *
	 * @return the country remote service
	 */
	public com.liferay.portal.kernel.service.CountryService
		getCountryService() {

		return countryService;
	}

	/**
	 * Sets the country remote service.
	 *
	 * @param countryService the country remote service
	 */
	public void setCountryService(
		com.liferay.portal.kernel.service.CountryService countryService) {

		this.countryService = countryService;
	}

	/**
	 * Returns the country persistence.
	 *
	 * @return the country persistence
	 */
	public CountryPersistence getCountryPersistence() {
		return countryPersistence;
	}

	/**
	 * Sets the country persistence.
	 *
	 * @param countryPersistence the country persistence
	 */
	public void setCountryPersistence(CountryPersistence countryPersistence) {
		this.countryPersistence = countryPersistence;
	}

	/**
	 * Returns the organization local service.
	 *
	 * @return the organization local service
	 */
	public com.liferay.portal.kernel.service.OrganizationLocalService
		getOrganizationLocalService() {

		return organizationLocalService;
	}

	/**
	 * Sets the organization local service.
	 *
	 * @param organizationLocalService the organization local service
	 */
	public void setOrganizationLocalService(
		com.liferay.portal.kernel.service.OrganizationLocalService
			organizationLocalService) {

		this.organizationLocalService = organizationLocalService;
	}

	/**
	 * Returns the organization remote service.
	 *
	 * @return the organization remote service
	 */
	public com.liferay.portal.kernel.service.OrganizationService
		getOrganizationService() {

		return organizationService;
	}

	/**
	 * Sets the organization remote service.
	 *
	 * @param organizationService the organization remote service
	 */
	public void setOrganizationService(
		com.liferay.portal.kernel.service.OrganizationService
			organizationService) {

		this.organizationService = organizationService;
	}

	/**
	 * Returns the organization persistence.
	 *
	 * @return the organization persistence
	 */
	public OrganizationPersistence getOrganizationPersistence() {
		return organizationPersistence;
	}

	/**
	 * Sets the organization persistence.
	 *
	 * @param organizationPersistence the organization persistence
	 */
	public void setOrganizationPersistence(
		OrganizationPersistence organizationPersistence) {

		this.organizationPersistence = organizationPersistence;
	}

	/**
	 * Returns the organization finder.
	 *
	 * @return the organization finder
	 */
	public OrganizationFinder getOrganizationFinder() {
		return organizationFinder;
	}

	/**
	 * Sets the organization finder.
	 *
	 * @param organizationFinder the organization finder
	 */
	public void setOrganizationFinder(OrganizationFinder organizationFinder) {
		this.organizationFinder = organizationFinder;
	}

	/**
	 * Returns the region localization persistence.
	 *
	 * @return the region localization persistence
	 */
	public RegionLocalizationPersistence getRegionLocalizationPersistence() {
		return regionLocalizationPersistence;
	}

	/**
	 * Sets the region localization persistence.
	 *
	 * @param regionLocalizationPersistence the region localization persistence
	 */
	public void setRegionLocalizationPersistence(
		RegionLocalizationPersistence regionLocalizationPersistence) {

		this.regionLocalizationPersistence = regionLocalizationPersistence;
	}

	/**
	 * Returns the user local service.
	 *
	 * @return the user local service
	 */
	public com.liferay.portal.kernel.service.UserLocalService
		getUserLocalService() {

		return userLocalService;
	}

	/**
	 * Sets the user local service.
	 *
	 * @param userLocalService the user local service
	 */
	public void setUserLocalService(
		com.liferay.portal.kernel.service.UserLocalService userLocalService) {

		this.userLocalService = userLocalService;
	}

	/**
	 * Returns the user remote service.
	 *
	 * @return the user remote service
	 */
	public com.liferay.portal.kernel.service.UserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user remote service.
	 *
	 * @param userService the user remote service
	 */
	public void setUserService(
		com.liferay.portal.kernel.service.UserService userService) {

		this.userService = userService;
	}

	/**
	 * Returns the user persistence.
	 *
	 * @return the user persistence
	 */
	public UserPersistence getUserPersistence() {
		return userPersistence;
	}

	/**
	 * Sets the user persistence.
	 *
	 * @param userPersistence the user persistence
	 */
	public void setUserPersistence(UserPersistence userPersistence) {
		this.userPersistence = userPersistence;
	}

	/**
	 * Returns the user finder.
	 *
	 * @return the user finder
	 */
	public UserFinder getUserFinder() {
		return userFinder;
	}

	/**
	 * Sets the user finder.
	 *
	 * @param userFinder the user finder
	 */
	public void setUserFinder(UserFinder userFinder) {
		this.userFinder = userFinder;
	}

	public void afterPropertiesSet() {
	}

	public void destroy() {
	}

	/**
	 * Returns the OSGi service identifier.
	 *
	 * @return the OSGi service identifier
	 */
	@Override
	public String getOSGiServiceIdentifier() {
		return RegionService.class.getName();
	}

	protected Class<?> getModelClass() {
		return Region.class;
	}

	protected String getModelClassName() {
		return Region.class.getName();
	}

	/**
	 * Performs a SQL query.
	 *
	 * @param sql the sql query
	 */
	protected void runSQL(String sql) {
		try {
			DataSource dataSource = regionPersistence.getDataSource();

			DB db = DBManagerUtil.getDB();

			sql = db.buildSQL(sql);
			sql = PortalUtil.transformSQL(sql);

			SqlUpdate sqlUpdate = SqlUpdateFactoryUtil.getSqlUpdate(
				dataSource, sql);

			sqlUpdate.update();
		}
		catch (Exception exception) {
			throw new SystemException(exception);
		}
	}

	@BeanReference(
		type = com.liferay.portal.kernel.service.RegionLocalService.class
	)
	protected com.liferay.portal.kernel.service.RegionLocalService
		regionLocalService;

	@BeanReference(type = RegionService.class)
	protected RegionService regionService;

	@BeanReference(type = RegionPersistence.class)
	protected RegionPersistence regionPersistence;

	@BeanReference(
		type = com.liferay.counter.kernel.service.CounterLocalService.class
	)
	protected com.liferay.counter.kernel.service.CounterLocalService
		counterLocalService;

	@BeanReference(
		type = com.liferay.portal.kernel.service.AddressLocalService.class
	)
	protected com.liferay.portal.kernel.service.AddressLocalService
		addressLocalService;

	@BeanReference(
		type = com.liferay.portal.kernel.service.AddressService.class
	)
	protected com.liferay.portal.kernel.service.AddressService addressService;

	@BeanReference(type = AddressPersistence.class)
	protected AddressPersistence addressPersistence;

	@BeanReference(
		type = com.liferay.portal.kernel.service.CountryLocalService.class
	)
	protected com.liferay.portal.kernel.service.CountryLocalService
		countryLocalService;

	@BeanReference(
		type = com.liferay.portal.kernel.service.CountryService.class
	)
	protected com.liferay.portal.kernel.service.CountryService countryService;

	@BeanReference(type = CountryPersistence.class)
	protected CountryPersistence countryPersistence;

	@BeanReference(
		type = com.liferay.portal.kernel.service.OrganizationLocalService.class
	)
	protected com.liferay.portal.kernel.service.OrganizationLocalService
		organizationLocalService;

	@BeanReference(
		type = com.liferay.portal.kernel.service.OrganizationService.class
	)
	protected com.liferay.portal.kernel.service.OrganizationService
		organizationService;

	@BeanReference(type = OrganizationPersistence.class)
	protected OrganizationPersistence organizationPersistence;

	@BeanReference(type = OrganizationFinder.class)
	protected OrganizationFinder organizationFinder;

	@BeanReference(type = RegionLocalizationPersistence.class)
	protected RegionLocalizationPersistence regionLocalizationPersistence;

	@BeanReference(
		type = com.liferay.portal.kernel.service.UserLocalService.class
	)
	protected com.liferay.portal.kernel.service.UserLocalService
		userLocalService;

	@BeanReference(type = com.liferay.portal.kernel.service.UserService.class)
	protected com.liferay.portal.kernel.service.UserService userService;

	@BeanReference(type = UserPersistence.class)
	protected UserPersistence userPersistence;

	@BeanReference(type = UserFinder.class)
	protected UserFinder userFinder;

}