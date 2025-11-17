<%@ include file="\init.jsp" %>

<%

	//Read "employeeId" from renderRequest parameters.
	long employeeId = ParamUtil.getLong(renderRequest, "employeeId");
	
	//Created a variable to hold the employee object.
	Employee employee =null;
	
	// If we received a valid employeeId fetch the employee from database.
	if(employeeId>0){
		
		//store the employee details in employee if employeeId received
		employee=EmployeeLocalServiceUtil.getEmployee(employeeId);
	}

%>

<!-- Return URL  -->
<portlet:renderURL var="viewURL">
	<portlet:param name="mvcPath" value="/view.jsp"></portlet:param>
</portlet:renderURL>

<!--Action URL for Submitting the Form  -->
<portlet:actionURL name="addEmployee" var="addEmployeeURL"></portlet:actionURL>

<!--Displays a back arrow linking to view.jsp.  -->
<liferay-ui:header backURL="<%= viewURL.toString() %>" title="<%= employee == null ? "ADD Employee" : employee.getName() %>" />

<!-- Employee form -->
<aui:form action="<%= addEmployeeURL %>" name="<portlet:namespace/>fm">
	
	<!-- Binds form fields to the Employee object. -->
	<aui:model-context bean="<%=employee %>" model="<%= Employee.class %>" />
	
	<!-- Field set for form input  -->
	<aui:fieldset>
		<aui:input type="hidden" name="employeeId" value="<%= employee != null ? employee.getEmployeeId() : 0 %>" />
		<aui:input name="name"/>
		<aui:input name="department"/>
		<aui:input name="role"/>
		<aui:input name="salary"/>
		<aui:input name="joinedDate" type="Date"/>
	</aui:fieldset>
	
	<!--Submit + Cancel Buttons  -->
	<aui:button-row>
		<aui:button type="submit" />
		<aui:button type="cancel" onClick="<%= viewURL.toString() %>"/>
	</aui:button-row>
</aui:form>