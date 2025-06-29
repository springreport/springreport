ALTER TABLE luckysheet_report_cell ADD COLUMN sub_block_range varchar(50) DEFAULT NULL COMMENT '子循环块范围'; 
ALTER TABLE luckysheet_report_cell ADD COLUMN compare_attr1 varchar(50) DEFAULT NULL COMMENT '同比/环比本期属性'; 
ALTER TABLE luckysheet_report_cell ADD COLUMN compare_attr2 varchar(50) DEFAULT NULL COMMENT '同比/环比同期属性'; 
ALTER TABLE luckysheet_report_block_cell ADD COLUMN is_sub_cell tinyint(4) DEFAULT 2 COMMENT '是否子循环块单元格1是 2否'; 