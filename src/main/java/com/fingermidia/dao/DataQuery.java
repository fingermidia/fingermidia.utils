package com.fingermidia.dao;

import com.fingermidia.utils.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataQuery {
    public static final String SELECT = " select ";
    public static final String FROM = " from ";
    public static final String WHERE = " where ";
    public static final String AND = " and ";
    public static final String OR = " or ";

    private static final boolean LOG = false;

    public static Connection openConnectionPostgre(String server, String database, String user, String password) throws Exception {
        long inicio = System.currentTimeMillis();
        Connection conn = null;
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://" + server + ":5432/" + database, user, password);
        long fim = System.currentTimeMillis();
        long tempo = fim - inicio;
        if (LOG) {
            System.out.println("Tempo Abrir Conexão: " + tempo);
        }
        return conn;
    }

    public static Connection openConnectionMysql(String server, String database, String user, String password) throws Exception {
        long inicio = System.currentTimeMillis();
        Connection conn = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://" + server + ":3306/" + database, user, password);
        long fim = System.currentTimeMillis();
        long tempo = fim - inicio;
        if (LOG) {
            System.out.println("Tempo Abrir Conexão: " + tempo);
        }
        return conn;
    }

    public static int executeUpdate(Connection conn, StringBuilder query, Object... parametros) throws SQLException {
        return executeUpdate(conn, query.toString(), parametros);
    }

    public static int executeUpdate(Connection conn, String query, Object... parametros) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Recebe os parâmetros da Query
            for (int i = 1; i <= parametros.length; i++) {
                pstmt.setObject(i, parametros[i - 1]);
            }
            return pstmt.executeUpdate();
        }
    }

    public static int executeUpdate(Connection conn, StringBuilder query, Object p) throws SQLException {
        return executeUpdate(conn, query.toString(), p);
    }

    public static int executeUpdate(Connection conn, String query, Object p) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setObject(1, p);
            return pstmt.executeUpdate();
        }
    }

    public static int executeUpdate(Connection conn, StringBuilder query, List<Object> p) throws SQLException {
        return executeUpdate(conn, query.toString(), p);
    }

    public static int executeUpdate(Connection conn, String query, List<Object> p) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            int i = 1;
            for (Object o : p) {
                pstmt.setObject(i++, o);
            }
            return pstmt.executeUpdate();
        }
    }

    public static int executeUpdate(Connection conn, StringBuilder query) throws SQLException {
        return executeUpdate(conn, query.toString());
    }

    public static int executeUpdate(Connection conn, String query) throws SQLException {
        try (Statement stm = conn.createStatement()) {
            return stm.executeUpdate(query);
        }
    }

    public static Object retiraInject(Object o) {
        if (o != null && o.getClass().getCanonicalName().contains("String")) {
            String s = (String) o;
            o = s.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        }
        return o;
    }

    public static void closeResultSet(ResultSet rs) throws Exception {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                throw new Exception("Error closing ResultSet : " + e.getMessage());
            }
        }
    }

    public static List<HashMap<String, Object>> convertResultSetToList(ResultSetData rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<HashMap<String, Object>> list = new ArrayList<>();
        while (rs.next()) {
            HashMap<String, Object> row = new HashMap<>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(row);
        }
        return list;
    }

    public static JSONArray convertResultSetToJsonArray(ResultSetData rs) throws SQLException {
        JSONArray ja = new JSONArray();
        while (rs.next()) {
            ja.put(convertResultSetToJsonObjectWithoutNext(rs));
        }
        return ja;
    }

    public static JSONObject convertResultSetToJsonObject(ResultSetData rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        if (rs.next()) {
            return convertResultSetToJsonObjectWithoutNext(md, rs);
        } else {
            return null;
        }
    }

    public static JSONObject convertResultSetToJsonObjectWithoutNext(ResultSetData rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        return convertResultSetToJsonObjectWithoutNext(md, rs);
    }

    public static JSONObject convertResultSetToJsonObjectWithoutNext(ResultSetMetaData md, ResultSetData rs) throws SQLException {
        JSONObject jo = new JSONObject();
        for (int i = 1; i <= md.getColumnCount(); ++i) {
            Object o = rs.getObject(i);
            if (o.getClass() == Timestamp.class) {
                DateTime d = new DateTime((Timestamp) o);
                jo.put(md.getColumnName(i), d.toString("dd/MM/yyyy HH:mm:ss"));
            } else {
                jo.put(md.getColumnName(i), o);
            }
        }
        return jo;
    }

}
