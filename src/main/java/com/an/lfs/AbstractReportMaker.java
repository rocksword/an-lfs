package com.an.lfs;

import java.util.ArrayList;
import java.util.List;

import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.an.lfs.vo.Cell;

public class AbstractReportMaker {
    /**
     * @param size
     * @return
     */
    protected List<Cell> getBlankRow(int size) {
        List<Cell> head = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            head.add(new Cell(""));
        }
        return head;
    }

    /**
     * @param sheet
     * @param rows
     * @throws WriteException
     * @throws RowsExceededException
     */
    protected void addRows(WritableSheet sheet, List<List<Cell>> rows) throws WriteException, RowsExceededException {
        for (int row = 0; row < rows.size(); row++) {
            List<Cell> rowList = rows.get(row);
            for (int col = 0; col < rowList.size(); col++) {
                Cell cell = rowList.get(col);
                if (cell.getFmt() != null) {
                    sheet.addCell(new Label(col, row, cell.getVal(), cell.getFmt()));
                } else {
                    sheet.addCell(new Label(col, row, cell.getVal()));
                }
            }
        }
    }
}
