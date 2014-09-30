package com.an.lfs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.an.lfs.db.SussDao;
import com.an.lfs.enu.Country;
import com.an.lfs.service.BoardService;
import com.an.lfs.vo.BoardPo;
import com.an.lfs.vo.BoardSummary;
import com.an.lfs.vo.BoardTeam;
import com.an.lfs.vo.LeagueSummary;
import com.an.lfs.vo.MatchInfo;
import com.an.lfs.vo.MatchRuleMgr;

public class LfsMain {
    private static final Logger LOGGER = LoggerFactory.getLogger(LfsMain.class);
    public final static Country[] LEAGUE_COUNTRY = new Country[] { Country.EL, Country.CL };
    public final static Country[] ALL_COUNTRY = new Country[] { Country.ARG, Country.BRA, Country.ENG, Country.ENG_B,
            Country.ENG_C, Country.ESP, Country.FRA, Country.FRA_B, Country.GER, Country.GER_B, Country.ITA,
            Country.JPN, Country.JPN_B, Country.NOR, Country.NED, Country.NED_B, Country.SWE, Country.USA, };
    private static int[] YEAR_RANGE = new int[] { 2013, 2014 };
    private static final boolean initialized = true;

    public static void main(String[] args) throws Exception {
        init();

        if (!initialized) {
            initBoardData();
        }

        exportReport();
    }

    // 6: League summary 5: Board summary 4: board report, 3: rate difference match report 2: match report,
    // 1: league match report 0: old style match
    // report
    private static int REPORT_TYPE = 6;

    private static void exportReport() throws Exception {
        BoardService boardService = AppContextFactory.getBoardService();

        List<LeagueSummary> leagueSumList = new ArrayList<>();
        for (Country cty : ALL_COUNTRY) {
            switch (REPORT_TYPE) {
            case 6:
                int lastYearNum = 11;
                LeagueSummary leagueSum = boardService.getLeagueSummary(cty.getVal(), lastYearNum);
                leagueSumList.add(leagueSum);
                break;
            case 5:
                List<BoardSummary> bsList = boardService.getBoardSummary(cty.getVal());
                Collections.sort(bsList);
                for (BoardSummary bs : bsList) {
                    LOGGER.info("{}", bs);
                }
                break;
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

        Collections.sort(leagueSumList);
        for (LeagueSummary sum : leagueSumList) {
            LOGGER.info("{}", sum);
        }
    }

    private static void initBoardData() {
        SussDao dao = AppContextFactory.getDao();
        dao.clearBoard();

        Country[] allCountries = new Country[] { Country.ARG, Country.BRA, Country.ENG, Country.ENG_B, Country.ENG_C,
                Country.ESP, Country.FRA, Country.FRA_B, Country.GER, Country.GER_B, Country.ITA, Country.JPN,
                Country.JPN_B, Country.NOR, Country.NED, Country.NED_B, Country.SWE, Country.USA, };
        for (Country cty : allCountries) {
            BoardLoader loader = new BoardLoader(cty);
            List<BoardPo> bpList = loader.getBoardPoList();
            dao.addBoards(bpList);
        }
    }

    private static void init() throws FileNotFoundException {
        String filepath = LfsUtil.getConfFilePath("log4j.properties");
        System.out.println("filepath: " + filepath);
        File f = new File(filepath);
        PropertyConfigurator.configure(new FileInputStream(f));

        AppContextFactory.init();
    }
}
