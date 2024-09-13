package com.example.csv_to_sql_scripts_converter;

public class SQLGenerator {

    public static String generateInsert(String tableName, String[] headers, String[] values) {
        StringBuilder query = new StringBuilder("INSERT INTO ");
        query.append(tableName).append(" (");

        for (int i = 0; i < headers.length; i++) {
            query.append(headers[i]);
            if (i < headers.length - 1) query.append(", ");
        }

        query.append(") VALUES (");

        for (int i = 0; i < values.length; i++) {
            query.append("'").append(values[i]).append("'");
            if (i < values.length - 1) query.append(", ");
        }

        query.append(");");
        return query.toString();
    }

    public static String generateUpdate(String tableName, String[] headers, String[] values, String idColumn, String idValue) {
        StringBuilder query = new StringBuilder("UPDATE ");
        query.append(tableName).append(" SET ");

        for (int i = 0; i < headers.length; i++) {
            query.append(headers[i]).append(" = '").append(values[i]).append("'");
            if (i < headers.length - 1) query.append(", ");
        }

        query.append(" WHERE ").append(idColumn).append(" = '").append(idValue).append("';");
        return query.toString();
    }

    /**
     *
     * @param tableName name of de table that build script.
     * @param idColumn name of identification.
     * @param idValue value of the identification colum.
     * @return
     */
    public static String generateDelete(String tableName, String idColumn, String idValue) {
        return "DELETE FROM " + tableName + " WHERE " + idColumn + " = '" + idValue + "';";
    }
}

