package com.an.lfs.enu;

/**
 * Parse from average rate<br>
 * 2.1 3.1 4.1 -> Win<br>
 * 3.1 2.1 4.1 -> Draw<br>
 * 4.1 3.1 2.1 -> Lose<br>
 */
public enum ForecastRet {
    NULL("NULL"), //
    WIN("Win"), //
    DRAW("Draw"), //
    LOSE("Lose");

    public boolean isInvalid() {
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

    private ForecastRet(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
