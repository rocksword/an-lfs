package com.an.lfs.vo;

import com.an.lfs.LfsUtil;
import com.an.lfs.enu.ScoreType;

public class MatchInfo {
    private int turn;
    private String date;
    private String host;
    private String score;
    private String guest;
    private ScoreType scoreType;

    public void setScore(String score) {
        this.scoreType = LfsUtil.getScoreType(score);
        this.score = score;
    }

    public ScoreType getScoreType() {
        return scoreType;
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
