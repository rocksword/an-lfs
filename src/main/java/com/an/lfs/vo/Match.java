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

    private boolean scoreWin = false;
    // draw match, for example, 1-1, 2-2
    private boolean scoreDraw = false;
    private boolean scoreLose = false;

    private boolean minWin = false;
    private boolean minDraw = false;
    private boolean minLose = false;

    private boolean passBet = false;

    // true - win the bet, false - lose the bet
    public boolean isPassBet() {
        return passBet;
    }

    public void initPassBet() {
        float min = 0;
        if (Float.compare(win, lose) < 0) {
            min = win;
            minWin = true;
            minDraw = false;
            minLose = false;
        } else if (Float.compare(win, lose) > 0) {
            min = lose;
            minWin = false;
            minDraw = false;
            minLose = true;
        } else if (Float.compare(win, lose) == 0) {
            min = win;
            minWin = true;
            minDraw = false;
            minLose = true;
        }

        if (Float.compare(draw, min) <= 0) {
            minDraw = true;
            minWin = false;
            minLose = false;
        }

        String[] strs = score.split("-");
        if (strs[0].compareTo(strs[1]) > 0) {
            scoreWin = true;
        } else if (strs[0].compareTo(strs[1]) < 0) {
            scoreLose = true;
        } else if (strs[0].compareTo(strs[1]) == 0) {
            scoreDraw = true;
        }

        if (minDraw && scoreDraw) {
            passBet = true;
        } else if (minWin && scoreWin) {
            passBet = true;
        } else if (minLose && scoreLose) {
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
        return "Match [id=" + id + ", index=" + index + ", year=" + year + ", time=" + time + ", host=" + host
                + ", guest=" + guest + ", score=" + score + ", win=" + win + ", draw=" + draw + ", lose=" + lose
                + ", scoreWin=" + scoreWin + ", scoreDraw=" + scoreDraw + ", scoreLose=" + scoreLose + ", minWin="
                + minWin + ", minDraw=" + minDraw + ", minLose=" + minLose + ", passBet=" + passBet + "]";
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
