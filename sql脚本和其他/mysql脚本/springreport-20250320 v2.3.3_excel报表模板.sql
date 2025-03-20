ALTER TABLE report_tpl ADD COLUMN is_template tinyint(4) DEFAULT 2 COMMENT '是否是模板 1是 2否'; 
ALTER TABLE report_tpl ADD COLUMN template_field bigint(20) DEFAULT NULL COMMENT '模板所属行业'; 