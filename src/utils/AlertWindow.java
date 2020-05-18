package utils;

import application.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AlertWindow {

    /**
     * Donné membre contenant un objet de type Alert
     */
    Alert alert;

    /**
     *
     */
    String StageToClose;



    /**
     * Constructeur par défaut
     */
    public AlertWindow(String Windowstitle, String Message, @Nullable String StageToClose){

        this.StageToClose = StageToClose;

        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Main.configuration.AlertMessageRessourceBundle.getString(Windowstitle));
        alert.setHeaderText(null);
        alert.setContentText(Main.configuration.AlertMessageRessourceBundle.getString(Message));

    }

    public void show(){

        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent()){

            if(result.get() == ButtonType.OK){

                if(StageToClose != null){
                    Main.Stages.get(StageToClose).close();
                }

            }

        }

    }

}
