package logic;

import java.util.*;

public class BFS {
    public static boolean[][] run(Queue<int[]> queue, int[][][] cells, int M, int N) {
        boolean[][] visited = new boolean[M][N];

        while (!queue.isEmpty()) {
            int[] first = queue.poll();
            int row = first[0];
            int col = first[1];

            if (visited[row][col]) continue;

            int curType = cells[row][col][1];
            if (curType == -1) continue; // Desert: skip cell

            visited[row][col] = true;

            int curHeight = cells[row][col][0];

            int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};
            for (int[] d : directions) {
                int nr = row + d[0];
                int nc = col + d[1];
                if (nr < 0 || nr >= M || nc < 0 || nc >= N) continue;
                if (visited[nr][nc]) continue;

                int neighType = cells[nr][nc][1];
                if (neighType == -1) continue; // Desert neighbor skip

                int neighHeight = cells[nr][nc][0];

                if (curType == -2) {
                    // If current is river: flow only to river neighbors of the same height
                    if (neighType == -2 ) {
                        if (neighHeight == curHeight) {
                            queue.add(new int[]{nr, nc});
                        } else {
                            continue;
                        }
                    }



                } else {

                    if (neighType == -2) {  // neighbor is river
                        // Flow from normal cell to river only if heights are equal
                        if (neighHeight == curHeight) {
                            queue.add(new int[]{nr, nc});
                        } else {
                            continue;
                        }
                    } else {  // neighbor is normal
                        // flow if neighbor  is strictly higher
                        if (neighHeight > curHeight) {
                            queue.add(new int[]{nr, nc});
                        }
                    }
                }
            }
        }

        return visited;
    }
}
