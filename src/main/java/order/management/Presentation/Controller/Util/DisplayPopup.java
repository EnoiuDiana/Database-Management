package order.management.Presentation.Controller.Util;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DisplayPopup {

    /**
     * Method to display a popup window
     * @param title the title of the popup
     * @param labelMassage the massage of the popup
     */
    public static void displayPopup(String title, String labelMassage) {
        Stage popupWindow = new Stage();
        popupWindow.initModality(Modality.APPLICATION_MODAL);
        popupWindow.setTitle(title);
        Label label1= new Label(labelMassage);
        Button button1= new Button("Close");
        button1.setOnAction(ex -> popupWindow.close());
        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1, button1);
        layout.setAlignment(Pos.CENTER);
        Scene scene1= new Scene(layout, 300, 250);
        popupWindow.setScene(scene1);
        popupWindow.showAndWait();
    }
}
