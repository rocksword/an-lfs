package com.an.lfs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.enu.Country;
import com.an.lfs.tool.FileLineIterator;
import com.an.lfs.vo.BoardTeam;

public class BoardLoader {
    private static final Log logger = LogFactory.getLog(BoardLoader.class);
    private Country country;

    public BoardLoader(Country country) {
        this.country = country;
        load();
    }

    private Map<Integer, List<BoardTeam>> boardTeamMap = new HashMap<>();

    public Map<Integer, List<BoardTeam>> getBoardTeamMap() {
        return boardTeamMap;
    }

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
                        bt.setTeam(team);
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
}
