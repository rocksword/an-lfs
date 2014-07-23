package com.an.lfs.vo;

public enum ScoreResult {
    INVALID(-9), //
    WIN(1), //
    DRAW(0), //
    LOSE(-1);

    public boolean isInvalid() {
        return val == INVALID.getVal();
    }

    public boolean isWin() {
        return val == WIN.getVal();
    }

    public boolean isDraw() {
        return val == DRAW.getVal();
    }

    public boolean isLose() {
        return val == LOSE.getVal();
    }

    private int val;

    private ScoreResult(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
