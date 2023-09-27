package edu.dental.database.mysql_api.dao;

import edu.dental.database.interfaces.DAO;
import edu.dental.domain.entities.WorkRecord;

import java.util.Collection;

public class WorkRecordMySql implements DAO<WorkRecord> {
    @Override
    public boolean put(WorkRecord object) {
        return false;
    }

    @Override
    public Collection<WorkRecord> getAll() {
        return null;
    }

    @Override
    public WorkRecord get(int id) {
        return null;
    }

    @Override
    public WorkRecord search(Object value1, Object value2) {
        return null;
    }

    @Override
    public boolean edit(WorkRecord object) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
