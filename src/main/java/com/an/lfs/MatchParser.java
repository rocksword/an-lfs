package com.an.lfs;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.vo.Match;

public class MatchParser {
    private static final Log logger = LogFactory.getLog(MatchParser.class);

    public MatchParser() {
    }

    public static boolean parse(String year, Map<String, Match> matchMap, List<String> claimRateKeys) {
        String filename = year + ".txt";
        String filepath = LfsUtil.getInputFilePath(LfsMain.ARGUMENT, filename);
        FileLineIterator iter = new FileLineIterator(filepath);
        Pattern pat = Pattern.compile("\t");
        String line = null;
        while ((line = iter.nextLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }
            String[] splits = pat.split(line.toString());
            if (splits.length < 8) {
                logger.info("Invalid line " + line);
                continue;
            }
            Match mat = new Match();
            int index = Integer.parseInt(splits[0].trim());
            String time = splits[1].trim();
            String host = TeamMgr.getName(splits[2].trim());
            String score = splits[3].trim();
            String guest = TeamMgr.getName(splits[4].trim());

            mat.setIndex(index);
            mat.setYear(year);
            mat.setTime(time);
            mat.setHost(host);
            mat.setScore(score);
            mat.setGuest(guest);

            String key = mat.getKey();
            mat.initScoreResult();

            claimRateKeys.add(key);
            matchMap.put(key, mat);
        }
        iter.close();
        return true;
    }
}
