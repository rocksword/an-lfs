package com.an.lfs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.an.lfs.tool.FileLineIterator;
import com.an.lfs.vo.Category;
import com.an.lfs.vo.Match;
import com.an.lfs.vo.MatchCategory;
import com.an.lfs.vo.RateResult;
import com.an.lfs.vo.ScoreResult;

public class LfsUtil {
    private static final Log logger = LogFactory.getLog(LfsUtil.class);
    private static final String DIR_CONF = "conf";
    private static final String DIR_INPUT = "input";
    private static final String DIR_OUTPUT = "output";
    private static String STATIS_HEADER = "CATEGORY,NUM,PER,PASS_NUM,FAIL_NUM,PASS_PER,FAIL_PER\n";
    private static String MATCH_HEADER = "CATEGORY,NUM,PER\n";

    public static void exportStatis(String filepath, MatchCategory matchCategory, Collection<Match> matches)
            throws IOException {

        int total = matches.size();
        StringBuilder content = new StringBuilder();
        // Host oriented category
        content.append("Host,,,,,,\n");
        content.append(STATIS_HEADER);
        List<String> hostCats = new ArrayList<>();
        Map<String, Category> hostCatMap = matchCategory.getHostCatMap();
        hostCats.addAll(hostCatMap.keySet());
        Collections.sort(hostCats, floatCompare);

        for (String hostCat : hostCats) {
            Category cat = hostCatMap.get(hostCat);
            int passNum = cat.getPassNum();
            int failNum = cat.getFailNum();
            String per = getPercent(total, passNum, failNum) + "%";
            String passPer = getPassPer(passNum, failNum) + "%";
            String failPer = getFailPer(passNum, failNum) + "%";
            appendLine(content, hostCat, passNum, failNum, per, passPer, failPer);
        }

        // Guest oriented category
        content.append("Guest,,,,,,\n");
        content.append(STATIS_HEADER);
        List<String> guestCats = new ArrayList<>();
        Map<String, Category> guestCatMap = matchCategory.getGuestCatMap();
        guestCats.addAll(guestCatMap.keySet());
        Collections.sort(guestCats, floatCompare);

        for (String guestCat : guestCats) {
            Category cat = guestCatMap.get(guestCat);
            int passNum = cat.getPassNum();
            int failNum = cat.getFailNum();
            String per = getPercent(total, passNum, failNum) + "%";
            String passPer = getPassPer(passNum, failNum) + "%";
            String failPer = getFailPer(passNum, failNum) + "%";
            appendLine(content, guestCat, passNum, failNum, per, passPer, failPer);
        }

        // Middle category
        content.append("Middle,,,,,,\n");
        content.append(STATIS_HEADER);
        List<String> middleCats = new ArrayList<>();
        Map<String, Category> middleCatMap = matchCategory.getMiddleCatMap();
        middleCats.addAll(middleCatMap.keySet());
        Collections.sort(middleCats, floatCompare);

        for (String middleCat : middleCats) {
            Category cat = middleCatMap.get(middleCat);
            int passNum = cat.getPassNum();
            int failNum = cat.getFailNum();
            String per = getPercent(total, passNum, failNum) + "%";
            String passPer = getPassPer(passNum, failNum) + "%";
            String failPer = getFailPer(passNum, failNum) + "%";
            appendLine(content, middleCat, passNum, failNum, per, passPer, failPer);
        }

        // All match
        int winNum = 0;
        int drawNum = 0;
        int loseNum = 0;
        for (Match mat : matches) {
            if (mat.isWin()) {
                winNum++;
            } else if (mat.isDraw()) {
                drawNum++;
            } else {
                loseNum++;
            }
        }
        String winPer = (int) (100 * (float) winNum / (float) total) + "%";
        String drawPer = (int) (100 * (float) drawNum / (float) total) + "%";
        String losePer = (int) (100 * (float) loseNum / (float) total) + "%";
        content.append(",,,,,,\n");
        content.append(MATCH_HEADER);
        content.append("Win").append(",").append(winNum).append(",").append(winPer).append("\n");
        content.append("Draw").append(",").append(drawNum).append(",").append(drawPer).append("\n");
        content.append("Lose").append(",").append(loseNum).append(",").append(losePer).append("\n");

        FileLineIterator.writeFile(filepath, content.toString());
    }

    private static void appendLine(StringBuilder content, String name, int passNum, int failNum, String per,
            String passPer, String failPer) {
        content.append(name);
        content.append(LfsConst.COMMA).append(passNum + failNum);
        content.append(LfsConst.COMMA).append(per);
        content.append(LfsConst.COMMA).append(passNum);
        content.append(LfsConst.COMMA).append(failNum);
        content.append(LfsConst.COMMA).append(passPer);
        content.append(LfsConst.COMMA).append(failPer);
        content.append(LfsConst.NEXT_LINE);
    }

    private static int getFailPer(int passNum, int failNum) {
        return (int) (100 * (float) failNum / (float) (passNum + failNum));
    }

    private static int getPassPer(int passNum, int failNum) {
        return (int) (100 * (float) passNum / (float) (passNum + failNum));
    }

    private static int getPercent(int total, int passNum, int failNum) {
        return (int) (100 * (float) (passNum + failNum) / (float) total);
    }

    private static Comparator<String> floatCompare = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return Float.compare(Float.parseFloat(o1), Float.parseFloat(o2));
        }
    };

    public static String getCat(float val) {
        String result = null;
        if (Float.compare(1.2f, val) >= 0) {
            result = "1.2";
        } else if (Float.compare(1.4f, val) >= 0) {
            result = "1.4";
        } else if (Float.compare(1.6f, val) >= 0) {
            result = "1.6";
        } else if (Float.compare(1.8f, val) >= 0) {
            result = "1.8";
        } else if (Float.compare(2.0f, val) >= 0) {
            result = "2.0";
        } else if (Float.compare(2.3f, val) >= 0) {
            result = "2.3";
        } else if (Float.compare(2.6f, val) >= 0) {
            result = "2.6";
        } else if (Float.compare(3.0f, val) >= 0) {
            result = "3.0";
        } else if (Float.compare(4.0f, val) >= 0) {
            result = "4.0";
        } else if (Float.compare(5.0f, val) >= 0) {
            result = "5.0";
        } else if (Float.compare(6.0f, val) >= 0) {
            result = "6.0";
        } else if (Float.compare(7.0f, val) >= 0) {
            result = "7.0";
        } else if (Float.compare(8.0f, val) >= 0) {
            result = "8.0";
        } else {
            result = "9.0";
        }
        return result;
    }

    public static ScoreResult getScoreResult(String score) {
        if (score == null || score.trim().isEmpty()) {
            return ScoreResult.INVALID;
        }

        String[] strs = score.trim().split("-");
        if (strs.length != 2) {
            logger.error("Invalid score: " + score);
            return ScoreResult.INVALID;
        }

        ScoreResult scoreResult = ScoreResult.WIN;
        if (strs[0].trim().compareTo(strs[1].trim()) == 0) {
            scoreResult = ScoreResult.DRAW;
        } else if (strs[0].trim().compareTo(strs[1].trim()) < 0) {
            scoreResult = ScoreResult.LOSE;
        }
        return scoreResult;
    }

    /**
     * @param win
     * @param draw
     * @param lose
     * @return
     */
    public static boolean isValidRate(float win, float draw, float lose) {
        if ((win != 0.0f) || (draw != 0.0f) || (lose != 0.0f)) {
            return true;
        }
        return false;
    }

    /**
     * @param win
     * @param draw
     * @param lose
     * @return
     */
    public static RateResult getRateResult(float win, float draw, float lose) {
        if ((win == 0.0f) || (draw == 0.0f) || (lose == 0.0f)) {
            return RateResult.INVALID;
        }

        float min = win;
        RateResult rateResult = RateResult.WIN;
        if (Float.compare(draw, min) < 0) {
            min = draw;
            rateResult = RateResult.DRAW;
        }
        if (Float.compare(lose, min) < 0) {
            min = lose;
            rateResult = RateResult.LOSE;
        }
        return rateResult;
    }

    /**
     * @param country
     * @param year
     * @return spa_2013
     */
    public static String getMatchDirName(String country, int year) {
        return String.format("%s_%s", country, year);
    }

    public static String getStatisFilepath(String dirName) {
        String filepath = dirName + "_statis.csv";
        return filepath;
    }

    public synchronized static String getLfsHome() {
        String dir = System.getenv(LfsConst.LFS_HOME);
        if (StringUtils.isEmpty(dir)) {
            throw new RuntimeException("Not found LFS_HOME, please set it.");
        }
        return dir;
    }

    public synchronized static String getConfFilePath(String filename) {
        String home = getLfsHome();
        File file = new File(home);
        return new StringBuilder().append(file.getAbsolutePath()).append(File.separator).append(DIR_CONF)
                .append(File.separator).append(filename).toString();
    }

    public synchronized static String getInputFilePath(String relativeDir, String filename) {
        String home = getLfsHome();
        File file = new File(home);
        return new StringBuilder().append(file.getAbsolutePath()).append(File.separator).append(DIR_INPUT)
                .append(File.separator).append(relativeDir).append(File.separator).append(filename).toString();
    }

    public synchronized static String getOutputFilePath(String filename) {
        String home = getLfsHome();
        File file = new File(home);
        return new StringBuilder().append(file.getAbsolutePath()).append(File.separator).append(DIR_OUTPUT)
                .append(File.separator).append(filename).toString();
    }

    // invalid chars in domain names
    private static String[] invalidDomainNameChars = { "/", "!", "#", "@", "$", "%", "<", ">", "^", "&", "*", "(", ")",
            "[", "]", "{", "}", ",", "?", "\"", "'", "+", "|", "_", "'", ":" };

    public static boolean isValidUrl(String urlString) {
        String regex = "^(https|http|ftp|rtsp|mms)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(urlString);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    public static boolean isIpAddr(String ipAddr) {
        String ip = "(2[5][0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})\\.(25[0-5]|2[0-4]\\d|1\\d{2}|\\d{1,2})";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddr);
        return matcher.matches();
    }

    public static boolean isValidDomain(String domain) {
        if (domain == null) {
            return false;
        }

        domain = domain.trim();
        if (domain.isEmpty()) {
            return false;
        }
        if (domain.startsWith(".") || domain.startsWith("-")) {
            return false;
        }
        if (!domain.contains(".")) {
            return false;
        }

        for (String invalid : invalidDomainNameChars) {
            if (domain.contains(invalid)) {
                return false;
            }
        }
        return true;
    }

    public static void logCollection(Collection<?> collection) {
        logger.info("Total size: " + collection.size());
        for (Object obj : collection) {
            logger.info(obj);
        }
    }
}
