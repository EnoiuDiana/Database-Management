package order.management.Presentation.Controller.Util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NewSceneLoader {

    /**
     * method to load a new scene
     * @param event
     * @param sceneToLoad scene name to be loaded
     * @throws IOException
     */
    public void loadNewScene(ActionEvent event, String sceneToLoad) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/order/management/Presentation/View/" + sceneToLoad + ".fxml"));
        Parent simulationSceneParent = fxmlLoader.load();
        Scene simulationScene = new Scene(simulationSceneParent);
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(simulationScene);
        window.show();
    }
}
