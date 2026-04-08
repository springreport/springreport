ALTER TABLE report_tpl_sheet ADD COLUMN is_loop tinyint(4) DEFAULT 2 COMMENT '是否循环 1是 2否 默认2'; 
ALTER TABLE report_tpl_sheet ADD COLUMN loop_settings text DEFAULT NULL COMMENT '循环设置'; 