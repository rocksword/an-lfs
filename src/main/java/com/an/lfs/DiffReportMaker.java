package com.an.lfs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.lfs.enu.Country;
import com.an.lfs.vo.Cell;
import com.an.lfs.vo.MatchDiffRow;
import com.an.lfs.vo.MatchInfo;
import com.an.lfs.vo.MatchRate;
import com.an.lfs.vo.Rate;

public class DiffReportMaker extends AbstractReportMaker {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiffReportMaker.class);
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

    private List<String> sumTypeNames = new ArrayList<>();

    public DiffReportMaker() {
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

    public void make(int year, Country country, Map<Integer, List<MatchInfo>> yearMatchMap, RateLoader rateLoader)
            throws Exception {
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

    private void initSumRowData(List<MatchDiffRow> allRow, List<List<Cell>> sumRowList) {
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

    private void addDiffRate(MatchRate matchRate, MatchDiffRow reportRow) {
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
}
