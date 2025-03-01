ALTER TABLE report_forms_datasource ADD COLUMN is_main tinyint(4) DEFAULT 2 COMMENT '是否主数据集 1是 2否'; 
ALTER TABLE report_forms_datasource_attrs ADD COLUMN main_column varchar(100) DEFAULT NULL COMMENT '主表数据列'; 
ALTER TABLE report_forms_datasource_attrs ADD COLUMN main_name varchar(50) DEFAULT NULL COMMENT '主表数据源绑定名'; 
ALTER TABLE report_forms_datasource_attrs ADD COLUMN main_datasource_id bigint(20) DEFAULT NULL COMMENT '主表数据源id'; 
ALTER TABLE report_forms_datasource_attrs ADD COLUMN main_table varchar(50) DEFAULT NULL COMMENT '主表表名称'; 

