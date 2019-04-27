package client;

import core.Database;
import core.Table;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
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

//        loadListener();
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
                boolean isDatabase = false;
                TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
                for (Database database : ConnectionManager.sendGetDropdown()) {
                    if (database.getName().equals(item.getValue()))
                        isDatabase = true;
                }
                if (isDatabase) {
                    ConnectionManager.sendUseDatabase(item.getValue());
                    return;
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

            dynamicPane.getChildren().add(pane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
