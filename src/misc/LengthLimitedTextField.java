package misc;

import javafx.scene.control.TextField;

public class LengthLimitedTextField extends TextField {

    public LengthLimitedTextField(final int maxCharacters) {
        final TextField thisField = this;
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            //如果文本长于maxCharacters，则通过删除最后输入的字符来强制长度正确
            if (newValue.length() > maxCharacters) {
                thisField.deleteNextChar();
            }
        });
    }

}