package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.ASearchingAlgorithm;
import algorithms.search.Solution;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Screen;
import javafx.util.Duration;

import javax.sound.sampled.Clip;
import java.io.IOException;
import java.sql.Time;
import java.util.Observable;

public class MazeDisplayController implements IView {
    @FXML
    public MazeDisplay mazeDisplay;
    private Solution solution;
    private MyViewModel myViewModel;
    private Timeline timeline = new Timeline();


    StringProperty update_player_position_row = new SimpleStringProperty();
    StringProperty update_player_position_col = new SimpleStringProperty();

    private Clip wall_sound;

    public void changeViews(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        Main.getStage().setScene(new Scene(root));
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Main.getStage().setX((screenBounds.getWidth() - Main.getStage().getWidth()) / 2);
        Main.getStage().setY((screenBounds.getHeight() - Main.getStage().getHeight()) / 2);
        MyViewController myViewController = fxmlLoader.getController();
        myViewController.changeViews();
    }

    public void drawMaze(Maze maze,boolean flag) {
        mazeDisplay.drawMaze(maze,flag);
    }

    public void keyPressed(KeyEvent keyEvent) {
        int player_row_position = mazeDisplay.getRow_player();
        int player_col_position = mazeDisplay.getCol_player();
        switch (keyEvent.getCode()){
            case W:
                mazeDisplay.setPlayerPosition(player_row_position-1,player_col_position);
                set_update_player_position_row(player_row_position-1 + "");
                set_update_player_position_col(player_col_position + "");

                break;
            case S:
                mazeDisplay.setPlayerPosition(player_row_position+1,player_col_position);
                set_update_player_position_row(player_row_position+1 + "");
                set_update_player_position_col(player_col_position + "");
                break;
            case D:
                mazeDisplay.setPlayerPosition(player_row_position,player_col_position+1);
                set_update_player_position_row(player_row_position +"");
                set_update_player_position_col(player_col_position+1 +"");
                break;
            case A:
                mazeDisplay.setPlayerPosition(player_row_position,player_col_position-1);
                set_update_player_position_row(player_row_position +"");
                set_update_player_position_col(player_col_position-1 +"");
                break;
            case Q:
                mazeDisplay.setPlayerPosition(player_row_position-1,player_col_position-1);
                set_update_player_position_row(player_row_position-1 +"");
                set_update_player_position_col(player_col_position-1 +"");
                break;
            case E:
                mazeDisplay.setPlayerPosition(player_row_position-1,player_col_position+1);
                set_update_player_position_row(player_row_position-1 +"");
                set_update_player_position_col(player_col_position+1 +"");
                break;
            case Z:
                mazeDisplay.setPlayerPosition(player_row_position+1,player_col_position-1);
                set_update_player_position_row(player_row_position+1 +"");
                set_update_player_position_col(player_col_position-1 +"");
                break;
            case X:
                mazeDisplay.setPlayerPosition(player_row_position+1,player_col_position+1);
                set_update_player_position_row(player_row_position+1 +"");
                set_update_player_position_col(player_col_position+1 +"");
                break;
            case H:
                this.solution = myViewModel.getSolution(mazeDisplay.getMaze());
                mazeDisplay.setSolutionOnMaze(solution);
                mazeDisplay.drawMaze(mazeDisplay.getMaze(),true);
                break;
        }

        keyEvent.consume();

    }
    public void set_update_player_position_row(String update_player_position_row) {
        this.update_player_position_row.set(update_player_position_row);
    }

    public void set_update_player_position_col(String update_player_position_col) {
        this.update_player_position_col.set(update_player_position_col);
    }

    public void showSolution(ActionEvent actionEvent) {
       this.solution = myViewModel.getSolution(mazeDisplay.getMaze());
       mazeDisplay.setSolutionOnMaze(solution);
       mazeDisplay.drawMaze(mazeDisplay.getMaze(),true);
    }


    @Override
    public void handleGenerateMaze() throws IOException {

    }

    @Override
    public void addViewModel(MyViewModel viewModel) {
        this.myViewModel = viewModel;
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public void setAvatar(String avatar) {
        this.mazeDisplay.setAvatar(avatar);
    }

    public void zoom(Node node, double factor, double x, double y) {
        // determine scale
        double oldScale = node.getScaleX();
        double scale = oldScale * factor;
        double f = (scale / oldScale) - 1;
        // determine offset that we will have to move the node
        Bounds bounds = node.localToScene(node.getBoundsInLocal());
        double dx = (x - (bounds.getWidth() / 2 + bounds.getMinX()));
        double dy = (y - (bounds.getHeight() / 2 + bounds.getMinY()));
        // timeline that scales and moves the node

        timeline.getKeyFrames().clear();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.millis(100), new KeyValue(node.translateXProperty(), node.getTranslateX() - f * dx)),
                new KeyFrame(Duration.millis(100), new KeyValue(node.translateYProperty(), node.getTranslateY() - f * dy)),
                new KeyFrame(Duration.millis(100), new KeyValue(node.scaleXProperty(), scale)),
                new KeyFrame(Duration.millis(100), new KeyValue(node.scaleYProperty(), scale))
        );
        timeline.play();
    }

    public void Zoom(ScrollEvent event) {
        double m_zoom;
        if (event.isControlDown()) {
            m_zoom = 1.5;
            if (event.getDeltaY() > 0) {
                m_zoom = 1.1*m_zoom;

            } else if (event.getDeltaY() < 0) {
                m_zoom = 1.1/ m_zoom;
            }
            zoom(mazeDisplay, m_zoom, event.getSceneX(), event.getSceneY());
            mazeDisplay.setScaleX(mazeDisplay.getScaleX() * m_zoom);
            mazeDisplay.setScaleY(mazeDisplay.getScaleY() * m_zoom);
            event.consume();// event handling from the root
        }
    }
}
