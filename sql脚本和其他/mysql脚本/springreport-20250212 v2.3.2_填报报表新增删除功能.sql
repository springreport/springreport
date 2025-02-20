ALTER TABLE report_forms_datasource_attrs ADD COLUMN fill_value varchar(50) DEFAULT NULL COMMENT '填充值'; 
ALTER TABLE report_forms_datasource_attrs ADD COLUMN delete_type tinyint(4) DEFAULT 1 COMMENT '删除方式 1物理删除 2逻辑删除'; 
ALTER TABLE report_forms_datasource_attrs ADD COLUMN delete_value varchar(50) DEFAULT NULL COMMENT '删除值'; 

