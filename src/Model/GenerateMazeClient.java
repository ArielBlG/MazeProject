package Model;

import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;

import java.io.*;

public class GenerateMazeClient implements IClientStrategy {
    private Maze maze;
    private int row;
    private int col;
    private String avatar;
    public GenerateMazeClient(int Row, int Col) {
        this.row = Row;
        this.col = Col;
    }

    public Maze getMaze() {
        return maze;
    }


    public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
        try {
            ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
            ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
            toServer.flush();
            int[] mazeDimensions = new int[]{this.row, this.col};
            toServer.writeObject(mazeDimensions);
            toServer.flush();
            byte[] compressedMaze = (byte[])((byte[])fromServer.readObject());
            InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
            byte[] decompressedMaze = new byte[mazeDimensions[0] * mazeDimensions[1] + 12];
            is.read(decompressedMaze);
            this.maze = new Maze(decompressedMaze);
            //maze.print();
        } catch (Exception var10) {
            var10.printStackTrace();
        }

    }
}

