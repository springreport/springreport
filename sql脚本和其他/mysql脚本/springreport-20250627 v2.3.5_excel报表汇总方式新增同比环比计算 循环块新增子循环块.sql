ALTER TABLE luckysheet_report_cell ADD COLUMN sub_block_range varchar(50) DEFAULT NULL COMMENT '子循环块范围'; 
ALTER TABLE luckysheet_report_cell ADD COLUMN compare_attr1 varchar(50) DEFAULT NULL COMMENT '同比/环比本期属性'; 
ALTER TABLE luckysheet_report_cell ADD COLUMN compare_attr2 varchar(50) DEFAULT NULL COMMENT '同比/环比同期属性'; 