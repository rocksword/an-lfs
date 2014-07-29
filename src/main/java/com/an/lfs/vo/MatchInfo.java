package com.an.lfs.vo;

import java.util.ArrayList;
import java.util.List;

import jxl.write.WriteException;

public class MatchInfo {
    private int turn;
    private String date;
    private String host;
    private String score;
    private String guest;
    private float win;
    private float draw;
    private float lose;

    public List<Cell> getRow(int year) throws WriteException {
        List<Cell> row = new ArrayList<>();
        row.add(new Cell(year));
        row.add(new Cell(turn));
        row.add(new Cell(host));
        row.add(new Cell(score));
        row.add(new Cell(guest));
        row.add(new Cell(win));
        row.add(new Cell(draw));
        row.add(new Cell(lose));
        return row;
    }

    public MatchInfo() {
    }

    @Override
    public String toString() {
        return "MatchInfo [turn=" + turn + ", " + (date != null ? "date=" + date + ", " : "")
                + (host != null ? "host=" + host + ", " : "") + (score != null ? "score=" + score + ", " : "")
                + (guest != null ? "guest=" + guest + ", " : "") + "win=" + win + ", draw=" + draw + ", lose=" + lose
                + "]";
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
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
}
