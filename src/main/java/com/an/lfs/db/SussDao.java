package com.an.lfs.db;

import java.util.List;

public interface SussDao {
    public static final String TBL_BOARD = "board";

    public boolean addBoards(List<BoardPo> boards);

    public BoardPo getBoard(int id);

    public void getAllBoards(List<BoardPo> boards);

    public boolean clearBoard();
}
