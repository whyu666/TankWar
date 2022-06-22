package misc;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class LengthLimitedTextField extends TextField {
    /**
     * @param maxCharacters The max allowed characters that can be entered into this {@link TextField}.
     */
    public LengthLimitedTextField(final int maxCharacters) {
        final TextField thisField = this;
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            // Force correct length by deleting the last entered character if text is longer than maxCharacters
            if (newValue.length() > maxCharacters) {
                thisField.deleteNextChar();
            }
        });
    }
}