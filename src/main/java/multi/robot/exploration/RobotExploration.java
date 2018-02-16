package multi.robot.exploration;

public class RobotExploration
{
    private int height, width, numRobots, k, unexplored;
    private Grid grid;
    private Robot robots[];

    RobotExploration(int height, int width, int k, int numRobots, 
                           Position initLocations[], Position obstacles[])
    {
        // initialize values
        this.height = height;
        this.width = width;
        this.numRobots = numRobots;
        this.k = k;

        // create grid for simulation
        grid = new Grid(height, width);
        unexplored = (height * width) - numRobots - obstacles.length;

        // set robots and obstacles
        // TODO: clean
        robots = new Robot[numRobots];
        for (int r = 0; r < numRobots; ++r)
        {
            robots[r] = new Robot(initLocations[r]);
        }

        int numObstacles = obstacles.length;
        for (int obs = 0; obs < numObstacles; ++obs)
        {
            grid.setCellState(obstacles[obs], State.OBSTACLE);
        }

        // initialize the grid
        init();
    }

    void init()
    {
        Position[] robotPos = new Position[numRobots];
        for (int i = 0; i < numRobots; ++i)
        {
            grid.setCellState(robots[i].getPosition(), State.OCCUPIED);
            robotPos[i] = robots[i].getPosition();
        }

        updateFrontier(robotPos);
        grid.printFrontier();
        System.out.println("frontier: " + grid.getFrontierSize());
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
        Position move;

        for (int i = 0; i < numRobots; ++i)
        {
            move = randomMovement(robots[i].getPosition());

            while (!validMovement(move, cfg_c, i))
            {
                move = randomMovement(robots[i].getPosition());
            }

            cfg_c[i] = move;
        }

        return cfg_c;
    }

    // generates a random movement for a swarmie (out of eight possible moves)
    Position randomMovement(Position currentPos)
    {
        return new Position(currentPos, height, width);
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
    boolean validMovement(Position move, Position cfg_c[], int cfgCount)
    {
        // prevents robot collision into same spot
        for (int i = 0; i < cfgCount; ++i)
        {
            if (cfg_c[i].equals(move))
            {
                    return false;
            }
        }

        // checks if position of random move is an obstacle or occupied
        State s = grid.getCellState(move);
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
            System.out.println("Move (" + config[i].x() + "," + config[i].y() + ")");
            grid.setCellState(robots[i].getPosition(), State.EXPLORED);

            // keeps track of unexplored cells
            if (grid.getCellState(config[i]) == State.FRONTIER)
            {
                --unexplored;
            }

            // sets robot position and updates cell state
            robots[i].setPosition(config[i]);
            grid.setCellState(config[i], State.OCCUPIED);
        }
    }

    void updateFrontier(Position[] robots)
    {
        for (int i = 0; i < robots.length; ++i)
        {
            grid.checkAndUpdateFrontier(robots[i]);
        }
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
       
        Position[] pos = generateCfg_c();
        executeCfg(pos);
        updateFrontier(pos);

        grid.printFrontier();
        System.out.println("Frontier: " + grid.getFrontierSize());
        System.out.println("Unexplored: " + unexplored);
        grid.printGrid();

        if (unexplored == 0)
        {
            System.out.println("Grid Explored!");
            return false;
        }

        return true;
    }
}
