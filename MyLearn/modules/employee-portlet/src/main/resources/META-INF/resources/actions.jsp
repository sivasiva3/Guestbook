<%@ include file="\init.jsp" %>

<%
	// Gets the mvcPath parameter from the request
	String mvcPath=ParamUtil.getString(request, "mvcPath");

	//Retrieves the current row object from Search Container
	ResultRow row=(ResultRow) request.getAttribute(WebKeys.SEARCH_CONTAINER_RESULT_ROW);
	
	// Gets the Employee object stored inside the ResultRow
	Employee employee = (Employee) row.getObject();
	

%>

<%-- Starts a dropdown menu for action icons (Edit/Delete) --%>
<liferay-ui:icon-menu>

	<%-- Creates a render URL to navigate to edit.jsp --%>
	<portlet:renderURL var="editURL">
	
		 <%-- Passes the selected employeeId to the edit page --%>
		<portlet:param name="employeeId" value="<%=String.valueOf(employee.getEmployeeId()) %>"/>
		
		<%-- Sets the JSP page to be opened (edit.jsp) --%>
		<portlet:param name="mvcPath" value="/edit.jsp"/>
	</portlet:renderURL>
	
	<%-- Displays the Edit icon in the dropdown which opens edit.jsp with employeeId --%>
	<liferay-ui:icon image="edit" message="Edit" url="<%=editURL.toString() %>" />
	
	<%-- Creates an action URL that triggers deleteEmployee ActionCommand --%>
	<portlet:actionURL var="deleteURL" name="deleteEmployee" >
	
		<%-- Sends employeeId to the delete action so that backend knows which row to delete --%>
		<portlet:param name="employeeId" value="<%=String.valueOf(employee.getEmployeeId()) %>"/>
	</portlet:actionURL>
	
	 <%-- Displays the Delete icon which calls the delete action URL --%>
	<liferay-ui:icon image="delete" message="Delete" url="<%=deleteURL.toString() %>" />
	
</liferay-ui:icon-menu>