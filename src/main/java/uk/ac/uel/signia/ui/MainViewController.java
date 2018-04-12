package uk.ac.uel.signia.ui;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainViewController {

    @FXML
    private Pane center;

    @FXML
    public void initialize() {
        try {
            loadFxml("lessonList");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToLesson(int lessonId) {
        try {
            loadFxml("lesson");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFxml(String name) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/" + name + ".fxml"));
        Pane newLoadedPane = fxmlLoader.load();
        Object childController = fxmlLoader.getController();
        if (childController instanceof ParentAware) {
            ((ParentAware) childController).setParentController(this);
        }
        center.getChildren().clear();
        center.getChildren().add(newLoadedPane);
    }

}
