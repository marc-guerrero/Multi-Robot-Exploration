package multi.robot.exploration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension; import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point; import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class Grid
{
    private int height;
    private int width;
    private Cell cell[][];

    private char cellVal[] = {'X', '-', '+', '%', 'O'};
    private Color cellColor[] = {Color.BLACK, Color.WHITE, Color.blue, Color.CYAN, Color.GREEN};

    private HashMap<Position, Position> frontier = new HashMap<>();
    private JFrame frame;

    public Grid(int height, int width)
    {
        // store width and height of grid
        this.width = width;
        this.height = height;

        // create grid of cells
        cell = new Cell[height][width];
        for (int x = 0; x < height; ++x)
        {
            for (int y = 0; y < width; ++y)
            {
                cell[x][y] = new Cell(x, y);                
            }
        }

        // Create Frame
        frame = new JFrame("Multi Robot Exploration");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(new GridPane(cell, cellColor));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setCellState(Position pos, State state)
    {
        cell[pos.x()][pos.y()].setState(state);
    }

    public State getCellState(Position pos)
    {
        return cell[pos.x()][pos.y()].getState();
    }

    public Cell getCell(Position pos)
    {
        return cell[pos.x()][pos.y()];
    }

    public void printGrid()
    {
        /*for (int x = 0; x < height; ++x)
        {
            for (int y = 0; y < width; ++y)
            {
                System.out.print(cellVal[cell[x][y].getState().ordinal()] + " ");
            }
            System.out.println();
        }
        System.out.println();*/

        repaint();
    }

    public void repaint()
    {
        try{
            TimeUnit.MILLISECONDS.sleep(500);
        }
        catch(InterruptedException ex){}

        frame.repaint();
    }

    public void checkAndUpdateFrontier(Position pos)
    {
        int x = pos.x();
        int y = pos.y();

        // TODO: Cleanup
        // Makes sure surrounding grid cells are not out of bounds
        int up = Math.max(x-1, 0);
        int down = Math.min(x+1, height-1);
        int left = Math.max(y-1, 0);
        int right = Math.min(y+1, width-1);

        updateFrontier(x,y);
        updateFrontier(up,y);
        updateFrontier(down,y);
        updateFrontier(x,left);
        updateFrontier(x,right);
        updateFrontier(up,left);
        updateFrontier(up,right);
        updateFrontier(down,left);
        updateFrontier(down,right);
    }

    private void updateFrontier(int a, int b)
    {
        Position pos = new Position(a,b);
        if (cell[a][b].setFrontier())
        {
            if (frontier.containsKey(pos))
            {
                return;
            }

            //System.out.println("adding frontier " + a + " " + b);
            frontier.put(pos,pos);
        }
        else
        {
            frontier.remove(pos);
        }
    }

    public int getClosestFrontier(Position pos)
    {
        int closest = Integer.MAX_VALUE;

        for (Position value : frontier.values())
        {
            int distance = distance(pos, value);

            if (distance < closest)
            {
                closest = distance;
            }
        }

        return closest;
    }

    public int distance(Position pos, Position front)
    {
        AStar<Grid> algo = new AStar<>();

        int start[] = {pos.x(), pos.y()};
        int end[] = {front.x(), front.y()};

        List<Cell> path = algo.pathFromGrid(this.cell, start, end);

        if (path == null)
        {
            System.out.println("null");
            System.out.println("POS and FRONT: " + "(" + pos.x() + "," + pos.y() + ")(" + front.x() + "," + front.y() + ")" + "FAILED ");

            return -3;
        }

        /*double posX = pos.x();
        double posY = pos.y();
        double frontX = front.x();
        double frontY = front.y();

        int distance = (int)Math.sqrt(Math.pow(posX - frontX, 2) + Math.pow(posY - frontY, 2));*/

        /*if (path.size() - 1 !=  distance)
        {
            System.out.println("POS and FRONT: " + "(" + pos.x() + "," + pos.y() + ")(" + front.x() + "," + front.y() + ")" + "FAILED " + path.size() + " " + distance);

            System.out.println("Path:");

            for (Cell p : path)
            {
                System.out.println("(" + p.x() + "," + p.y() + ")");
            }

            System.exit(0);
        }*/ 

        return path.size();
    }

    public int getFrontierSize()
    {
        return frontier.size();
    }

    public void printFrontier()
    {
        //System.out.println("Printing Frontier Cells");
        Set set = frontier.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext())
        {
            Map.Entry mentry = (Map.Entry)iterator.next();
            //System.out.println("(" + ((Position)mentry.getValue()).x() + "," + ((Position)mentry.getValue()).y() + ")");

        }
    }

    private class GridPane extends JPanel {
        private Cell cell[][];
        private Color color[];
        int columnCount, rowCount;
        private Rectangle cells[][];
        boolean refresh = true;

        public GridPane(Cell cell[][], Color color[]) {
            this.cell = cell;
            this.color = color;
            rowCount = cell.length;
            columnCount = cell[0].length;
            cells = new Rectangle[rowCount][columnCount];
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(500, 500);
        }

        @Override
        public void invalidate() {
            refresh = true;
            super.invalidate();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            int width = getWidth();
            int height = getHeight();

            int cellWidth = width / columnCount;
            int cellHeight = height / rowCount;

            int xOffset = (width - (columnCount * cellWidth)) / 2;
            int yOffset = (height - (rowCount * cellHeight)) / 2;

            if (refresh) {
                for (int row = 0; row < rowCount; row++) {
                    for (int col = 0; col < columnCount; col++) {
                        Rectangle rect = new Rectangle(
                                xOffset + (col * cellWidth),
                                yOffset + (row * cellHeight),
                                cellWidth,
                                cellHeight);
                        cells[row][col] = rect;
                    }
                }
                refresh = false;
            }

            for (int i = 0; i < rowCount; ++i)
            {
                for (int j = 0; j < columnCount; ++j)
                {
                    g2d.setColor(color[cell[i][j].getState().ordinal()]);
                    g2d.fill(cells[i][j]);
                    g2d.setColor(Color.GRAY);
                    g2d.draw(cells[i][j]);

                }
            }

            g2d.dispose();
        }
    }
}
