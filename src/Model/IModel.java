package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.Observable;

import java.net.UnknownHostException;

public interface IModel extends Observable {
    Maze generateMaze(int rows, int cols);
    Solution getSolution(Maze maze);
    Maze createGeneratorClient(int row, int cols) throws UnknownHostException;
    public Solution createSolverClient(Maze maze);

}
