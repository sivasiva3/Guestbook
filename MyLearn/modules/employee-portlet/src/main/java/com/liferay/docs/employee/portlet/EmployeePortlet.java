package com.liferay.docs.employee.portlet;

import com.liferay.docs.employee.constants.EmployeePortletKeys;
import com.liferay.docs.employee.model.Employee;
import com.liferay.docs.employee.service.EmployeeLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.util.ParamUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author KCDC 1
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Employee",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + EmployeePortletKeys.EMPLOYEE,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class EmployeePortlet extends MVCPortlet {
	// Using this reference we can use the LocalService Methods in Portlet.
	//Injecting the Service Builder API into the MVC Portlet using @Reference
	@Reference
	private EmployeeLocalService _employeeLocalService;
	
	//add or update a employee
	public void addEmployee(ActionRequest actionRequest,ActionResponse actionResponse)throws PortalException{
		
		//Creates a ServiceContext object from the current request (actionRequest) for the Employee entity.
		ServiceContext serviceContext=ServiceContextFactory.getInstance(Employee.class.getName(),actionRequest);
		
		SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
		
		// Retrieves the field values entered by the user from the actionRequest safely  
		String name = ParamUtil.getString(actionRequest, "name");
		String department = ParamUtil.getString(actionRequest, "department");
		String role=ParamUtil.getString(actionRequest,"role");
		long salary = ParamUtil.getLong(actionRequest, "salary");
		long employeeId=ParamUtil.getLong(actionRequest,"employeeId");
		Date joinedDate= ParamUtil.getDate(actionRequest, "joinedDate", dateFormat);
		
		try {
			
			//Update existing employee if employee already exists.
			if(employeeId >0) {
				
				// Calls the EmployeeLocalService to update a new employee record
				_employeeLocalService.updateEmployee(employeeId, name, department, role, salary, joinedDate, serviceContext);
			}
			
			//add new employee
			else {
				
				// Calls the EmployeeLocalService to add a new employee record
				_employeeLocalService.addEmployee(serviceContext.getUserId(), serviceContext.getScopeGroupId(),
						name, department, role, salary, joinedDate, serviceContext);
				
			}
			// Pass employeeId to the render phase so the next view can show this guestbook
			actionResponse.setRenderParameter("employeeId", Long.toString(employeeId));
		}
		catch(PortalException e) {
			e.printStackTrace();
		}
		
		// After added employee redirect to a view.jsp page
		actionResponse.setRenderParameter("mvcPath", "/view.jsp");
	}
	
	// Delete a employee
	public void deleteEmployee(ActionRequest actionRequest,ActionResponse actionResponse) throws PortalException{
		
		// Retrieves the field employeeId
		long employeeId =ParamUtil.getLong(actionRequest,"employeeId");
		
		try {
			
			// Pass employeeId so the same guestbook reloads after delete
			actionResponse.setRenderParameter("employeeId", Long.toString(employeeId));
			
			// Delete the entry from the database using local service
			_employeeLocalService.deleteEmployee(employeeId);
			
		}
		catch(PortalException e) {
			e.printStackTrace();
		}
		
		// After deleted employee redirect to a view.jsp page
		actionResponse.setRenderParameter("employeeId", "");
		actionResponse.setRenderParameter("mvcPath", "/view.jsp");
			
	}	
	
	// Load all employees and selected employee for display
	@Override
	public void render(RenderRequest renderRequest,RenderResponse renderResponse) throws IOException, PortletException{
		try {
			
			
			ServiceContext serviceContext=ServiceContextFactory.getInstance(Employee.class.getName(),renderRequest);

		    // Get the current site/group ID
			long groupId=serviceContext.getScopeGroupId();
			
			// Get the employeeId from the request parameters
			long employeeId= ParamUtil.getLong(renderRequest, "employeeId");
			
			// Get all employees that belong to the current group/site from the service layer
			List<Employee> employees=_employeeLocalService.getEmployees(groupId);
			

	        // If a specific employeeId exists, fetch that employee too
	        if (employeeId > 0) {
	            Employee employee = _employeeLocalService.getEmployee(employeeId);
	            renderRequest.setAttribute("employee", employee);
	        }
			// Store the employees list in the request, so it can be accessed in the JSP
			renderRequest.setAttribute("employees", employees);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// Calls the main render method to display the JSP page on the screen
		super.render(renderRequest, renderResponse);
	}
	
}