package com.example.csv_to_sql_scripts_converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class CsvToSqlScriptsConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(CsvToSqlScriptsConverterApplication.class, args);

        String csvFilePath = "data.csv"; // Path to your CSV file
        String sqlFilePath = "output.sql"; // Output SQL file
        String tableName = "users"; // Your table name
        String idColumn = "id"; // The column used to identify the record

        // Read CSV file
        List<String[]> records = CsvReader.readCSV(csvFilePath);

        // Write SQL file
        SQLFileWriter.writeSQLFile(sqlFilePath, records, tableName, idColumn);

        System.out.println("SQL file generated successfully!");

    }

}
