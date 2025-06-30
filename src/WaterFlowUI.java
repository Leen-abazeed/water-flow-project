import logic.WaterFlowSolver;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.awt.Point;

public class WaterFlowUI extends JFrame {

    private JPanel mainPanel;
    private JTextField rowsField, colsField;
    private JButton generateButton, sendButton, clearButton;
    private JPanel gridPanel;
    private CellField[][] cells;
    private int rows, cols;

    public WaterFlowUI() {
        setTitle("Water Flow Grid");
        setSize(850, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // center on screen

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(173, 216, 230)); // baby blue
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        setContentPane(mainPanel);

        buildTopPanel();
        buildBottomPanel();

        setVisible(true);
    }

    private void buildTopPanel() {
        JPanel top = new JPanel();
        top.setBackground(mainPanel.getBackground());
        top.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));

        top.add(new JLabel("Rows:"));
        rowsField = new JTextField(3);
        top.add(rowsField);

        top.add(new JLabel("Cols:"));
        colsField = new JTextField(3);
        top.add(colsField);

        generateButton = createRoundedButton("Generate Grid");
        top.add(generateButton);

        generateButton.addActionListener(e -> {
            try {
                rows = Integer.parseInt(rowsField.getText().trim());
                cols = Integer.parseInt(colsField.getText().trim());
                if (rows <= 0 || cols <= 0 || rows > 30 || cols > 30) {
                    JOptionPane.showMessageDialog(this, "Rows and Cols must be between 1 and 30");
                    return;
                }
                buildGridPanel(rows, cols);
                sendButton.setEnabled(true);
                clearButton.setVisible(false);
                pack();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid integers for rows and columns.");
            }
        });

        mainPanel.add(top, BorderLayout.NORTH);
    }

    private void buildGridPanel(int r, int c) {
        if (gridPanel != null) {
            mainPanel.remove(gridPanel);
        }
        gridPanel = new JPanel(new GridLayout(r, c, 5, 5));
        gridPanel.setOpaque(false);
        cells = new CellField[r][c];

        for (int i = 0; i < r; i++) {
            for (int j = 0; j < c; j++) {
                CellField cell = new CellField(i, j);
                cells[i][j] = cell;
                gridPanel.add(cell);
            }
        }
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void buildBottomPanel() {
        JPanel bottom = new JPanel();
        bottom.setBackground(mainPanel.getBackground());
        bottom.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        sendButton = createRoundedButton("Send");
        sendButton.setEnabled(false);
        clearButton = createRoundedButton("Clear");
        clearButton.setVisible(false);

        bottom.add(sendButton);
        bottom.add(clearButton);

        sendButton.addActionListener(e -> onSend());
        clearButton.addActionListener(e -> onClear());

        mainPanel.add(bottom, BorderLayout.SOUTH);
    }

    private void onSend() {
        int[][][] cellsData = new int[rows][cols][2]; // [height, type]

        try {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    String text = cells[i][j].getText().trim();
                    if (text.isEmpty()) {
                        JOptionPane.showMessageDialog(this, "Please fill all cells with heights.");
                        return;
                    }
                    int h = Integer.parseInt(text);
                    if (h < 0) {
                        JOptionPane.showMessageDialog(this, "Heights must be >= 0");
                        return;
                    }
                    cellsData[i][j][0] = h;

                    switch (cells[i][j].getCellType()) {
                        case DESERT -> cellsData[i][j][1] = -1;
                        case RIVER -> cellsData[i][j][1] = -2;
                        default -> cellsData[i][j][1] = 0;
                    }
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid integer heights.");
            return;
        }

        WaterFlowSolver solver = new WaterFlowSolver(cellsData);
        List<Point> result = solver.solve();

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                cells[i][j].setResultMark(false);

        for (Point p : result) {
            cells[p.x][p.y].setResultMark(true);
        }

        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                cells[i][j].setEditable(false);

        sendButton.setEnabled(false);
        clearButton.setVisible(true);
    }

    private void onClear() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++) {
                cells[i][j].setEditable(true);
                cells[i][j].setText("");
                cells[i][j].setCellType(CellType.NORMAL);
                cells[i][j].setResultMark(false);
            }

        clearButton.setVisible(false);
        sendButton.setEnabled(true);
    }

    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Fill entire button background to hide corners
                g2.setColor(new Color(173, 216, 230));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Paint rounded background
                g2.setColor(getModel().isPressed() ? new Color(100, 149, 237) : new Color(70, 130, 180));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Draw button text
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), x, y);

                g2.dispose();
            }

            @Override
            public void setContentAreaFilled(boolean b) {}

            @Override
            protected void paintBorder(Graphics g) {}

            @Override
            public boolean isFocusPainted() {
                return false;
            }

            @Override
            public boolean isFocusOwner() {
                return false;
            }
        };

        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(120, 40));
        return button;
    }

    private enum CellType {
        NORMAL, RIVER, DESERT
    }

    private class CellField extends JTextField {
        private CellType cellType = CellType.NORMAL;
        private boolean resultMark = false;
        private final int row, col;

        public CellField(int row, int col) {
            super();
            this.row = row;
            this.col = col;

            setHorizontalAlignment(JTextField.CENTER);
            setFont(new Font("Arial", Font.BOLD, 16));
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            setPreferredSize(new Dimension(50, 50));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e) || e.isControlDown()) {
                        cycleCellType();
                    }
                }
            });

            updateBackground();
        }

        private void cycleCellType() {
            switch (cellType) {
                case NORMAL -> cellType = CellType.RIVER;
                case RIVER -> cellType = CellType.DESERT;
                case DESERT -> cellType = CellType.NORMAL;
            }
            updateBackground();
        }

        private void updateBackground() {
            Color c;
            if (resultMark) {
                c = new Color(144, 238, 144); // light green for result
            } else {
                switch (cellType) {
                    case NORMAL -> c = Color.LIGHT_GRAY;
                    case RIVER -> c = new Color(64, 164, 223);
                    case DESERT -> c = new Color(255, 223, 99);
                    default -> c = Color.LIGHT_GRAY;
                }
            }
            setBackground(c);
            repaint();
        }

        public CellType getCellType() {
            return cellType;
        }

        public void setCellType(CellType type) {
            this.cellType = type;
            updateBackground();
        }

        public void setResultMark(boolean mark) {
            this.resultMark = mark;
            updateBackground();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

            super.paintComponent(g2);
            g2.dispose();
        }
    }
}
