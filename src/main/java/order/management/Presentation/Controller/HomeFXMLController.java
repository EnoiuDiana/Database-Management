package order.management.Presentation.Controller;


import javafx.event.ActionEvent;

import javafx.scene.control.Button;

import order.management.Presentation.Controller.Util.NewSceneLoader;

import java.io.IOException;

/**
 * The controller of the home.fxml
 */
public class HomeFXMLController {

    /**
     * method to go to one of the three windows,i.e customers, items or place order
     * @param event
     * @throws IOException
     */
    public void buttonPushed(ActionEvent event) throws IOException {
        Button button = (Button) event.getSource();
        String sceneToLoad = button.getId();
        NewSceneLoader newSceneLoader = new NewSceneLoader();
        newSceneLoader.loadNewScene(event, sceneToLoad);
    }

}
