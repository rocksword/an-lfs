package com.an.lfs.vo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Match {
    private static final Log logger = LogFactory.getLog(Match.class);
    private int id;
    private int index;
    private String year; // yyyy
    private String time; // MM-dd hh:mm
    private String host;
    private String guest;
    private String score;
    private float win;
    private float draw;
    private float lose;

    public Match() {
    }

    private boolean minWin = false;
    private boolean minDraw = false;
    private boolean minLose = false;

    private boolean passBet = false;
    private ScoreResult scoreResult = ScoreResult.WIN;

    public ScoreResult getScoreResult() {
        return scoreResult;
    }

    public String getMatAvg() {
        return win + " " + draw + " " + lose;
    }

    public String getMatPass() {
        return passBet ? "T" : "F";
    }

    // true - win the bet, false - lose the bet
    public boolean isPassBet() {
        return passBet;
    }

    public boolean isMinWin() {
        return minWin;
    }

    public boolean isMinDraw() {
        return minDraw;
    }

    public boolean isMinLose() {
        return minLose;
    }

    public void initPassBet() {
        float min = win;
        minWin = true;
        minDraw = false;
        minLose = false;
        if (Float.compare(draw, min) < 0) {
            min = draw;
            minWin = false;
            minDraw = true;
            minLose = false;
        }
        if (Float.compare(lose, min) < 0) {
            min = lose;
            minWin = false;
            minDraw = false;
            minLose = true;
        }

        String[] strs = score.split("-");
        if (strs[0].compareTo(strs[1]) < 0) {
            scoreResult = ScoreResult.LOSE;
        } else if (strs[0].compareTo(strs[1]) == 0) {
            scoreResult = ScoreResult.DRAW;
        }

        if (minDraw && (scoreResult.getVal() == ScoreResult.DRAW.getVal())) {
            passBet = true;
        } else if (minWin && (scoreResult.getVal() == ScoreResult.WIN.getVal())) {
            passBet = true;
        } else if (minLose && (scoreResult.getVal() == ScoreResult.LOSE.getVal())) {
            passBet = true;
        }
    }

    /**
     * @return year_index_host_guest
     */
    public String getKey() {
        String result = String.format("%s_%02d_%s_%s", year, index, host, guest);
        return result;
    }

    @Override
    public String toString() {
        return "Match [id=" + id + ", index=" + index + ", " + (year != null ? "year=" + year + ", " : "")
                + (time != null ? "time=" + time + ", " : "") + (host != null ? "host=" + host + ", " : "")
                + (guest != null ? "guest=" + guest + ", " : "") + (score != null ? "score=" + score + ", " : "")
                + "win=" + win + ", draw=" + draw + ", lose=" + lose + ", minWin=" + minWin + ", minDraw=" + minDraw
                + ", minLose=" + minLose + ", passBet=" + passBet + "]";
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
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
