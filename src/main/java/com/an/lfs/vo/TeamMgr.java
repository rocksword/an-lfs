package com.an.lfs.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.LfsUtil;
import com.an.lfs.enu.Country;
import com.an.lfs.tool.FileLineIterator;

public class TeamMgr {
    private static final Log logger = LogFactory.getLog(TeamMgr.class);

    // Country -> {Raw team name -> teamKey}
    private static Map<Country, Map<String, String>> ctyTeamMap = new HashMap<>();

    public TeamMgr() {
    }

    static {
        // for (String country : LfsUtil.COUNTRIES) {
        // ctyTeamMap.put(country, new HashMap<String, String>());
        // Map<String, String> teamMap = ctyTeamMap.get(country);
        // String filepath = LfsUtil.getConfFilePath(String.format("team_%s.txt", country));
        // try (FileLineIterator iter = new FileLineIterator(filepath);) {
        // String line = null;
        // while ((line = iter.nextLine()) != null) {
        // if (line.trim().isEmpty()) {
        // continue;
        // }
        //
        // String[] strs = line.trim().split(",");
        // if (strs.length != 2) {
        // logger.info("Invalid line: " + line);
        // continue;
        // }
        // teamMap.put(strs[0].trim(), strs[1].trim());
        // }
        // } catch (Exception e) {
        // logger.error("Error :" + e);
        // }
        // }

        String filepath = LfsUtil.getConfFilePath(String.format("team.txt"));

        String line = null;
        Country country = null;
        Map<String, String> teamMap = null;
        try (FileLineIterator iter = new FileLineIterator(filepath);) {
            while ((line = iter.nextLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    if (line.startsWith("country")) {
                        String cty = line.substring(line.indexOf(":") + 1);
                        country = Country.getCountry(cty);
                        teamMap = new HashMap<>();
                    } else if (line.startsWith("end:")) {
                        ctyTeamMap.put(country, teamMap);
                    } else {
                        String[] strs = line.split(",");
                        if (strs.length != 2) {
                            logger.info("Invalid line: " + line);
                            continue;
                        }
                        teamMap.put(strs[0].trim(), strs[1].trim());
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error :" + e);
        }
    }

    /**
     * @param country
     * @param team
     * @return
     */
    public static String getName(Country country, String team) {
        Map<String, String> teamMap = ctyTeamMap.get(country);
        if (!teamMap.containsKey(team)) {
            logger.warn("Not found team: " + team);
            return null;
        }
        return teamMap.get(team);
    }

    public static Map<Country, Map<String, String>> getCtyTeamMap() {
        return ctyTeamMap;
    }
}
