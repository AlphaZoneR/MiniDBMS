package client;

import core.Database;
import core.Table;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MyController {

    @FXML
    private TreeView<String> treeView;

    @FXML
    private TextField createDbName;

    @FXML
    private Button createDbButton;

    @FXML
    private TableView<String> tableView;

    public void initialize() {
        loadTreeItems();

        loadCreateButton();
    }

    void loadTreeItems() {
        treeView.setShowRoot(false);
        TreeItem<String> root = new TreeItem<>("Root Node");
        root.setExpanded(true);
        for (Database database : ConnectionManager.sendGetDropdown()) {
            DatabaseTreeItem db = new DatabaseTreeItem(database.getName());
            for (Table table : database.getTables()) {
                db.getChildren().add(new TableTreeItem(database.getName(), table.getName()));
            }
            root.getChildren().add(db);

        }
        treeView.setRoot(root);
        treeView.setCellFactory(p -> new MyTreeCell());
    }

    private void loadCreateButton() {
        createDbButton.setOnAction(event -> {
                String databaseName = createDbName.getText();
                ConnectionManager.sendCreateDatabase(databaseName);
                loadTreeItems();
                createDbName.setText("");
        });
    }

}
