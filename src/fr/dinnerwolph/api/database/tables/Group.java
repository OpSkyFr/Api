package fr.dinnerwolph.api.database.tables;

import fr.dinnerwolph.api.Api;
import fr.dinnerwolph.api.database.BasicSQLDatabase;
import fr.dinnerwolph.api.groups.Groups;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Dinnerwolph
 */

public class Group {

    private BasicSQLDatabase sqlDatabase = new BasicSQLDatabase(Api.getData());

    public Group() {
        getAllGroups();
    }

    public void getAllGroups() {
        sqlDatabase.execute(connection -> {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM `Group`");
            ResultSet resultSet = statement.executeQuery();
            int i = 999;
            while (resultSet.next()) {
                Groups groups = new Groups(resultSet.getInt("groupId"), resultSet.getString("GroupName"), resultSet.getString("suffix"), resultSet.getString("prefix"), i--);
                Api.getInstance().addGroups(groups);
            }
        });
    }
}
