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

package com.liferay.dynamic.data.mapping.io.internal.exporter;

import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldTypeServicesTracker;
import com.liferay.dynamic.data.mapping.form.field.type.DDMFormFieldValueRenderer;
import com.liferay.dynamic.data.mapping.io.exporter.DDMFormExporter;
import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.DDMFormField;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceRecord;
import com.liferay.dynamic.data.mapping.model.DDMFormInstanceVersion;
import com.liferay.dynamic.data.mapping.model.DDMStructureVersion;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.service.DDMFormInstanceVersionLocalService;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.util.comparator.FormInstanceVersionVersionComparator;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Marcellus Tavares
 * @author Manuel de la Peña
 */
public abstract class BaseDDMFormExporter implements DDMFormExporter {

	@Override
	public byte[] export(long formInstanceId) throws Exception {
		return doExport(
			formInstanceId, WorkflowConstants.STATUS_ANY, QueryUtil.ALL_POS,
			QueryUtil.ALL_POS, null);
	}

	@Override
	public byte[] export(long formInstanceId, int status) throws Exception {
		return doExport(
			formInstanceId, status, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	@Override
	public byte[] export(long formInstanceId, int status, int start, int end)
		throws Exception {

		return doExport(formInstanceId, status, start, end, null);
	}

	@Override
	public byte[] export(
			long formInstanceId, int status, int start, int end,
			OrderByComparator<DDMFormInstanceRecord> orderByComparator)
		throws Exception {

		return doExport(formInstanceId, status, start, end, orderByComparator);
	}

	public abstract DDMFormFieldTypeServicesTracker
		getDDMFormFieldTypeServicesTracker();

	public abstract DDMFormInstanceVersionLocalService
		getDDMFormInstanceVersionLocalService();

	@Override
	public Locale getLocale() {
		if (_locale == null) {
			_locale = LocaleUtil.getSiteDefault();
		}

		return _locale;
	}

	@Override
	public void setLocale(Locale locale) {
		_locale = locale;
	}

	protected abstract byte[] doExport(
			long formInstanceId, int status, int start, int end,
			OrderByComparator<DDMFormInstanceRecord> orderByComparator)
		throws Exception;

	protected String formatDate(
		Date date, DateTimeFormatter dateTimeFormatter) {

		LocalDateTime localDateTime = LocalDateTime.ofInstant(
			date.toInstant(), ZoneId.systemDefault());

		return dateTimeFormatter.format(localDateTime);
	}

	protected DateTimeFormatter getDateTimeFormatter() {
		DateTimeFormatter dateTimeFormatter =
			DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

		return dateTimeFormatter.withLocale(getLocale());
	}

	protected DDMFormFieldRenderedValue getDDMFormFieldRenderedValue(
		DDMFormField ddmFormField,
		Map<String, List<DDMFormFieldValue>> ddmFormFieldValueMap) {

		List<DDMFormFieldValue> ddmFormFieldValues = ddmFormFieldValueMap.get(
			ddmFormField.getName());

		DDMFormFieldTypeServicesTracker ddmFormFieldTypeServicesTracker =
			getDDMFormFieldTypeServicesTracker();

		DDMFormFieldValueRenderer ddmFormFieldValueRenderer =
			ddmFormFieldTypeServicesTracker.getDDMFormFieldValueRenderer(
				ddmFormField.getType());

		Stream<DDMFormFieldValue> stream = ddmFormFieldValues.stream();

		String valueString = HtmlUtil.render(
			StringUtil.merge(
				stream.map(
					ddmForFieldValue -> ddmFormFieldValueRenderer.render(
						ddmForFieldValue, getLocale())
				).filter(
					Validator::isNotNull
				).collect(
					Collectors.toList()
				),
				StringPool.COMMA_AND_SPACE));

		return new DDMFormFieldRenderedValue(
			ddmFormField.getName(), ddmFormField.getLabel(), valueString);
	}

	protected Map<String, String> getDDMFormFieldsLabels(
		Collection<DDMFormField> ddmFormFields, Locale locale) {

		Map<String, String> ddmFormFieldsLabels = new HashMap<>();

		for (DDMFormField ddmFormField : ddmFormFields) {
			LocalizedValue label = ddmFormField.getLabel();

			ddmFormFieldsLabels.put(
				ddmFormField.getName(), label.getString(locale));
		}

		return _formatLabels(ddmFormFieldsLabels);
	}

	protected Map<String, DDMFormField> getDistinctFields(long formInstanceId)
		throws Exception {

		List<DDMStructureVersion> ddmStructureVersions = getStructureVersions(
			formInstanceId);

		Map<String, DDMFormField> ddmFormFields = new LinkedHashMap<>();

		Stream<DDMStructureVersion> stream = ddmStructureVersions.stream();

		stream.map(
			this::getNontransientDDMFormFieldsMap
		).forEach(
			map -> map.forEach(
				(key, ddmFormField) -> ddmFormFields.putIfAbsent(
					key, ddmFormField))
		);

		return ddmFormFields;
	}

	protected Map<String, DDMFormField> getNontransientDDMFormFieldsMap(
		DDMStructureVersion ddmStructureVersion) {

		DDMForm ddmForm = ddmStructureVersion.getDDMForm();

		return ddmForm.getNontransientDDMFormFieldsMap(true);
	}

	protected Map<String, DDMFormFieldRenderedValue> getRenderedValues(
			Collection<DDMFormField> ddmFormFields, DDMFormValues ddmFormValues)
		throws Exception {

		Map<String, List<DDMFormFieldValue>> ddmFormFieldValueMap =
			ddmFormValues.getDDMFormFieldValuesMap();

		Stream<DDMFormField> ddmFormFieldStream = ddmFormFields.stream();

		ddmFormFieldStream = ddmFormFieldStream.filter(
			ddmFormField -> ddmFormFieldValueMap.containsKey(
				ddmFormField.getName()));

		Stream<DDMFormFieldRenderedValue> valueStream = ddmFormFieldStream.map(
			ddmFormField -> getDDMFormFieldRenderedValue(
				ddmFormField, ddmFormFieldValueMap));

		return valueStream.collect(
			Collectors.toMap(
				DDMFormFieldRenderedValue::getFieldName, value -> value));
	}

	protected String getStatusMessage(int status) {
		String statusLabel = WorkflowConstants.getStatusLabel(status);

		return LanguageUtil.get(_locale, statusLabel);
	}

	protected List<DDMStructureVersion> getStructureVersions(
			long formInstanceId)
		throws Exception {

		DDMFormInstanceVersionLocalService ddmFormInstanceVersionLocalService =
			getDDMFormInstanceVersionLocalService();

		List<DDMFormInstanceVersion> formInstanceVersions =
			ddmFormInstanceVersionLocalService.getFormInstanceVersions(
				formInstanceId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);

		formInstanceVersions = ListUtil.sort(
			formInstanceVersions, new FormInstanceVersionVersionComparator());

		List<DDMStructureVersion> ddmStructureVersions = new ArrayList<>();

		for (DDMFormInstanceVersion formInstanceVersion :
				formInstanceVersions) {

			ddmStructureVersions.add(formInstanceVersion.getStructureVersion());
		}

		return ddmStructureVersions;
	}

	protected static class DDMFormFieldRenderedValue {

		protected DDMFormFieldRenderedValue(
			String fieldName, LocalizedValue label, String value) {

			_fieldName = fieldName;
			_label = label;
			_value = value;
		}

		protected String getFieldName() {
			return _fieldName;
		}

		protected LocalizedValue getLabel() {
			return _label;
		}

		protected String getValue() {
			return _value;
		}

		private final String _fieldName;
		private final LocalizedValue _label;
		private final String _value;

	}

	private Map<String, String> _formatLabels(
		Map<String, String> ddmFormFieldsLabel) {

		Map<String, String> labelsFieldName = new HashMap<>();

		ddmFormFieldsLabel.forEach(
			(fieldName, label) -> {
				if (!labelsFieldName.containsKey(label)) {
					ddmFormFieldsLabel.put(fieldName, label);
				}
				else {
					String previousFieldName = labelsFieldName.get(label);

					ddmFormFieldsLabel.put(
						previousFieldName,
						_formatLabelString(previousFieldName, label));

					ddmFormFieldsLabel.put(
						fieldName, _formatLabelString(fieldName, label));
				}

				labelsFieldName.put(label, fieldName);
			});

		return ddmFormFieldsLabel;
	}

	private String _formatLabelString(String fieldName, String label) {
		if (Validator.isNull(label)) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler(5);

		sb.append(label);
		sb.append(StringPool.SPACE);
		sb.append(StringPool.OPEN_PARENTHESIS);
		sb.append(fieldName);
		sb.append(StringPool.CLOSE_PARENTHESIS);

		return sb.toString();
	}

	private Locale _locale;

}