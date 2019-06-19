package client;


import core.Entry;
import core.Field;
import core.Table;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TableViewController {

    @FXML
    private TableView tableView;

    @FXML
    private FlowPane flowPane;

    @FXML
    private FlowPane buttonPane;

    @FXML
    private Button insertButton;

    @FXML
    private Button filterButton;

    @FXML
    private Button deleteButton;

    private String database;

    private String table;

    public void initialize() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void loadTableView() {
        ConnectionManager.sendUseDatabase(database);
        Table t = ConnectionManager.sendGetTable(table);
        List<Entry> entries = ConnectionManager.sendSelectAll(table);
        List<String> fieldNames = new ArrayList<>();

        loadData(t, entries, fieldNames);
        loadInsert(fieldNames);

        initializeInsertButton(fieldNames);
        initializeFilterButton(fieldNames);
        initializeDeleteButton(fieldNames);

        initializeContextMenu(fieldNames);
    }

    public void loadSelectView(String query) {
        ConnectionManager.sendUseDatabase(database);
        List<Entry> entries = ConnectionManager.sendQuery(query);
        List<String> fieldNames = new ArrayList<>();
        if(entries.size() > 0) {
            fieldNames.addAll(entries.get(0).getKeys());

            loadData(entries, fieldNames);
        }

        buttonPane.getChildren().clear();
    }

    private void loadData(Table t, List<Entry> entries, List<String> fieldNames) {
        tableView.getItems().clear();
        tableView.getColumns().clear();
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        int i = 0;
        for (Field field : t.getFields()) {
            fieldNames.add(field.getName());
            final int j = i;
            i++;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(field.getName());
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));
            tableView.getColumns().add(column);
        }
        loadEntries(data, entries, fieldNames);
        tableView.setItems(data);
    }

    private void loadData(List<Entry> entries, List<String> fieldNames) {
        tableView.getItems().clear();
        tableView.getColumns().clear();
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        int i = 0;
        for (String field : fieldNames) {
            final int j = i;
            i++;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(field);
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));
            tableView.getColumns().add(column);
        }
        loadEntries(data, entries, fieldNames);
        tableView.setItems(data);
    }

    private void loadEntries(ObservableList<ObservableList> data, List<Entry> entries, List<String> fieldNames) {
        for (Entry entry : entries) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (String field : fieldNames) {
                row.add(entry.getAll().get(field));
            }
            data.add(row);
        }
    }

    private void loadInsert(List<String> fieldNames) {
        for (String field : fieldNames) {
            TextField textField = new TextField();
            textField.setPromptText(field);
            textField.setPrefWidth((flowPane.getPrefWidth() - 10 * fieldNames.size() + 10) / fieldNames.size());
            flowPane.getChildren().add(textField);
        }
    }

    private void initializeInsertButton(List<String> fieldNames) {
        insertButton.setOnAction(event -> {
            Entry entry = new Entry(table);
            for (Node node : flowPane.getChildren()) {
                TextField textField = (TextField) node;
                if (textField.getText().isEmpty())
                    return;
                entry.add(textField.getPromptText(), textField.getText());
                textField.setText("");
            }
            ConnectionManager.sendUseDatabase(database);
            ConnectionManager.sendInsert(table, entry);

            Table t = ConnectionManager.sendGetTable(table);
            List<Entry> entries = ConnectionManager.sendSelectAll(table);
            loadData(t, entries, fieldNames);
        });
    }

    private void initializeFilterButton(List<String> fieldNames) {
        filterButton.setOnAction(event -> {
            JSONObject filter = new JSONObject();
            loadJSONFromTextFields(filter);

            ConnectionManager.sendUseDatabase(database);
            Table t = ConnectionManager.sendGetTable(table);
            List<Entry> entries = ConnectionManager.sendSelectByFilter(table, filter);
            loadData(t, entries, fieldNames);
        });
    }

    private void initializeDeleteButton(List<String> fieldNames) {
        deleteButton.setOnAction(event -> {
            JSONObject filter = new JSONObject();
            loadJSONFromTextFields(filter);

            ConnectionManager.sendUseDatabase(database);
            Table t = ConnectionManager.sendGetTable(table);
            System.out.println(ConnectionManager.sendDeleteByFilter(table, filter));
            List<Entry> entries = ConnectionManager.sendSelectAll(table);
            loadData(t, entries, fieldNames);
        });
    }

    private void loadJSONFromTextFields(JSONObject filter) {
        for (Node node : flowPane.getChildren()) {
            TextField textField = (TextField) node;
            if (textField.getText().isEmpty())
                continue;
            String key = textField.getPromptText();
            String text = textField.getText();
            textField.setText("");
            Object value;
            if(text.matches("[0-9]+")) {
                // ==
                value = Integer.parseInt(text);
            } else if (text.matches("<=?[0-9]+")) {
                // < or <=
                if (text.charAt(1) == '=') {
                    value = new JSONObject().put("$lte", Integer.parseInt(text.substring(2)));
                } else {
                    value = new JSONObject().put("$lt", Integer.parseInt(text.substring(1)));
                }
            } else if (text.matches(">=?[0-9]+")) {
                // > or >=
                if (text.charAt(1) == '=') {
                    value = new JSONObject().put("$gte", Integer.parseInt(text.substring(2)));
                } else {
                    value = new JSONObject().put("$gt", Integer.parseInt(text.substring(1)));
                }
            } else  {
                // String
                value = text;
            }
            if (value != null)
                filter.put(key, value);
        }
    }

    private void initializeContextMenu(List<String> fieldNames) {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem delete = new MenuItem("DELETE ENTRY");
        delete.setOnAction(event -> {
            ObservableList item = (ObservableList)tableView.getSelectionModel().getSelectedItem();
            int index = 0;
            for (Node node : flowPane.getChildren()) {
                TextField textField = (TextField) node;
                textField.setText((String)item.get(index++));
            }
            JSONObject filter = new JSONObject();
            loadJSONFromTextFields(filter);

            ConnectionManager.sendUseDatabase(database);
            Table t = ConnectionManager.sendGetTable(table);
            System.out.println(ConnectionManager.sendDeleteByFilter(table, filter));
            List<Entry> entries = ConnectionManager.sendSelectAll(table);
            loadData(t, entries, fieldNames);
        });
        contextMenu.getItems().add(delete);
        tableView.setContextMenu(contextMenu);
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
