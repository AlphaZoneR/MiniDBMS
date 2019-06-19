package client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class SelectController {

    @FXML
    TextArea textArea;

    @FXML
    Button selectButton;

    private String database;

    public void initialize() {

        loadSendQuery();
    }

    private void loadSendQuery() {
        if (Client.lastQuery != null) {
            textArea.setText(Client.lastQuery);
        }
        selectButton.setOnAction(event -> {
            Client.lastQuery = textArea.getText();
            Client.controller.loadSelectView(database, textArea.getText());
        });
    }

    public void setDatabase(String database) {
        this.database = database;
    }
}
