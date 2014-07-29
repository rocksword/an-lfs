package com.an.lfs.vo;

import java.util.ArrayList;
import java.util.List;

import jxl.write.WriteException;

import com.an.lfs.LfsUtil;
import com.an.lfs.enu.Country;

//排名,球队,赛,胜,平,负,进球,失球,净胜,均得,均失,胜%,平%,负%,积分
public class BoardTeam {
    private int rank;
    private String team;
    private int total;
    private int win;
    private int draw;
    private int lose;
    private int goalFor;
    private int goalAgainst;
    private int goalNet;
    private float avgFor;
    private float avgAgainst;
    private float winPer;
    private float drawPer;
    private float losePer;
    private int score;

    public List<Cell> getRow(Country cty, int year, int teamCnt) throws WriteException {
        List<Cell> row = new ArrayList<>();

        String teamName = TeamMgr.getName(cty, team);

        row.add(new Cell(year));
        row.add(new Cell(rank));
        if (rank < 4) {
            row.add(new Cell(teamName, LfsUtil.getRoseFmt()));
        } else if (rank > teamCnt - 3) {
            row.add(new Cell(teamName, LfsUtil.getYellowFmt()));
        } else {
            row.add(new Cell(teamName));
        }
        row.add(new Cell(total));
        row.add(new Cell(win));
        row.add(new Cell(draw));
        row.add(new Cell(lose));
        row.add(new Cell(goalFor));
        row.add(new Cell(goalAgainst));
        row.add(new Cell(goalNet));
        row.add(new Cell(avgFor));
        row.add(new Cell(avgAgainst));

        if (rank < 4) {
            row.add(new Cell(winPer, LfsUtil.getRoseFmt()));
        } else {
            row.add(new Cell(winPer));
        }

        row.add(new Cell(drawPer));

        if (rank > teamCnt - 3) {
            row.add(new Cell(losePer, LfsUtil.getYellowFmt()));
        } else {
            row.add(new Cell(losePer));
        }

        row.add(new Cell(score));
        return row;
    }

    public BoardTeam() {
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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getGoalFor() {
        return goalFor;
    }

    public void setGoalFor(int goalFor) {
        this.goalFor = goalFor;
    }

    public int getGoalAgainst() {
        return goalAgainst;
    }

    public void setGoalAgainst(int goalAgainst) {
        this.goalAgainst = goalAgainst;
    }

    public int getGoalNet() {
        return goalNet;
    }

    public void setGoalNet(int goalNet) {
        this.goalNet = goalNet;
    }

    public float getAvgFor() {
        return avgFor;
    }

    public void setAvgFor(float avgFor) {
        this.avgFor = avgFor;
    }

    public float getAvgAgainst() {
        return avgAgainst;
    }

    public void setAvgAgainst(float avgAgainst) {
        this.avgAgainst = avgAgainst;
    }

    public float getWinPer() {
        return winPer;
    }

    public void setWinPer(float winPer) {
        this.winPer = winPer;
    }

    public float getDrawPer() {
        return drawPer;
    }

    public void setDrawPer(float drawPer) {
        this.drawPer = drawPer;
    }

    public float getLosePer() {
        return losePer;
    }

    public void setLosePer(float losePer) {
        this.losePer = losePer;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
