package com.springreport.dto.doctpl;

import lombok.Data;

@Data
public class DocDto {

	/** header - 页眉 */
    private String header = "[]";

    /** footer - 页脚 */
    private String footer = "[]";
    
    /** main - 内容 */
    private String main = "[]";

    /** paper_direction - 纸张方向 vertical纵向 horizontal横向 */
    private String paperDirection = "vertical";

    /** width - 宽度 */
    private Integer width = 794;

    /** height - 高度 */
    private Integer height = 1123;
    
    /** margins - 页边距 */
    private String margins = "[]";
}
