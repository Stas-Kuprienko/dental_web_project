package edu.dental.database.dao;

import edu.dental.database.DatabaseException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface Instantiation<T> {

    List<T> build() throws SQLException, IOException, DatabaseException;

}
