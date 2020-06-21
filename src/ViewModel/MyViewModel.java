package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private Maze maze;
    private Solution solution;
    //msg
    public MyViewModel(IModel model)
    {
        this.model = model;
        this.model.assignObserver(this);
    }
    @Override
    public void update(Observable o, Object arg) {
            if(o instanceof IModel){
                switch ((String)arg){
                    case "MazeGenerate":
                        this.maze = model.getMaze();
                        setChanged();
                        notifyObservers("MazeGenerate");

                        break;
                    case "SolveGenerate":
                        this.solution = model.getSolution();
                        setChanged();
                        notifyObservers("SolveGenerate");
                        break;
                }
            }
    }

    public void generateMazeObserver(int row, int col) throws UnknownHostException {
        model.GeneratorClient(row,col);

    }
    public void generateSolutionObserver(Maze maze){
        model.SolverClient(maze);
        setChanged();
        notifyObservers("SolveGenerate");
    }

    public Solution getSolution(){
        return this.solution;
    }
    public Maze getMaze(){
        return this.maze;
    }

}
