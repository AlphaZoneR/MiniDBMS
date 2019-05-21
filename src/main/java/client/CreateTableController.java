package client;

import core.Field;
import core.Table;
import java.lang.Enum.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
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
    private ComboBox fieldType;

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

    @FXML
    private Button cancelTable;

    private ArrayList<Field> fields;

    private String database;

    private Field.Type[] fTypes = Field.Type.values();

    public void initialize() {

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        fields = new ArrayList<>();

        initializeColumnFactory();

        loadAddField();

        loadCreateTable();

        loadCancelTable();
    }

    private void initializeColumnFactory() {
        TableColumn<String, Field> name = new TableColumn<>("Field Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Field, String> type = new TableColumn<>("Field Type");
        type.setCellValueFactory(cell -> new SimpleStringProperty((fTypes[cell.getValue().getType()]).name()));


        TableColumn<Field, Boolean> isForeign = new TableColumn<>("Is Foreign");
//        isForeign.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().getForeign()));
//        isForeign.setCellFactory(col -> new TableCell<Field, Boolean>() {
//            @Override
//            protected void updateItem(Boolean item, boolean empty) {
//                super.updateItem(item, empty);
//                setText(empty ? null : item ? "True" : "False");
//            }
//        });

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

        fieldType.getItems().addAll(
                Field.Type.values()[0],
                Field.Type.values()[1],
                Field.Type.values()[2],
                Field.Type.values()[3]);

        tableView.getColumns().addAll(name, type, isForeign, isPrimary, isUnique);
    }

    private void loadAddField() {
        addField.setOnAction(event -> {
            if (fieldName.getText().equals("") && fieldType.getValue().toString().isEmpty() )
                return;
            String name = fieldName.getText();
            int type = ((Field.Type)fieldType.getValue()).ordinal();
            Field field = new Field(name, type);
            field.setPrimary(fieldPrimary.isSelected());
            field.setUnique(fieldUnique.isSelected());
            tableView.getItems().add(field);
            fields.add(field);

            fieldName.setText("");
            fieldType.getSelectionModel().clearSelection();
            fieldForeign.setSelected(false);
            fieldPrimary.setSelected(false);
            fieldUnique.setSelected(false);

        });
    }

    private void loadCreateTable() {
        createTable.setOnAction(event -> {
            String name = tableName.getText();
            Table table = new Table(name, fields);
            System.out.println(ConnectionManager.sendUseDatabase(database));
            System.out.println(ConnectionManager.sendCreateTable(table));
            Client.controller.loadTreeItems();
            Client.controller.clearPane();
        });
    }

    private void loadCancelTable() {
        cancelTable.setOnAction(event -> Client.controller.clearPane());
    }

    public void setDatabase(String database) {
        this.database = database;
    }

}
