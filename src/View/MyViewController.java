package View;

import Model.IModel;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MyViewController implements IView, Initializable,Observer {

    @FXML
    public TextField rows_from_user;

    @FXML
    public TextField cols_from_user;

    @FXML
    public Button btn_generate;
    @FXML
    public MazeDisplay mazeDisplay;

    private MyViewModel viewModel;
    private Maze maze;
    private Stage stage;
    private Scene scene2;
    public Stage org_stage;
    private Scene org_scene;

    public RadioButton char_april_radio;
    public RadioButton char_donatello_radio;
    public RadioButton char_leonardo_radio;
    public RadioButton char_michelangelo_radio;
    public RadioButton char_raphael_radio;
    public RadioButton char_splinter_radio;

    final ToggleGroup char_selection = new ToggleGroup();
    final HashMap<RadioButton,String> chars_and_pic = new HashMap<RadioButton, String>();



    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof MyViewModel) {
            switch ((String) arg) {
                case "MazeGenerate":
                    this.maze = viewModel.getMaze();
                    MazeDisplayController mazeDisplayController = null;
                    try {
                        mazeDisplayController = Main.changeToMazeScene().getController();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mazeDisplayController.setAvatar(this.chars_and_pic.get(this.char_selection.getSelectedToggle()));
                    mazeDisplayController.drawMaze(maze,false);
                    break;
            }
        }
    }

    @Override
    public void handleGenerateMaze() throws IOException {
        //Check if fields are valid
        int rows,cols;
        try {
            rows = Integer.valueOf(this.rows_from_user.getText());
        }
        catch (NumberFormatException e)
        {
            rows = 10;
        }
        try {
            cols = Integer.valueOf(this.cols_from_user.getText());
        }
        catch (NumberFormatException e)
        {
            cols = 10;
        }

        //this.maze = this.viewModel.generateMaze(rows,cols);
        this.viewModel.generateMazeObserver(rows,cols);

        //mazeDisplayController.drawMaze(maze,false);
    }

    @Override
    public void addViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void exitProgram() {
        Main.exitProgram();
    }

    public void changeViews(){
        Main.getStage().setScene(Main.getScene());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.char_april_radio.setToggleGroup(this.char_selection);
        this.char_donatello_radio.setToggleGroup(this.char_selection);
        this.char_leonardo_radio.setToggleGroup(this.char_selection);
        this.char_michelangelo_radio.setToggleGroup(this.char_selection);
        this.char_raphael_radio.setToggleGroup(this.char_selection);
        this.char_splinter_radio.setToggleGroup(this.char_selection);

        this.chars_and_pic.put(this.char_april_radio,"april-o-neil.png");
        this.chars_and_pic.put(this.char_donatello_radio,"donatello.png");
        this.chars_and_pic.put(this.char_leonardo_radio,"leonardo.png");
        this.chars_and_pic.put(this.char_michelangelo_radio,"michelangelo.png");
        this.chars_and_pic.put(this.char_raphael_radio,"raphael.png");
        this.chars_and_pic.put(this.char_splinter_radio,"splinter.png");
    }
}
