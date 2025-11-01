package com.springreport.base;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import com.springreport.util.ListUtil;
import com.springreport.util.StringUtil;

public class ServerTablePolicy extends DynamicTableRenderPolicy {
    @Override
    public void render(XWPFTable xwpfTable, Object tableData) throws Exception {
        if (null == tableData) {
            return;
        }
        int rowSize = xwpfTable.getRows().size();
        // 参数数据声明
        ServerTableData serverTableData = (ServerTableData) tableData;
        List<RowRenderData> serverDataList = serverTableData.getServerDataList();

        if (CollectionUtils.isNotEmpty(serverDataList)) {
            XWPFTableRow row = xwpfTable.getRow(rowSize-1);
            for (int i = 0; i < serverDataList.size(); i++) {
            	XWPFTableRow newRow = xwpfTable.insertNewTableRow(rowSize+i);
            	createCellsAndCopyStyles(newRow, row);
                // 渲染一行数据
                TableRenderPolicy.Helper.renderRow(newRow, serverDataList.get(i));
			}
            
            // 处理合并
            if(!StringUtil.isEmptyMap(serverTableData.getMergeInfos())) {
            	for (Integer key : serverTableData.getMergeInfos().keySet()) {
            		List<WordTableMerge> merges = serverTableData.getMergeInfos().get(key);
            		if(ListUtil.isNotEmpty(merges)) {
            			for (int i = 0; i < merges.size(); i++) {
            				TableTools.mergeCellsVertically(xwpfTable, key, merges.get(i).getStr()+rowSize, merges.get(i).getEdr()+rowSize);
						}
            		}
            	}

            }
            xwpfTable.removeRow(rowSize-1);
        }
    }
    
    private static void createCellsAndCopyStyles(XWPFTableRow targetRow, XWPFTableRow sourceRow) {
        targetRow.getCtRow().setTrPr(sourceRow.getCtRow().getTrPr());
        List<XWPFTableCell> tableCells = sourceRow.getTableCells();
        if (CollectionUtils.isEmpty(tableCells)) {
            return;
        }
        for (XWPFTableCell sourceCell : tableCells) {
            XWPFTableCell newCell = targetRow.addNewTableCell();
            newCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
            List<XWPFParagraph> sourceParagraphs = sourceCell.getParagraphs();
            if (CollectionUtils.isEmpty(sourceParagraphs)) {
                continue;
            }
            XWPFParagraph sourceParagraph = sourceParagraphs.get(0);
            List<XWPFParagraph> targetParagraphs = newCell.getParagraphs();
            if (CollectionUtils.isEmpty(targetParagraphs)) {
                XWPFParagraph p = newCell.addParagraph();
                p.getCTP().setPPr(sourceParagraph.getCTP().getPPr());
                XWPFRun run = p.getRuns().isEmpty() ? p.createRun() : p.getRuns().get(0);
                run.setFontFamily(sourceParagraph.getRuns().get(0).getFontFamily());
            } else {
                XWPFParagraph p = targetParagraphs.get(0);
                p.getCTP().setPPr(sourceParagraph.getCTP().getPPr());
                XWPFRun run = p.getRuns().isEmpty() ? p.createRun() : p.getRuns().get(0);
                run.setFontFamily(ListUtil.isNotEmpty(sourceParagraph.getRuns())?sourceParagraph.getRuns().get(0).getFontFamily():null);
            }
        }
    }
    private XWPFTableRow deepCloneRow(XWPFTable table, XWPFTableRow row) throws Exception {
        CTRow ctrow = CTRow.Factory.parse(row.getCtRow().newInputStream());//重点行
        XWPFTableRow newRow = new XWPFTableRow(ctrow, table);
        return newRow;
    }
}
