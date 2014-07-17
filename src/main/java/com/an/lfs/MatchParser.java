package com.an.lfs;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MatchParser {
    private static final Log logger = LogFactory.getLog(MatchParser.class);

    public MatchParser() {
    }

    public boolean parse(String year, List<MatchItem> result) {
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
            MatchItem item = new MatchItem();
            int index = Integer.parseInt(splits[0].trim());
            String time = splits[1].trim();
            String host = LfsUtil.getTeamName(splits[2].trim());
            String score = splits[3].trim();
            String guest = LfsUtil.getTeamName(splits[4].trim());
            float win = Float.parseFloat(splits[5].trim());
            float draw = Float.parseFloat(splits[6].trim());
            float lose = Float.parseFloat(splits[7].trim());

            item.setIndex(index);
            item.setYear(year);
            item.setTime(time);
            item.setHost(host);
            item.setScore(score);
            item.setGuest(guest);
            item.setEven(new float[] { win, draw, lose });

            LfsUtil.claimRateKeys.add(item.getKey());
            result.add(item);
        }
        iter.close();
        return true;
    }
}
