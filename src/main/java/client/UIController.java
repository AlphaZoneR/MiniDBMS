package client;

import core.Database;
import core.Table;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class UIController {

    @FXML
    private TreeView<String> treeView;

    @FXML
    private TextField createDbName;

    @FXML
    private Button createDbButton;

    @FXML
    private Pane dynamicPane;

    public void initialize() {
        loadTreeItems();

        loadCreateButton();

        loadListener();
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

    private void loadListener() {
        treeView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
                if (item != null) {
                    if (item.getClass() == TableTreeItem.class) {
                        loadTableView(item.getParent().getValue(), item.getValue());
                    } else {
                        if (!item.isExpanded()) {
                            loadCreateTable(item.getValue());
                            item.setExpanded(true);
                        }
                    }
                }
            }
        });
    }

    public void loadCreateTable(String database) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UIController.class.getResource("/CreateTable.fxml"));
            Pane pane = fxmlLoader.load();

            CreateTableController controller = fxmlLoader.getController();
            controller.setDatabase(database);

            clearPane();
            dynamicPane.getChildren().add(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTableView(String database, String table) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UIController.class.getResource("/TableView.fxml"));
            Pane pane = fxmlLoader.load();

            TableViewController controller = fxmlLoader.getController();
            controller.setDatabase(database);
            controller.setTable(table);
            controller.loadTableView();

            clearPane();
            dynamicPane.getChildren().add(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSelect(String database) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UIController.class.getResource("/Select.fxml"));
            Pane pane = fxmlLoader.load();

            SelectController controller = fxmlLoader.getController();
            controller.setDatabase(database);

            clearPane();
            dynamicPane.getChildren().add(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSelectView(String database, String query) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UIController.class.getResource("/TableView.fxml"));
            Pane pane = fxmlLoader.load();

            TableViewController controller = fxmlLoader.getController();
            controller.setDatabase(database);
            controller.loadSelectView(query);

            clearPane();
            dynamicPane.getChildren().add(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearPane() {
        dynamicPane.getChildren().clear();
    }

}
