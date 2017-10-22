package Maze;
import java.io.*;
import java.util.*;

public class MazeSolver {

    public static void main(String[] args) {
        String a = "straightLine";
        String b = "manhattan";
        String heuristic = a;
        Maze x = new Maze("others/mediumMaze.lay.txt");
        solve(x, heuristic);
        x.display();
    }
    
    public static int solveMaze(Maze maze, String hueristics){
        List<Square> closedList = new ArrayList<>();
        List<Square> openList = new ArrayList<>();
        Square current = maze.start;
        current.g = 0;
        current.h = maze.hCost(hueristics, current);
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
                        x.h = maze.hCost(hueristics, x);
                        x.f = (x.g + x.h);
                        openList.add(x);
                    }
                }
            }
        }
        if (closedList.contains(maze.goal)){
            System.out.println("Successfuly found the target square at " + maze.goal.row + "," + maze.goal.col);
            trace(maze, maze.goal);
            return closedList.size();
        } else {
            System.out.println("Failed to find the target square at " + maze.goal.row + "," + maze.goal.col);
            return 0;
        }
    }
    
    public static void solve(Maze m, String heuristic){
        int nodesExpanded = 0;
        m.nextGoal(heuristic);
        while(m.goal != null){
            nodesExpanded += solveMaze(m, heuristic);
            m.nextGoal(heuristic);
        }
        System.out.println(nodesExpanded + " nodes expanded");
    }
    
    public static void trace(Maze m, Square goal){
        Square current = goal.parent;
        while(current.parent != null){
            if (current.val.equals(".")) {
                m.removeGoal(current);
                System.out.println("here!!!");
            }
            current.val = ".";
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
    
}
