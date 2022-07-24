package com.test.okr.utils.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

/**
 * @author ChenJiWei
 * @version V1.0
 * @date 2022/07/15
 * @description 自适应高度调整
 */
public class SelfAdaptiveHandler implements CellWriteHandler {

    private int maxCharacterCount;

    public SelfAdaptiveHandler(int maxCharacterCount) {
        if (maxCharacterCount < 0) {
            this.maxCharacterCount = 100;
        } else {
            this.maxCharacterCount = maxCharacterCount;
        }
    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        CellWriteHandler.super.afterCellDispose(writeSheetHolder, writeTableHolder, cellDataList, cell, head, relativeRowIndex, isHead);
        if (!isHead && cell.getColumnIndex() == 2) {
            final String value = cell.getStringCellValue();
            final int length = value.split("；\r\n", 10).length;
            cell.getRow().setHeight((short) (460 + (length - 1) * 300));
            //cell.getSheet().setColumnWidth(cell.getColumnIndex(), maxCharacterCount * 500);
        }
    }
}
