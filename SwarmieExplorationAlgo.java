import java.util.Random;

public class SwarmieExplorationAlgo
{
    private int height, width, numRobots, k, unexplored;
    private Grid grid;
    private Robot robots[];
    private Position frontier[];

    SwarmieExplorationAlgo(int height, int width, int k, int numRobots, 
                           Position initLocations[], Position obstacles[])
    {
        // initialize values
        this.height = height;
        this.width = width;
        this.numRobots = numRobots;
        this.k = k;

        // create grid for simulation
        grid = new Grid(height, width);
        unexplored = (height * width) - numRobots;

        // set robots and obstacles
        // TODO: clean
        robots = new Robot[numRobots];
        for (int r = 0; r < numRobots; ++r)
        {
            robots[r] = new Robot(initLocations[r].x(), initLocations[r].y());
        }

        int numObstacles = obstacles.length;
        for (int obs = 0; obs < numObstacles; ++obs)
        {
            grid.setCellState(obstacles[obs].x(), obstacles[obs].y(), State.OBSTACLE);
        }

        // initialize the grid
        init();
    }

    void init()
    {
        for (int i = 0; i < numRobots; ++i)
        {
            // update frontier before movement
            updateFrontier(robots[i].x(), robots[i].y());
            grid.setCellState(robots[i].x(), robots[i].y(), State.OCCUPIED);
        }

        grid.printGrid();
    }

    // generate a population of configuration changes.
    Position[] generatePopulation()
    {
        Position cfg[] = new Position[numRobots];
        cfg = generateCfg_c();
        int maxUtility = calcUtility(cfg);

        // interate through all configuration changes, choosing the config with
        // the highest utility
        int nextUtility;
        Position nextCfg[] = new Position[numRobots];
        for (int i = 1; i < k; ++i)
        {
            nextCfg = generateCfg_c();
            nextUtility = calcUtility(nextCfg);

            if (nextUtility > maxUtility)
            {
                cfg = nextCfg;
                maxUtility = nextUtility;
            }
        }

        return cfg;
    }

    // generates a configuration change for a group swarmies
    Position[] generateCfg_c()
    {
        Position cfg_c[] = new Position[numRobots];
        for (int i = 0; i < numRobots; ++i)
        {
            cfg_c[i] = randomMovement(robots[i].x(), robots[i].y());
        }

        return cfg_c;
    }

    // generates a random movement for a swarmie (out of eight possible moves)
    Position randomMovement(int x, int y)
    {
        Position move = new Position(x, y, width, height);

        while (!validMovement(move))
        {
            move = new Position(x, y, width, height);
        }

        return move;
    }

    // selects the configuration change with the best utility rating
    int calcUtility(Position[] moves)
    {
        /* Utility:
         *          -3 if loss of comm (TODO: implement com)
         *          -md otherwise
         */

        return 0;
    }

    // used to calculate utility
    boolean validMovement(Position m)
    {
        State s = grid.getCellState(m.x(), m.y());
        if (s != State.OBSTACLE && s != State.OCCUPIED)
        {
            return true;
        }

        return false;
    }

    // updates the swarmies configuration (i.e. move the swarmies to their next location)
    void executeCfg(Position[] config)
    {
        int x, y;
        for (int i = 0; i < numRobots; ++i)
        {
            x = robots[i].x();
            y = robots[i].y();

            grid.setCellState(x, y, State.EXPLORED);

            updateFrontier(x, y);

            robots[i].setPosition(config[i].x(), config[i].y());
        }
    }

    // updates frontier after a configuration change
    void updateFrontier(int x, int y)
    {
        grid.checkAndSetFrontier(x, y);
    }

    boolean runInteration()
    {
        /* check if we have explored entire area 
        if (unexplored < 1)
        {
            return true;
        }

        return false;*/

        // 1. generate configuation change population

        // 2. select the best configuration change

        // 3. update frontier

        // 4. move robot positions

        // 5. re-draw grid

        // Testing random movement
        grid.setCellState(robots[2].x(), robots[2].y(), State.EXPLORED);
        Position move = randomMovement(robots[2].x(), robots[2].y());
        robots[2].setPosition(move.x(), move.y());
        updateFrontier(move.x(), move.y());
        grid.setCellState(move.x(), move.y(), State.OCCUPIED);

        grid.printGrid();

        return false;
    }

    public static void main(String[] args)
    {
        // TODO: Handle file input.
        int t = 50;
        Position initLoc[] = {new Position(0,9), new Position(1,9), new Position(2,9)};
        Position obstacles[] = {new Position(0,0), new Position(1,0), new Position(2,2),
                                new Position(3,3), new Position(4,4), new Position(4,4),
                                new Position(5,5), new Position(6,6), new Position(7,7),
                                new Position(8,8), new Position(9,9)};

        SwarmieExplorationAlgo algo = new SwarmieExplorationAlgo(10, 10, 10, 3, initLoc, obstacles);

        // run algorithm for t (time) steps
        for (int i = 0; i < t; ++i)
        {
            if (algo.runInteration())
            {
                return;
            }
        }
    }
}
