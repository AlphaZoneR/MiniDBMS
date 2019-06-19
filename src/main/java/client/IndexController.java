package client;

import core.Field;
import core.Table;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.util.List;

public class IndexController {

    @FXML
    private ComboBox comboBox;

    @FXML
    private Button indexButton;

    private String database;

    private String table;

    public void initialize() {
        loadSendIndex();
    }

    private void loadFields() {
        try {
            ConnectionManager.sendUseDatabase(database);
            Table t = ConnectionManager.sendGetTable(table);
            List<Field> fields = t.getFields();
            for(Field field: fields) {
                comboBox.getItems().add(field.getName());
            }
        } catch (RuntimeException e) {
            Client.controller.setResponse(e.getMessage());
        }
    }

    private void loadSendIndex() {
        indexButton.setOnAction(event -> {
            if (comboBox.getSelectionModel().getSelectedItem() != null)
                Client.controller.clearPane();
        });
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setTable(String table) {
        this.table = table;
        loadFields();
    }
}
