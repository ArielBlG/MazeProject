package Model;

import Client.Client;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.ASearchingAlgorithm;
import algorithms.search.Solution;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {

    private MyMazeGenerator myMazeGenerator;
    private ASearchingAlgorithm solver;
    private Maze maze = null;
    private Solution solution = null;

    @Override
    public Maze createGeneratorClient(int row, int cols) throws UnknownHostException {
        GenerateMazeClient generateMazeClient = new GenerateMazeClient(row,cols);
        Client client = new Client(InetAddress.getLocalHost(), 5400,generateMazeClient);
        client.communicateWithServer();
        this.maze = generateMazeClient.getMaze();
        return this.maze;
    }

    @Override
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

    @Override
    public boolean validMove(int row, int col, int[][] maze){
        return   row < maze.length && row >= 0 && col < maze[0].length && col >= 0 && maze[row][col] != 1;
    }

    @Override
    public void GeneratorClient(int row, int cols) throws UnknownHostException {
        GenerateMazeClient generateMazeClient = new GenerateMazeClient(row,cols);
        Client client = new Client(InetAddress.getLocalHost(), 5400,generateMazeClient);
        client.communicateWithServer();
        this.maze = generateMazeClient.getMaze();

        setChanged();
        notifyObservers("MazeGenerate");
    }

    @Override
    public void SolverClient(Maze maze) {
        GenerateSolutionClient generateSolutionClient = new GenerateSolutionClient(this.maze);
        Client client = null;
        try {
            client = new Client(InetAddress.getLocalHost(),5401, generateSolutionClient);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        client.communicateWithServer();
        this.solution =  generateSolutionClient.getMazeSolution();

        setChanged();
        notifyObservers("SolveGenerate");
    }

    @Override
    public Maze getMaze() {
        return this.maze;
    }

    @Override
    public Solution getSolution() {
        return this.solution;
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }
}
