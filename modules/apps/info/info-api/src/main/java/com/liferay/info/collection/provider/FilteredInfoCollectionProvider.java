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

package com.liferay.info.collection.provider;

import com.liferay.info.filter.InfoFilter;
import com.liferay.petra.reflect.GenericUtil;

/**
 * @author Eudaldo Alonso
 */
public interface FilteredInfoCollectionProvider<T, F extends InfoFilter>
	extends InfoCollectionProvider<T> {

	public default Class<F> getInfoFilterClass() {
		return (Class<F>)GenericUtil.getGenericClass(this, 1);
	}

}