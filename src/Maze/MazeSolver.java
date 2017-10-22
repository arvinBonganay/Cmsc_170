package Maze;
import java.io.*;
import java.util.*;

public class MazeSolver {
    // edit later
    
    public static void main(String[] args) {
        String heuristic = "manhattan";
        Maze x = new Maze("others/openMaze.lay.txt");
        solve(x, heuristic);
        x.display();
    }
    
    public static void solveMaze(Maze maze, String hueristics){
        List<Square> closedList = new ArrayList<>();
        List<Square> openList = new ArrayList<>();
        Square current = maze.start;
        current.g = 0;
        current.h = hCost(hueristics, maze, current);
        current.f = current.g + current.h;
        openList.add(current);
        while(!closedList.contains(maze.goal) && !openList.isEmpty()){
            current = minCost(openList); 
            closedList.add(current);
            openList.remove(current);
            List<Square> neighbor = maze.neighbor(current);
            for (Square x: neighbor){
                if (!x.val.equals("%") && !closedList.contains(x)){
                    if (openList.contains(x)){
                        int tempG = current.g + 1;
                        if (x.g > tempG){
                            x.g = tempG;
                            x.parent = current;
                            x.f = (x.g + x.h);
                        }
                    } else {
                        x.g = current.g + 1;
                        x.parent = current;
                        x.h = hCost(hueristics, maze, x);
                        x.f = (x.g + x.h);
                        openList.add(x);
                    }
                }
            }
        }
        if (closedList.contains(maze.goal)){
            System.out.println("Successfuly found the target square");
            System.out.println(closedList.size());
            trace(maze.goal);
        } else {
            System.out.println("Failed to find the target square at " + maze.goal.row + "," + maze.goal.col);
        }
    }
    
    public static void solve(Maze m, String heuristic){
        int i = 0;
        while(m.goal != null){
            System.out.println(i);
            solveMaze(m, heuristic);
            m.start = m.goal;
            m.start.parent = null;
            m.nextGoal();
        }
    }
    
    public static void trace(Square goal){
        Square current = goal;
        while(current.parent != null){
            current.val = "*";
            current = current.parent;
        }
    }
    
    public static Square minCost(List<Square> l){
        Square min = l.get(0);
        for (Square sq: l){
            if (sq.f < min.f){
                min = sq;
            }
        }
        return min;
    }
    
    public static int hCost(String hueristics, Maze m, Square sq){
        int x1 = sq.row;
        int y1 = sq.col;
        int x2 = m.goal.row;
        int y2 = m.goal.col;
        
        if (hueristics.equals("manhattan")){
            return manhattanD(x1, y1, x2, y2);
        } else if (hueristics.equals("straightLine")){ 
            return straightLineD(x1, y1, x2, y2);
        } else {
            System.err.println("Incorrect heuristic");
            return 0;
        }
    }
    
    public static int manhattanD(int x1, int x2, int y1, int y2){
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
    
    public static int straightLineD(int x1, int x2, int y1, int y2){
        return Math.max(Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
}
