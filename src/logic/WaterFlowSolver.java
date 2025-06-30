package logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class WaterFlowSolver {
    private final int[][][] cells;  // cells[row][col][0] = height, cells[row][col][1] = type

    public WaterFlowSolver(int[][][] cells) {
        this.cells = cells;
    }

    public List<Point> solve() {
        // Calls PacificAtlantic.pacificAtlantic that returns List<List<Integer>> with coordinates
        List<List<Integer>> reachable = PacificAtlantic.pacificAtlantic(cells);

        List<Point> result = new ArrayList<>();
        for (List<Integer> coord : reachable) {
            int r = coord.get(0);
            int c = coord.get(1);
            result.add(new Point(r, c));
        }
        return result;
    }
}
