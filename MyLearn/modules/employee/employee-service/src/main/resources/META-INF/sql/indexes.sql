create index IX_170D8BA on EB_Employee (groupId, department[$COLUMN_LENGTH:75$]);
create index IX_D25CA811 on EB_Employee (groupId, employeeId);
create index IX_CF521AF9 on EB_Employee (groupId, name[$COLUMN_LENGTH:75$], department[$COLUMN_LENGTH:75$]);
create index IX_9D25B0EB on EB_Employee (groupId, role_[$COLUMN_LENGTH:75$], department[$COLUMN_LENGTH:75$]);
create index IX_79D93C6A on EB_Employee (uuid_[$COLUMN_LENGTH:75$], companyId);
create unique index IX_6A79D56C on EB_Employee (uuid_[$COLUMN_LENGTH:75$], groupId);