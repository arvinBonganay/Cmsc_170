package Maze;

import java.io.*;
import java.util.*;

public class mazeSearch {
    public static class Square{
        char val;
        int f, g, h, row, col;
        Square parent;
        Square(char val, int row, int col){
            this.val = val;
            this.row = row;
            this.col = col;
        }
    }
    
    public static class Maze{
        List<List <Square>> maze;
        int colSize;
        int rowSize;    
        Square start;
        Square goal;
        Maze(List<List <Square>> maze){
            this.maze = maze;
            rowSize = maze.size();
            colSize = maze.get(0).size();
            for (List<Square> x: maze){
                for (int i = 0; i < x.size(); i++){
                    if (x.get(i).val == 'P'){
                        start = x.get(i);
                        start.parent = null;
                    } else if (x.get(i).val == '.')
                        goal = x.get(i);
                }
            }
        }
        void display(){
            for (List<Square> x: maze){
                for (int i = 0; i < x.size(); i++){
                    System.out.print(x.get(i).val + " ");
                }
                System.out.println();
            }
        }
        Square get(int row, int col){
            return maze.get(row).get(col);
        }
    }
    
    public static void main(String[] args) {
        Maze x = read("others/bigMaze.lay.txt");
        solveMaze(x, "straightLine");
        x.display();
    }
    
    public static Maze read(String filename){
        List<List <Square>> row = new ArrayList<>();
        try (FileInputStream in = new FileInputStream(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
            String line;
            int r = 0;
            while ((line = reader.readLine()) != null){
                List<Square> data = new ArrayList();
                for (int i = 0; i < line.length(); i++){
                    data.add(new Square(line.charAt(i), r, i));
                }
                row.add(data);
                r++;
            }
            in.close();
            reader.close();
        } catch (Exception e){
            System.out.print("File does not exist");
        }
        return new Maze(row);
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
            current = minCost(openList); // can also use the list sort for lesser time complexity
            closedList.add(current);
            openList.remove(current);
            List<Square> neighbor = neighbor(current, maze);
            for (Square x: neighbor){
                if (x.val != '%' && !closedList.contains(x)){
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
            System.out.println(maze.goal.row + " " + maze.goal.col);
        } else {
            System.out.println("Failed to find the target square");
        }
    }
    
    public static void trace(Square goal){
        Square current = goal;
        while(current.parent != null){
            current.val = '.';
            current = current.parent;
        }
    }
    
    
    public static List<Square> neighbor(Square sq, Maze m){
        int row = sq.row;
        int col = sq.col;
        int mRow = m.rowSize;
        int mCol = m.colSize;
        List<Square> l = new ArrayList<>();
        if (col - 1 >= 0){
            l.add(m.get(row, col - 1));
        }
        if (row + 1 <mRow){
            l.add(m.get(row + 1, col));
        }
        if (col + 1 < mCol){
            l.add(m.get(row, col + 1));
        }
        if (row - 1 >= 0){
            l.add(m.get(row - 1, col));
        }
        return l;
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
            System.err.println("Incorrect heuristics");
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
