package com.an.lfs.vo;

public enum ScoreResult {
    WIN(1), //
    DRAW(0), //
    LOSE(-1);

    private int val;

    private ScoreResult(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
