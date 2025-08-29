package com.bfh.javaqualifier;

public class SqlSolver {

    // Default SQL answer (you can customize this)
    public static String getAnswer() {
        return """
               SELECT e.name, d.department_name
               FROM employees e
               JOIN departments d ON e.department_id = d.id
               WHERE e.salary > 50000
               ORDER BY e.name ASC;
               """;
    }

    // Method expected by Application.java
    public String generateFinalQuery(String regNo) {
        // You can customize this logic using regNo if required
        // For now just return the default SQL
        return getAnswer();
    }
} 
