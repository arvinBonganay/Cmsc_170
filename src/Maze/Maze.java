package Maze;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Maze {
    List<List <Square>> maze;
    int colSize;
    int rowSize;    
    Square start;
    Square goal;
    List<Square> goals;
    int goalCounter;

    Maze(String filename){
        List<List <Square>> row = new ArrayList<>();
        goals = new ArrayList<>();
        try (FileInputStream in = new FileInputStream(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));) {
            String line;
            int r = 0;
            while ((line = reader.readLine()) != null){
                List<Square> data = new ArrayList();
                for (int i = 0; i < line.length(); i++){
                    String val = line.charAt(i) + "";
                    Square s = new Square(val, r, i);
                    data.add(s);
                    if (val.equals("P")){
                        start = s;
                    } else if (val.equals(".")) {
                        goals.add(s);
                    }
                }
                row.add(data);
                r++;
            }
            in.close();
            reader.close();
        } catch (IOException e){
            System.out.print("File does not exist");
        } catch (Exception e){
            e.printStackTrace();
        }
        maze = row;
        rowSize = maze.size();
        colSize = maze.get(0).size();
        goal = goals.get(0);
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
    
    List<Square> neighbor(Square sq){
        int row = sq.row;
        int col = sq.col;
        int mRow = maze.size();
        int mCol = maze.get(0).size();
        List<Square> l = new ArrayList<>();
        if (col - 1 >= 0){
            l.add(maze.get(row).get(col - 1));
        }
        if (row + 1 <mRow){
            l.add(maze.get(row + 1).get(col));
        }
        if (col + 1 < mCol){
            l.add(maze.get(row).get(col + 1));
        }
        if (row - 1 >= 0){
            l.add(maze.get(row - 1).get(col));
        }
        return l;
    }
    
    void nextGoal(){
        goalCounter++;
        if (goalCounter < goals.size()){
            goal = goals.get(goalCounter);
        } else {
            goal = null;
        }
    }
}
