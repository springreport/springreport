ALTER TABLE luckysheet_report_cell ADD COLUMN source_type tinyint(4) DEFAULT 1 COMMENT '数据字典数据来源 1数据字典 2sql语句 3自定义'; 
ALTER TABLE luckysheet_report_cell ADD COLUMN dict_content text DEFAULT NULL COMMENT 'sql语句或者自定义数据字典内容'; 

ALTER TABLE report_tpl ADD COLUMN is_refresh tinyint(4) DEFAULT 2 COMMENT '是否自动刷新 1是 2否'; 
ALTER TABLE report_tpl ADD COLUMN refresh_time bigint DEFAULT NULL COMMENT '自动刷新时长 单位：秒'; 