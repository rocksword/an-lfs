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
    private ScoreResult scoreResult = ScoreResult.WIN;

    public void initScoreResult() {
        String[] strs = score.split("-");
        if (strs.length != 2) {
            logger.error("Invalid score: " + score);
            return;
        }

        if (strs[0].compareTo(strs[1]) == 0) {
            scoreResult = ScoreResult.DRAW;
        } else if (strs[0].compareTo(strs[1]) < 0) {
            scoreResult = ScoreResult.LOSE;
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

    public ScoreResult getScoreResult() {
        return scoreResult;
    }

    public void setScoreResult(ScoreResult scoreResult) {
        this.scoreResult = scoreResult;
    }
}
