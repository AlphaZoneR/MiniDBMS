package client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SelectController {

    @FXML
    TextField textField;

    @FXML
    Button selectButton;

    private String database;

    public void initialize() {

        loadSendQuery();
    }

    private void loadSendQuery() {
        selectButton.setOnAction(event -> {
            Client.controller.loadSelectView(database, textField.getText());
        });
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
