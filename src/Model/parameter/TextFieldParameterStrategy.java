package Model.parameter;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;


public class TextFieldParameterStrategy implements ParameterStrategy {

    private final StringProperty stringProperty;
    private final String PropertyName;
    private final String TextFieldId;

    public TextFieldParameterStrategy(TextField textField, String PropertyName){

        this.stringProperty = textField.textProperty();
        this.PropertyName = PropertyName;
        this.TextFieldId = textField.getId();

        /* stringProperty.addListener((observableValue, oldValue, newValue) -> {
            System.out.println(this.TextFieldId + " à changé");
        });
         */

    }

    @Override
    public String getValue() {
        return stringProperty.getValue();
    }

    @Override
    public void setValue(String value) {
        stringProperty.setValue(value);
    }
}
