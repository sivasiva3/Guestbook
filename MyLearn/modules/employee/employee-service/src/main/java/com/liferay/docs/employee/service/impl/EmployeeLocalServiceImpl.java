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

package com.liferay.docs.employee.service.impl;

import com.liferay.docs.employee.model.Employee;
import com.liferay.docs.employee.service.base.EmployeeLocalServiceBaseImpl;
import com.liferay.portal.aop.AopService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import java.util.Date;
import java.util.List;

import org.osgi.service.component.annotations.Component;

/**
 * @author Brian Wing Shun Chan
 */
@Component(
	property = "model.class.name=com.liferay.docs.employee.model.Employee",
	service = AopService.class
)
public class EmployeeLocalServiceImpl extends EmployeeLocalServiceBaseImpl {
	
	//Create method for employee
	public Employee addEmployee(long userId,long groupId,String name,String department,String role,
			long salary,Date joinedDate,ServiceContext service) throws PortalException{
		
		Date now=new Date();
		
		long employeeId=counterLocalService.increment(Employee.class.getName());
		
		Employee employee=employeePersistence.create(employeeId);
		
		employee.setUuid(PortalUUIDUtil.generate());
		employee.setGroupId(groupId);
		employee.setCompanyId(service.getCompanyId());
		
		employee.setUserId(userId);
		employee.setCreateDate(service.getCreateDate(now));
		employee.setModifiedDate(service.getModifiedDate(now));
		
		employee.setName(name);
		employee.setDepartment(department);
		employee.setRole(role);
		employee.setSalary(salary);
		employee.setJoinedDate(joinedDate);
		
		employeePersistence.update(employee);
		
		return employee;
	}
	
	//Update method for employee
	public Employee updateEmployee(long employeeId,String name,String department,String role,long salary,Date joinedDate,
			ServiceContext service)throws PortalException {
		
		//method to find the employee to update
		Employee employee = employeePersistence.findByPrimaryKey(employeeId);
		
		Date now =new Date();
		
		employee.setModifiedDate(service.getModifiedDate(now));
		employee.setName(name);
		employee.setDepartment(department);
		employee.setRole(role);
		employee.setSalary(salary);
		employee.setJoinedDate(joinedDate);
		
		employeePersistence.updateImpl(employee);
		
		return null;
		
	}
	
	//Delete method for employee
	public Employee deleteEmployee(long employeeId) throws PortalException{
		
		//method to find the employee by employeeId to delete
		Employee employee =employeePersistence.findByPrimaryKey(employeeId);

		
		//method to delete the employee
		return employeePersistence.remove(employee);
		
	}
	
	//Get single employee
	public Employee getEmployee(long employeeId)throws PortalException{
		
		//method to get the single employee by employeeId
		return employeePersistence.findByPrimaryKey(employeeId);		
	}
	
	//Get all employee
	public List<Employee> getEmployees(long groupId) throws PortalException{
		
		//method to get all the employees by groupId
		return employeePersistence.findByGroupId(groupId);
		
	}
	
	
	public List<Employee> getEmployees(long groupId,long employeeId,int start,int end) throws PortalException{
		
		//method to get all the employees by groupId
		return employeePersistence.findByG_E(groupId, employeeId, start, end);
		
	}
	
	public long getEmployeesCount(long groupId) {
		
		//method to count all the employees by groupId
		return employeePersistence.countByGroupId(groupId);
	}
	
}