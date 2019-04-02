package api;

import core.Entry;
import core.Table;

public class GeneralManager {
    public static GeneralManager manager = new GeneralManager();

    public void insert(Entry entry) throws RuntimeException {
        if (JSONManager.manager.isTable(RedisManager.manager.getCurrentDatabase(), entry.getTable())) {
            Table table = new Table(JSONManager.manager.getTable(RedisManager.manager.getCurrentDatabase(), entry.getTable()));

            if (!table.hasOnePrimaryField()) {
                throw new RuntimeException("Cannot insert into table [" + entry.getTable() + "] because it has none or more than one primary key!");
            }

            String primaryFieldName = table.getPrimaryFieldName();

            if (!entry.has(primaryFieldName) && !table.isPrimaryKeyIdentity()) {
                throw new RuntimeException("Cannot insert into table [" + entry.getTable() + "] because entry is missing (primary key, value) and primary key is not identity!");
            }

            if (table.isPrimaryKeyIdentity() && !entry.has(primaryFieldName)) {
                int currentRowCount = JSONManager.manager.getRowCount(RedisManager.manager.getCurrentDatabase(), table.getName());
                entry.add(primaryFieldName, String.format("%d", currentRowCount));
                try {
                    RedisManager.manager.insert(entry);
                    JSONManager.manager.incRowCount(RedisManager.manager.getCurrentDatabase(), table.getName());
                } catch (RuntimeException e) {
                        throw e;
                }
            } else {
                try{
                    RedisManager.manager.insert(entry);
                    JSONManager.manager.incRowCount(RedisManager.manager.getCurrentDatabase(), table.getName());
                } catch (RuntimeException e) {
                     throw e;
                }
            }
        } else {
            throw new RuntimeException("Cannot insert into table [" + entry.getTable() + "] because table does not exist in the current database!");
        }
    }

    public void deleteAll(String table) throws RuntimeException {
        try {
            RedisManager.manager.deleteAll(table);
            JSONManager.manager.nullifyRowCount(RedisManager.manager.getCurrentDatabase(), table);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public void deleteWherePrimaryKeyIs(String table, String value) {
        try {
            RedisManager.manager.deleteWherePrimaryKeyIs(table, value);
//            JSONManager.manager.decRowCount(RedisManager.manager.getCurrentDatabase(), table);
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
