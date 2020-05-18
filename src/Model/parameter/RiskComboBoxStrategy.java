package Model.parameter;

import Model.databaseModels.Risk;
import application.Configuration;
import application.Main;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.util.Callback;

public class RiskComboBoxStrategy implements ParameterStrategy {

    private final ReadOnlyObjectProperty<Risk> riskProperty;
    private final String PropertyName;

    private final SelectionModel<Risk> selectionModel;

    private final ComboBox<Risk> comboBox;

    /**
     * Constructeur par défaut
     * @param comboBox La comboBox à paramétrer
     * @param PropertyName Nom du paramétres à enregistrer
     * @param items ObservableList contenenant les itmems à ajouter à la ComboBox
     */
    public RiskComboBoxStrategy(ComboBox<Risk> comboBox, String PropertyName, ObservableList<Risk> items) {

        System.out.println("STEP 1");

        System.out.println(items);

        comboBox.setDisable(true);

        this.PropertyName = PropertyName;
        this.comboBox = comboBox;

        selectionModel = this.comboBox.getSelectionModel();
        riskProperty = selectionModel.selectedItemProperty();

        this.comboBox.setItems(items);
        this.comboBox.getSelectionModel().selectFirst();

        this.CreateFactories();

        riskProperty.addListener((observableValue, oldValue, newValue) -> {

            //on met à jour la valeur du paramétre correspondant avec la nouvelle séléction
            Configuration.preferences.setPreferenceValue(
                    this.PropertyName,
                    String.valueOf(riskProperty.getValue().getRiskValue())
            );

            System.out.println("Valeur de la comboBox:   " + riskProperty.getValue().getRiskValue());

        });


    }

    @Override
    public String getValue() {
        return String.valueOf(selectionModel.getSelectedItem().getRiskValue());
    }

    @Override
    public void setValue(String value) {

        //on test si un des risques contenus dans notre ObservableList
        // contient le risque passer en paramétre de la fonction

        System.out.println("STEP 2");

        for(Risk risk: comboBox.getItems()){

            System.out.println(risk.getRiskValue());

            if(risk.getRiskValue() == Double.parseDouble(value)){
                selectionModel.select(risk);
            }else{
                selectionModel.selectFirst();
            }

        }

    }

    /*
        Factories
     */
    private void CreateFactories(){

        //Factory pour afficher le risque
        comboBox.setCellFactory(new Callback<>() {

            @Override
            public ListCell<Risk> call(ListView<Risk> riskListView) {

                return new ListCell<>() {

                    @Override
                    public void updateItem(Risk risk, boolean b) {
                    super.updateItem(risk, b);
                    if(risk != null){
                        setText(String.valueOf(risk.getRiskValue()));
                    }
                    }

                };

            }
        });

        //Factory pour afficher le risque
        comboBox.setButtonCell(new ListCell<>(){

            @Override
            public void updateItem(Risk risk, boolean b) {
            super.updateItem(risk, b);
            if(risk != null){
                setText(String.valueOf(risk.getRiskValue()));
            }
            }
        });

    }
}
