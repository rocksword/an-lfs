/**
 * 
 */
package com.an.lfs.vo;

public class MatchDiffRow {
    // Raw value
    private int year;
    private int turn;
    private String date;
    private String host;
    private String guest;
    private String score;
    private float win;
    private float draw;
    private float lose;
    // Computed value
    private float diffVal;

    public MatchDiffRow() {
    }

    @Override
    public String toString() {
        return "MatchDiffRow [year=" + year + ", turn=" + turn + ", date=" + date + ", host=" + host + ", guest="
                + guest + ", score=" + score + ", win=" + win + ", draw=" + draw + ", lose=" + lose + ", diffVal="
                + diffVal + "]";
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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

    public float getDiffVal() {
        return diffVal;
    }

    public void setDiffVal(float diffVal) {
        this.diffVal = diffVal;
    }
}
