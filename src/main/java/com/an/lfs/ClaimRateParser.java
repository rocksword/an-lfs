package com.an.lfs;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ClaimRateParser {
    private static final Log logger = LogFactory.getLog(ClaimRateParser.class);

    public ClaimRateParser() {
    }

    public boolean parse(Map<String, List<ClaimRate>> result) {
        for (String key : LfsUtil.claimRateKeys) {
            String filename = key + ".txt";
            String filepath = LfsUtil.getInputFilePath(filename);

            FileLineIterator iter = new FileLineIterator(filepath);
            String line = null;
            Pattern pat = Pattern.compile("\t");

            List<ClaimRate> rates = new ArrayList<>();
            while ((line = iter.nextLine()) != null) {
                String[] splits = pat.split(line.toString());
                if (splits.length < 8) {
                    logger.info("Invalid line " + line);
                    continue;
                }
                int id = Integer.parseInt(splits[0].trim());
                String compName = LfsUtil.getCompName(splits[1].trim());
                float win = Float.parseFloat(splits[2].trim());
                float draw = Float.parseFloat(splits[3].trim());
                float lose = Float.parseFloat(splits[4].trim());
                String winPerStr = splits[5].trim();
                float winPer = Float.parseFloat(winPerStr.substring(0, winPerStr.length() - 1));
                String drawPerStr = splits[6].trim();
                float drawPer = Float.parseFloat(drawPerStr.substring(0, drawPerStr.length() - 1));
                String losePerStr = splits[7].trim();
                float losePer = Float.parseFloat(losePerStr.substring(0, losePerStr.length() - 1));
                String retStr = splits[8].trim();
                float ret = Float.parseFloat(retStr.substring(0, retStr.length() - 1));
                float winK = Float.parseFloat(splits[9].trim());
                float drawK = Float.parseFloat(splits[10].trim());
                float loseK = Float.parseFloat(splits[11].trim());

                ClaimRate rate = new ClaimRate();
                rate.setId(id);
                rate.setCompName(compName);
                rate.setWin(win);
                rate.setDraw(draw);
                rate.setLose(lose);
                rate.setWinPer(winPer);
                rate.setDrawPer(drawPer);
                rate.setLosePer(losePer);
                rate.setRet(ret);
                rate.setWinK(winK);
                rate.setDrawK(drawK);
                rate.setLoseK(loseK);

                logger.debug(rate);
                rates.add(rate);
            }

            result.put(key, rates);
            iter.close();
        }
        return true;
    }
}
