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

package com.liferay.portal.vulcan.util;

import com.liferay.portal.kernel.search.BooleanClause;
import com.liferay.portal.kernel.search.BooleanClauseFactoryUtil;
import com.liferay.portal.kernel.search.BooleanClauseOccur;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.QueryConfig;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.kernel.search.Sort;
import com.liferay.portal.kernel.search.filter.BooleanFilter;
import com.liferay.portal.kernel.search.filter.Filter;
import com.liferay.portal.kernel.search.generic.BooleanQueryImpl;
import com.liferay.portal.kernel.search.generic.MatchAllQuery;
import com.liferay.portal.kernel.security.permission.PermissionChecker;
import com.liferay.portal.kernel.security.permission.PermissionThreadLocal;
import com.liferay.portal.vulcan.pagination.Pagination;

import java.util.function.Consumer;

/**
 * @author Brian Wing Shun Chan
 */
public class SearchUtil {

	public static SearchContext createSearchContext(
			Consumer<BooleanQuery> booleanQueryConsumer,
			Filter filter, Pagination pagination,
			Consumer<QueryConfig> queryConfigConsumer, Sort[] sorts)
		throws Exception {

		BooleanClause<?> booleanClause = _getBooleanClause(
			filter, booleanQueryConsumer);

		PermissionChecker permissionChecker =
			PermissionThreadLocal.getPermissionChecker();

		return _createSearchContext(
			booleanClause, pagination, permissionChecker, queryConfigConsumer,
			sorts);
	}

	private static SearchContext _createSearchContext(
		BooleanClause<?> booleanClause, Pagination pagination,
		PermissionChecker permissionChecker,
		Consumer<QueryConfig> queryConfigConsumer, Sort[] sorts) {

		SearchContext searchContext = new SearchContext();

		searchContext.setBooleanClauses(new BooleanClause[] {booleanClause});
		searchContext.setEnd(pagination.getEndPosition());
		searchContext.setSorts(sorts);
		searchContext.setStart(pagination.getStartPosition());
		searchContext.setUserId(permissionChecker.getUserId());

		QueryConfig queryConfig = searchContext.getQueryConfig();

		queryConfig.setHighlightEnabled(false);
		queryConfig.setScoreEnabled(false);

		queryConfigConsumer.accept(queryConfig);

		return searchContext;
	}

	private static BooleanClause<?> _getBooleanClause(
			Filter filter, Consumer<BooleanQuery> booleanQueryConsumer)
		throws Exception {

		BooleanQuery booleanQuery = new BooleanQueryImpl();

		booleanQuery.add(new MatchAllQuery(), BooleanClauseOccur.MUST);

		BooleanFilter booleanFilter = new BooleanFilter();

		if (filter != null) {
			booleanFilter.add(filter, BooleanClauseOccur.MUST);
		}

		booleanQuery.setPreBooleanFilter(booleanFilter);

		booleanQueryConsumer.accept(booleanQuery);

		return BooleanClauseFactoryUtil.create(
			booleanQuery, BooleanClauseOccur.MUST.getName());
	}

}