package com.an.lfs.vo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Match {
    private static final Log logger = LogFactory.getLog(Match.class);
    // Common
    private int id;
    private int index;
    private int year; // yyyy
    private String time; // MM-dd hh:mm
    private String host;
    private String guest;
    private String score;
    // Simple analyzer
    private float win;
    private float draw;
    private float lose;
    //
    private ScoreResult scoreResult;
    private RateResult rateResult;
    //
    // 1.2,1.4,1.6,1.8,2.0,2.3,2.6,3.0,4.0,5.0
    private String hostCat = null;
    // 1.2,1.4,1.6,1.8,2.0,2.3,2.6,3.0,4.0,5.0
    private String guestCat = null;
    private String middleCat = null;

    //
    public boolean isPass() {
        boolean pass = false;
        if (rateResult.isWin() && scoreResult.isWin()) {
            pass = true;
        } else if (rateResult.isDraw() && scoreResult.isDraw()) {
            pass = true;
        } else if (rateResult.isLose() && scoreResult.isLose()) {
            pass = true;
        }
        return pass;
    }

    public String getScoreResultStr() {
        if (scoreResult.isWin()) {
            return "+";
        } else if (scoreResult.isDraw()) {
            return "=";
        } else {
            return "-";
        }
    }

    /**
     * @return year_index
     */
    public String getSimpleKey() {
        String result = String.format("%s_%02d", year, index);
        return result;
    }

    /**
     * @return year_index_host_guest
     */
    public String getKey() {
        String result = String.format("%s_%02d_%s_%s", year, index, host, guest);
        return result;
    }

    public Match() {
    }

    @Override
    public String toString() {
        return "Match [id=" + id + ", index=" + index + ", year=" + year + ", "
                + (time != null ? "time=" + time + ", " : "") + (host != null ? "host=" + host + ", " : "")
                + (guest != null ? "guest=" + guest + ", " : "") + (score != null ? "score=" + score + ", " : "")
                + "win=" + win + ", draw=" + draw + ", lose=" + lose + ", "
                + (scoreResult != null ? "scoreResult=" + scoreResult + ", " : "")
                + (rateResult != null ? "rateResult=" + rateResult + ", " : "")
                + (hostCat != null ? "hostCat=" + hostCat + ", " : "")
                + (guestCat != null ? "guestCat=" + guestCat + ", " : "")
                + (middleCat != null ? "middleCat=" + middleCat : "") + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public ScoreResult getScoreResult() {
        return scoreResult;
    }

    public void setScoreResult(ScoreResult scoreResult) {
        this.scoreResult = scoreResult;
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

    public RateResult getRateResult() {
        return rateResult;
    }

    public void setRateResult(RateResult rateResult) {
        this.rateResult = rateResult;
    }

    public String getHostCat() {
        return hostCat;
    }

    public void setHostCat(String hostCat) {
        this.hostCat = hostCat;
    }

    public String getGuestCat() {
        return guestCat;
    }

    public void setGuestCat(String guestCat) {
        this.guestCat = guestCat;
    }

    public String getMiddleCat() {
        return middleCat;
    }

    public void setMiddleCat(String middleCat) {
        this.middleCat = middleCat;
    }
}
