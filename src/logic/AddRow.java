package logic;

import java.util.Queue;

public class AddRow {
    public static void addRow(int row, Queue<int[]> queue, int totalCols, int[][][] cells) {
        for (int col = 0; col < totalCols; col++) {
            int type = cells[row][col][1];
            if (type != -1) {  // skip desert cells
                queue.add(new int[] {row, col});
            }
        }
    }
}
