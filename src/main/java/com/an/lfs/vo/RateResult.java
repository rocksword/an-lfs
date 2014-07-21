package com.an.lfs.vo;

/**
 * 1 2 3 -> WIN<br>
 * 3 2 3 -> DRAW<br>
 * 3 2 1 -> LOSE<br>
 */
public enum RateResult {
    WIN(1), //
    DRAW(0), //
    LOSE(-1);

    private int val;

    private RateResult(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
