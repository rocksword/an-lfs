package com.an.lfs;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.vo.Match;

public class MatchParser {
    private static final Log logger = LogFactory.getLog(MatchParser.class);

    public MatchParser() {
    }

    public boolean parse(String year, List<Match> matchs, List<String> claimRateKeys) {
        String filename = year + ".txt";
        String filepath = LfsUtil.getInputFilePath(filename);
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
            Match match = new Match();
            int index = Integer.parseInt(splits[0].trim());
            String time = splits[1].trim();
            String host = TeamMgr.getName(splits[2].trim());
            String score = splits[3].trim();
            String guest = TeamMgr.getName(splits[4].trim());
            float win = Float.parseFloat(splits[5].trim());
            float draw = Float.parseFloat(splits[6].trim());
            float lose = Float.parseFloat(splits[7].trim());

            match.setIndex(index);
            match.setYear(year);
            match.setTime(time);
            match.setHost(host);
            match.setScore(score);
            match.setGuest(guest);
            match.setWin(win);
            match.setDraw(draw);
            match.setLose(lose);

            claimRateKeys.add(match.getKey());
            matchs.add(match);
        }
        iter.close();
        return true;
    }
}
