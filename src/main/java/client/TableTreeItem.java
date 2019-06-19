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
            try {
                ConnectionManager.sendUseDatabase(database);
                ConnectionManager.sendDropTable(table);
            } catch (RuntimeException e) {
                Client.controller.setResponse(e.getMessage());
            }
            Client.controller.loadTreeItems();

        });

        MenuItem view = new MenuItem("VIEW");
        view.setOnAction(event -> Client.controller.loadTableView(database, table));

        MenuItem index = new MenuItem("INDEX");
        index.setOnAction(event -> Client.controller.loadIndex(database, table));

        return new ContextMenu(delete, view, index);
    }
}
