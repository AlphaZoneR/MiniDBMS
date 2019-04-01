package client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class TableTreeItem extends AbstractTreeItem{
    private String database;
    private String table;

    public TableTreeItem(String database, String table) {
        this.database = database;
        this.table = table;
        this.setValue(table);
    }

    @Override
    public ContextMenu getMenu(){
        MenuItem delete = new MenuItem("DELETE");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ConnectionManager.sendUseDatabase(database);
                ConnectionManager.sendDropTable(database, table);
            }
        });
        return new ContextMenu(delete);
    }
}
