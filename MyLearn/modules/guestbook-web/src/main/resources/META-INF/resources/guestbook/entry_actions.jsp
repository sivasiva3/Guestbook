<%@ include file="/init.jsp" %>
 <%@ taglib uri="http://liferay.com/tld/ui" prefix="liferay-ui"%>
<%@ page import="com.liferay.portal.kernel.util.ParamUtil" %>
<%@ page import="com.liferay.docs.guestbook.model.GuestbookEntry" %>
<%@ page import="com.liferay.portal.kernel.dao.search.ResultRow" %>
<%@ page import="com.liferay.portal.kernel.util.WebKeys" %>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<portlet:defineObjects />

<%
	String mvcPath=ParamUtil.getString(request,"mvcPath");
	ResultRow row=(ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	GuestbookEntry entry=(GuestbookEntry)row.getObject();
%>
<liferay-ui:icon-menu>
	<portlet:renderURL var="editURL">
		<portlet:param name="entryId" value="<%= String.valueOf(entry.getEntryId()) %>"/>
		<portlet:param name="mvcPath" value="/guestbook/edit_entry.jsp" />
	</portlet:renderURL>
	<liferay-ui:icon image="edit" message="Edit" url="<%= editURL.toString() %>"></liferay-ui:icon>
	<portlet:actionURL name="deleteEntry" var="deleteURL">
		<portlet:param name="entryId" value="<%= String.valueOf(entry.getEntryId()) %>"/>
		 <portlet:param name="guestbookId" value="<%= String.valueOf(entry.getGuestbookId()) %>"/>
	</portlet:actionURL>
	<liferay-ui:icon-delete url="<%=deleteURL.toString() %>"/>
</liferay-ui:icon-menu>
