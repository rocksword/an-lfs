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
import com.an.lfs.vo.MatchDiffRow;
import com.an.lfs.vo.MatchInfo;
import com.an.lfs.vo.MatchRate;
import com.an.lfs.vo.MatchRule;
import com.an.lfs.vo.MatchRuleMgr;
import com.an.lfs.vo.Rate;

public class ReportMaker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReportMaker.class);
    public static final String TYPE_N_3 = "-3.0";
    public static final String TYPE_N_3_25 = "-3.0 : -2.5";
    public static final String TYPE_N_25_2 = "-2.5 : -2.0";
    public static final String TYPE_N_2_15 = "-2.0   : -1.5";
    public static final String TYPE_N_15_1 = "-1.5 : -1.0";
    public static final String TYPE_N_1_05 = "-1.0   : -0.5";
    public static final String TYPE_N_05_0 = "-0.5 : 0.0";
    public static final String TYPE_0_05 = "+0.0 : +0.5";
    public static final String TYPE_05_1 = "+0.5 : +1.0";
    public static final String TYPE_1_15 = "+1.0 : +1.5";
    public static final String TYPE_15_2 = "+1.5 : +2.0";
    public static final String TYPE_2_25 = "+2.0 : +2.5";
    public static final String TYPE_25_3 = "+2.5 : +3.0";
    public static final String TYPE_3 = "+3.0 : ";
    private static String[] matchReportHeadNames = new String[] { "Year", "T", "Date", "Host", "R", "Score", "PK",
            "Guest", "R", "TN-LN", "ScoreRet", "RankFc", };

    private static String[] ruleHeadNames = new String[] { "ID", "RateFc", "TrendFc", "ScoreRet", "Count", "Percent" };

    private static String[] boardReportHeadNames = new String[] { "Year", "R", "Team", "T", "W", "D", "L", "For",
            "Against", "Net", "For", "Against", "W%", "D%", "L%", "Score" };

    private static String[] leagueMatchReportHeadNames = new String[] { "T", "Date", "Host", "Score", "Guest",
            "ScoreRet" };

    private static List<String> sumTypeNames = new ArrayList<>();
    static {
        sumTypeNames.add("TOTAL");
        sumTypeNames.add("DRAW");
        sumTypeNames.add(TYPE_N_3);
        sumTypeNames.add(TYPE_N_3_25);
        sumTypeNames.add(TYPE_N_25_2);
        sumTypeNames.add(TYPE_N_2_15);
        sumTypeNames.add(TYPE_N_15_1);
        sumTypeNames.add(TYPE_N_1_05);
        sumTypeNames.add(TYPE_N_05_0);
        sumTypeNames.add(TYPE_0_05);
        sumTypeNames.add(TYPE_05_1);
        sumTypeNames.add(TYPE_1_15);
        sumTypeNames.add(TYPE_15_2);
        sumTypeNames.add(TYPE_25_3);
        sumTypeNames.add(TYPE_3);
    }

    public static void makeMatchDiffReport(int year, Country country, Map<Integer, List<MatchInfo>> yearMatchMap,
            RateLoader rateLoader) throws Exception {
        if (yearMatchMap.isEmpty()) {
            LOGGER.warn("Empty yearMatchMap.");
            return;
        }

        List<MatchDiffRow> matchDiffRowList = new ArrayList<>();

        LOGGER.info("country: " + country.getVal() + ", year: " + year);
        for (MatchInfo mi : yearMatchMap.get(year)) {
            MatchDiffRow diffRow = new MatchDiffRow();
            diffRow.setYear(year);
            diffRow.setTurn(mi.getTurn());
            diffRow.setDate(mi.getDate().substring(0, 5));
            diffRow.setHost(mi.getHost());
            diffRow.setGuest(mi.getGuest());
            diffRow.setScore(mi.getScore());
            MatchRate matchRate = rateLoader.getMatchRate(mi.getHost(), mi.getGuest());
            if (matchRate != null) {
                addDiffRate(matchRate, diffRow);
            }
            if (String.valueOf(diffRow.getWin()).equals("0.0")) {
                LOGGER.warn("Invalid diffRow: " + diffRow);
                continue;
            }
            matchDiffRowList.add(diffRow);
        }

        // Compute first sheet rows
        List<List<Cell>> matchRowList = new ArrayList<>();
        List<Cell> head = new ArrayList<>();
        String[] headNames = new String[] { "Year", "T", "Date", "Host", "Score", "Guest", "D-Val", "Win", "Draw",
                "Lose" };
        for (String name : headNames) {
            head.add(new Cell(name));
        }
        matchRowList.add(head);

        for (MatchDiffRow diffRow : matchDiffRowList) {
            List<Cell> row = new ArrayList<>();
            row.add(new Cell(diffRow.getYear()));
            row.add(new Cell(diffRow.getTurn()));
            row.add(new Cell(diffRow.getDate()));
            row.add(new Cell(diffRow.getHost()));
            row.add(new Cell(diffRow.getScore()));
            row.add(new Cell(diffRow.getGuest()));
            row.add(new Cell(LfsUtil.DECIMAL_FORMAT.format(diffRow.getDiffVal())));
            row.add(new Cell(diffRow.getWin()));
            row.add(new Cell(diffRow.getDraw()));
            row.add(new Cell(diffRow.getLose()));
            matchRowList.add(row);
        }
        String filepath = LfsUtil.getOutputFilePath(LfsUtil.getMatchDiffExcelFile(year, country));
        LOGGER.info("Generate file " + filepath);
        WritableWorkbook wb = Workbook.createWorkbook(new File(filepath));

        // Compute second sheet rows
        List<List<Cell>> sumRowList = new ArrayList<>();
        List<Cell> headList = new ArrayList<>();
        headList.add(new Cell("Diff Type"));
        headList.add(new Cell("Draw Count"));
        headList.add(new Cell("Draw Percent(%)"));
        headList.add(new Cell("Diff Total Count"));
        headList.add(new Cell("Diff Draw Count"));
        headList.add(new Cell("Diff Draw Percent(%)"));
        sumRowList.add(headList);
        initSumRowData(matchDiffRowList, sumRowList);

        // Construct diff summary sheet
        WritableSheet sheet0 = wb.createSheet("summary", 0);
        addRows(sheet0, sumRowList);

        WritableSheet sheet1 = wb.createSheet("diff", 1);
        addRows(sheet1, matchRowList);

        wb.write();
        wb.close();
    }

    private static void initSumRowData(List<MatchDiffRow> allRow, List<List<Cell>> sumRowList) {
        int totalMatchCnt = 0;
        int cntN30_ = 0;
        int cntN25_30 = 0;
        int cntN20_25 = 0;
        int cntN15_20 = 0;
        int cntN10_15 = 0;
        int cntN05_10 = 0;
        int cntN00_05 = 0;
        int cnt00_05 = 0;
        int cnt05_10 = 0;
        int cnt10_15 = 0;
        int cnt15_20 = 0;
        int cnt20_25 = 0;
        int cnt25_30 = 0;
        int cnt30_ = 0;

        int totalDrawCnt = 0;
        int drawCntN30_ = 0;
        int drawCntN25_30 = 0;
        int drawCntN20_25 = 0;
        int drawCntN15_20 = 0;
        int drawCntN10_15 = 0;
        int drawCntN05_10 = 0;
        int drawCntN00_05 = 0;
        int drawCnt00_05 = 0;
        int drawCnt05_10 = 0;
        int drawCnt10_15 = 0;
        int drawCnt15_20 = 0;
        int drawCnt20_25 = 0;
        int drawCnt25_30 = 0;
        int drawCnt30_ = 0;
        for (MatchDiffRow row : allRow) {
            totalMatchCnt++;
            boolean isDraw = LfsUtil.getScoreRet(row.getScore()).isDraw();
            if (isDraw) {
                totalDrawCnt++;
            }

            float dv = row.getDiffVal();
            if (dv < -3.0f) {
                cntN30_++;
                if (isDraw) {
                    drawCntN30_++;
                }
            } else if (dv >= -3.0f && dv < -2.5f) {
                cntN25_30++;
                if (isDraw) {
                    drawCntN25_30++;
                }
            } else if (dv >= -2.5f && dv < -2.0f) {
                cntN20_25++;
                if (isDraw) {
                    drawCntN20_25++;
                }
            } else if (dv >= -2.0f && dv < -1.5f) {
                cntN15_20++;
                if (isDraw) {
                    drawCntN15_20++;
                }
            } else if (dv >= -1.5f && dv < -1.0f) {
                cntN10_15++;
                if (isDraw) {
                    drawCntN10_15++;
                }
            } else if (dv >= -1.0f && dv < -0.5f) {
                cntN05_10++;
                if (isDraw) {
                    drawCntN05_10++;
                }
            } else if (dv >= -0.5f && dv < 0.0f) {
                cntN00_05++;
                if (isDraw) {
                    drawCntN00_05++;
                }
            } else if (dv >= 0.0f && dv < 0.5f) {
                cnt00_05++;
                if (isDraw) {
                    drawCnt00_05++;
                }
            } else if (dv >= 0.5f && dv < 1.0f) {
                cnt05_10++;
                if (isDraw) {
                    drawCnt05_10++;
                }
            } else if (dv >= 1.0f && dv < 1.5f) {
                cnt10_15++;
                if (isDraw) {
                    drawCnt10_15++;
                }
            } else if (dv >= 1.5f && dv < 2.0f) {
                cnt15_20++;
                if (isDraw) {
                    drawCnt15_20++;
                }
            } else if (dv >= 2.0f && dv < 2.5f) {
                cnt20_25++;
                if (isDraw) {
                    drawCnt20_25++;
                }
            } else if (dv >= 2.5f && dv < 3.0f) {
                cnt25_30++;
                if (isDraw) {
                    drawCnt25_30++;
                }
            } else if (dv >= 3.0f) {
                cnt30_++;
                if (isDraw) {
                    drawCnt30_++;
                }
            }
        }

        List<Integer> typeCntList = new ArrayList<>();
        typeCntList.add(totalMatchCnt);
        typeCntList.add(totalMatchCnt);
        typeCntList.add(cntN30_);
        typeCntList.add(cntN25_30);
        typeCntList.add(cntN20_25);
        typeCntList.add(cntN15_20);
        typeCntList.add(cntN10_15);
        typeCntList.add(cntN05_10);
        typeCntList.add(cntN00_05);
        typeCntList.add(cnt00_05);
        typeCntList.add(cnt05_10);
        typeCntList.add(cnt10_15);
        typeCntList.add(cnt15_20);
        typeCntList.add(cnt20_25);
        typeCntList.add(cnt25_30);
        typeCntList.add(cnt30_);

        List<Integer> drawCntList = new ArrayList<>();
        drawCntList.add(totalMatchCnt);
        drawCntList.add(totalDrawCnt);
        drawCntList.add(drawCntN30_);
        drawCntList.add(drawCntN25_30);
        drawCntList.add(drawCntN20_25);
        drawCntList.add(drawCntN15_20);
        drawCntList.add(drawCntN10_15);
        drawCntList.add(drawCntN05_10);
        drawCntList.add(drawCntN00_05);
        drawCntList.add(drawCnt00_05);
        drawCntList.add(drawCnt05_10);
        drawCntList.add(drawCnt10_15);
        drawCntList.add(drawCnt15_20);
        drawCntList.add(drawCnt20_25);
        drawCntList.add(drawCnt25_30);
        drawCntList.add(drawCnt30_);

        List<Integer> drawPerList = new ArrayList<>();
        drawPerList.add(100);
        drawPerList.add((int) ((float) totalDrawCnt / (float) totalMatchCnt * 100f));
        drawPerList.add((int) ((float) drawCntN30_ / (float) totalDrawCnt * 100f));
        drawPerList.add((int) ((float) drawCntN25_30 / (float) totalDrawCnt * 100f));
        drawPerList.add((int) ((float) drawCntN20_25 / (float) totalDrawCnt * 100f));
        drawPerList.add((int) ((float) drawCntN15_20 / (float) totalDrawCnt * 100f));
        drawPerList.add((int) ((float) drawCntN10_15 / (float) totalDrawCnt * 100f));
        drawPerList.add((int) ((float) drawCntN05_10 / (float) totalDrawCnt * 100f));
        drawPerList.add((int) ((float) drawCntN00_05 / (float) totalDrawCnt * 100f));
        drawPerList.add((int) ((float) drawCnt00_05 / (float) totalDrawCnt * 100f));
        drawPerList.add((int) ((float) drawCnt05_10 / (float) totalDrawCnt * 100f));
        drawPerList.add((int) ((float) drawCnt10_15 / (float) totalDrawCnt * 100f));
        drawPerList.add((int) ((float) drawCnt15_20 / (float) totalDrawCnt * 100f));
        drawPerList.add((int) ((float) drawCnt20_25 / (float) totalDrawCnt * 100f));
        drawPerList.add((int) ((float) drawCnt25_30 / (float) totalDrawCnt * 100f));
        drawPerList.add((int) ((float) drawCnt30_ / (float) totalDrawCnt * 100f));

        List<Integer> typeDrawCntList = new ArrayList<>();
        typeDrawCntList.add(totalMatchCnt);
        typeDrawCntList.add(totalDrawCnt);
        typeDrawCntList.add(drawCntN30_);
        typeDrawCntList.add(drawCntN25_30);
        typeDrawCntList.add(drawCntN20_25);
        typeDrawCntList.add(drawCntN15_20);
        typeDrawCntList.add(drawCntN10_15);
        typeDrawCntList.add(drawCntN05_10);
        typeDrawCntList.add(drawCntN00_05);
        typeDrawCntList.add(drawCnt00_05);
        typeDrawCntList.add(drawCnt05_10);
        typeDrawCntList.add(drawCnt10_15);
        typeDrawCntList.add(drawCnt15_20);
        typeDrawCntList.add(drawCnt20_25);
        typeDrawCntList.add(drawCnt25_30);
        typeDrawCntList.add(drawCnt30_);

        List<Integer> typePerList = new ArrayList<>();
        typePerList.add(100);
        typePerList.add(100);
        typePerList.add((int) ((float) drawCntN30_ / (float) cntN30_ * 100f));
        typePerList.add((int) ((float) drawCntN25_30 / (float) cntN25_30 * 100f));
        typePerList.add((int) ((float) drawCntN20_25 / (float) cntN20_25 * 100f));
        typePerList.add((int) ((float) drawCntN15_20 / (float) cntN15_20 * 100f));
        typePerList.add((int) ((float) drawCntN10_15 / (float) cntN10_15 * 100f));
        typePerList.add((int) ((float) drawCntN05_10 / (float) cntN05_10 * 100f));
        typePerList.add((int) ((float) drawCntN00_05 / (float) cntN00_05 * 100f));
        typePerList.add((int) ((float) drawCnt00_05 / (float) cnt00_05 * 100f));
        typePerList.add((int) ((float) drawCnt05_10 / (float) cnt05_10 * 100f));
        typePerList.add((int) ((float) drawCnt10_15 / (float) cnt10_15 * 100f));
        typePerList.add((int) ((float) drawCnt15_20 / (float) cnt15_20 * 100f));
        typePerList.add((int) ((float) drawCnt20_25 / (float) cnt20_25 * 100f));
        typePerList.add((int) ((float) drawCnt25_30 / (float) cnt25_30 * 100f));
        typePerList.add((int) ((float) drawCnt30_ / (float) cnt30_ * 100f));

        for (int i = 0; i < sumTypeNames.size(); i++) {
            List<Cell> sumRow = new ArrayList<>();
            sumRow.add(new Cell(sumTypeNames.get(i)));
            sumRow.add(new Cell(drawCntList.get(i)));
            sumRow.add(new Cell(drawPerList.get(i) + "%"));
            sumRow.add(new Cell(typeCntList.get(i)));
            sumRow.add(new Cell(typeDrawCntList.get(i)));
            sumRow.add(new Cell(typePerList.get(i) + "%"));
            sumRowList.add(sumRow);
        }
    }

    private static void addDiffRate(MatchRate matchRate, MatchDiffRow reportRow) {
        for (String com : matchRate.getCompany()) {
            Rate rate = matchRate.getRate(com);
            float win = rate.getWin();
            float draw = rate.getDraw();
            float lose = rate.getLose();
            reportRow.setWin(win);
            reportRow.setDraw(draw);
            reportRow.setLose(lose);
            reportRow.setDiffVal(lose - win);
        }
    }

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

        List<List<Cell>> matchRowList = new ArrayList<>();
        List<Cell> head = getMatchReportHead();
        LfsUtil.addCompanyRateHead(rateLoader, head);
        matchRowList.add(head);

        int totalYear = years.size();
        for (int i = 0; i < totalYear; i++) {
            if (i > 0) {
                matchRowList.add(getBlankRow(boardReportHeadNames.length));
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

                matchRowList.add(row);
            }
        }

        String filepath = LfsUtil.getOutputFilePath(LfsUtil.getMatchExcelFile(country));
        LOGGER.info("Generate file " + filepath);
        WritableWorkbook wb = Workbook.createWorkbook(new File(filepath));
        WritableSheet sheet = wb.createSheet("match", 0);
        addRows(sheet, matchRowList);

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