package client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

public class DatabaseTreeItem extends AbstractTreeItem{

    private String database;

    public DatabaseTreeItem(String database) {
        this.database = database;
        this.setValue(database);
    }

    @Override
    public ContextMenu getMenu(){
        MenuItem delete = new MenuItem("DELETE DATABASE");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ConnectionManager.sendUseDatabase(database);
                ConnectionManager.sendDropDatabase(database);
                Client.controller.loadTreeItems();
            }
        });
        return new ContextMenu(delete);
    }
}


