package client;

import core.Table;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class TableViewController {

    @FXML
    private TableView tableView;

    private String database;

    private String table;

    public void initialize() {
        //;
    }

    public void loadTableView() {
        Table t = ConnectionManager.sendGetTable(database, table);
        for (String name : t.getFields()) {
            TableColumn column = new TableColumn(name);
            column.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    return new SimpleStringProperty(name);
                }
            });
            tableView.getColumns().add(column);
        }
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setTable(String table) {
        this.table = table;
    }
}
