ALTER TABLE luckysheet_report_cell ADD COLUMN forms_attrs text DEFAULT NULL COMMENT '填报属性配置'; 

ALTER TABLE report_forms_datasource_attrs ADD COLUMN fill_strategy tinyint(4) DEFAULT 1 COMMENT '填充策略 1 插入 2更新 3插入/更新数据'; 
ALTER TABLE report_forms_datasource_attrs ADD COLUMN fill_type tinyint(4) DEFAULT 1 COMMENT '填充类型 1系统时间 2用户id 3用户名 4商户号'; 

ALTER TABLE report_sheet_pdf_print_setting ADD COLUMN custom_margin tinyint(4) DEFAULT 2 COMMENT '自定义页边距 1是 2否'; 
ALTER TABLE report_sheet_pdf_print_setting ADD COLUMN left_margin int(6) DEFAULT 36 COMMENT '左边距'; 
ALTER TABLE report_sheet_pdf_print_setting ADD COLUMN right_margin int(6) DEFAULT 36 COMMENT '右边距'; 
ALTER TABLE report_sheet_pdf_print_setting ADD COLUMN top_margin int(6) DEFAULT 36 COMMENT '上边距'; 
ALTER TABLE report_sheet_pdf_print_setting ADD COLUMN bottom_margin int(6) DEFAULT 36 COMMENT '下边距'; 
