package com.an.lfs.enu;

/**
 * 
 * Result value
 */
public enum Result {
    NULL("NULL"), //
    WIN("Win"), //
    DRAW("Draw"), //
    LOSE("Lose");
    public static Result[] allResults = new Result[] { WIN, DRAW, LOSE };

    public boolean isNull() {
        return val.equals(NULL.getVal());
    }

    public boolean isWin() {
        return val.equals(WIN.getVal());
    }

    public boolean isDraw() {
        return val.equals(DRAW.getVal());
    }

    public boolean isLose() {
        return val.equals(LOSE.getVal());
    }

    private String val;

    private Result(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
