package com.an.lfs;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.lfs.enu.Country;
import com.an.lfs.tool.FileLineIterator;
import com.an.lfs.vo.MatchInfo;

public class LeagueMatchLoader {
    private static final Logger logger = LoggerFactory.getLogger(LeagueMatchLoader.class);
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
                logger.warn("Not found " + filepath);
                continue;
            }

            List<MatchInfo> matchList = new ArrayList<>();

            String line = null;
            try (FileLineIterator iter = new FileLineIterator(filepath);) {
                while ((line = iter.nextLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        String[] strs = line.split(",");
                        if (strs.length != 7) {
                            logger.warn("Invalid line {}, length {} ", line, strs.length);
                            continue;
                        }
                        int turn = Integer.parseInt(strs[0].trim());
                        String date = strs[1].trim();
                        String host = strs[2].trim();
                        String score = strs[3].trim();
                        String guest = strs[4].trim();
                        MatchInfo mi = new MatchInfo();
                        mi.setTurn(turn);
                        mi.setDate(date);
                        mi.setHost(host);
                        mi.setScore(score);
                        mi.setGuest(guest);
                        matchList.add(mi);
                    }
                }
            } catch (Exception e) {
                logger.error("Invalid line: " + line);
                e.printStackTrace();
            }
            yearMatchMap.put(year, matchList);
        }
    }

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
