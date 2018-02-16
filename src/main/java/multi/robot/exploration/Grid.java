package multi.robot.exploration;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

public class Grid
{
    private int height;
    private int width;
    private Cell cell[][];
    private char cellVal[] = {'X', '-', '+', '%', 'O'};
    private HashMap<Position, Position> frontier = new HashMap<>();

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


    void checkAndUpdateFrontier(Position pos)
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

            System.out.println("adding frontier " + a + " " + b);
            frontier.put(pos,pos);
        }
        else
        {
            frontier.remove(pos);
        }
    }

    Position getClosestFrontier(Position pos)
    {
        return null;
    }

    int getFrontierSize()
    {
        return frontier.size();
    }

    void printFrontier()
    {
        System.out.println("Printing Frontier Cells");
        Set set = frontier.entrySet();
        Iterator iterator = set.iterator();
        while (iterator.hasNext())
        {
            Map.Entry mentry = (Map.Entry)iterator.next();
            System.out.println("(" + ((Position)mentry.getValue()).x() + "," + ((Position)mentry.getValue()).y() + ")");

        }
    }

}
