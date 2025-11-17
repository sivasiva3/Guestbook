<%@ include file="/init.jsp" %>

<h2>Welcome</h2>
<portlet:actionURL var="submitURL"/>
<aui:form action="${submitURL}" method="post">
	<aui:fieldset>
		<aui:input type="text" name="name"/>
		<aui:input type="text" name="location"/>
	</aui:fieldset>
	<aui:button type="submit"></aui:button>
	<aui:button type="reset"></aui:button>
</aui:form>
<% 
	String name=(String) request.getAttribute("name");
	String location=(String) request.getAttribute("location");
	
	if(name!=null && !name.isEmpty()){
		System.out.println("name: " +name+" Age: "+location);
	}
%>

	


