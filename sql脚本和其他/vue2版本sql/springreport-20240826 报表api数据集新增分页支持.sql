ALTER TABLE report_tpl_dataset ADD COLUMN current_page_attr varchar(50) DEFAULT NULL COMMENT '当前页码属性'; 
ALTER TABLE report_tpl_dataset ADD COLUMN page_count_attr varchar(50) DEFAULT NULL COMMENT '每页显示条数属性'; 
ALTER TABLE report_tpl_dataset ADD COLUMN total_attr varchar(50) DEFAULT NULL COMMENT '数据总条数属性'; 