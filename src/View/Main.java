package View;

import Model.IModel;
import Model.MyModel;
import Server.Server;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.io.Serializable;
import java.util.Observer;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;

public class Main extends Application {
    private static Stage stage;
    private static Scene scene;
    private static IModel model;
    private static Server maze_generator_server;
    private static Server solve_maze_server;
    private static MyViewModel myViewModel;
    @Override
    public void start(Stage primaryStage) throws Exception{

         maze_generator_server = new Server(5400, 100000, new ServerStrategyGenerateMaze());
         solve_maze_server = new Server(5401, 1000000000, new ServerStrategySolveSearchProblem());
        maze_generator_server.start();
        solve_maze_server.start();
        stage = primaryStage;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = loader.load();
        stage.setTitle("Itzik and Ariel Maze Game");
        scene = new Scene(root);
        IView myView = loader.getController();
        model = new MyModel();
        myViewModel = new MyViewModel(model);
        myView.addViewModel(myViewModel);
        myViewModel.addObserver((Observer) myView);


        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setWidth(screenBounds.getWidth()*0.9);
        stage.setHeight(screenBounds.getHeight()*0.9);
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);

        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop(){
        maze_generator_server.stop();
        solve_maze_server.stop();
    }
    public static Stage getStage(){
        return stage;
    }
    public static Scene getScene(){
        return scene;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
    public static FXMLLoader changeToMazeScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MazeUI.fxml"));
        Parent root = fxmlLoader.load();
        IView mazeView = fxmlLoader.getController();
        mazeView.addViewModel(myViewModel);
        myViewModel.addObserver(mazeView);
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        return fxmlLoader;
    }
}
