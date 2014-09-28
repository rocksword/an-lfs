package com.an.lfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;

import com.an.lfs.enu.Country;
import com.an.lfs.vo.BoardTeam;
import com.an.lfs.vo.MatchInfo;
import com.an.lfs.vo.MatchRuleMgr;

public class LfsMain {
    public final static Country[] LEAGUE_COUNTRY = new Country[] { Country.EL, Country.CL };
    public final static Country[] ALL_COUNTRY = new Country[] { Country.BRA };
    private static int[] YEAR_RANGE = new int[] { 2013, 2014 };
    // 5:draw match report 4: board report, 3: rate difference match report 2: match report,
    // 1: league match report 0: old style match
    // report
    private static int REPORT_TYPE = 0;

    public static void main(String[] args) throws Exception {
        init();

        for (Country cty : ALL_COUNTRY) {
            switch (REPORT_TYPE) {
            case 4:
                Map<Integer, List<BoardTeam>> teamMap = new BoardLoader(cty).getBoardTeamMap();
                new BoardReportMaker().make(cty, teamMap);
                break;
            case 3:
                for (int year = YEAR_RANGE[0]; year <= YEAR_RANGE[1]; year++) {
                    Map<Integer, List<MatchInfo>> map = new MatchLoader(cty, year).getYearMatchMap();
                    new DiffReportMaker().make(year, cty, map, new RateLoader(cty, year));
                }
                break;
            case 2:
                for (int year = YEAR_RANGE[0]; year <= YEAR_RANGE[1]; year++) {
                    Map<Integer, List<MatchInfo>> map = new MatchLoader(cty, year).getYearMatchMap();
                    new ReportMaker().make(cty, map, new RateLoader(cty, year), new MatchRuleMgr());
                }
                break;
            case 1:
                for (int year = YEAR_RANGE[0]; year <= YEAR_RANGE[1]; year++) {
                    Map<Integer, List<MatchInfo>> map = new LeagueMatchLoader(cty, YEAR_RANGE[0], YEAR_RANGE[1])
                            .getYearMatchMap();
                    new ReportMaker().makeLeague(cty, map, new RateLoader(cty, year), new MatchRuleMgr());
                }
                break;
            case 0:
                for (int year = YEAR_RANGE[0]; year <= YEAR_RANGE[1]; year++) {
                    MatchReportMaker maker = new MatchReportMaker(cty, year);
                    maker.analyzeMatch();
                    maker.analyzeRate();
                    maker.make();
                }
                break;
            default:
                break;
            }
        }
    }

    private static void init() throws FileNotFoundException {
        String filepath = LfsUtil.getConfFilePath("log4j.properties");
        System.out.println("filepath: " + filepath);
        File f = new File(filepath);
        PropertyConfigurator.configure(new FileInputStream(f));
    }
}
