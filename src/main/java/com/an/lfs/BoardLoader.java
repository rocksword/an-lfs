package com.an.lfs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.enu.Country;
import com.an.lfs.tool.FileLineIterator;
import com.an.lfs.vo.BoardTeam;
import com.an.lfs.vo.TeamMgr;

public class BoardLoader {
    private static final Log logger = LogFactory.getLog(BoardLoader.class);
    private Country country;
    private Map<Integer, List<BoardTeam>> boardTeamMap = new HashMap<>();

    private void load() {
        String filepath = LfsUtil.getInputFilePath(country);
        File f = new File(filepath);
        if (!f.exists()) {
            logger.debug("Not found " + filepath);
            return;
        }
        String line = null;
        Integer year = null;
        List<BoardTeam> teamList = null;
        try (FileLineIterator iter = new FileLineIterator(filepath);) {
            while ((line = iter.nextLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    if (line.startsWith("year:")) {
                        year = Integer.parseInt(line.substring(line.indexOf(":") + 1));
                        teamList = new ArrayList<>();
                    } else if (line.startsWith("end")) {
                        boardTeamMap.put(year, teamList);
                    } else {
                        String[] strs = line.split("\t");
                        if (strs.length != 15) {
                            logger.info("Invalid line: " + line);
                            continue;
                        }
                        int rank = Integer.parseInt(strs[0].trim());
                        String team = strs[1].trim();
                        int total = Integer.parseInt(strs[2].trim());
                        int win = Integer.parseInt(strs[3].trim());
                        int draw = Integer.parseInt(strs[4].trim());
                        int lose = Integer.parseInt(strs[5].trim());
                        int goalFor = Integer.parseInt(strs[6].trim());
                        int goalAgainst = Integer.parseInt(strs[7].trim());
                        int goalNet = Integer.parseInt(strs[8].trim());
                        float avgFor = Float.parseFloat(strs[9].trim());
                        float avgAgainst = Float.parseFloat(strs[10].trim());
                        float winPer = Float.parseFloat(strs[11].trim());
                        float drawPer = Float.parseFloat(strs[12].trim());
                        float losePer = Float.parseFloat(strs[13].trim());
                        int score = Integer.parseInt(strs[14].trim());
                        BoardTeam bt = new BoardTeam();
                        bt.setRank(rank);
                        bt.setTeam(TeamMgr.getName(country, team));
                        bt.setTotal(total);
                        bt.setWin(win);
                        bt.setDraw(draw);
                        bt.setLose(lose);
                        bt.setGoalFor(goalFor);
                        bt.setGoalAgainst(goalAgainst);
                        bt.setGoalNet(goalNet);
                        bt.setAvgFor(avgFor);
                        bt.setAvgAgainst(avgAgainst);
                        bt.setWinPer(winPer);
                        bt.setDrawPer(drawPer);
                        bt.setLosePer(losePer);
                        bt.setScore(score);
                        teamList.add(bt);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error :" + e);
            logger.info("Invalid line: " + line);
        }
    }

    public BoardLoader(Country country) {
        this.country = country;
        load();
    }

    public Map<Integer, List<BoardTeam>> getBoardTeamMap() {
        return boardTeamMap;
    }

    public Map<String, Integer> getTeamRank(int year) {
        Map<String, Integer> ret = new HashMap<>();
        List<BoardTeam> list = boardTeamMap.get(year);
        for (BoardTeam bt : list) {
            ret.put(bt.getTeam(), bt.getRank());
        }
        return ret;
    }

    public Set<String> getTop3Team(int year) {
        Set<String> ret = new HashSet<>();
        List<BoardTeam> list = boardTeamMap.get(year);
        ret.add(list.get(0).getTeam());
        ret.add(list.get(1).getTeam());
        ret.add(list.get(2).getTeam());
        return ret;
    }

    public Set<String> getLast3Team(int year) {
        Set<String> ret = new HashSet<>();
        List<BoardTeam> list = boardTeamMap.get(year);
        int cnt = list.size();
        ret.add(list.get(cnt - 1).getTeam());
        ret.add(list.get(cnt - 2).getTeam());
        ret.add(list.get(cnt - 3).getTeam());
        return ret;
    }
}
