package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.Observable;

import java.net.UnknownHostException;
import java.util.Observer;

public interface IModel{
    Maze createGeneratorClient(int row, int cols) throws UnknownHostException;
    Solution createSolverClient(Maze maze);
    boolean validMove(int row, int col, int[][] maze);
    void GeneratorClient(int row, int cols) throws UnknownHostException;
    void SolverClient(Maze maze);
    Maze getMaze();
    Solution getSolution();
    void assignObserver(Observer o);


}
