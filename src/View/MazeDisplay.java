package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Random;

public class MazeDisplay extends Canvas {
    private Maze maze;
    private int row_player = 0;
    private int col_player = 0;
    private boolean started_game = false;
    StringProperty sound_wall = new SimpleStringProperty();
    StringProperty sound_walking = new SimpleStringProperty();
    StringProperty wallImagesFolder = new SimpleStringProperty();
    StringProperty targetImg = new SimpleStringProperty();
    StringProperty startImg = new SimpleStringProperty();



    private Image[] wallImages;
    private int[][] wallImagesAssignment;
    private Image avatar;
    private Image target;
    private Image start;

    public int getRow_player() {
        return row_player;
    }
    public int getCol_player() {
        return col_player;
    }

    public String getWallImagesFolder() {
        return wallImagesFolder.get();
    }

    public String getTargetImg(){return targetImg.get();}

    public void setTargetImg(String target){this.targetImg.set(target);};

    public String getStartImg(){return startImg.get();}

    public void setStartImg(String start){this.startImg.set(start);};


    public void setPlayerPosition(int row,int col){
        AudioClip player;
        if((validMove(row,col,this.maze.getM_maze())) && (checkDiagonal(row,col))) {
            this.row_player = row;
            this.col_player = col;
            player = new AudioClip(Paths.get("src/Resources/Sounds/move_sound.wav").toUri().toString());
            player.play();
            Draw(false);
        }
        else
        {
            player = new AudioClip(Paths.get("src/Resources/Sounds/wall_sound.wav").toUri().toString());
            player.play();
        }

    }

    public void drawMaze(Maze maze, boolean flag){
        this.maze = maze;
        if(!this.started_game){
            this.started_game = true;

            Random rnd = new Random();

            this.row_player = this.maze.getStartPosition().getRowIndex();
            this.col_player = this.maze.getStartPosition().getColumnIndex();

            this.wallImages = new Image[new File(this.getWallImagesFolder()).list().length];
            this.wallImagesAssignment = new int[this.maze.getRowSize()][this.maze.getColSize()];
            try {
                this.target = new Image(new FileInputStream(this.getTargetImg()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            try {
                this.start = new Image(new FileInputStream(this.getStartImg()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            for(int i = 0; i < wallImages.length;i++)
            {
                try {
                    this.wallImages[i] = new Image(new FileInputStream(this.getWallImagesFolder()+"/"+(i+1)+".png"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            for(int i = 0; i < this.wallImagesAssignment.length; i++)
            {
                for(int j = 0 ; j < this.wallImagesAssignment[i].length; j++)
                {
                    int img_index = rnd.nextInt(2);
                    this.wallImagesAssignment[i][j] = img_index;
                }
            }
        }
        Draw(flag);
    }
    public void Draw(boolean flag){
        if(maze != null){
            int[][] maze_arr = maze.getM_maze();
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            int row_start = this.maze.getStartPosition().getRowIndex();
            int col_start = this.maze.getStartPosition().getColumnIndex();
            int row_end = this.maze.getGoalPosition().getRowIndex();
            int col_end = this.maze.getGoalPosition().getColumnIndex();
            int row = maze_arr.length;
            int col = maze_arr[0].length;
            double cellHeight = canvasHeight/row;
            double cellWidth = canvasWidth/col;
            GraphicsContext graphicsContext = this.getGraphicsContext2D();
            graphicsContext.clearRect(0,0,canvasWidth,canvasHeight);


            double w,h;
            for(int i=0;i<row;i++)
            {
                for(int j=0;j<col;j++)
                {
                    if(i == row_start && j == col_start){
                        h = i * cellHeight;
                        w = j * cellWidth;;
                        graphicsContext.drawImage(this.start,w,h,cellWidth,cellHeight);
                        continue;
                    }
                    if(i == row_end && j == col_end){
                        h = i * cellHeight;
                        w = j * cellWidth;
                        graphicsContext.drawImage(this.target,w,h,cellWidth,cellHeight);
                        continue;
                    }
                    if(maze_arr[i][j] == 1) // Wall
                    {
                        h = i * cellHeight;
                        w = j * cellWidth;
                        graphicsContext.drawImage(this.wallImages[this.wallImagesAssignment[i][j]],w,h,cellWidth,cellHeight);
                    }
                    else {
                        if (!(!flag || maze_arr[i][j] == 0)) {
                            h = i * cellHeight;
                            w = j * cellWidth;
                            graphicsContext.setFill(Color.TEAL);
                            graphicsContext.fillRect(w, h, cellWidth, cellHeight);
                        }
                    }
                }
            }
            if(validMove(getRow_player(),getCol_player(),maze_arr)) {
                if(getRow_player() == this.maze.getGoalPosition().getRowIndex() && getCol_player() == this.maze.getGoalPosition().getColumnIndex()){
                    double h_player = getRow_player() * cellHeight;
                    double w_player = getCol_player() * cellWidth;

                    graphicsContext.setFill(Color.GOLD);
                    graphicsContext.fillRect(w_player, h_player, cellWidth, cellHeight);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Winning screen");
                    alert.setHeaderText(null);
                    alert.setContentText("You have solved the maze!");

                    alert.showAndWait();
                }
                else {
                    double h_player = getRow_player() * cellHeight;
                    double w_player = getCol_player() * cellWidth;


//                    graphicsContext.setFill(Color.BLACK);
//                    graphicsContext.fillRect(w_player, h_player, cellWidth, cellHeight);
                    graphicsContext.drawImage(avatar,w_player,h_player,cellWidth,cellHeight);
                }
            }

        }
    }
    private boolean validMove(int row, int col, int[][] maze){
        return   row < maze.length && row >= 0 && col < maze[0].length && col >= 0 && maze[row][col] != 1;
    }

    public void setSolutionOnMaze(Solution solution) {
        for (AState state: solution.getSolutionPath()){
            Position position = ((MazeState)state).getM_position();
            this.maze.set_value(position.getRowIndex(),position.getColumnIndex(),2);
        }
    }
    public Maze getMaze() {
        return this.maze;
    }

    public String getSound_walking() {
        return sound_walking.get();
    }

    public StringProperty sound_walkingProperty() {
        return sound_walking;
    }

    public void setSound_walking(String sound_walking) {
        this.sound_walking.set(sound_walking);
    }

    public void setWallImagesFolder(String wallImagesFolder) {
        this.wallImagesFolder.set(wallImagesFolder);
    }

    public String getSound_wall() {
        return sound_wall.get();
    }

    public StringProperty sound_wallProperty() {
        return sound_wall;
    }

    public void setSound_wall(String sound_wall) {
        this.sound_wall.set(sound_wall);
    }


    private boolean checkDiagonal(int row, int col){
        //check if this spot is diagonal or normal move
        int curr_row = this.getRow_player();
        int curr_col = this.getCol_player();
        if(curr_row == row || curr_col == col)
            return true;
        if(row+1 == curr_row && col+1 == curr_col){
            return !(this.maze.get_cell_value(row, col + 1) == 1 && this.maze.get_cell_value(row + 1, col) == 1);
        }
        if(row-1 == curr_row && col-1 == curr_col){
            return !(this.maze.get_cell_value(row, col - 1) == 1 && this.maze.get_cell_value(row - 1, col) == 1);
        }
        if(row+1 == curr_row && col-1 == curr_col)
            return !(this.maze.get_cell_value(row, col - 1) == 1 && this.maze.get_cell_value(row + 1, col) == 1);
        if(row-1 == curr_row && col+1 == curr_col)
            return !(this.maze.get_cell_value(row-1, col ) == 1 && this.maze.get_cell_value(row, col+1) == 1);
        return true;
    }

    @Override
    public double prefWidth(double height) {
        Screen screen = Screen.getPrimary();
        return screen.getVisualBounds().getWidth()-50;
    }

    @Override
    public double prefHeight(double width) {
        Screen screen = Screen.getPrimary();
        return screen.getVisualBounds().getHeight()-50;
    }


    public void setAvatar(String avatar) {
        try {
            this.avatar = new Image(new FileInputStream("./src/Resources/Images/Characters/"+avatar));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}