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

public class LeagueMatchLoader {
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

                String date = null;
                while ((line = iter.nextLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        if (line.length() == 10) {
                            date = line;
                            continue;
                        }
                        String[] strs = line.split("\t");
                        if (strs.length == 11) {
                            logger.debug(String.format("0 %s,1 %s,2 %s,3 %s,4 %s,5 %s,6 %s", strs[0], strs[1], strs[2],
                                    strs[3], strs[4], strs[5], strs[6]));
                            String host = strs[1].trim();
                            String score = strs[2].trim();
                            if (!score.isEmpty() && !score.equals("-")) {
                                score = score.substring(0, score.indexOf("("));
                            }
                            String guest = strs[3].trim();

                            float win = 0f;
                            if (!strs[4].trim().isEmpty() && !strs[4].trim().equals("-")) {
                                win = Float.parseFloat(strs[4].trim());
                            }
                            float draw = 0f;
                            if (!strs[5].trim().isEmpty() && !strs[5].trim().equals("-")) {
                                draw = Float.parseFloat(strs[5].trim());
                            }
                            float lose = 0f;
                            if (!strs[6].trim().isEmpty() && !strs[6].trim().equals("-")) {
                                lose = Float.parseFloat(strs[6].trim());
                            }
                            MatchInfo mi = new MatchInfo();
                            mi.setDate(date);
                            mi.setHost(TeamMgr.getName(country, host, year));
                            mi.setScore(score);
                            mi.setGuest(TeamMgr.getName(country, guest, year));
                            mi.setValues(win, draw, lose);
                            matchList.add(mi);
                            logger.debug(mi);
                        } else if (strs.length == 1) {
                            continue;
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
    public LeagueMatchLoader(Country country, int startYear, int endYear) {
        this.country = country;
        this.startYear = startYear;
        this.endYear = endYear;
        load();
    }

    public Map<Integer, List<MatchInfo>> getYearMatchMap() {
        return yearMatchMap;
    }
}
