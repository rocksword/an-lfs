package com.an.lfs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.lfs.enu.Country;
import com.an.lfs.enu.Result;
import com.an.lfs.vo.BoardTeam;
import com.an.lfs.vo.Cell;
import com.an.lfs.vo.MatchInfo;
import com.an.lfs.vo.MatchRate;
import com.an.lfs.vo.MatchRule;
import com.an.lfs.vo.MatchRuleMgr;
import com.an.lfs.vo.Rate;

public class ReportMaker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportMaker.class);

    private static String[] matchReportHeadNames = new String[] { "Year", "T", "Date", "Host", "R", "Score", "PK",
            "Guest", "R", "TN-LN", "ScoreRet", "RankFc", };

    private static String[] ruleHeadNames = new String[] { "ID", "RateFc", "TrendFc", "ScoreRet", "Count", "Percent" };

    private static String[] boardReportHeadNames = new String[] { "Year", "R", "Team", "T", "W", "D", "L", "For",
            "Against", "Net", "For", "Against", "W%", "D%", "L%", "Score" };

    private static String[] leagueMatchReportHeadNames = new String[] { "T", "Date", "Host", "Score", "Guest",
            "ScoreRet" };

    /**
     * Export to file: ned_b_match.xls
     * 
     * @param country
     * @param yearMatchMap
     * @param rateLoader
     * @param matchRuleMgr
     * @throws Exception
     */
    public static void makeMatchReport(Country country, Map<Integer, List<MatchInfo>> yearMatchMap,
            RateLoader rateLoader, MatchRuleMgr matchRuleMgr) throws Exception {
        if (yearMatchMap.isEmpty()) {
            return;
        }
        List<Integer> years = new ArrayList<>();
        years.addAll(yearMatchMap.keySet());
        Collections.sort(years);
        LOGGER.info("years: " + years);
        BoardLoader board = new BoardLoader(country);

        List<List<Cell>> rows = new ArrayList<>();
        List<Cell> head = getMatchReportHead();
        LfsUtil.addCompanyRateHead(rateLoader, head);
        rows.add(head);

        int totalYear = years.size();
        for (int i = 0; i < totalYear; i++) {
            if (i > 0) {
                rows.add(getBlankRow(boardReportHeadNames.length));
            }

            int year = years.get(totalYear - 1 - i);
            LOGGER.info("country: " + country.getVal() + ", year: " + year);

            Set<String> topN = board.getTopNTeam(year);
            Set<String> lastN = board.getLastNTeam(year);
            Map<String, Integer> teamRank = board.getTeamRank(year);
            List<MatchInfo> matchList = yearMatchMap.get(year);
            for (MatchInfo mi : matchList) {
                boolean isHostTopN = topN.contains(mi.getHost());
                boolean isHostLastN = lastN.contains(mi.getHost());
                boolean isGuestTopN = topN.contains(mi.getGuest());
                boolean isGuestLastN = lastN.contains(mi.getGuest());

                String host = mi.getHost();
                String guest = mi.getGuest();

                List<Cell> row = new ArrayList<>();
                row.add(new Cell(year));
                row.add(new Cell(mi.getTurn()));
                row.add(new Cell(mi.getDate().substring(0, 5)));

                row.add(new Cell(host, isHostTopN ? LfsUtil.getRoseFmt()
                        : (isHostLastN ? LfsUtil.getYellowFmt() : null)));

                Integer hostRank = getTeamRank(teamRank, host);
                Integer guestRank = getTeamRank(teamRank, guest);

                row.add(new Cell(hostRank));
                row.add(new Cell(mi.getScore()));

                row.add(new Cell(LfsUtil.getRankPKString(hostRank, guestRank)));
                row.add(new Cell(guest, isGuestTopN ? LfsUtil.getRoseFmt() : (isGuestLastN ? LfsUtil.getYellowFmt()
                        : null)));

                row.add(new Cell(guestRank));
                row.add(new Cell(getTopLastStr(isHostTopN, isGuestTopN, isHostLastN, isGuestLastN, host, guest,
                        teamRank)));

                // Score result
                Result scoreRet = LfsUtil.getScoreRet(mi.getScore());
                row.add(LfsUtil.getResultCell(scoreRet));

                // Rank forecast
                row.add(LfsUtil.getResultCell(LfsUtil.getRankFc(hostRank, guestRank)));

                MatchRate matchRate = rateLoader.getMatchRate(host, guest);
                addCompanyRate(matchRate, scoreRet, row, matchRuleMgr);

                rows.add(row);
            }
        }

        String filepath = LfsUtil.getOutputFilePath(LfsUtil.getMatchExcelFile(country));
        LOGGER.info("Generate file " + filepath);
        WritableWorkbook wb = Workbook.createWorkbook(new File(filepath));
        WritableSheet sheet = wb.createSheet("match", 0);
        addRows(sheet, rows);

        // Construct rule sheet
        List<List<Cell>> ruleRows = getRuleRows(matchRuleMgr);
        WritableSheet sheet2 = wb.createSheet("rule", 1);
        addRows(sheet2, ruleRows);

        wb.write();
        wb.close();
    }

    private static WritableCellFormat getEndRateFont(float rate, float rateEnd) throws WriteException {
        WritableCellFormat font = null;
        if (rateEnd > rate) {
            font = LfsUtil.getRedFont();
        } else if (rateEnd < rate) {
            font = LfsUtil.getGreenFont();
        }
        return font;
    }

    private static String getTopLastStr(boolean isHostTopN, boolean isGuestTopN, boolean isHostLastN,
            boolean isGuestLastN, String host, String guest, Map<String, Integer> teamRank) {
        String ret = "";
        if (isHostTopN) {
            ret = "TN";
        } else if (isHostLastN) {
            ret = "LN";
        } else {
            ret = "  ";
        }
        if (isGuestTopN) {
            ret = ret + " - " + "TN";
        } else if (isGuestLastN) {
            ret = ret + " - " + "LN";
        } else {
            ret = ret + " - " + "  ";
        }
        return ret;
    }

    private static Integer getTeamRank(Map<String, Integer> teamRank, String team) {
        if (!teamRank.containsKey(team)) {
            LOGGER.warn("Not found team rank, team: " + team);
            return 0;
        }
        return teamRank.get(team);
    }

    private static List<Cell> getMatchReportHead() {
        List<Cell> head = new ArrayList<>();
        for (String name : matchReportHeadNames) {
            head.add(new Cell(name));
        }
        return head;
    }

    public static void makeBoardReport(Country cty, Map<Integer, List<BoardTeam>> teamMap) throws Exception {
        if (teamMap.isEmpty()) {
            LOGGER.warn("Empty team map, country " + cty.getVal());
            return;
        }
        List<Integer> years = new ArrayList<>();
        years.addAll(teamMap.keySet());
        Collections.sort(years);

        List<List<Cell>> rows = new ArrayList<>();
        rows.add(getBoardReportHead());

        int yearCnt = years.size();
        for (int i = 0; i < yearCnt; i++) {
            if (i > 0) {
                rows.add(getBlankRow(boardReportHeadNames.length));
            }
            int year = years.get((yearCnt - 1 - i));
            List<BoardTeam> teams = teamMap.get(year);
            for (BoardTeam bt : teams) {
                rows.add(bt.getRow(cty, year, teams.size()));
            }
        }

        String filepath = LfsUtil.getOutputFilePath(LfsUtil.getBoardExcelFile(cty));
        LOGGER.info("Generate file " + filepath);
        WritableWorkbook wb = Workbook.createWorkbook(new File(filepath));
        WritableSheet sheet = wb.createSheet("board", 0);
        addRows(sheet, rows);
        wb.write();
        wb.close();
    }

    public static void makeLeagueMatchReport(Country country, Map<Integer, List<MatchInfo>> yearMatchMap,
            RateLoader rateLoader, MatchRuleMgr matchRuleMgr) throws Exception {
        if (yearMatchMap.isEmpty()) {
            return;
        }
        List<Integer> years = new ArrayList<>();
        years.addAll(yearMatchMap.keySet());
        Collections.sort(years);

        // Construct match sheet
        List<List<Cell>> matchRows = new ArrayList<>();
        List<Cell> head = getLeagueMatchReportHead();
        LfsUtil.addCompanyRateHead(rateLoader, head);
        matchRows.add(head);

        List<List<Cell>> tmpRows = new ArrayList<>();
        int totalYear = years.size();
        for (int i = 0; i < totalYear; i++) {
            int year = years.get(totalYear - 1 - i);
            LOGGER.info("country: " + country.getVal() + ", year: " + year);

            List<MatchInfo> matchList = yearMatchMap.get(year);
            for (MatchInfo mi : matchList) {
                String host = mi.getHost();
                String guest = mi.getGuest();
                List<Cell> row = new ArrayList<>();
                row.add(new Cell(mi.getTurn()));// 0
                row.add(new Cell(mi.getDate().substring(0, 5)));// 1
                row.add(new Cell(host));// 2
                row.add(new Cell(mi.getScore()));
                row.add(new Cell(guest));// 4

                Result scoreRet = LfsUtil.getScoreRet(mi.getScore());
                row.add(LfsUtil.getResultCell(scoreRet));

                MatchRate mr = rateLoader.getMatchRate(host, guest);
                addCompanyRate(mr, scoreRet, row, matchRuleMgr);

                tmpRows.add(row);
            }
        }
        Collections.sort(tmpRows, leagueRowComparator);
        matchRows.addAll(tmpRows);

        String filepath = LfsUtil.getOutputFilePath(LfsUtil.getMatchExcelFile(country));
        LOGGER.info("Generate file " + filepath);
        WritableWorkbook wb = Workbook.createWorkbook(new File(filepath));
        WritableSheet sheet1 = wb.createSheet("match", 0);
        addRows(sheet1, matchRows);

        // Construct rule sheet
        List<List<Cell>> ruleRows = getRuleRows(matchRuleMgr);
        WritableSheet sheet2 = wb.createSheet("rule", 1);
        addRows(sheet2, ruleRows);

        wb.write();
        wb.close();
    }

    private static List<List<Cell>> getRuleRows(MatchRuleMgr matchRuleMgr) {
        List<List<Cell>> ruleRows = new ArrayList<>();
        List<Cell> ruleHead = new ArrayList<>();
        for (String name : ruleHeadNames) {
            ruleHead.add(new Cell(name));
        }
        ruleRows.add(ruleHead);

        for (MatchRule mr : matchRuleMgr.getMatchRule()) {
            List<Cell> row = new ArrayList<>();
            row.add(new Cell(mr.getId()));
            row.add(new Cell(LfsUtil.getResultStr(mr.getRateFc())));
            row.add(new Cell(LfsUtil.getResultStr(mr.getTrendFc())));
            row.add(new Cell(LfsUtil.getResultStr(mr.getScoreRet())));
            row.add(new Cell(mr.getCount()));
            row.add(new Cell(mr.getPercent() + "%"));
            ruleRows.add(row);
        }
        return ruleRows;
    }

    private static Comparator<List<Cell>> leagueRowComparator = new Comparator<List<Cell>>() {
        @Override
        public int compare(List<Cell> o1, List<Cell> o2) {
            String turn1 = o1.get(0).getVal();// turn
            String turn2 = o2.get(0).getVal();
            if (turn1.compareTo(turn2) > 0) {
                return 1;
            } else if (turn1.compareTo(turn2) < 0) {
                return -1;
            } else {
                String key1 = getRowKey(o1);
                String key2 = getRowKey(o2);
                return key1.compareTo(key2);
            }
        }

        private String getRowKey(List<Cell> o1) {
            List<String> teams = new ArrayList<>();
            teams.add(o1.get(2).getVal());// host
            teams.add(o1.get(4).getVal());// guest
            Collections.sort(teams);
            String key = teams.get(0) + teams.get(1);
            return key;
        }
    };

    private static void addCompanyRate(MatchRate matchRate, Result scoreRet, List<Cell> row, MatchRuleMgr matchRuleMgr)
            throws WriteException {
        if (matchRate == null) {
            return;
        }
        for (String com : matchRate.getCompany()) {
            Rate rate = matchRate.getRate(com);
            float win = rate.getWin();
            float draw = rate.getDraw();
            float lose = rate.getLose();
            float winEnd = rate.getWinEnd();
            float drawEnd = rate.getDrawEnd();
            float loseEnd = rate.getLoseEnd();
            float winRatio = rate.getWinRatio();
            float drawRatio = rate.getDrawRatio();
            float loseRatio = rate.getLoseRatio();

            // Rate forecast
            Result rateFc = LfsUtil.getRateFc(winEnd, drawEnd, loseEnd);
            row.add(LfsUtil.getResultCell(rateFc));

            // Trend forecast
            Result trendFc = LfsUtil.getTrendFc(win, draw, lose, winEnd, drawEnd, loseEnd);
            row.add(LfsUtil.getResultCell(trendFc));

            matchRuleMgr.addRule(rateFc, trendFc, scoreRet);
            String forecastId = LfsUtil.getForecastId(rateFc, trendFc);
            // Forecast result
            if (LfsUtil.leagueForecastRuleMap.containsKey(forecastId)) {
                row.add(new Cell(LfsUtil.getResultStr(LfsUtil.leagueForecastRuleMap.get(forecastId).getScoreRet())));
            } else {
                row.add(new Cell(""));
            }
            // Difference Value
            row.add(new Cell(LfsUtil.getDValueCategory(rate)));

            // Bet result, 0:min, 1: middle, 3: max
            String betRetVal = getBetRetValue(scoreRet, win, draw, lose);
            row.add(new Cell(betRetVal));

            // Original rate
            WritableCellFormat format = getRateValueFormat(betRetVal);
            row.add(scoreRet.isWin() ? new Cell(win, format) : new Cell(win));
            row.add(scoreRet.isDraw() ? new Cell(draw, format) : new Cell(draw));
            row.add(scoreRet.isLose() ? new Cell(lose, format) : new Cell(lose));

            // Final rate
            row.add(new Cell(winEnd, getEndRateFont(win, winEnd)));
            row.add(new Cell(drawEnd, getEndRateFont(draw, drawEnd)));
            row.add(new Cell(loseEnd, getEndRateFont(lose, loseEnd)));

            // Buy ratio
            Result ret = LfsUtil.getRatioFc(winRatio, drawRatio, loseRatio);
            row.add(new Cell(winRatio, ret.isWin() ? LfsUtil.getRedFont() : null));
            row.add(new Cell(drawRatio, ret.isDraw() ? LfsUtil.getRedFont() : null));
            row.add(new Cell(loseRatio, ret.isLose() ? LfsUtil.getRedFont() : null));
        }
    }

    private static String getBetRetValue(Result scoreRet, float win, float draw, float lose) {
        String[] betRet = new String[3];
        if (win <= draw && win <= lose) {
            betRet[0] = "0";
            if (draw < lose) {
                betRet[1] = "1";
                betRet[2] = "3";
            } else {
                betRet[1] = "3";
                betRet[2] = "1";
            }
        } else if (draw <= win && draw <= lose) {
            betRet[1] = "0";
            if (win < lose) {
                betRet[0] = "1";
                betRet[2] = "3";
            } else {
                betRet[0] = "3";
                betRet[2] = "1";
            }
        } else if (lose <= win && lose <= draw) {
            betRet[2] = "0";
            if (win < draw) {
                betRet[0] = "1";
                betRet[1] = "3";
            } else {
                betRet[0] = "3";
                betRet[1] = "1";
            }
        }
        String betRetVal = "NULL";
        if (scoreRet.isWin()) {
            betRetVal = betRet[0];
        } else if (scoreRet.isDraw()) {
            betRetVal = betRet[1];
        } else if (scoreRet.isLose()) {
            betRetVal = betRet[2];
        }
        return betRetVal;
    }

    private static WritableCellFormat getRateValueFormat(String betRetVal) throws WriteException {
        WritableCellFormat format = null;
        if (betRetVal.equals("0")) {
            format = LfsUtil.getRedFmt();
        } else if (betRetVal.equals("1")) {
            format = LfsUtil.getYellowFmt();
        } else if (betRetVal.equals("3")) {
            format = LfsUtil.getBlueFmt();
        }
        return format;
    }

    private static List<Cell> getLeagueMatchReportHead() {
        List<Cell> head = new ArrayList<>();
        for (String name : leagueMatchReportHeadNames) {
            head.add(new Cell(name));
        }
        return head;
    }

    private static void addRows(WritableSheet sheet, List<List<Cell>> rows) throws WriteException,
            RowsExceededException {
        for (int row = 0; row < rows.size(); row++) {
            List<Cell> rowList = rows.get(row);
            for (int col = 0; col < rowList.size(); col++) {
                Cell cell = rowList.get(col);
                if (cell.getFmt() != null) {
                    sheet.addCell(new Label(col, row, cell.getVal(), cell.getFmt()));
                } else {
                    sheet.addCell(new Label(col, row, cell.getVal()));
                }
            }
        }
    }

    private static List<Cell> getBoardReportHead() {
        List<Cell> head = new ArrayList<>();
        for (String name : boardReportHeadNames) {
            head.add(new Cell(name));
        }
        return head;
    }

    private static List<Cell> getBlankRow(int size) {
        List<Cell> head = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            head.add(new Cell(""));
        }
        return head;
    }
}
