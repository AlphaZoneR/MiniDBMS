package client;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class DatabaseTreeItem extends AbstractTreeItem {

    private String database;

    DatabaseTreeItem(String database) {
        this.database = database;
        this.setValue(database);
    }

    @Override
    public ContextMenu getMenu() {
        MenuItem delete = new MenuItem("DELETE DATABASE");
        delete.setOnAction(event -> {
            ConnectionManager.sendUseDatabase(database);
            ConnectionManager.sendDropDatabase(database);
            Client.controller.loadTreeItems();

        });
        MenuItem create = new MenuItem("CREATE TABLE");
        create.setOnAction(event -> {
            ConnectionManager.sendUseDatabase(database);
            Client.controller.loadCreateTable(database);
        });

        MenuItem select = new MenuItem("SELECT");
        select.setOnAction(event -> {
            ConnectionManager.sendUseDatabase(database);
            Client.controller.loadSelect(database);
        });

        return new ContextMenu(delete, create, select);
    }
}


