package client;
import core.Database;
import core.Field;
import core.Table;

import java.util.ArrayList;

public class Test {
        public static void main(String[] args) {
            ConnectionManager.sendCreateDatabase("test1");
            ConnectionManager.sendUseDatabase("test1");
            Field field = new Field("field1", "int");
            ArrayList<Field> fields = new ArrayList<>();
            fields.add(field);
            Table table = new Table("table1", fields);
            ConnectionManager.sendCreateTable("test1", table);
        }
}
