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
import com.an.lfs.vo.TeamMgr;

//jpn_b_2014.txt
public class MatchLoader {
    private static final Logger logger = LoggerFactory.getLogger(MatchLoader.class);
    private Country country;

    // Year ->
    private Map<Integer, List<MatchInfo>> yearMatchMap = new HashMap<>();

    public Map<Integer, List<MatchInfo>> getYearMatchMap() {
        return yearMatchMap;
    }

    public MatchLoader(Country country, int year) {
        this.country = country;
        load(year);
    }

    private void load(int year) {
        String filepath = LfsUtil.getInputFilePath(country, year);
        File f = new File(filepath);
        if (!f.exists()) {
            logger.warn("Not found " + filepath);
            return;
        }
        List<MatchInfo> matchList = new ArrayList<>();
        String line = null;
        try (FileLineIterator iter = new FileLineIterator(filepath);) {
            while ((line = iter.nextLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] strs = line.split(",");
                    if (strs.length == 6 || strs.length == 7) {
                        MatchInfo mi = new MatchInfo();
                        mi.setTurn(Integer.parseInt(strs[0].trim()));
                        mi.setDate(strs[1].trim().substring(0, strs[1].indexOf(" ")));
                        mi.setHost(TeamMgr.getName(country, strs[2].trim(), year));
                        mi.setScore(strs[3].trim());
                        mi.setGuest(TeamMgr.getName(country, strs[4].trim(), year));
                        matchList.add(mi);
                    } else {
                        logger.error("strs.length: " + strs.length);
                        logger.error("Invalid line: " + line);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Invalid line: " + line);
            e.printStackTrace();
            return;
        }
        yearMatchMap.put(year, matchList);
    }
}
