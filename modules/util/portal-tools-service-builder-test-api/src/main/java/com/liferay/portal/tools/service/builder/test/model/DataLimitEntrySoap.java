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

package com.liferay.portal.tools.service.builder.test.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services.
 *
 * @author Brian Wing Shun Chan
 * @deprecated As of Athanasius (7.3.x), with no direct replacement
 * @generated
 */
@Deprecated
public class DataLimitEntrySoap implements Serializable {

	public static DataLimitEntrySoap toSoapModel(DataLimitEntry model) {
		DataLimitEntrySoap soapModel = new DataLimitEntrySoap();

		soapModel.setDataLimitEntryId(model.getDataLimitEntryId());
		soapModel.setCompanyId(model.getCompanyId());
		soapModel.setUserId(model.getUserId());
		soapModel.setUserName(model.getUserName());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());

		return soapModel;
	}

	public static DataLimitEntrySoap[] toSoapModels(DataLimitEntry[] models) {
		DataLimitEntrySoap[] soapModels = new DataLimitEntrySoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static DataLimitEntrySoap[][] toSoapModels(
		DataLimitEntry[][] models) {

		DataLimitEntrySoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels =
				new DataLimitEntrySoap[models.length][models[0].length];
		}
		else {
			soapModels = new DataLimitEntrySoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static DataLimitEntrySoap[] toSoapModels(
		List<DataLimitEntry> models) {

		List<DataLimitEntrySoap> soapModels = new ArrayList<DataLimitEntrySoap>(
			models.size());

		for (DataLimitEntry model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new DataLimitEntrySoap[soapModels.size()]);
	}

	public DataLimitEntrySoap() {
	}

	public long getPrimaryKey() {
		return _dataLimitEntryId;
	}

	public void setPrimaryKey(long pk) {
		setDataLimitEntryId(pk);
	}

	public long getDataLimitEntryId() {
		return _dataLimitEntryId;
	}

	public void setDataLimitEntryId(long dataLimitEntryId) {
		_dataLimitEntryId = dataLimitEntryId;
	}

	public long getCompanyId() {
		return _companyId;
	}

	public void setCompanyId(long companyId) {
		_companyId = companyId;
	}

	public long getUserId() {
		return _userId;
	}

	public void setUserId(long userId) {
		_userId = userId;
	}

	public String getUserName() {
		return _userName;
	}

	public void setUserName(String userName) {
		_userName = userName;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	private long _dataLimitEntryId;
	private long _companyId;
	private long _userId;
	private String _userName;
	private Date _createDate;
	private Date _modifiedDate;

}