package com.an.lfs.vo;

public class MatchInfo {
    private int turn;
    private String date;
    private String host;
    private String score;
    private String guest;

    public void setScore(String score) {
        this.score = score;
    }

    public MatchInfo() {
    }

    @Override
    public String toString() {
        return "MatchInfo [turn=" + turn + ", date=" + date + ", host=" + host + ", score=" + score + ", guest="
                + guest + "]";
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

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }
}
