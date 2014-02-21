insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_001', '/html/main.html', '/html/main.html', '首页', 0, '', 1);
insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_001_001', '/html/rule/rule.html', '/html/rule/rule.html', '最新检查结果', 1, '', 1);
insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_001_002', '/html/rule/rule.html', '/html/rule/rule.html', '历史检查结果', 1, '', 2);

insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_002', '/html/rule/rulequery.html', '/html/rule/rulequery.html', '查询', 0, '', 2);
insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_002_001', '/html/rule/subpages/resultList.html', '/html/rule/subpages/resultList.html', '执行结果查询', 1, '', 1);
insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_002_002', '/html/rule/rule.html', '/html/rule/rule.html', '历史检查结果', 1, '', 2);

insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_003', '/html/report/report.html', '/html/report/report.html', '报表统计', 0, '', 3);
insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_003_001', '/html/report/subpages/reportTends.jsp', '/html/report/subpages/reportTends.jsp', '趋势报表', 1, '', 1);
insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_003_002', '/html/report/subpages/reportPassRate.html', '/html/report/subpages/reportPassRate.html', '通过率', 1, '', 2);
insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_003_003', '/html/rule/rule.html', '/html/rule/rule.html', '部门报表统计', 1, '', 3);

insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_004', '/html/account/account.html', '/html/account/account.html', '账户管理', 0, '', 4);
insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_004_001', '/html/account/subpages/users.html', '/html/account/subpages/users.html', '用户管理', 1, '', 1);
insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_004_002', '/html/account/subpages/editpwd.html', '/html/account/subpages/editpwd.html', '修改密码', 1, '', 2);

insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_005', '/html/manager/manager.html', '/html/manager/manager.html', '系统管理', 0, '', 5);
insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_005_001', '/html/manager/subpages/resource.html', '/html/manager/subpages/resource.html', '资源管理', 1, '', 1);
insert into rv_resource (resourceid, resourcecode, url, resourcename, securitylevel, moduleid, seq) values ('res_005_002', '/html/manager/subpages/menus.html', '/html/manager/subpages/menus.html', '菜单管理', 1, '', 2);