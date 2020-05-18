package Model.parameter;

import application.Configuration;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionModel;

public class ComboBoxParameterStrategy implements ParameterStrategy {

    private final ReadOnlyObjectProperty<String> StringProperty;
    private final String PropertyName;

    private final ObservableList<String> items;
    private SelectionModel<String> selectionModel;


    public ComboBoxParameterStrategy(ComboBox<String> comboBox, String PropertyName, ObservableList<String> items) {

        selectionModel = comboBox.getSelectionModel();
        StringProperty = selectionModel.selectedItemProperty();

        this.PropertyName = PropertyName;
        this.items = items;

        comboBox.setItems(items);

        StringProperty.addListener((observableValue, oldValue, newValue) -> {
            Configuration.preferences.setPreferenceValue(this.PropertyName,StringProperty.getValue());

            System.out.println("Valeur de la comboBox:   " + StringProperty.getValue());

        });
    }

    @Override
    public String getValue() {
        return StringProperty.getValue();
    }

    @Override
    public void setValue(String value) {

        if(items.contains(value)){
            selectionModel.select(value);
        }else{
            selectionModel.selectFirst();
        }

    }
}
