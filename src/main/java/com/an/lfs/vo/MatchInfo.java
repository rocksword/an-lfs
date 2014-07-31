package com.an.lfs.vo;

import com.an.lfs.LfsUtil;
import com.an.lfs.enu.BetRet;
import com.an.lfs.enu.ForecastRet;
import com.an.lfs.enu.ScoreType;

public class MatchInfo {
    private int turn;
    private String date;
    private String host;
    private String score;
    private String guest;
    private float win;
    private float draw;
    private float lose;

    private ScoreType scoreType;
    private ForecastRet fcRet;

    public MatchInfo() {
    }

    public void setScore(String score) {
        this.scoreType = LfsUtil.getScoreType(score);
        this.score = score;
    }

    public void setValues(float win, float draw, float lose) {
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.fcRet = LfsUtil.getForecastRet(win, draw, lose);
    }

    public ScoreType getScoreType() {
        return scoreType;
    }

    public ForecastRet getForecastRet() {
        return fcRet;
    }

    public BetRet getBetRet() {
        return LfsUtil.getBetRet(fcRet, scoreType);
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

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
    }

    public float getWin() {
        return win;
    }

    public float getDraw() {
        return draw;
    }

    public float getLose() {
        return lose;
    }
}
