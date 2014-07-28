package com.an.lfs.vo;

import jxl.write.WritableCellFormat;

public class Cell {
    private String val;
    private WritableCellFormat fmt;

    public Cell(String val) {
        this.val = val;
    }

    public Cell(int val) {
        this.val = String.valueOf(val);
    }

    public Cell(float val) {
        this.val = String.valueOf(val);
    }

    public Cell(String val, WritableCellFormat fmt) {
        this.val = val;
        this.fmt = fmt;
    }

    public Cell(int val, WritableCellFormat fmt) {
        this.val = String.valueOf(val);
        this.fmt = fmt;
    }

    public Cell(float val, WritableCellFormat fmt) {
        this.val = String.valueOf(val);
        this.fmt = fmt;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public WritableCellFormat getFmt() {
        return fmt;
    }

    public void setFmt(WritableCellFormat fmt) {
        this.fmt = fmt;
    }
}
