package com.an.lfs;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.vo.Country;

public class TeamMgr {
    private static final Log logger = LogFactory.getLog(TeamMgr.class);

    // Country -> {Raw team name -> teamKey}
    private static Map<Country, Map<String, String>> ctyTeamMap = new HashMap<>();

    public TeamMgr() {
    }

    static {
        for (Country cty : LfsConst.COUNTRIES) {
            ctyTeamMap.put(cty, new HashMap<String, String>());
            Map<String, String> teamMap = ctyTeamMap.get(cty);
            String filepath = LfsUtil.getConfFilePath(String.format("team_%s.txt", cty.getVal()));
            FileLineIterator iter = new FileLineIterator(filepath);
            String line = null;
            while ((line = iter.nextLine()) != null) {
                String[] strs = line.split(",");
                if (strs.length != 2) {
                    logger.info("Invalid line: " + line);
                    continue;
                }
                teamMap.put(strs[0].trim(), strs[1].trim());
            }
            iter.close();
        }
    }

    public static String getName(Country country, String team) {
        return ctyTeamMap.get(country).get(team);
    }
}
