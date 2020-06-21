package Model;

import Client.Client;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.ASearchingAlgorithm;
import algorithms.search.BestFirstSearch;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;
import javafx.beans.InvalidationListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MyModel implements IModel {

    private MyMazeGenerator myMazeGenerator;
    private ASearchingAlgorithm solver;
    private Maze maze = null;
    @Override
    public void addListener(InvalidationListener listener) {

    }

    @Override
    public void removeListener(InvalidationListener listener) {

    }

    @Override
    public Maze generateMaze(int rows, int cols) {
        if( this.myMazeGenerator == null)
        {
            this.myMazeGenerator = new MyMazeGenerator();
        }
        return myMazeGenerator.generate(rows,cols);
    }

    @Override
    public Solution getSolution(Maze maze) {
        if(this.solver == null)
            this.solver = new BestFirstSearch();
        return this.solver.solve(new SearchableMaze(maze));
    }

    public Maze createGeneratorClient(int row, int cols) throws UnknownHostException {
        GenerateMazeClient generateMazeClient = new GenerateMazeClient(row,cols);
        Client client = new Client(InetAddress.getLocalHost(), 5400,generateMazeClient);
        client.communicateWithServer();
        this.maze = generateMazeClient.getMaze();
        return this.maze;
    }

    public Solution createSolverClient(Maze maze){
        GenerateSolutionClient generateSolutionClient = new GenerateSolutionClient(this.maze);
        Client client = null;
        try {
            client = new Client(InetAddress.getLocalHost(),5401, generateSolutionClient);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        client.communicateWithServer();
        return generateSolutionClient.getMazeSolution();
    }
}
