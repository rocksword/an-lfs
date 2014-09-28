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

/**
 * Load score board from bra.txt
 * 
 * @author Anthony
 * 
 */
public class BoardLoader {
    private static final Log logger = LogFactory.getLog(BoardLoader.class);
    private Country country;
    private Map<Integer, List<BoardTeam>> boardTeamMap = new HashMap<>();

    private void load() {
        String filepath = LfsUtil.getInputFilePath(country);
        logger.info("filepath: " + filepath);
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
                        String[] strs = line.split(",");
                        if (strs.length != 15) {
                            logger.info("Invalid line: " + line);
                            continue;
                        }
                        BoardTeam bt = new BoardTeam();
                        bt.setRank(Integer.parseInt(strs[0].trim()));
                        bt.setTeam(TeamMgr.getName(country, strs[1].trim(), year));
                        bt.setTotal(Integer.parseInt(strs[2].trim()));
                        bt.setWin(Integer.parseInt(strs[3].trim()));
                        bt.setDraw(Integer.parseInt(strs[4].trim()));
                        bt.setLose(Integer.parseInt(strs[5].trim()));
                        bt.setGoalFor(Integer.parseInt(strs[6].trim()));
                        bt.setGoalAgainst(Integer.parseInt(strs[7].trim()));
                        bt.setGoalNet(Integer.parseInt(strs[8].trim()));
                        bt.setAvgFor(Float.parseFloat(strs[9].trim()));
                        bt.setAvgAgainst(Float.parseFloat(strs[10].trim()));
                        bt.setWinPer(Float.parseFloat(strs[11].trim().substring(0, strs[11].length() - 1)));
                        bt.setDrawPer(Float.parseFloat(strs[12].trim().substring(0, strs[12].length() - 1)));
                        bt.setLosePer(Float.parseFloat(strs[13].trim().substring(0, strs[13].length() - 1)));
                        bt.setScore(Integer.parseInt(strs[14].trim()));
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

    public Set<String> getTopNTeam(int year) {
        Set<String> ret = new HashSet<>();
        List<BoardTeam> list = boardTeamMap.get(year);
        for (int i = 0; i < LfsUtil.TOP_N; i++) {
            try {
                String team = list.get(i).getTeam();
                ret.add(team);
            } catch (Exception e) {
                logger.error(year + ", " + list);
                logger.error(e);
            }
        }
        return ret;
    }

    public Set<String> getLastNTeam(int year) {
        Set<String> ret = new HashSet<>();
        List<BoardTeam> list = boardTeamMap.get(year);
        int cnt = list.size();
        for (int i = 0; i < LfsUtil.TOP_N; i++) {
            ret.add(list.get(cnt - 1 - i).getTeam());
        }
        return ret;
    }
}
