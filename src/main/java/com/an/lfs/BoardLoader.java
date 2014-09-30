package com.an.lfs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.lfs.enu.Country;
import com.an.lfs.tool.FileLineIterator;
import com.an.lfs.vo.BoardPo;
import com.an.lfs.vo.BoardTeam;

/**
 * Load score board from bra.txt
 * 
 * @author Anthony
 * 
 */
public class BoardLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(BoardLoader.class);
    private Country country;
    //
    private List<BoardPo> boardPoList = new ArrayList<>();
    //
    private Map<Integer, List<BoardTeam>> boardTeamMap = new HashMap<>();

    private void load() {
        String filepath = LfsUtil.getInputFilePath(country);
        LOGGER.info("filepath: {}", filepath);
        File f = new File(filepath);
        if (!f.exists()) {
            LOGGER.debug("Not found {}", filepath);
            return;
        }
        String line = null;
        Integer playYear = null;
        List<BoardTeam> btList = null;

        try (FileLineIterator iter = new FileLineIterator(filepath);) {
            while ((line = iter.nextLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    if (line.startsWith("year:")) {
                        playYear = Integer.parseInt(line.substring(line.indexOf(":") + 1));
                        btList = new ArrayList<>();
                    } else if (line.startsWith("end")) {
                        boardTeamMap.put(playYear, btList);
                    } else {
                        String[] strs = line.split(",");
                        if (strs.length != 15) {
                            LOGGER.info("Invalid line: {}", line);
                            continue;
                        }

                        int rank = Integer.parseInt(strs[0].trim());
                        String team = strs[1].trim();
                        int matchCnt = Integer.parseInt(strs[2].trim());
                        int winCnt = Integer.parseInt(strs[3].trim());
                        int drawCnt = Integer.parseInt(strs[4].trim());
                        int loseCnt = Integer.parseInt(strs[5].trim());
                        int goalIn = Integer.parseInt(strs[6].trim());
                        int goalAgainst = Integer.parseInt(strs[7].trim());
                        BoardPo bp = new BoardPo();
                        bp.setPlayYear(playYear);
                        bp.setLeague(country.getVal());
                        bp.setRank(rank);
                        bp.setTeam(team);
                        bp.setMatchCnt(matchCnt);
                        bp.setWinCnt(winCnt);
                        bp.setDrawCnt(drawCnt);
                        bp.setLoseCnt(loseCnt);
                        bp.setGoalIn(goalIn);
                        bp.setGoalAgainst(goalAgainst);
                        boardPoList.add(bp);

                        BoardTeam bt = new BoardTeam();
                        bt.setRank(rank);
                        bt.setTeam(team);
                        bt.setTotal(matchCnt);
                        bt.setWin(winCnt);
                        bt.setDraw(drawCnt);
                        bt.setLose(loseCnt);
                        bt.setGoalFor(goalIn);
                        bt.setGoalAgainst(goalAgainst);
                        bt.setGoalNet(Integer.parseInt(strs[8].trim()));
                        bt.setAvgFor(Float.parseFloat(strs[9].trim()));
                        bt.setAvgAgainst(Float.parseFloat(strs[10].trim()));
                        bt.setWinPer(Float.parseFloat(strs[11].trim().substring(0, strs[11].length() - 1)));
                        bt.setDrawPer(Float.parseFloat(strs[12].trim().substring(0, strs[12].length() - 1)));
                        bt.setLosePer(Float.parseFloat(strs[13].trim().substring(0, strs[13].length() - 1)));
                        bt.setScore(Integer.parseInt(strs[14].trim()));
                        btList.add(bt);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error :", e);
            LOGGER.info("Invalid line: {}", line);
        }
    }

    public BoardLoader(Country country) {
        this.country = country;
        load();
    }

    public Map<Integer, List<BoardTeam>> getBoardTeamMap() {
        return boardTeamMap;
    }

    public List<BoardPo> getBoardPoList() {
        return boardPoList;
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
                LOGGER.error("{} , {}", year, list);
                LOGGER.error("Error: ", e);
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
