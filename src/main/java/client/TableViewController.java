package client;

import com.sun.tools.javac.comp.Flow;
import core.Entry;
import core.Field;
import core.Table;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private Button insertButton;

    @FXML
    private Button filterButton;

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
        for (Entry entry : entries) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (String field : fieldNames) {
                row.add(entry.getAll().get(field));
            }
            data.add(row);
        }
        tableView.setItems(data);
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
                        value = new JSONObject().put("$gte", text.substring(2));
                    } else {
                        value = new JSONObject().put("$gt", text.substring(1));
                    }
                } else {
                    // > or >=
                    if (text.charAt(1) == '=') {
                        value = new JSONObject().put("$lte", text.substring(2));
                    } else {
                        value = new JSONObject().put("$lt", Integer.parseInt(text.substring(1)));
                    }
                }
                if (value != null)
                    filter.put(key, value);
            }
            ConnectionManager.sendUseDatabase(database);
            Table t = ConnectionManager.sendGetTable(table);
            System.out.println(filter);
            List<Entry> entries = ConnectionManager.sendSelectByFilter(table, filter);
            loadData(t, entries, fieldNames);
        });
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
