package edu.dental.database.dao;

import edu.dental.database.DatabaseException;
import edu.dental.entities.IDHaving;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface DAO {

    interface DatabaseRequest extends AutoCloseable {

        boolean setID(IDHaving object) throws SQLException;

        Blob createBlob() throws SQLException;

        Statement getPreparedStatement();

        @Override
        void close();
    }

    interface Instantiation<T> {

        List<T> build() throws SQLException, IOException, DatabaseException;

    }
}
