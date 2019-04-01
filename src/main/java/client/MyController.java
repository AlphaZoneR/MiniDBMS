package client;

import core.Database;
import core.Table;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

public class MyController {

    @FXML
    private TreeView<String> treeView;

    @FXML
    private TextField createDbName;

    @FXML
    private Button createDbButton;

    public void initialize() {
        loadTreeItems();

        loadCreateButton();
    }

    public void loadTreeItems() {
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
        treeView.setCellFactory(new Callback<TreeView<String>,TreeCell<String>>(){
            @Override
            public TreeCell<String> call(TreeView<String> p) {
                return new MyTreeCell();
            }
        });
    }

    public void loadCreateButton() {
        createDbButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String databaseName = createDbName.getText();
                ConnectionManager.sendCreateDatabase(databaseName);
                loadTreeItems();
                createDbName.setText("");
            }
        });
    }

}
