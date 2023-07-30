package dental.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * The class for working with the database by SQL queries. All methods are statics.
 */
public class DBManager {
    private DBManager() {
    }

    private static final Properties PROP = new Properties();

    /**
     * Generate the {@link Connection} object.
     *
     * @return The {@link Connection} object with the {@linkplain DBConfig#DB_URL URL}, {@linkplain DBManager#PROP user and password}.
     * @throws SQLException If something goes wrong.
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBConfig.getProp(DBConfig.DB_URL),
                DBConfig.getProp(DBConfig.DB_LOGIN),
                DBConfig.getProp(DBConfig.DB_PASSWORD));
    }

    public static void createTable(String query) {
        try (Statement state = getConnection().createStatement()) {
            state.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public static final String TABLE_RECORD_ITEM =
            "CREATE TABLE `mydb`.`recorditem` (\n" +
            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `patient` VARCHAR(25) NOT NULL DEFAULT 'noname',\n" +
            "  `clinic` VARCHAR(25) NOT NULL DEFAULT 'noclinic',\n" +
            "  `works` INT NOT NULL DEFAULT 0,\n" +
            "  `accepted` DATETIME NULL,\n" +
            "  `completed` DATETIME NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE)\n" +
            "ENGINE = InnoDB\n" +
            "DEFAULT CHARACTER SET = utf32;";
    
    public static final String TABLE_WORK =
            "CREATE TABLE IF NOT EXISTS mydb.work (\n" +
            "  `title` VARCHAR(15) NOT NULL,\n" +
            "  `quantity` INT NULL DEFAULT 0,\n" +
            "  `price` INT NOT NULL,\n" +
            "  `recordItem` INT NOT NULL,\n" +
            "  UNIQUE INDEX `title_UNIQUE` (`title` ASC) VISIBLE,\n" +
            "  PRIMARY KEY (`recordItem`));";

}
