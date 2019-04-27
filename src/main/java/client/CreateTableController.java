package client;

import core.Field;
import core.Table;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

public class CreateTableController {

    @FXML
    private TextField tableName;

    @FXML
    private TableView tableView;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldType;

    @FXML
    private CheckBox fieldForeign;

    @FXML
    private CheckBox fieldPrimary;

    @FXML
    private CheckBox fieldUnique;

    @FXML
    private Button addField;

    @FXML
    private Button createTable;

    private ArrayList<Field> fields;

    private String database;

    public void initialize() {
        fields = new ArrayList<>();

        initializeColumnFactory();

        loadAddField();

        loadCreateTable();
    }

    private void initializeColumnFactory() {
        TableColumn<String, Field> name = new TableColumn<>("Field Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<String, Field> type = new TableColumn<>("Field Type");
        type.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Field, Boolean> isForeign = new TableColumn<>("Is Foreign");
        isForeign.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().getForeign()));
        isForeign.setCellFactory(col -> new TableCell<Field, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "True" : "False");
            }
        });

        TableColumn<Field, Boolean> isPrimary = new TableColumn<>("Is Primary");
        isPrimary.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().getPrimary()));
        isPrimary.setCellFactory(col -> new TableCell<Field, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "True" : "False");
            }
        });

        TableColumn<Field, Boolean> isUnique = new TableColumn<>("Is Unique");
        isUnique.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().getUnique()));
        isUnique.setCellFactory(col -> new TableCell<Field, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "True" : "False");
            }
        });

        tableView.getColumns().addAll(name, type, isForeign, isPrimary, isUnique);
    }

    private void loadAddField() {
        addField.setOnAction(event -> {
            if (fieldName.getText().equals("") || fieldType.getText().equals(""))
                return;
            String name = fieldName.getText();
            String type = fieldType.getText();
            Field field = new Field(name, type);
            field.setForeign(fieldForeign.isSelected());
            field.setPrimary(fieldPrimary.isSelected());
            field.setUnique(fieldUnique.isSelected());
            tableView.getItems().add(field);
            fields.add(field);

            fieldName.setText("");
            fieldType.setText("");
            fieldForeign.setSelected(false);
            fieldPrimary.setSelected(false);
            fieldUnique.setSelected(false);

        });
    }

    private void loadCreateTable() {
        createTable.setOnAction(event -> {
            String name = tableName.getText();
            Table table = new Table(name, fields);
            ConnectionManager.sendCreateTable(database, table);
            Client.controller.loadTreeItems();
        });
    }

    public void setDatabase(String database) {
        this.database = database;
    }


}
