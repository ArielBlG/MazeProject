package View;

import ViewModel.MyViewModel;
import javafx.beans.Observable;

import java.io.IOException;
import java.util.Observer;

public interface IView{
    MyViewModel viewModel = null;
    void handleGenerateMaze() throws IOException;
    void addViewModel(MyViewModel viewModel);
    void exitProgram();
}
