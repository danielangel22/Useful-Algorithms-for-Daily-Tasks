package com.example.springboot.util;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class JsonToJavaClassGenerator {


    public static void main(String[] args) {
        try {
            String json = FileUtils.readFileToString(new File("input.json"), "UTF-8");

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);
            printGreen("Start generation classes.");
            generateJavaClasses(rootNode, "com.example.springboot.dto", "RootClassRequest");//replace for your package and your entity master
            printGreen("Done.");
        } catch (IOException e) {
            System.err.println("Error in the process = " + e.getLocalizedMessage());
        }
    }

    public static void generateJavaClasses(JsonNode node, String packageName, String rootClassName) {
        if (node.isObject()) {
            generateClass((ObjectNode) node, packageName, rootClassName);
        } else if (node.isArray()) {
            generateClassesFromList((ArrayNode) node, packageName, rootClassName);
        }
    }

    private static void generateClass(ObjectNode node, String packageName, String className) {
        StringBuilder classContent = new StringBuilder();
        classContent.append("package ").append(packageName).append(";\n\n");

        StringBuilder importStatements = new StringBuilder();
        StringBuilder fields = new StringBuilder();
        StringBuilder getterSetter = new StringBuilder();

        Iterator<String> fieldNames = node.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            JsonNode fieldNode = node.get(fieldName);
            String fieldType;
            if (fieldNode.isObject()) {
                String subClassName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                printGreen("Generating sub classes.");
                generateClass((ObjectNode) fieldNode, packageName, subClassName);
                importStatements.append("import ").append(packageName).append(".").append(subClassName).append(";\n");
                fieldType = subClassName;
            } else {
                fieldType = getFieldType(fieldNode);
            }
            fields.append("\tprivate ").append(fieldType).append(" ").append(fieldName).append(";\n");
            String capitalizedFieldName = fieldName.substring(0, 1).toUpperCase().concat(fieldName.substring(1));
            getterSetter.append("\tpublic ").append(fieldType).append(" get").append(capitalizedFieldName).append("() {\n")
                    .append("\t\treturn ").append(fieldName).append(";\n")
                    .append("\t}\n\n")
                    .append("\tpublic void set").append(capitalizedFieldName).append("(").append(fieldType).append(" ").append(fieldName).append(") {\n")
                    .append("\t\tthis.").append(fieldName).append(" = ").append(fieldName).append(";\n")
                    .append("\t}\n\n");
        }

        classContent.append(importStatements);
        classContent.append("\n\n");
        classContent.append("public class ").append(className).append(" {\n\n");
        classContent.append(fields);
        classContent.append(getterSetter);
        classContent.append("\n}");

        try {
            String filePath = "src/main/java/" + packageName.replace(".", "/");
            FileUtils.forceMkdir(new File(filePath));
            FileUtils.writeStringToFile(new File(filePath + "/" + className + ".java"), classContent.toString(), "UTF-8");
        } catch (IOException e) {
            System.err.println("Error writing the class = " + className);
        }
    }

    private static String getFieldType(JsonNode fieldNode) {

        if (fieldNode.isTextual()) {
            return "String";
        } else if (fieldNode.isBoolean()) {
            return "boolean";
        } else if (fieldNode.isDouble() || fieldNode.isFloat()) {
            return "double";
        } else if (fieldNode.isInt() || fieldNode.isLong()) {
            return "int";
        } else if (fieldNode.isArray()) {
            return "List<" + getFieldType(fieldNode.get(0)) + ">";
        } else {
            return "Object";
        }
    }

    private static void generateClassesFromList(ArrayNode arrayNode, String packageName, String rootClassName) {
        if (!arrayNode.isEmpty()) {
            generateJavaClasses(arrayNode.get(0), packageName, rootClassName);
        }
    }

    private static void printGreen(String message) {
        System.out.println("\u001B[32m " + message + "\u001B[0m");
    }
}
