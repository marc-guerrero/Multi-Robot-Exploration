package multi.robot.exploration;

import java.util.*;

public class AStar<T extends Object> {

    private static class Node {
        private static final double INIT_SCORE = Double.MAX_VALUE;

        public Object object;
        public List<Node> neighbors;
        public double score = INIT_SCORE;
        public double fScore = INIT_SCORE;

        public Node(Object object) {
            this.object = object;
            this.neighbors = new ArrayList<>();
        }

        public Node(List<Node> neighbors, Object object) {
            this.neighbors = neighbors;
            this.object = object;
        }

        public String toString() {
            return this.object + ": S=" + this.score + " FS=" + this.fScore;
        }
    }

    public static List<Node> reconstructPath(HashMap<Node, Node> cameFrom, Node current) {
        List<Node> path = new ArrayList<Node>();
        path.add(current);

        while(cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            path.add(current);
        }

        return path;
    }

    public List<Cell> pathFromGrid(Cell[][] grid, int[] start, int[] end) {
        Node[][] nodeGrid = new Node[grid.length][grid[0].length];

        for(int i=0; i < grid.length; i++) {
            for(int j=0; j<grid[0].length; j++) {
                nodeGrid[i][j] = new Node(grid[i][j]);
            }
        }

        Node current;
        Node startNode = nodeGrid[start[0]][start[1]];
        Node endNode = nodeGrid[end[0]][end[1]];

        startNode.score = 0;
        endNode.fScore = Math.abs(Math.sqrt(Math.pow((double)(end[0]-start[0]), 2.0) + Math.pow((double)(end[1]-start[1]), 2.0)));

        // Traversal map
        HashMap<Node, Node> cameFrom = new HashMap<>();

        //  Closed Nodes
        HashSet<Node> closedSet = new HashSet<>();

        //  Reachable, but unexplored
        HashSet<Node> openSet = new HashSet<>();

        //  Queue of f-Scores (Hueristic), should be 1-1 with openset
        PriorityQueue<Node> fScoreQueue = new PriorityQueue<>((o1, o2) -> o1.fScore < o2.fScore ? -1 : o1.fScore > o2.fScore ? 1 : 0);

        //  Starting node is reachable, but unexplored, also add to the fScoreQueue
        fScoreQueue.add(startNode);
        openSet.add(startNode);

        //  ArrayList of neighbors to fill with grid Neighbors
        ArrayList<Node> neighbors = new ArrayList<>();

        while(!openSet.isEmpty()) {
            //  Closest node when considering the hueristic that is reachable, but unexplored
            current = fScoreQueue.peek();

            //  End
            if(current == endNode) {
                List<Node> nodePath = reconstructPath(cameFrom, current);
                List<Cell> cellPath = new ArrayList<>();
                for(Node n : nodePath) {
                    cellPath.add((Cell) n.object);
                }

                return cellPath;
            }

            //  This node is being explored
            fScoreQueue.poll();
            openSet.remove(current);
            closedSet.add(current);

            //  Fill the neighbors array
            getNeighborsFromGrid(current, nodeGrid, neighbors);

            for(Node neighbor : neighbors) {
                //  This node is already explored
                if(closedSet.contains(neighbor)) continue;

                //  +1 Used for the cost of traversal
                double tentScore = current.score + 1;

                //  Newly discovered neighbor
                if(!openSet.contains(neighbor)) {
                    openSet.add(neighbor);
                    fScoreQueue.add(neighbor);
                } else if(tentScore >= current.score) continue;

                //  Track the traversal
                cameFrom.put(neighbor, current);

                //  Update the fScore
                Cell cellNeighbor = (Cell) neighbor.object;
                neighbor.score = tentScore;
                neighbor.fScore = neighbor.score + Math.sqrt(Math.pow((double)(end[1]-cellNeighbor.y()), 2.0) + Math.pow((double)(end[0]-cellNeighbor.x()), 2.0));

                fScoreQueue.remove(neighbor);
                fScoreQueue.add(neighbor);
            }
        }

        return null;
    }

    /**
     *  Fill dest with neighbor nodes from the grid
     *
     * @param current
     * @param nodeGrid
     * @param dest
     */
    private static void getNeighborsFromGrid(Node current, Node[][] nodeGrid, ArrayList<Node> dest) {
        dest.clear();

        int ic = ((Cell)current.object).x();
        int jc = ((Cell)current.object).y();

        int is = Math.max(0, ic-1);
        int js = Math.max(0, jc-1);

        int ie = Math.min(nodeGrid.length-1, ((Cell)current.object).x()+1);
        int je = Math.min(nodeGrid[0].length-1, ((Cell)current.object).y()+1);

        for(int i=is; i<=ie; i++) {
            for(int j=js; j<=je; j++) {
                Node n = nodeGrid[i][j];
                if(n == current) continue;

                //  Diagonal, check adjacent diagnals too
                if(i != ic && j != jc) {
                    int cr1 = i > ic ? ic+1 : ic-1;
                    int cc1 = jc;

                    int cr2 = ic;
                    int cc2 = j > jc ? jc+1 : jc-1;

                    boolean cb1 = true;
                    boolean cb2 = true;

                    if(cr1 >= 0 && cr1 < nodeGrid.length && cc1 >=0 && cc1 < nodeGrid[0].length) {
                        Cell c = (Cell) nodeGrid[cr1][cc1].object;
                        if(c.isBlocked() /*|| c.isOccupied()*/) {
                            cb1 = false;
                        }
                    }

                    if(cr2 >= 0 && cr2 < nodeGrid.length && cc2 >=0 && cc2 < nodeGrid[0].length) {
                        Cell c = (Cell) nodeGrid[cr2][cc2].object;
                        if(c.isBlocked() /*|| c.isOccupied()*/) {
                            cb2 = false;
                        }
                    }

                    if(cb1 && cb2) {
                        Cell c = (Cell) n.object;
                        if(!c.isBlocked() /*&& !c.isOccupied()*/) {
                            dest.add(n);
                        }
                    }
                } else {
                    Cell c = (Cell) n.object;
                    if(!c.isBlocked() /*&& !c.isOccupied()*/) {
                        dest.add(n);
                    }
                }

            }
        }
    }
}
