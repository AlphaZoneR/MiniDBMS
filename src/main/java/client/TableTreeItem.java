package client;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class TableTreeItem extends AbstractTreeItem{

    private String database;

    private String table;

    TableTreeItem(String database, String table) {
        this.database = database;
        this.table = table;
        this.setValue(table);
    }

    @Override
    public ContextMenu getMenu(){
        MenuItem delete = new MenuItem("DELETE");
        delete.setOnAction(event -> {
                ConnectionManager.sendUseDatabase(database);
                ConnectionManager.sendDropTable(database, table);
                Client.controller.loadTreeItems();

        });
        return new ContextMenu(delete);
    }
}
