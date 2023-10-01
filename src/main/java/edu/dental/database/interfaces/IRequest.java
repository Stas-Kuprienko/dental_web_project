package edu.dental.database.interfaces;

import edu.dental.domain.entities.IDHaving;

import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Statement;

public interface IRequest extends AutoCloseable {

    boolean setID(IDHaving object) throws SQLException;

    Blob createBlob() throws SQLException;

    Statement getStatement();

    @Override
    void close() ;
}
