package logic;

import java.util.Queue;

public class AddCol {
    public static void addCol(int col, Queue<int[]> queue, int totalRows, int[][][] cells) {
        for (int row = 0; row < totalRows; row++) {
            int type = cells[row][col][1];
            if (type != -1) {  // skip desert cells
                queue.add(new int[] {row, col});
            }
        }
    }
}
