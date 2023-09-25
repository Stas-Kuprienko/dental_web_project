package edu.dental.database.dao;

import edu.dental.domain.entities.IDHaving;

import java.sql.Blob;
import java.sql.SQLException;
import java.sql.Statement;

public interface IRequest extends AutoCloseable {

    boolean setID(IDHaving object, Statement statement) throws SQLException;

    Blob createBlob() throws SQLException;

    Statement getStatement();

    @Override
    void close() ;
}
