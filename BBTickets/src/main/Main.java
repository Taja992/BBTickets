import DAL.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GUI/view/main.fxml"));
        //once we start opening program on the login screen we can use this instead for style
//        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("GUI/view/login.fxml"));
//        Parent root = fxmlLoader.load();
//        root.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15; -fx-border-radius: 15;");
//        Scene scene = new Scene(root, 320, 240);
//        scene.setFill(Color.TRANSPARENT);
//        primaryStage.initStyle(StageStyle.TRANSPARENT);
//        primaryStage.setTitle("Ticket Interface");
//        primaryStage.setScene(scene);
//        primaryStage.show();
        Scene scene = new Scene(fxmlLoader.load());
        primaryStage.setTitle("Ticket Interface");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Test database connection
       /* try {
            ConnectionManager connectionManager = new ConnectionManager();
            Connection connection = connectionManager.getConnection();
            System.out.println("Connected to database successfully!");
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed to connect to database: " + e.getMessage());
        }*/
    }
}
