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

import serviceFetch from './serviceFetch';

export default {
	/**
	 * Adds an item to layoutData
	 * @param {object} options
	 * @param {object} options.config Application config
	 * @param {object} options.itemConfig item config
	 * @param {string} options.itemType item type
	 * @param {object} options.parentItemId Parent to be added to
	 * @param {object} options.position Position to be added to
	 * @param {object} options.segmentsExperienceId
	 * @return {Promise<object>}
	 */
	addItem({
		config,
		itemConfig,
		itemType,
		parentItemId,
		position,
		segmentsExperienceId
	}) {
		const {addItemURL} = config;

		return serviceFetch(config, addItemURL, {
			itemConfig: JSON.stringify(itemConfig),
			itemType,
			parentItemId,
			position,
			segmentsExperienceId
		});
	},

	/**
	 * Remove an item inside layoutData
	 * @param {object} options
	 * @param {object} options.config Application config
	 * @param {object} options.itemId id of the item to be removed
	 * @param {object} options.segmentsExperienceId
	 * @return {Promise<object>}
	 */
	deleteItem({config, itemId, segmentsExperienceId}) {
		const {deleteItemURL} = config;

		return serviceFetch(config, deleteItemURL, {
			itemId,
			segmentsExperienceId
		});
	},

	/**
	 * Move an item inside layoutData
	 * @param {object} options
	 * @param {object} options.config Application config
	 * @param {object} options.itemId id of the item to be moved
	 * @param {object} options.parentItemId id of the target parent
	 * @param {object} options.position position in the parent where the item is placed
	 * @param {object} options.segmentsExperienceId
	 * @return {Promise<object>}
	 */
	moveItem({config, itemId, parentItemId, position, segmentsExperienceId}) {
		const {moveItemURL} = config;

		return serviceFetch(config, moveItemURL, {
			itemId,
			parentItemId,
			position,
			segmentsExperienceId
		});
	},

	/**
	 * Updates layout's layoutData
	 * @param {object} options
	 * @param {object} options.config Application config
	 * @param {string} options.segmentsExperienceId Current segmentsExperienceId
	 * @param {object} options.layoutData New layoutData
	 * @return {Promise<void>}
	 */
	updateLayoutData({config, layoutData, segmentsExperienceId}) {
		const {classNameId, classPK, updateLayoutPageTemplateDataURL} = config;

		return serviceFetch(config, updateLayoutPageTemplateDataURL, {
			classNameId,
			classPK,
			data: JSON.stringify(layoutData),
			segmentsExperienceId
		});
	}
};
