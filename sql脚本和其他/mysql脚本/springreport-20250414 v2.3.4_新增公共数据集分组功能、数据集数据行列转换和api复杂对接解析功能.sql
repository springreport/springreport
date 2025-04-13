ALTER TABLE luckysheet_report_cell ADD COLUMN is_object bit(1) DEFAULT b'0' COMMENT '是否是复杂对象'; 
ALTER TABLE luckysheet_report_cell ADD COLUMN data_type tinyint(4) DEFAULT 1 COMMENT '数据类型 1数组 2对象数组 3对象'; 
ALTER TABLE luckysheet_report_cell ADD COLUMN data_attr varchar(100) DEFAULT NULL COMMENT '属性'; 
ALTER TABLE luckysheet_report_cell ADD COLUMN sub_extend tinyint(4) DEFAULT 1 COMMENT '子数据扩展方向 1不扩展 2纵向扩展 3横向扩展'; 


ALTER TABLE report_tpl_dataset ADD COLUMN is_common tinyint(4) DEFAULT 2 COMMENT '是否公共数据集 1是 2否'; 
ALTER TABLE report_tpl_dataset ADD COLUMN common_type tinyint(4) DEFAULT 1 COMMENT '类型 1excel报表 2word报表 3ppt报表'; 
ALTER TABLE report_tpl_dataset ADD COLUMN is_convert tinyint(4) DEFAULT 2 COMMENT '是否行列转置 1是 2否'; 
ALTER TABLE report_tpl_dataset ADD COLUMN header_name varchar(100) DEFAULT NULL COMMENT '行转列字段'; 
ALTER TABLE report_tpl_dataset ADD COLUMN value_field varchar(100) DEFAULT NULL COMMENT '行转列值'; 
ALTER TABLE report_tpl_dataset ADD COLUMN fixed_column varchar(200) DEFAULT NULL COMMENT '固定列'; 

