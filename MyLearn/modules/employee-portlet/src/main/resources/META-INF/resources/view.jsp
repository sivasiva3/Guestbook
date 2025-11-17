<%@ include file="\init.jsp" %>

<%
	// Fetch employeeId from request parameters
	long employeeId=ParamUtil.getLong(request,"employeeId");
	
	// If employeeId is not found in request, try retrieving it from renderRequest attributes
	if(employeeId == 0 && renderRequest.getAttribute("employeeId")!= null){
		employeeId=(Long)renderRequest.getAttribute("employeeId");
	}
	
	// If employeeId is still 0, fetch all employees for the group
	if(employeeId==0){
		
		// Fetch the list of Employee objects for this group.
		List<Employee> employees=EmployeeLocalServiceUtil.getEmployees(scopeGroupId);
		
		// If employees exist
		if(!employees.isEmpty()){
			
			 // pick the first employee as the default
			employeeId =employees.get(0).getEmployeeId(); 
		}
	}
%>



<%-- Button row for adding a new employee --%>
<aui:button-row cssClass="employee-buttons">
	
	<!-- Render URL for Add Employee Button -->
	<portlet:renderURL var="addEmployeeURL">
		<portlet:param name="mvcPath" value="/edit.jsp"/>
		<%-- <portlet:param name="employeeId" value="<%= String.valueOf(employeeId) %>"/> --%>
	</portlet:renderURL>
	
	<!-- Button that navigates to the Add Employee page using the URL created above -->
	<aui:button value="Add Employee" onClick="<%= addEmployeeURL %>"></aui:button>
	
</aui:button-row>

<%-- Initializes the search container and sets the total number of employee records --%>
<liferay-ui:search-container total="<%= EmployeeLocalServiceUtil.getEmployeesCount() %>">

	<%-- Fetches the paginated list of employees for the current page --%>
	<liferay-ui:search-container-results results="<%= EmployeeLocalServiceUtil.getEmployees(searchContainer.getStart(),searchContainer.getEnd()) %>" />
	
	<%-- Defines a row in the table, each row will represent an Employee object --%>
	<liferay-ui:search-container-row className="com.liferay.docs.employee.model.Employee" modelVar="employee">
		
		<%-- Displays the properties of Employee --%> 
		<liferay-ui:search-container-column-text property="name"/>
		<liferay-ui:search-container-column-text property="department"/>
		<liferay-ui:search-container-column-text property="role"/>
		<liferay-ui:search-container-column-text property="salary"/>
		<liferay-ui:search-container-column-text 
    name="Joined Date"
    value="<%= employee.getJoinedDate() == null 
        ? \"\" 
        : new java.text.SimpleDateFormat(\"dd/MM/yyyy\").format(employee.getJoinedDate()) %>" 
/>

		
		<%-- Renders a JSP inside the table column (usually for Edit/Delete actions) --%>
		<liferay-ui:search-container-column-jsp align="center" path="/actions.jsp" />
		
	</liferay-ui:search-container-row>
	
	<%-- Generates pagination controls (Next, Previous, pages list) --%>
	<liferay-ui:search-iterator/>
	
</liferay-ui:search-container>





























