create table EB_Employee (
	uuid_ VARCHAR(75) null,
	employeeId LONG not null primary key,
	groupId LONG,
	companyId LONG,
	userId LONG,
	userName VARCHAR(75) null,
	createDate DATE null,
	modifiedDate DATE null,
	name VARCHAR(75) null,
	department VARCHAR(75) null,
	role_ VARCHAR(75) null,
	salary LONG,
	joinedDate DATE null
);