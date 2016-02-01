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

package com.liferay.content.targeting.rule.user.group.member;

import com.liferay.content.targeting.anonymous.users.model.AnonymousUser;
import com.liferay.content.targeting.api.model.BaseRule;
import com.liferay.content.targeting.api.model.Rule;
import com.liferay.content.targeting.model.RuleInstance;
import com.liferay.content.targeting.model.UserSegment;
import com.liferay.content.targeting.rule.categories.UserAttributesRuleCategory;
import com.liferay.content.targeting.util.ContentTargetingContextUtil;
import com.liferay.content.targeting.util.PortletKeys;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.PortletDataException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayWindowState;
import com.liferay.portal.kernel.util.Constants;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Company;
import com.liferay.portal.model.User;
import com.liferay.portal.model.UserGroup;
import com.liferay.portal.service.UserGroupLocalServiceUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Eudaldo Alonso
 * @author Brendan Johan Lee
 */
@Component(immediate = true, service = Rule.class)
public class UserGroupMemberRule extends BaseRule {
    private static final Log LOG = LogFactoryUtil.getLog(UserGroupMemberRule.class);

	@Activate
	@Override
	public void activate() {
		super.activate();
	}

	@Deactivate
	@Override
	public void deActivate() {
		super.deActivate();
	}

	@Override
	public boolean evaluate(
			HttpServletRequest request, RuleInstance ruleInstance,
			AnonymousUser anonymousUser)
		throws Exception {

		long userGroupId = GetterUtil.getLong(ruleInstance.getTypeSettings());

		return UserGroupLocalServiceUtil.hasUserUserGroup(
			anonymousUser.getUserId(), userGroupId);
	}

	@Override
	public void exportData(
			PortletDataContext portletDataContext, Element userSegmentElement,
			UserSegment userSegment, Element ruleInstanceElement,
			RuleInstance ruleInstance)
		throws Exception {

		long userGroupId = GetterUtil.getLong(ruleInstance.getTypeSettings());

		UserGroup userGroup = UserGroupLocalServiceUtil.fetchUserGroup(
			userGroupId);

		if (userGroup != null ) {
			ruleInstance.setTypeSettings(userGroup.getUuid());

			portletDataContext.addReferenceElement(
				ruleInstance, ruleInstanceElement, userGroup,
				PortletDataContext.REFERENCE_TYPE_WEAK, true);

			return;
		}

		throw new PortletDataException(
			getExportImportErrorMessage(
				userSegment, ruleInstance, UserGroup.class.getName(),
				String.valueOf(userGroupId), Constants.EXPORT));
	}

	@Override
	public String getIcon() {
		return "icon-group";
	}

	@Override
	public String getRuleCategoryKey() {
		return UserAttributesRuleCategory.KEY;
	}

	@Override
	public String getSummary(RuleInstance ruleInstance, Locale locale) {
		try {
			long userGroupId = GetterUtil.getLong(
				ruleInstance.getTypeSettings());

			UserGroup userGroup = UserGroupLocalServiceUtil.fetchUserGroup(
				userGroupId);

			if (userGroup == null) {
				return StringPool.BLANK;
			}

			return userGroup.getName();
		}
		catch (Exception e) {
		}

		return StringPool.BLANK;
	}

	@Override
	public void importData(
			PortletDataContext portletDataContext, UserSegment userSegment,
			RuleInstance ruleInstance)
		throws Exception {

		String userGroupUuid = ruleInstance.getTypeSettings();

		UserGroup userGroup =
			UserGroupLocalServiceUtil.fetchUserGroupByUuidAndCompanyId(
					userGroupUuid, portletDataContext.getCompanyId());

		if (userGroup != null ) {
			ruleInstance.setTypeSettings(
				String.valueOf(userGroup.getUserGroupId()));

			return;
		}

		throw new PortletDataException(
			getExportImportErrorMessage(
				userSegment, ruleInstance, UserGroup.class.getName(),
				userGroupUuid, Constants.IMPORT));
	}

	@Override
	public String processRule(
		PortletRequest request, PortletResponse response, String id,
		Map<String, String> values) {

		return values.get("userGroupId");
	}

	@Override
	protected void populateContext(
		RuleInstance ruleInstance, Map<String, Object> context,
		Map<String, String> values) {

		long userGroupId = 0;

		if (!values.isEmpty()) {
			userGroupId = GetterUtil.getLong(values.get("userGroupId"));
		}
		else if (ruleInstance != null) {
			userGroupId = GetterUtil.getLong(ruleInstance.getTypeSettings());
		}

		context.put("userGroupId", userGroupId);

		Company company = (Company)context.get("company");

		List<UserGroup> userGroups = new ArrayList<UserGroup>();
        UserGroup userGroup = null;
        User user = (User)context.get("user"); /* So we don't get nullpointer, but will not be able to choose groups user is member of */
        RenderResponse renderResponse = (RenderResponse)context.get("renderResponse");
		try {
			userGroups = UserGroupLocalServiceUtil.getUserGroups(
				company.getCompanyId());
            userGroup = UserGroupLocalServiceUtil.getUserGroup(userGroupId);
            user = company.getDefaultUser();
		}
		catch (Exception e) {
            LOG.warn(e.getMessage());
		}

        JSONObject groups = JSONFactoryUtil.createJSONObject();

        Iterator<UserGroup> iterator = userGroups.iterator();
        while (iterator.hasNext()) {
            UserGroup ug = iterator.next();
            groups.put(String.valueOf(ug.getUserGroupId()), ug.getDescription().replaceAll("'",""));
        }

        context.put("userGroup", userGroup);
        context.put("userGroups", userGroups);
        context.put("groupDescription", groups.toString());

        HashMap<String, String> selectUserGroupURLParams = new HashMap<String, String>();
        selectUserGroupURLParams.put("struts_action", "/user_groups_admin/select_user_group");
        selectUserGroupURLParams.put("p_u_i_d", String.valueOf(user.getUserId()));
        selectUserGroupURLParams.put("eventName", renderResponse.getNamespace()+"selectUserGroup");

        context.put(
                "selectUserGroupURL",
                ContentTargetingContextUtil.getControlPanelPortletURL(
                        context, PortletKeys.USER_GROUPS_ADMIN, selectUserGroupURLParams, LiferayWindowState.POP_UP));

		if ((userGroups == null) || userGroups.isEmpty()) {
			boolean hasUserGroupsAdminViewPermission =
				ContentTargetingContextUtil.
					hasControlPanelPortletViewPermission(
						context, PortletKeys.USER_GROUPS_ADMIN);

			if (hasUserGroupsAdminViewPermission) {
				context.put(
					"userGroupsAdminURL",
					ContentTargetingContextUtil.getControlPanelPortletURL(
						context, PortletKeys.USER_GROUPS_ADMIN, null));
			}
		}
	}

}
