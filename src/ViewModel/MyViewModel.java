package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    //msg
    public MyViewModel(IModel model)
    {
        this.model = model;
    }
    @Override
    public void update(Observable o, Object arg) {

    }

    public Maze generateMaze(int row, int col)  {
        try {
            return model.createGeneratorClient(row, col);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Solution getSolution(Maze maze)
    {
        return model.createSolverClient(maze);
    }

    Solution createSolverClient(Maze maze){
        return this.model.createSolverClient(maze);
    }
    boolean validMove(int row, int col, int[][] maze){
        return this.model.validMove(row,col,maze);
    }
}
