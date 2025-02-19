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

package com.liferay.portal.util;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.util.CustomJspRegistry;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ryan Park
 * @author Brian Wing Shun Chan
 */
public class CustomJspRegistryImpl implements CustomJspRegistry {

	@Override
	public String getCustomJspFileName(
		String servletContextName, String fileName) {

		int pos = fileName.lastIndexOf(CharPool.PERIOD);

		if (pos == -1) {
			return StringBundler.concat(
				fileName, StringPool.PERIOD, servletContextName);
		}

		StringBundler sb = new StringBundler(4);

		sb.append(fileName.substring(0, pos));
		sb.append(CharPool.PERIOD);
		sb.append(servletContextName);
		sb.append(fileName.substring(pos));

		return sb.toString();
	}

	@Override
	public String getDisplayName(String servletContextName) {
		return _servletContextNames.get(servletContextName);
	}

	@Override
	public Set<String> getServletContextNames() {
		return _servletContextNames.keySet();
	}

	@Override
	public void registerServletContextName(
		String servletContextName, String displayName) {

		_servletContextNames.put(servletContextName, displayName);
	}

	@Override
	public void unregisterServletContextName(String servletContextName) {
		_servletContextNames.remove(servletContextName);
	}

	private final Map<String, String> _servletContextNames =
		new ConcurrentHashMap<>();

}