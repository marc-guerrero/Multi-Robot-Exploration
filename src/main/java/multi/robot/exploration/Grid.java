package multi.robot.exploration;

public class Grid
{
    private int height;
    private int width;
    private Cell cell[][];
    private char cellVal[] = {'X', '-', '+', '%', 'O'};

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
    }

    void setCellState(Position pos, State state)
    {
        cell[pos.x()][pos.y()].setState(state);
    }

    State getCellState(Position pos)
    {
        return cell[pos.x()][pos.y()].getState();
    }

    void printGrid()
    {
        for (int x = 0; x < height; ++x)
        {
            for (int y = 0; y < width; ++y)
            {
                System.out.print(cellVal[cell[x][y].getState().ordinal()] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    void checkAndSetFrontier(Position pos)
    {
        int x = pos.x();
        int y = pos.y();

        // TODO: Cleanup
        if(cell[x][y].getState() == State.UNEXPLORED || cell[x][y].getState() == State.FRONTIER)
        {
            // Makes sure surrounding grid cells are not out of bounds
            int up = Math.max(x-1, 0);
            int down = Math.min(x+1, height-1);
            int left = Math.max(y-1, 0);
            int right = Math.min(y+1, width-1);

            System.out.println("up, down, left, right: " + up + " " + down + " " + left + " " + right);
            cell[up][y].setFrontier();        
            cell[down][y].setFrontier();        
            cell[x][left].setFrontier();        
            cell[x][right].setFrontier();        
            cell[up][left].setFrontier();
            cell[up][right].setFrontier();
            cell[down][left].setFrontier();
            cell[down][right].setFrontier();
        }
    }
}
