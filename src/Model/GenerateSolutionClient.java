package Model;

import Client.IClientStrategy;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.search.AState;
import algorithms.search.Solution;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class GenerateSolutionClient implements IClientStrategy {

    private Maze maze;
    private Solution mazeSolution;
    private ArrayList<AState> mazeSolutionSteps;

    public GenerateSolutionClient(Maze maze) {
        this.maze = maze;
    }


    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
        try {
            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
            toServer.flush();
            toServer.writeObject(this.maze);
            toServer.flush();
            this.mazeSolution = (Solution)fromServer.readObject();

        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }

    public Solution getMazeSolution() {
        return mazeSolution;
    }

}

