package com.example.csv_to_sql_scripts_converter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Class of build sql file
 */
public class SQLFileWriter {

    public static void writeSQLFile(String filePathResult, List<String[]> records, String tableName, String idColumn) {
        File directory = new File("src\\main\\resources");
        if (!directory.exists()) {
            boolean dirCreated = directory.mkdirs(); // Create directory if not exists
            if (!dirCreated) {
                return;
            }
        }

        // Create a complete route for the file
        String filePath = directory.getPath() + File.separator + filePathResult;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath)))) {
            String[] headers = records.get(0); // First row is headers (columns of table)

            for (int i = 1; i < records.size(); i++) {
                String[] values = records.get(i);
                String idValue = values[0]; // Assuming the first column is the 'id'

                // Generate INSERT query
                String insertQuery = SQLGenerator.generateInsert(tableName, headers, values);
                writer.write(insertQuery);
                writer.newLine();
                System.out.println("Generating insert..");

                // Generate UPDATE query
                String updateQuery = SQLGenerator.generateUpdate(tableName, headers, values, idColumn, idValue);
                writer.write(updateQuery);
                writer.newLine();
                System.out.println("Generating update.");

                // Generate DELETE query
                String deleteQuery = SQLGenerator.generateDelete(tableName, idColumn, idValue);
                writer.write(deleteQuery);
                writer.newLine();
                System.out.println("Generating delete");

                // Flush periodically to handle large files efficiently
                if (i % 10000 == 0) {
                    writer.flush();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

