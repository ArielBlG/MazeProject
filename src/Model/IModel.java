package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.Observable;

import java.net.UnknownHostException;

public interface IModel extends Observable {
    Maze createGeneratorClient(int row, int cols) throws UnknownHostException;
    Solution createSolverClient(Maze maze);
    boolean validMove(int row, int col, int[][] maze);

}
