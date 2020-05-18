package utils;

import application.Configuration;
import application.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class StageOpener {

    public StageOpener(Stage Stage, String StageFXML, String Ressource, String StageName, String ParentStageName, Boolean AllowModality){

        try {

            Main.Stages.put(StageName, Stage);

            ResourceBundle bundle = ResourceBundle.getBundle(
                    "ressources." + Configuration.preferences.getPreferenceValue("language") + "." + Ressource
            );

            //on charge la scéne à partir du fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(StageFXML));

            //on charge les ressources
            loader.setResources(bundle);

            //on charge la view associée
            Parent root = loader.load();

            //on créé une nouvelle avec notre view
            Scene scene = new Scene(root);

            //on définit le composant racine à partir de notre view
            scene.setRoot(root);

            //on défini la scene sur sur la scene principale
            Stage.setScene(scene);

            //on affiche au milieu
            Stage.centerOnScreen();

            //On défini la fenetre comme non redimensionnable
            Stage.setResizable(false);

            //On défini le titre de la fenétre
            Stage.setTitle(Configuration.WindowTitleRessourceBundle.getString(StageName));

            //Stage.setAlwaysOnTop(true);

            //si on souhaite mettre une Modalité
            if(AllowModality){
                Stage.initModality(Modality.WINDOW_MODAL);
            }

            //si on shouhaite définir la fenêtre parent
            if(ParentStageName != null){
                Stage.initOwner(Main.Stages.get(ParentStageName));
            }

            //on affiche la scene principale
            Stage.show();

        }catch(Exception e) {
            e.printStackTrace();
        }


    }


}
