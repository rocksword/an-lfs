package com.an.lfs.vo;

public class ClaimRate {
    private int id;
    private String comp;
    private float win;
    private float draw;
    private float lose;
    private float winEnd;
    private float drawEnd;
    private float loseEnd;

    public ClaimRate() {
    }

    @Override
    public String toString() {
        return "ClaimRate [id=" + id + ", comp=" + comp + ", win=" + win + ", draw=" + draw + ", lose=" + lose
                + ", winEnd=" + winEnd + ", drawEnd=" + drawEnd + ", loseEnd=" + loseEnd + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public float getWin() {
        return win;
    }

    public void setWin(float win) {
        this.win = win;
    }

    public float getDraw() {
        return draw;
    }

    public void setDraw(float draw) {
        this.draw = draw;
    }

    public float getLose() {
        return lose;
    }

    public void setLose(float lose) {
        this.lose = lose;
    }

    public float getWinEnd() {
        return winEnd;
    }

    public void setWinEnd(float winEnd) {
        this.winEnd = winEnd;
    }

    public float getDrawEnd() {
        return drawEnd;
    }

    public void setDrawEnd(float drawEnd) {
        this.drawEnd = drawEnd;
    }

    public float getLoseEnd() {
        return loseEnd;
    }

    public void setLoseEnd(float loseEnd) {
        this.loseEnd = loseEnd;
    }
}
