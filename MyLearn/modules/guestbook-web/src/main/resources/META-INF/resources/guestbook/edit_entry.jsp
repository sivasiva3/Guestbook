<%@ include file="../init.jsp" %>

<%
 	long entryId = ParamUtil.getLong(renderRequest,"entryId");
 	GuestbookEntry entry=null;
 	long guestbookId=ParamUtil.getLong(renderRequest,"guestbookId");
 	if(entryId>0){
 		entry=GuestbookEntryLocalServiceUtil.getGuestbookEntry(entryId);
 	}
%>

<portlet:renderURL var="viewURL">
<portlet:param name="mvcPath" value="/guestbook/view.jsp"></portlet:param>
</portlet:renderURL>
 
<portlet:actionURL name="addEntry" var="addEntryURL" />
  
<liferay-ui:header backURL="<%= viewURL.toString() %>" title="<%= entry == null ? "ADD Entry" : entry.getName() %>" />
    
<aui:form action="<%= addEntryURL %>" name="<portlet:namespace />fm">
	
	 <aui:model-context bean="<%= entry %>" model="<%= GuestbookEntry.class %>" />
	
	 <aui:fieldset>
		 <aui:input name="name" />
		 <aui:input name="email" />
		 <aui:input name="message" />
		 <aui:input name="entryId" type="hidden" />
		 <aui:input name="guestbookId" type="hidden" value='<%= guestbookId %>'/>
	 </aui:fieldset>
	
	 <aui:fieldset>
	 	<liferay-asset:asset-categories-selector className="<%= GuestbookEntry.class.getName() %>" classPK="<%= entryId %>"/>
	 	<liferay-asset:asset-tags-selector className="<%= GuestbookEntry.class.getName() %>" classPK="<%= entryId %>" />
	 </aui:fieldset>
	
	 <aui:fieldset collapsed="<%= true %>" collapsible="<%=true %>" label="related-assets">
	 	<lifery-asset:input-asset-links className="<%= GuestbookEntry.class.getName() %>" classPK="<%= entryId %>" />
	 </aui:fieldset>
	
	 <aui:button-row>
		 <aui:button type="submit"></aui:button>
		 <aui:button type="cancel" onClick="<%= viewURL.toString() %>"></aui:button>
	 </aui:button-row>
	 
 </aui:form>