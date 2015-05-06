<#assign aui = PortletJspTagLibs["/META-INF/aui.tld"] />
<#assign liferay_ui = PortletJspTagLibs["/META-INF/liferay-ui.tld"] />
<#assign portlet = PortletJspTagLibs["/WEB-INF/tld/liferay-portlet.tld"]>
<#assign namespace><@portlet.namespace/></#assign>

<#setting number_format="computer">

<#if !userGroups?has_content >
	<div class="alert alert-warning">
		<strong><@liferay_ui["message"] key="there-are-no-user-groups-available" /></strong>

		<#assign enableLocationLabel = languageUtil.get(locale, "control-panel-user-groups") />

		<#if userGroupsAdminURL??>
			<#assign enableLocationLabel = "<a href=\"" + userGroupsAdminURL + "\">" + enableLocationLabel + "</a>" />
		</#if>

		<@liferay_ui["message"] arguments=enableLocationLabel key="user-groups-can-be-managed-in-x" />
	</div>
<#else>
    <@aui["input"] type="hidden" name="userGroupId" value="${userGroupId}"/>
    <div id="${namespace}userGroupDisplayName">${(userGroup.name)!""}</div>
    <div id="${namespace}userGroupDisplayDescription" class="field-description">${(userGroup.description)!""}</div>
    <@aui["button"] name="selectGroup" value="select"/>
</#if>

<@aui.script use="aui-base,escape">
  var groups = JSON.parse('${groupDescription}');
  A.one('#${namespace}selectGroup').on(
    'click',
    function(event) {
      Liferay.Util.selectEntity({
        dialog: {
          constrain: true,
          destroyOnHide: true,
          modal: true,
          width: 680
        },
        id: '${namespace}selectUserGroup',
        title: '<@liferay_ui["message"] arguments="user-group" key="select-x" />',
        uri: '${selectUserGroupURL}'
      },
      function(event) {
        A.one('#${namespace}userGroupDisplayName').setContent(event.usergroupname);
        A.one('#${namespace}userGroupDisplayDescription').setContent(groups[""+event.usergroupid]);
        A.one('#${namespace}userGroupId').set('value', event.usergroupid);
        console.log(event);
      });
    }
  );
</@>