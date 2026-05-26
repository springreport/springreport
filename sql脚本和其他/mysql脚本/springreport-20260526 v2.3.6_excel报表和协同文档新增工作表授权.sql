ALTER TABLE report_range_auth ADD COLUMN auth_type tinyint(4) DEFAULT 1 COMMENT '保护类型 1范围保护 2工作表保护';

ALTER TABLE report_range_auth_user ADD COLUMN range_sheet tinyint(4) DEFAULT 1 COMMENT '范围授权还是工作表授权 1范围授权 2工作表授权';

INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (2059072493129998338, 1626461319411445762, 'onlineTpl_copy', '复制', '复制文档', 8, 1404697301888294915, NULL, NULL, NULL, 1);
