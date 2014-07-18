package com.an.lfs.vo;

public class Match {
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
                + "win=" + win + ", draw=" + draw + ", lose=" + lose + "]";
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
