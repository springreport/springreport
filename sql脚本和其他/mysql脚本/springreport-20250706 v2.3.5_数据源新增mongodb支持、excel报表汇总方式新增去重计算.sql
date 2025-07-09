ALTER TABLE luckysheet_report_cell ADD COLUMN is_dump bit(1) DEFAULT b'0' COMMENT '是否去重'; 
ALTER TABLE luckysheet_report_cell ADD COLUMN dump_attr varchar(100) DEFAULT NULL COMMENT '去重属性'; 
ALTER TABLE luckysheet_report_cell ADD COLUMN keep_empty_cell bit(1) DEFAULT b'0' COMMENT '没有数据时是否保留空单元格'; 
ALTER TABLE report_tpl_dataset ADD COLUMN mongo_table varchar(200) DEFAULT NULL COMMENT 'mongodb表'; 
ALTER TABLE report_tpl_dataset ADD COLUMN mongo_search_type tinyint(4) DEFAULT 1 COMMENT 'mongo查询方式 1find 2aggregate 其他待扩展'; 
ALTER TABLE report_tpl_dataset ADD COLUMN mongo_order text DEFAULT NULL COMMENT 'mongodb排序方式'; 