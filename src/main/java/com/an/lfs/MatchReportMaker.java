package com.an.lfs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.lfs.enu.CmpType;
import com.an.lfs.enu.Country;
import com.an.lfs.enu.ForecastRetType;
import com.an.lfs.enu.RateType;
import com.an.lfs.enu.Result;
import com.an.lfs.enu.TeamType;
import com.an.lfs.vo.BetResultNum;
import com.an.lfs.vo.Cell;
import com.an.lfs.vo.LfsConfMgr;
import com.an.lfs.vo.Match;
import com.an.lfs.vo.MatchRateWy;
import com.an.lfs.vo.TeamMgr;

public class MatchReportMaker extends RateAnalyzer {
    private static final Logger logger = LoggerFactory.getLogger(MatchReportMaker.class);
    // '- + -' -> [3,3]
    private Map<CmpType, BetResultNum> cmpBetRetNumMap = new HashMap<>();
    private WritableWorkbook wb = null;

    public MatchReportMaker(Country country, int year) {
        super(country, year);
        for (CmpType ct : CmpType.cmpTypeMap.values()) {
            cmpBetRetNumMap.put(ct, new BetResultNum());
        }
    }

    public void make() throws Exception {
        String filepath = LfsUtil.getOutputFilePath(LfsUtil.getSumExcelFile(country, year));
        logger.info("Generate file " + filepath);
        wb = Workbook.createWorkbook(new File(filepath));

        logger.info("Rate sheet.");
        List<List<Cell>> rateRows = getRateSumRows();
        addSheet("Rate", 1, rateRows);

        logger.info("Rate compare sheet.");
        List<List<Cell>> tmpRows = new ArrayList<>();
        for (int i = 1; i < rateRows.size(); i++) {
            tmpRows.add(rateRows.get(i));
        }
        Collections.sort(tmpRows, cmpComparator);
        List<List<Cell>> rateCmpRows = new ArrayList<>();
        rateCmpRows.add(rateRows.get(0));
        rateCmpRows.addAll(tmpRows);
        addSheet("Rate CMP", 2, rateCmpRows);

        logger.info("Match sheet.");
        addSheet("Match", 0, getMatSumRows());
        wb.write();
        wb.close();
    }

    private void addSheet(String sheetName, int index, List<List<Cell>> rateRows) throws RowsExceededException,
            WriteException {
        WritableSheet sheet = wb.createSheet(sheetName, index);
        for (int row = 0; row < rateRows.size(); row++) {
            List<Cell> rowList = rateRows.get(row);
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

    String[] headers = new String[] { "KEY", "Host", "Guest", "S", "H", "M", "G",//
            "W", "D", "L", "Fc", "Sc", "B" };
    private int CMP_INDEX = headers.length + 8 - 1;

    private List<String> rateKeyList = new ArrayList<>();

    private List<List<Cell>> getRateSumRows() throws WriteException {
        rateKeyList.addAll(matchMap.keySet());
        Collections.sort(rateKeyList);

        List<List<Cell>> rows = new ArrayList<>();
        rows.add(getRatHead());

        for (String key : rateKeyList) {
            Match mat = matchMap.get(key);
            MatchRateWy matRate = matRateMap.get(key);
            if (matRate == null) {
                logger.warn("Match rate is null, " + key);
                continue;
            }
            Result sr = matRate.getScoreRet();
            List<Cell> row = new ArrayList<>();
            row.add(new Cell(mat.getSimpleKey()));// key
            row.add(new Cell(TeamMgr.getName(country, mat.getHost(), year)));
            row.add(new Cell(TeamMgr.getName(country, mat.getGuest(), year)));
            row.add(new Cell(mat.getScore()));
            row.add(new Cell(matRate.getRateType(TeamType.HOST).getVal()));
            row.add(new Cell(matRate.getRateType(TeamType.MID).getVal()));
            row.add(new Cell(matRate.getRateType(TeamType.GUEST).getVal()));
            row.add(new Cell(matRate.getWin(), sr.isWin() ? LfsUtil.getRedFont() : null));
            row.add(new Cell(matRate.getDraw(), sr.isDraw() ? LfsUtil.getRedFont() : null));
            row.add(new Cell(matRate.getLose(), sr.isLose() ? LfsUtil.getRedFont() : null));
            Result rateFc = LfsUtil.getRateFc(matRate.getWinEnd(), matRate.getDrawEnd(), matRate.getLoseEnd());
            row.add(new Cell(LfsUtil.getResultStr(rateFc)));
            row.add(new Cell(LfsUtil.getResultStr(sr)));
            row.add(new Cell(LfsUtil.getRateBetStr(LfsUtil.getRateBet(rateFc, sr))));

            int i = 0;
            for (String com : LfsConfMgr.getCompany(country)) {
                row.add(new Cell(matRate.getWin(com), sr.isWin() ? LfsUtil.getRedFont() : null));
                row.add(new Cell(matRate.getDraw(com), sr.isDraw() ? LfsUtil.getRedFont() : null));
                row.add(new Cell(matRate.getLose(com), sr.isLose() ? LfsUtil.getRedFont() : null));
                row.add(new Cell(LfsUtil.getRateBetStr(matRate.getBetRet(com))));
                CmpType cmp = matRate.getComparator(com);
                if (i == 0) {
                    BetResultNum betResultNum = cmpBetRetNumMap.get(cmp);
                    betResultNum.addBetResult(matRate.getBetRet(com));
                    cmpBetRetNumMap.put(cmp, betResultNum);
                }
                i++;
                row.add(new Cell(cmp.getVal()));
            }
            rows.add(row);
        }
        return rows;
    }

    private List<Cell> getRatHead() {
        List<Cell> headRow = new ArrayList<>();
        for (String header : headers) {
            headRow.add(new Cell(header));
        }
        for (String comp : LfsConfMgr.getCompany(country)) {
            headRow.add(new Cell(comp));
            headRow.add(new Cell("D"));
            headRow.add(new Cell("L"));
            headRow.add(new Cell("B"));
            headRow.add(new Cell("CMP"));
        }
        return headRow;
    }

    private List<List<Cell>> getMatSumRows() {
        List<List<Cell>> rows = new ArrayList<>();
        addBetHeadRow(rows);

        // Total
        List<Cell> totalRow = new ArrayList<>();
        BetResultNum betRet = matSum.getBetRetNum();
        totalRow.add(new Cell("TOTAL"));
        totalRow.add(new Cell(betRet.getPassNum()));
        totalRow.add(new Cell(betRet.getPassPer()));
        totalRow.add(new Cell(betRet.getFailNum()));
        totalRow.add(new Cell(betRet.getFailPer()));
        rows.add(totalRow);

        // Team type
        for (TeamType tt : TeamType.allTeamTypes) {
            addBetHeadRow(rows);
            for (RateType st : RateType.allRateTypes) {
                BetResultNum br = matSum.getBetRetNum(tt, st);
                if (br.getPassNum() != 0 && br.getFailNum() != 0) {
                    List<Cell> row = new ArrayList<>();
                    row.add(new Cell(st.getVal()));
                    row.add(new Cell(br.getPassNum()));
                    row.add(new Cell(br.getPassPer()));
                    row.add(new Cell(br.getFailNum()));
                    row.add(new Cell(br.getFailPer()));
                    rows.add(row);
                }
            }
        }

        // Comparator type
        addBetHeadRow(rows);
        for (CmpType ct : CmpType.cmpTypeList) {
            BetResultNum br = cmpBetRetNumMap.get(ct);
            if (br.getPassNum() != 0 || br.getFailNum() != 0) {
                List<Cell> row = new ArrayList<>();
                row.add(new Cell(ct.getVal()));
                row.add(new Cell(br.getPassNum()));
                row.add(new Cell(br.getPassPer()));
                row.add(new Cell(br.getFailNum()));
                row.add(new Cell(br.getFailPer()));
                rows.add(row);
            }
        }

        addMatRetHead(rows);
        for (ForecastRetType ft : ForecastRetType.allFcRetTypes) {
            List<Cell> row = new ArrayList<>();
            row.add(new Cell(ft.getVal()));
            row.add(new Cell(matSum.getFcRetNum(ft)));
            row.add(new Cell(matSum.getFcRetPer(ft)));
            rows.add(row);
        }

        addMatRetHead(rows);
        for (Result ret : Result.allResults) {
            List<Cell> row = new ArrayList<>();
            row.add(new Cell(ret.getVal()));
            row.add(new Cell(matSum.getScoreTypeNum(ret)));
            row.add(new Cell(matSum.getScoreTypePer(ret)));
            rows.add(row);
        }

        return rows;
    }

    private void addBetHeadRow(List<List<Cell>> rows) {
        List<Cell> blackRow = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            blackRow.add(new Cell(""));
        }
        rows.add(blackRow);
        List<Cell> headRow = new ArrayList<>();
        headRow.add(new Cell("Type"));
        headRow.add(new Cell("Pass"));
        headRow.add(new Cell("Percent"));
        headRow.add(new Cell("Fail"));
        headRow.add(new Cell("Percent"));
        rows.add(headRow);
    }

    private void addMatRetHead(List<List<Cell>> rows) {
        List<Cell> blackRow = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            blackRow.add(new Cell(""));
        }
        rows.add(blackRow);
        List<Cell> headRow = new ArrayList<>();
        headRow.add(new Cell("Type"));
        headRow.add(new Cell("Count"));
        headRow.add(new Cell("Percent"));
        rows.add(headRow);
    }

    Comparator<List<Cell>> cmpComparator = new Comparator<List<Cell>>() {
        @Override
        public int compare(List<Cell> o1, List<Cell> o2) {
            return o1.get(CMP_INDEX).getVal().compareTo(o2.get(CMP_INDEX).getVal());
        }
    };
}
