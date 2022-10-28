package com.fingermidia.dao;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class ResultSetData implements AutoCloseable {

    private ResultSet rs;
    private PreparedStatement ps;

    public ResultSetData(Connection conn, StringBuilder query, Object... parametros) throws SQLException {
        this(conn, query.toString(), Arrays.asList(parametros));
    }

    public ResultSetData(Connection conn, StringBuilder query, Object p) throws SQLException {
        this(conn, query.toString(), Arrays.asList(p));
    }

    public ResultSetData(Connection conn, StringBuilder query, List<Object> p) throws SQLException {
        this(conn, query.toString(), p);
    }

    public ResultSetData(Connection conn, String query, Object... parametros) throws SQLException {
        this(conn, query, Arrays.asList(parametros));
    }

    public ResultSetData(Connection conn, String query, Object p) throws SQLException {
        this(conn, query, Arrays.asList(p));
    }

    public ResultSetData(Connection conn, String query, List<Object> p) throws SQLException {
        this.ps = conn.prepareStatement(query);
        int i = 1;
        for (Object o : p) {
            this.ps.setObject(i++, DataQuery.retiraInject(o));
        }
        executeQuery();
    }

    public ResultSetData(PreparedStatement ps) {
        this.ps = ps;
    }

    public ResultSet getResultSet() {
        return rs;
    }

    public PreparedStatement getPs() {
        return ps;
    }

    public void executeQuery() throws SQLException {
        this.rs = ps.executeQuery();
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        return ps.getMetaData();
    }

    public boolean next() throws SQLException {
        return rs.next();
    }

    public Integer getInt(String column) throws SQLException {
        return rs.getInt(column);
    }

    public String getString(String column) throws SQLException {
        return rs.getString(column);
    }

    public Double getDouble(String column) throws SQLException {
        return rs.getDouble(column);
    }

    public Timestamp getTimestamp(String column) throws SQLException {
        return rs.getTimestamp(column);
    }

    public Boolean getBoolean(String column) throws SQLException {
        return rs.getBoolean(column);
    }

    public Object getObject(String column) throws SQLException {
        return rs.getObject(column);
    }

    public Object getObject(int column) throws SQLException {
        return rs.getObject(column);
    }

    public Float getFloat(String column) throws SQLException {
        return rs.getFloat(column);
    }

    @Override
    public void close() throws Exception {
        rs.close();
        ps.close();
        rs = null;
        ps = null;
    }

}
