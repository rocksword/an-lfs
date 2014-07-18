package com.an.lfs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TeamMgr {
    private static final Log logger = LogFactory.getLog(TeamMgr.class);
    // Raw team name -> teamKey
    private static Map<String, String> teams = new HashMap<>();

    public TeamMgr() {
    }

    static {
        init();
    }

    /**
     * @param team
     * @return
     */
    public static String getName(String team) {
        return teams.get(team);
    }

    private static void init() {
        String filepath = LfsUtil.getConfFilePath("team.txt");
        FileLineIterator iter = new FileLineIterator(filepath);
        String line = null;
        while ((line = iter.nextLine()) != null) {
            String[] strs = line.split(",");
            if (strs.length != 2) {
                logger.info("Invalid line: " + line);
                continue;
            }
            teams.put(strs[0].trim(), strs[1].trim());
        }
    }
}
