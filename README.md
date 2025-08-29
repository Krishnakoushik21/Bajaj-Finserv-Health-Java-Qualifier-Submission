# Bajaj Finserv Health | Qualifier 1 | JAVA — Spring Boot Auto Runner

This Spring Boot app performs the entire workflow **on startup** (no controllers):
1. **POST** `generateWebhook/JAVA` with your details.
2. Receives `webhook` and `accessToken`.
3. Picks the SQL **based on regNo last two digits** parity:
   - **Odd** → *SQL Question 1* (already implemented).
   - **Even** → *SQL Question 2* (TODO placeholder; paste the correct SQL).
4. **POST** the solution to the returned `webhook` URL using the JWT token in the `Authorization` header.

## Quick Start

### 1) Edit applicant details
In `src/main/resources/application.properties` update:
```
app.name=Your Name
app.regNo=REG12347
app.email=you@example.com
```

### 2) Build
```
mvn -q -e -DskipTests package
```

### 3) Run
```
java -jar target/java-qualifier-0.0.1-SNAPSHOT.jar
```

> Logs will show the token is used (masked) and where your SQL was submitted.

## Notes
- Spec shows header `Authorization: <accessToken>` (no `Bearer`). This implementation follows that.
- If the `webhook` is absent, the client falls back to the documented test URL:
  `https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA`

## Where is the SQL?
`SqlSolver.java` returns the final SQL. For **odd** regNo last two digits, the SQL for **Question 1** is:

```sql
SELECT 
    p.AMOUNT AS SALARY,
    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
    TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,
    d.DEPARTMENT_NAME
FROM PAYMENTS p
JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
WHERE DAY(p.PAYMENT_TIME) <> 1
ORDER BY p.AMOUNT DESC
LIMIT 1;
```

For **even** regNo last two digits, replace the TODO with the actual **Question 2** SQL.

## Submission Checklist (as per question)
- Public GitHub repo with:
  - Code
  - Final JAR output
  - **Raw** downloadable link to the JAR
- Public JAR file link (downloadable)

## Tech
- Java 17
- Spring Boot 3.3.x
- `RestTemplate`
- Runs automatically via `CommandLineRunner`
```

