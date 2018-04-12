package uk.ac.uel.signia.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class LessonListController extends ParentAware {

    @FXML
    private ListView<Label> alphabetLessons;

    @FXML
    public void initialize() {
       alphabetLessons
           .getSelectionModel()
           .selectedItemProperty()
           .addListener((observable, oldValue, newValue) -> {
               if (newValue.getId().equals("lesson1")) {
                   parentController.switchToLesson(1);
               }
           });
    }

}
