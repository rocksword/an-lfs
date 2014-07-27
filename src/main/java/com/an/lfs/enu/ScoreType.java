package com.an.lfs.enu;

/**
 * 
 * Parse from score<br>
 * 3-1 -> Win<br>
 * 2-2 -> Draw<br>
 * 1-3 -> Lose<br>
 */
public enum ScoreType {
    INVALID("INVALID"), //
    WIN("Win"), //
    DRAW("Draw"), //
    LOSE("Lose");
    public static ScoreType[] allScoreTypes = new ScoreType[] { WIN, DRAW, LOSE };

    public boolean isInvalid() {
        return val.equals(INVALID.getVal());
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

    private ScoreType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
