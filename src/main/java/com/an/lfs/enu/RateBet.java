package com.an.lfs.enu;

public enum RateBet {
    INVALID("INVALID"), //
    PASS("Pass"), //
    FAIL("Fail"); //

    public boolean isInvalid() {
        return val.equals(INVALID.getVal());
    }

    public boolean isPass() {
        return val.equals(PASS.getVal());
    }

    public boolean isFail() {
        return val.equals(FAIL.getVal());
    }

    private String val;

    private RateBet(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
