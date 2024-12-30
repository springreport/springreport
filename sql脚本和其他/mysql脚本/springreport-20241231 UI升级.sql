CREATE TABLE `report_tpl_dataset_group`  (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `tpl_id` bigint(20) NULL DEFAULT NULL COMMENT '模板id',
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '分组名称',
  `creator` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `updater` bigint(20) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 1 COMMENT '删除标记 1未删除 2已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

ALTER TABLE report_tpl_dataset ADD COLUMN group_id bigint DEFAULT NULL COMMENT '分组id'; 

ALTER TABLE report_sheet_pdf_print_setting ADD COLUMN fixed_header tinyint(4) DEFAULT 2 COMMENT '固定表头 1是 2否'; 
ALTER TABLE report_sheet_pdf_print_setting ADD COLUMN fixed_header_start int(8) DEFAULT NULL COMMENT '固定表头起始行'; 
ALTER TABLE report_sheet_pdf_print_setting ADD COLUMN fixed_header_end int(8) DEFAULT NULL COMMENT '固定表头结束行'; 


UPDATE sys_menu SET menu_name = '报表', menu_url = 'report', parent_menu_id = 0, menu_icon = 'icon-baobiaoguanli', is_hidden = 2, sort = 1, creator = NULL,  updater = 1404697301888294915,  del_flag = 1 WHERE id = 1404722852271263746;
UPDATE sys_menu SET menu_name = '报表类型管理', menu_url = 'reportType', parent_menu_id = 1404722852271263746, menu_icon = 'el-icon-s-grid', is_hidden = 2, sort = 1, creator = 1404697301888294914, updater = NULL, del_flag = 2 WHERE id = 1405299504554487810;
UPDATE sys_menu SET menu_name = '数据源管理', menu_url = 'reportDatasource', parent_menu_id = 1404722852271263746, menu_icon = 'icon-shujuyuanguanli', is_hidden = 2, sort = 2, creator = 1404697301888294914,  updater = 1404697301888294915, del_flag = 1 WHERE id = 1405299619029626882;
UPDATE sys_menu SET menu_name = 'Excel报表', menu_url = 'reportTpl', parent_menu_id = 1404722852271263746, menu_icon = 'icon-a-Excelxietongwendang', is_hidden = 2, sort = 3, creator = 1404697301888294914, updater = 1404697301888294915, del_flag = 1 WHERE id = 1405300363866714113;
UPDATE sys_menu SET menu_name = '系统管理', menu_url = 'system', parent_menu_id = 0, menu_icon = 'icon-xitongguanli', is_hidden = 2, sort = 2, creator = 1404697301888294914, updater = 1404697301888294915, del_flag = 1 WHERE id = 1405315562552209410;
UPDATE sys_menu SET menu_name = '用户管理', menu_url = 'sysUser', parent_menu_id = 1405315562552209410, menu_icon = 'icon-yonghuguanli', is_hidden = 2, sort = 1, creator = 1404697301888294914, updater = NULL, del_flag = 1 WHERE id = 1405466770491432962;
UPDATE sys_menu SET menu_name = '角色管理', menu_url = 'sysRole', parent_menu_id = 1405315562552209410, menu_icon = 'icon-jiaoseguanli', is_hidden = 2, sort = 2, creator = 1404697301888294914, updater = 1404697301888294915, del_flag = 1 WHERE id = 1405466871968423938;
UPDATE sys_menu SET menu_name = '菜单管理', menu_url = 'sysMenu', parent_menu_id = 1405315562552209410, menu_icon = 'icon-caidanguanli', is_hidden = 2, sort = 3, creator = 1404697301888294914, updater = 1404697301888294915, del_flag = 1 WHERE id = 1405466972459753473;
UPDATE sys_menu SET menu_name = '菜单功能', menu_url = 'sysApi', parent_menu_id = 1405315562552209410, menu_icon = 'icon-caidanguanli', is_hidden = 1, sort = 4, creator = 1404697301888294914, updater = 1404697301888294915, del_flag = 1 WHERE id = 1405467087488540673;
UPDATE sys_menu SET menu_name = 'Excel报表设计', menu_url = 'reportDesign', parent_menu_id = 1404722852271263746, menu_icon = 'el-icon-pie-chart', is_hidden = 1, sort = 4, creator = 1404697301888294914, updater = 1404697301888294915, del_flag = 1 WHERE id = 1405470840950218754;
UPDATE sys_menu SET menu_name = '大屏模板管理', menu_url = 'screenTpl', parent_menu_id = 1404722852271263746, menu_icon = 'icon-dapingmobanguanli', is_hidden = 2, sort = 5, creator = 1404697301888294914, updater = 1404697301888294915, del_flag = 1 WHERE id = 1421971956013498370;
UPDATE sys_menu SET menu_name = '大屏设计页面', menu_url = 'screenDesign', parent_menu_id = 1404722852271263746, menu_icon = '12', is_hidden = 1, sort = 6, creator = 1404697301888294914, updater = NULL, del_flag = 1 WHERE id = 1424270599875547138;
UPDATE sys_menu SET menu_name = '查看大屏页面', menu_url = 'screenView', parent_menu_id = 1404722852271263746, menu_icon = '12', is_hidden = 1, sort = 7, creator = 1404697301888294914, updater = NULL,  del_flag = 1 WHERE id = 1424270751675797506;
UPDATE sys_menu SET menu_name = '打印模板管理', menu_url = 'printTpl', parent_menu_id = 1404722852271263746, menu_icon = 'el-icon-printer', is_hidden = 2, sort = 12, creator = 1404697301888294914, updater = NULL, del_flag = 2 WHERE id = 1437685292558557185;
UPDATE sys_menu SET menu_name = '大屏模板组件', menu_url = 'screenContent', parent_menu_id = 1404722852271263746, menu_icon = 'icon', is_hidden = 1, sort = 8, creator = 1404697301888294914, updater = NULL, del_flag = 1 WHERE id = 1465604831325470721;
UPDATE sys_menu SET menu_name = '多大屏管理', menu_url = 'multiScreen', parent_menu_id = 1404722852271263746, menu_icon = 'el-icon-copy-document', is_hidden = 2, sort = 9, creator = 1404697301888294914, updater = NULL, del_flag = 2 WHERE id = 1534817266438144001;
UPDATE sys_menu SET menu_name = 'Excel报表查看', menu_url = 'viewReport', parent_menu_id = 1404722852271263746, menu_icon = 'icon-baobiaochakan', is_hidden = 2, sort = 5, creator = 1404697301888294914, updater = 1404697301888294915, del_flag = 1 WHERE id = 1544474827944828930;
UPDATE sys_menu SET menu_name = '报表填报', menu_url = 'luckyReportFroms', parent_menu_id = 1404722852271263746, menu_icon = 'icon', is_hidden = 1, sort = 10, creator = 1404697301888294914, updater = NULL, del_flag = 1 WHERE id = 1597384623035232257;
UPDATE sys_menu SET menu_name = '填报报表预览', menu_url = 'luckyReportFromsPreview', parent_menu_id = 1404722852271263746, menu_icon = 'icon', is_hidden = 1, sort = 13, creator = 1404697301888294914, updater = NULL, del_flag = 1 WHERE id = 1597389706904973314;
UPDATE sys_menu SET menu_name = '数据字典类型', menu_url = 'reportDatasourceDictType', parent_menu_id = 1404722852271263746, menu_icon = 'icon', is_hidden = 1, sort = 1, creator = 1404697301888294914, updater = NULL, del_flag = 1 WHERE id = 1597419165079023618;
UPDATE sys_menu SET menu_name = '数据字典值', menu_url = 'reportDatasourceDictData', parent_menu_id = 1404722852271263746, menu_icon = 'icon', is_hidden = 1, sort = 1, creator = 1404697301888294914, updater = NULL, del_flag = 1 WHERE id = 1597419308230619138;
UPDATE sys_menu SET menu_name = 'Excel协同文档', menu_url = 'onlineTpl', parent_menu_id = 1404722852271263746, menu_icon = 'icon-a-Excelxietongwendang', is_hidden = 2, sort = 3, creator = 1404697301888294914, updater = 1404697301888294915, del_flag = 1 WHERE id = 1626461319411445762;
UPDATE sys_menu SET menu_name = '报表定时任务', menu_url = 'reportTask', parent_menu_id = 1404722852271263746, menu_icon = 'icon', is_hidden = 1, sort = 14, creator = 1404697301888294914, updater = NULL, del_flag = 1 WHERE id = 1685828895406526466;
UPDATE sys_menu SET menu_name = '岗位管理', menu_url = 'sysPost', parent_menu_id = 1405315562552209410, menu_icon = 'icon-gangweiguanli', is_hidden = 2, sort = 4, creator = 1404697301888294914, updater = 1404697301888294915, del_flag = 1 WHERE id = 1738138171403386882;
UPDATE sys_menu SET menu_name = '租户管理', menu_url = 'merchant', parent_menu_id = 0, menu_icon = 'icon-zuhu', is_hidden = 2, sort = 3, creator = 1404697301888294915, updater = 1404697301888294915, del_flag = 1 WHERE id = 1738480133297819649;
UPDATE sys_menu SET menu_name = '权限模板', menu_url = 'sysMerchantAuthTemplate', parent_menu_id = 1738480133297819649, menu_icon = 'icon-quanxian', is_hidden = 2, sort = 1, creator = 1404697301888294915, updater = 1404697301888294915, del_flag = 1 WHERE id = 1738480515709292545;
UPDATE sys_menu SET menu_name = '租户管理', menu_url = 'sysMerchant', parent_menu_id = 1738480133297819649, menu_icon = 'icon-zuhuguanli', is_hidden = 2, sort = 2, creator = 1404697301888294915, updater = NULL, del_flag = 1 WHERE id = 1738480923462750209;
UPDATE sys_menu SET menu_name = '部门管理', menu_url = 'sysDept', parent_menu_id = 1405315562552209410, menu_icon = 'icon-bumenguanli', is_hidden = 2, sort = 5, creator = 1404697301888294915, updater = 1404697301888294915, del_flag = 1 WHERE id = 1738779159977222145;
UPDATE sys_menu SET menu_name = 'word报表', menu_url = 'docTpl', parent_menu_id = 1404722852271263746, menu_icon = 'icon-a-Wordbaobiao', is_hidden = 2, sort = 3, creator = 1404697301888294915, updater = 1404697301888294915, del_flag = 1 WHERE id = 1786056695102541825;
UPDATE sys_menu SET menu_name = 'word模板设计', menu_url = 'docDesign', parent_menu_id = 1404722852271263746, menu_icon = 'icon-Word', is_hidden = 1, sort = 3, creator = 1404697301888294915, updater = 1404697301888294915, del_flag = 1 WHERE id = 1788578745335652354;
UPDATE sys_menu SET menu_name = 'PPT模板管理', menu_url = '/slideTpl', parent_menu_id = 1404722852271263746, menu_icon = 'icon-PPTbaobiao', is_hidden = 2, sort = 10, creator = 1404697301888294915, updater = 1404697301888294915, del_flag = 1 WHERE id = 1829737342576586754;
UPDATE sys_menu SET menu_name = 'PPT模板设计', menu_url = '/slideDesign', parent_menu_id = 1404722852271263746, menu_icon = 'icon', is_hidden = 1, sort = 11, creator = 1404697301888294915, updater = 1404697301888294915, del_flag = 1 WHERE id = 1831206219214909441;
