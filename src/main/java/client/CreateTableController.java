package client;

import core.Field;
import core.Table;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;

public class CreateTableController {

    @FXML
    private TextField tableName;

    @FXML
    private TableView tableView;

    @FXML
    private FlowPane flowPane;

    @FXML
    private TextField fieldName;

    @FXML
    private ComboBox fieldType;

    @FXML
    private CheckBox fieldPrimary;

    @FXML
    private CheckBox fieldUnique;

    @FXML
    private CheckBox fieldIdentity;

    @FXML
    private CheckBox fieldNullable;

    @FXML
    private TextField fieldRef;

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

        loadListener();
    }

    private void initializeColumnFactory() {
        TableColumn<String, Field> name = new TableColumn<>("Field Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Field, String> type = new TableColumn<>("Field Type");
        type.setCellValueFactory(cell -> new SimpleStringProperty((fTypes[cell.getValue().getType()]).name()));

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
        isUnique.setCellFactory(col -> factory());

        TableColumn<Field, Boolean> isIdentity = new TableColumn<>("Is Identity");
        isIdentity.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().getIdentity()));
        isIdentity.setCellFactory(col -> factory());

        TableColumn<Field, Boolean> isNullable = new TableColumn<>("Is Nullable");
        isNullable.setCellValueFactory(cell -> new SimpleBooleanProperty(cell.getValue().getNullable()));
        isNullable.setCellFactory(col -> factory());

        fieldType.getItems().addAll(
                Field.Type.values()[0],
                Field.Type.values()[1],
                Field.Type.values()[2],
                Field.Type.values()[3]);

        tableView.getColumns().addAll(name, type, isPrimary, isUnique, isIdentity, isNullable);
    }

    private TableCell<Field, Boolean> factory() {
        return new TableCell<Field, Boolean>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item ? "True" : "False");
            }
        };
    }

    private void loadAddField() {
        addField.setOnAction(event -> {
            if (fieldName.getText().equals("") && fieldType.getValue().toString().isEmpty())
                return;
            String name = fieldName.getText();
            try {
                int type = ((Field.Type) fieldType.getValue()).ordinal();

                Field field = new Field(name, type);
                if (type < 3) {
                    field.setPrimary(fieldPrimary.isSelected());
                    field.setUnique(fieldUnique.isSelected());
                    field.setIdentity(fieldIdentity.isSelected());
                    field.setNull(fieldNullable.isSelected());
                } else {
                    field.setRef(fieldRef.getText());
                }
                tableView.getItems().add(field);
                fields.add(field);
            } catch (NullPointerException e) {
                Client.controller.setResponse("FieldType not set");
            }

            fieldName.setText("");
            fieldType.getSelectionModel().clearSelection();
            fieldPrimary.setSelected(false);
            fieldUnique.setSelected(false);
            fieldIdentity.setSelected(false);
            fieldNullable.setSelected(false);

        });
    }

    private void loadCreateTable() {
        createTable.setOnAction(event -> {
            String name = tableName.getText();
            Table table = new Table(name, fields);
            try {
                ConnectionManager.sendUseDatabase(database);
                ConnectionManager.sendCreateTable(table);
            } catch (RuntimeException e) {
                Client.controller.setResponse(e.getMessage());
            }
            Client.controller.loadTreeItems();
            Client.controller.clearPane();
        });
    }

    private void loadListener() {
        flowPane.getChildren().clear();
        flowPane.getChildren().addAll(fieldName, fieldType, fieldPrimary, fieldUnique, fieldIdentity, fieldNullable, addField);

        fieldType.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue != null && ((Field.Type)oldValue).name().equals("ExternalType")) {
                flowPane.getChildren().clear();
                flowPane.getChildren().addAll(fieldName, fieldType, fieldPrimary, fieldUnique, fieldIdentity, fieldNullable, addField);
            }
            if (newValue != null && ((Field.Type)newValue).name().equals("ExternalType")) {
                flowPane.getChildren().clear();
                flowPane.getChildren().addAll(fieldName, fieldType, fieldRef, addField);
            }
        });
    }

    private void loadCancelTable() {
        cancelTable.setOnAction(event -> Client.controller.clearPane());
    }

    public void setDatabase(String database) {
        this.database = database;
    }

}
