package com.an.lfs;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class InputParser {
    private static final Log logger = LogFactory.getLog(InputParser.class);

    public InputParser() {
    }

    public boolean parse(String year, String nextYear, List<MatchItem> result) {
        String filepath = LfsUtil.getInputFilePath("2013.txt");
        FileLineIterator iter = new FileLineIterator(filepath);
        String line = null;
        String startMonth = null;
        while ((line = iter.nextLine()) != null) {
            if (line.trim().isEmpty()) {
                continue;
            }
            Pattern pat = Pattern.compile("\t");
            String[] splits = pat.split(line.toString());
            if (splits.length < 8) {
                logger.info("Invalid line " + line);
                continue;
            }
            MatchItem item = new MatchItem();
            int index = Integer.parseInt(splits[0].trim());
            String time = splits[1].trim();
            if (startMonth == null) {
                startMonth = time.substring(0, 2);
            }
            if (time.substring(0, 2).compareTo(startMonth) < 0) {
                time = nextYear + "-" + time;
            } else {
                time = year + "-" + time;
            }
            String host = LfsUtil.getName(splits[2].trim());
            String score = splits[3].trim();
            String guest = LfsUtil.getName(splits[4].trim());
            float win = Float.parseFloat(splits[5].trim());
            float draw = Float.parseFloat(splits[6].trim());
            float lose = Float.parseFloat(splits[7].trim());

            item.setIndex(index);
            item.setTime(time);
            item.setHost(host);
            item.setScore(score);
            item.setGuest(guest);
            item.setEven(new float[] { win, draw, lose });

            logger.info(item.getKey());
            result.add(item);
        }
        return true;
    }
}
