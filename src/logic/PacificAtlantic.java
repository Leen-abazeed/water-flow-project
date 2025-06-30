package logic;

import java.util.*;

public class PacificAtlantic {

    public static List<List<Integer>> pacificAtlantic(int[][][] cells) {
        int M = cells.length;
        int N = cells[0].length;

        Queue<int[]> pacificQueue = new LinkedList<>();
        Queue<int[]> atlanticQueue = new LinkedList<>();

        // Add Pacific border (top row + left col)
        AddRow.addRow(0, pacificQueue, N, cells);
        AddCol.addCol(0, pacificQueue, M, cells);

        // Add Atlantic border (bottom row + right col)
        AddRow.addRow(M - 1, atlanticQueue, N, cells);
        AddCol.addCol(N - 1, atlanticQueue, M, cells);

        // Run BFS for Pacific and Atlantic
        boolean[][] pacificReachable = BFS.run(pacificQueue, cells, M, N);
        boolean[][] atlanticReachable = BFS.run(atlanticQueue, cells, M, N);

        // Collect cells reachable from both oceans
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (pacificReachable[i][j] && atlanticReachable[i][j]) {
                    result.add(Arrays.asList(i, j));
                }
            }
        }
        return result;
    }
}
