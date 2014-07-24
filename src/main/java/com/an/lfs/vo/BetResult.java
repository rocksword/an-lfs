package com.an.lfs.vo;

public enum BetResult {
    INVALID(-9), //
    PASS(1), //
    FAIL(0); //

    public boolean isInvalid() {
        return val == INVALID.getVal();
    }

    public boolean isPass() {
        return val == PASS.getVal();
    }

    public boolean isFail() {
        return val == FAIL.getVal();
    }

    private int val;

    private BetResult(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
