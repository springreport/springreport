ALTER TABLE report_type ADD COLUMN type tinyint(4) DEFAULT 1 COMMENT '类型 1excel报表 2word报表 3协同文档 4大屏';

ALTER TABLE online_tpl ADD COLUMN report_type bigint(20) DEFAULT NULL COMMENT '类型';

ALTER TABLE doc_tpl ADD COLUMN report_type bigint(20) DEFAULT NULL COMMENT '类型';

ALTER TABLE screen_tpl ADD COLUMN report_type bigint(20) DEFAULT NULL COMMENT '类型';

UPDATE sys_menu set del_flag = 2 where id = 1405299504554487810;

UPDATE sys_api set del_flag = 2 where id = 1405305445274398722;

UPDATE sys_api set del_flag = 2 where id = 1424221352564113410;

INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1854856564566609922, 1405300363866714113, 'reportTpl_folder', '新建目录', '新建目录', 12, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1854865751543582721, 1626461319411445762, 'onlineTpl_folder', '新建目录', '新建目录', 7, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1854867606784905217, 1786056695102541825, 'docTpl_folder', '新建目录', '新建目录', 8, 1404697301888294915, NULL, NULL, NULL, 1);
INSERT INTO sys_api(id, menu_id, api_code, api_name, api_function, sort, creator, create_time, updater, update_time, del_flag) VALUES (1854867819943628801, 1421971956013498370, 'screenTpl_folder', '新建目录', '新建目录', 12, 1404697301888294915, NULL, NULL, NULL, 1);
