package com.an.lfs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.tool.FileLineIterator;

public class TeamMgr {
    private static final Log logger = LogFactory.getLog(TeamMgr.class);

    // Country -> {Raw team name -> teamKey}
    private static Map<String, Map<String, String>> ctyTeamMap = new HashMap<>();

    public TeamMgr() {
    }

    static {
        for (String country : LfsConst.COUNTRIES) {
            ctyTeamMap.put(country, new HashMap<String, String>());
            Map<String, String> teamMap = ctyTeamMap.get(country);
            String filepath = LfsUtil.getConfFilePath(String.format("team_%s.txt", country));
            try (FileLineIterator iter = new FileLineIterator(filepath);) {

                String line = null;
                while ((line = iter.nextLine()) != null) {
                    String[] strs = line.split(",");
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
    }

    public static String getName(String country, String team) {
        return ctyTeamMap.get(country).get(team);
    }
}
