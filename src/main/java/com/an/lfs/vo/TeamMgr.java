package com.an.lfs.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.LfsUtil;
import com.an.lfs.tool.FileLineIterator;

public class TeamMgr {
    private static final Log logger = LogFactory.getLog(TeamMgr.class);

    // Country -> {Raw team name -> teamKey}
    private static Map<String, Map<String, String>> ctyTeamMap = new HashMap<>();

    public TeamMgr() {
    }

    static {
        for (String country : LfsUtil.COUNTRIES) {
            ctyTeamMap.put(country, new HashMap<String, String>());
            Map<String, String> teamMap = ctyTeamMap.get(country);
            String filepath = LfsUtil.getConfFilePath(String.format("team_%s.txt", country));
            try (FileLineIterator iter = new FileLineIterator(filepath);) {
                String line = null;
                while ((line = iter.nextLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    String[] strs = line.trim().split(",");
                    if (strs.length != 2) {
                        logger.info("Invalid line: " + line);
                        continue;
                    }
                    teamMap.put(strs[0].trim(), strs[1].trim());
                }
            } catch (Exception e) {
                logger.error("Error :" + e);
            }
        }

        String filepath = LfsUtil.getConfFilePath(String.format("team.txt"));
        try (FileLineIterator iter = new FileLineIterator(filepath);) {
            String line = null;
            while ((line = iter.nextLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] strs = line.trim().split(",");
                if (strs.length != 2) {
                    logger.info("Invalid line: " + line);
                    continue;
                }
                teamMap.put(strs[0].trim(), strs[1].trim());
            }
        } catch (Exception e) {
            logger.error("Error :" + e);
        }
    }

    public static String getName(String cty, String team) {
        Map<String, String> teamMap = ctyTeamMap.get(cty);
        if (!teamMap.containsKey(team)) {
            logger.warn("Not found team: " + team);
            return null;
        }
        return teamMap.get(team);
    }

    public static Map<String, Map<String, String>> getCtyTeamMap() {
        return ctyTeamMap;
    }
}
