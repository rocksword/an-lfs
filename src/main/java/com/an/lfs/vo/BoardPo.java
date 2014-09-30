package com.an.lfs.vo;

/**
 * @author Anthony
 * 
 */
public class BoardPo {
    private int id;
    // League name
    private String league;
    // Year
    private int playYear;
    // Team rank
    private int rank;
    private String team;
    // Total match count
    private int matchCnt;
    private int winCnt;
    private int drawCnt;
    private int loseCnt;
    private int goalIn;
    private int goalAgainst;

    public BoardPo() {
    }

    @Override
    public String toString() {
        return "BoardPo [id=" + id + ", league=" + league + ", playYear=" + playYear + ", rank=" + rank + ", team="
                + team + ", matchCnt=" + matchCnt + ", winCnt=" + winCnt + ", drawCnt=" + drawCnt + ", loseCnt="
                + loseCnt + ", goalIn=" + goalIn + ", goalAgainst=" + goalAgainst + "]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public int getPlayYear() {
        return playYear;
    }

    public void setPlayYear(int playYear) {
        this.playYear = playYear;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public int getMatchCnt() {
        return matchCnt;
    }

    public void setMatchCnt(int matchCnt) {
        this.matchCnt = matchCnt;
    }

    public int getWinCnt() {
        return winCnt;
    }

    public void setWinCnt(int winCnt) {
        this.winCnt = winCnt;
    }

    public int getDrawCnt() {
        return drawCnt;
    }

    public void setDrawCnt(int drawCnt) {
        this.drawCnt = drawCnt;
    }

    public int getLoseCnt() {
        return loseCnt;
    }

    public void setLoseCnt(int loseCnt) {
        this.loseCnt = loseCnt;
    }

    public int getGoalIn() {
        return goalIn;
    }

    public void setGoalIn(int goalIn) {
        this.goalIn = goalIn;
    }

    public int getGoalAgainst() {
        return goalAgainst;
    }

    public void setGoalAgainst(int goalAgainst) {
        this.goalAgainst = goalAgainst;
    }
}
