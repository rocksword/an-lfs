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
import com.an.lfs.vo.MatchInfo;
import com.an.lfs.vo.TeamMgr;

public class MatchLoader {
    private static final Log logger = LogFactory.getLog(MatchLoader.class);
    private Country country;
    private int startYear;
    private int endYear;

    // Year ->
    private Map<Integer, List<MatchInfo>> yearMatchMap = new HashMap<>();

    private void load() {
        for (int year = startYear; year <= endYear; year++) {
            String filepath = LfsUtil.getInputFilePath(country, year);
            File f = new File(filepath);
            if (!f.exists()) {
                logger.debug("Not found " + filepath);
                continue;
            }

            List<MatchInfo> matchList = new ArrayList<>();

            String line = null;
            try (FileLineIterator iter = new FileLineIterator(filepath);) {
                while ((line = iter.nextLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        String[] strs = line.split("\t");
                        if (strs.length == 13 || strs.length == 12) {
                            logger.debug(String.format("1 %s,2 %s,3 %s,4 %s,5 %s,6 %s,7 %s,8 %s", strs[0], strs[1],
                                    strs[2], strs[3], strs[4], strs[5], strs[6], strs[7]));
                            int turn = Integer.parseInt(strs[0].trim());
                            String date = strs[1].trim();
                            String host = strs[2].trim();
                            String score = strs[3].trim();
                            String guest = strs[4].trim();
                            float win = 0f;
                            if (!strs[5].trim().isEmpty() && !strs[5].trim().equals("-")) {
                                win = Float.parseFloat(strs[5].trim());
                            }
                            float draw = 0f;
                            if (!strs[6].trim().isEmpty() && !strs[6].trim().equals("-")) {
                                draw = Float.parseFloat(strs[6].trim());
                            }
                            float lose = 0f;
                            if (!strs[7].trim().isEmpty() && !strs[7].trim().equals("-")) {
                                lose = Float.parseFloat(strs[7].trim());
                            }
                            MatchInfo mi = new MatchInfo();
                            mi.setTurn(turn);
                            mi.setDate(date);
                            mi.setHost(TeamMgr.getName(country, host, year));
                            mi.setScore(score);
                            mi.setGuest(TeamMgr.getName(country, guest, year));
                            mi.setValues(win, draw, lose);
                            matchList.add(mi);
                            logger.debug(mi);
                        } else {
                            logger.error("Invalid line: " + line);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("Invalid line: " + line);
                e.printStackTrace();
                continue;
            }

            yearMatchMap.put(year, matchList);
        }
    }

    /**
     * @param country
     * @param startYear
     * @param endYear
     */
    public MatchLoader(Country country, int startYear, int endYear) {
        this.country = country;
        this.startYear = startYear;
        this.endYear = endYear;
        load();
    }

    public Map<Integer, List<MatchInfo>> getYearMatchMap() {
        return yearMatchMap;
    }
}
