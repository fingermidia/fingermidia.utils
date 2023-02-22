
package com.fingermidia.dao.fw;

import com.fingermidia.dao.DataQuery;
import com.fingermidia.dao.ResultSetData;
import com.fingermidia.utils.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author dirceu
 */
public class DAOBase {

    private static final List<Object> ALLOW_NULLABLE = Arrays.asList(Timestamp.class, Integer.class, Double.class, Float.class);

    public void insert(Connection c, TOBase t) throws SQLException, Exception {

        boolean temAnterior = false;

        String tabela = Helper.obterNomeTabela(t);
        List<Field> colunas = Helper.obterColunas(t);
        List<Object> valores = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(tabela);
        sql.append("(");

        StringBuilder parm = new StringBuilder();
        parm.append(") values (");

        for (Field f : colunas) {
            Object o = Helper.runGetter(f, t);

            if ((o != null || ALLOW_NULLABLE.contains(f.getType())) && !f.getAnnotation(Column.class).jsonData()) {
                if (temAnterior) {
                    sql.append(", ");
                    parm.append(", ");
                } else {
                    temAnterior = true;
                }
                valores.add(o);
                sql.append(f.getAnnotation(Column.class).name());
                parm.append("?");
            } else if (f.getAnnotation(Column.class).jsonData()) {
                if (temAnterior) {
                    sql.append(", ");
                    parm.append(", ");
                } else {
                    temAnterior = true;
                }

                valores.add(o);
                sql.append(f.getAnnotation(Column.class).name());
                parm.append("?::json");
            }
        }

        parm.append(")");

        sql.append(parm);

        DataQuery.executeUpdate(c, sql.toString(), valores);
    }

    public int update(Connection c, TOBase t) throws SQLException, Exception {

        boolean temAnterior = false;

        String tabela = Helper.obterNomeTabela(t);
        List<Field> colunasSemChave = Helper.obterColunasSemChave(t);
        List<Field> colunasChave = Helper.obterColunasChave(t);
        List<Object> valores = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(tabela).append(" set ");

        for (Field f : colunasSemChave) {
            Object o = Helper.runGetter(f, t);

            if ((o != null || ALLOW_NULLABLE.contains(f.getType())) && !f.getAnnotation(Column.class).jsonData()) {
                if (temAnterior) {
                    sql.append(", ");
                } else {
                    temAnterior = true;
                }
                valores.add(o);
                sql.append(f.getAnnotation(Column.class).name());
                sql.append(" = ?");

            } else if (f.getAnnotation(Column.class).jsonData()) {
                if (temAnterior) {
                    sql.append(", ");
                } else {
                    temAnterior = true;
                }
                valores.add(o);
                sql.append(f.getAnnotation(Column.class).name());
                sql.append(" = ?::json");
            }
        }

        sql.append(DataQuery.WHERE);

        temAnterior = false;

        for (Field f : colunasChave) {
            Object o = Helper.runGetter(f, t);

            if (o != null) {
                if (temAnterior) {
                    sql.append(DataQuery.AND);
                } else {
                    temAnterior = true;
                }
                valores.add(o);
                sql.append(f.getAnnotation(Column.class).name());
                sql.append(" = ?");
            }
        }

        return DataQuery.executeUpdate(c, sql.toString(), valores);
    }

    public int delete(Connection c, TOBase t) throws SQLException, Exception {

        boolean temAnterior = false;

        String tabela = Helper.obterNomeTabela(t);
        List<Field> colunasChave = Helper.obterColunasChave(t);
        List<Object> valores = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("delete from ").append(tabela);

        sql.append(DataQuery.WHERE);

        for (Field f : colunasChave) {
            Object o = Helper.runGetter(f, t);

            if (o != null) {
                if (temAnterior) {
                    sql.append(DataQuery.AND);
                } else {
                    temAnterior = true;
                }
                valores.add(o);
                sql.append(f.getAnnotation(Column.class).name());
                sql.append(" = ?");
            }
        }

        return DataQuery.executeUpdate(c, sql.toString(), valores);
    }

    public JSONObject obtemJSONResultSet(ResultSetData rs, List<Field> colunas) throws Exception {
        JSONObject j = new JSONObject();
        for (Field f : colunas) {

            if (f.getAnnotation(Column.class).showJSON()) {
                String coluna = f.getAnnotation(Column.class).name();
                String nomeJson = f.getAnnotation(Column.class).jsonName();

                if (f.getAnnotation(Column.class).dateTime()) {
                    Timestamp ts = rs.getTimestamp(coluna);
                    DateTime d = new DateTime(ts);
                    j.put(nomeJson, d.toString(f.getAnnotation(Column.class).dateTimeFormat()));
                } else if (f.getType() == String.class) {
                    Object o = rs.getObject(coluna);
                    if (o != null) {
                        String v = (String) o;
                        j.put(nomeJson, v.trim());
                    }
                } else {
                    j.put(nomeJson, rs.getObject(coluna));
                }
            }

        }
        return j;
    }

    public JSONArray list(Connection c, TOBase t) throws Exception {

        boolean temAnterior = false;
        List<Object> valores = new ArrayList<>();

        String tabela = Helper.obterNomeTabela(t);

        List<Field> colunas = Helper.obterColunas(t);
        List<Field> colunasOrdenacao = Helper.obterColunasOrdenacaoPrincipal(t);

        StringBuilder sql = new StringBuilder();
        sql.append(DataQuery.SELECT);

        for (Field f : colunas) {
            if (temAnterior) {
                sql.append(", ");
            } else {
                temAnterior = true;
            }
            sql.append(f.getAnnotation(Column.class).name());
        }

        sql.append(DataQuery.FROM).append(tabela);

        sql.append(" order by ");

        temAnterior = false;

        if (colunasOrdenacao.size() > 0) {
            for (Field f : colunasOrdenacao) {
                if (temAnterior) {
                    sql.append(", ");
                } else {
                    temAnterior = true;
                }
                sql.append(f.getAnnotation(Column.class).name());
            }
        } else {
            sql.append(" 1 ");
        }

        //System.out.println(sql.toString());
        JSONArray ja = new JSONArray();

        try (ResultSetData rs = new ResultSetData(c, sql.toString(), valores)) {
            while (rs.next()) {

                JSONObject j = obtemJSONResultSet(rs, colunas);

                ja.put(j);

            }
        }

        return ja;
    }

    public JSONObject list(Connection c, TOBase t, String busca, int reg, int pag) throws Exception {
        JSONObject jo = new JSONObject();

        boolean temAnterior = false;

        String tabela = Helper.obterNomeTabela(t);

        List<Field> colunas = Helper.obterColunas(t);
        List<Field> colunasBusca = Helper.obterColunasBusca(t);
        List<Field> colunasOrdenacao = Helper.obterColunasOrdenacaoPrincipal(t);
        List<Object> valores = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append(DataQuery.SELECT);

        for (Field f : colunas) {
            if (temAnterior) {
                sql.append(", ");
            } else {
                temAnterior = true;
            }
            sql.append(f.getAnnotation(Column.class).name());
        }

        sql.append(DataQuery.FROM).append(tabela);

        temAnterior = false;

        if (colunasBusca.size() > 0) {
            sql.append(" where (");
            for (Field f : colunasBusca) {
                if (temAnterior) {
                    sql.append(DataQuery.OR);
                } else {
                    temAnterior = true;
                }
                sql.append(f.getAnnotation(Column.class).name()).append(" ilike '%' || ? || '%' ");
                valores.add(busca);
            }
            sql.append(" ) ");
        }

        sql.append(" order by ");

        temAnterior = false;

        if (colunasOrdenacao.size() > 0) {
            for (Field f : colunasOrdenacao) {
                if (temAnterior) {
                    sql.append(", ");
                } else {
                    temAnterior = true;
                }
                sql.append(f.getAnnotation(Column.class).name());
            }
        } else {
            sql.append(" 1 ");
        }

        sql.append(" limit ").append(reg).append(" offset ").append(pag);

        JSONArray ja = new JSONArray();

        try (ResultSetData rs = new ResultSetData(c, sql.toString(), valores)) {
            while (rs.next()) {

                JSONObject j = obtemJSONResultSet(rs, colunas);

                ja.put(j);

            }
        }

        jo.put("list", ja);
        jo.put("total", total(c, t, busca));
        jo.put("page", (pag / reg) + 1);
        jo.put("registers", reg);

        return jo;
    }

    public int total(Connection c, TOBase t, String busca) throws Exception {

        List<Field> colunasBusca = Helper.obterColunasBusca(t);

        String tabela = Helper.obterNomeTabela(t);
        List<Object> valores = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("select count(1) as total ");
        sql.append(DataQuery.FROM).append(tabela);

        boolean temAnterior = false;

        if (colunasBusca.size() > 0) {
            sql.append(" where (");
            for (Field f : colunasBusca) {
                if (temAnterior) {
                    sql.append(DataQuery.OR);
                } else {
                    temAnterior = true;
                }
                sql.append(f.getAnnotation(Column.class).name()).append(" ilike '%' || ? || '%' ");
                valores.add(busca);
            }
            sql.append(" ) ");
        }

        try (ResultSetData rs = new ResultSetData(c, sql.toString(), valores)) {

            if (rs.next()) {
                return rs.getInt("total");
            } else {
                return 0;
            }
        }
    }

    public TOBase get(Connection c, TOBase t) throws Exception {

        boolean temAnterior = false;

        String tabela = Helper.obterNomeTabela(t);

        List<Field> colunas = Helper.obterColunas(t);
        List<Field> colunasChave = Helper.obterColunasChave(t);
        List<Object> valores = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append(DataQuery.SELECT);

        for (Field f : colunas) {
            if (temAnterior) {
                sql.append(", ");
            } else {
                temAnterior = true;
            }
            sql.append(f.getAnnotation(Column.class).name());

        }

        sql.append(DataQuery.FROM).append(tabela);
        sql.append(DataQuery.WHERE);

        temAnterior = false;

        for (Field f : colunasChave) {
            Object o = Helper.runGetter(f, t);

            if (o != null) {
                if (temAnterior) {
                    sql.append(DataQuery.AND);
                } else {
                    temAnterior = true;
                }
                valores.add(o);
                sql.append(f.getAnnotation(Column.class).name());
                sql.append(" = ?");
            }
        }

        try (ResultSetData rs = new ResultSetData(c, sql.toString(), valores)) {
            if (rs.next()) {

                for (Field f : colunas) {
                    String coluna = f.getAnnotation(Column.class).name();
                    Helper.runSetter(coluna, t, rs.getObject(coluna));
                }

                return t;

            }

        }

        return null;
    }
}