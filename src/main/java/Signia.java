import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Signia extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Parent root = FXMLLoader.load(Signia.class.getResource("main.fxml"));
    primaryStage.setTitle("Signia");
    Scene scene = new Scene(root);
//    scene.getStylesheets().add(Signia.class.getResource("styles.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
