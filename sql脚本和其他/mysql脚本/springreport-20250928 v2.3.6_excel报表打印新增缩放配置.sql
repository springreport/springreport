ALTER TABLE report_sheet_pdf_print_setting ADD COLUMN font_multi float(6, 2) DEFAULT 1.00 COMMENT '字体缩放倍数'; 

ALTER TABLE report_sheet_pdf_print_setting ADD COLUMN rowheight_multi float(6, 2) DEFAULT 1.00 COMMENT '行高缩放倍数'; 