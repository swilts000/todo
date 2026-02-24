# Todo App - Database Connection Troubleshooting Guide

## What Changed

We've made the following improvements to help debug and fix database connection issues:

### 1. **Enhanced Backend Error Handling**
   - Added comprehensive error logging with SLF4J
   - All API endpoints now return proper HTTP status codes (201 for created, 500 for errors)
   - Error responses include detailed error messages
   - Database operations are now logged (POST, GET, DELETE, PUT)

### 2. **Improved Frontend Feedback**
   - Enhanced error messages from backend are now displayed in the notification area
   - Shows success messages with saved Todo ID
   - Clear indication when todos are added/deleted/loaded

### 3. **Better Database Configuration**
   - Created `application-local.properties` for local development
   - Enhanced connection pooling configuration (HikariCP)
   - Better timeout and connection settings

### 4. **Startup Diagnostics**
   - Application now logs connection details on startup
   - Shows database URL, username, and port

---

## How to Run with Docker

### Prerequisites
- Docker and Docker Compose installed

### Steps
```bash
cd C:\Users\xwilt\OneDrive\Desktop\Kubernetes__Project\todo
docker-compose up --build
```

Then:
- **Frontend**: http://localhost:8081
- **Backend**: http://localhost:8080/api/todos
- **MySQL**: localhost:3306

### Database Connection Details
- Host: `mysql` (inside Docker network)
- Port: `3306`
- Database: `tododb`
- Username: `root`
- Password: `password`

---

## How to Run Locally (For Development)

### Prerequisites
- Java 17+
- Maven
- MySQL Server running on localhost:3306

### Step 1: Create the Database
```sql
CREATE DATABASE tododb;
```

### Step 2: Update Database Connection (Optional)
The `application-local.properties` file is already configured to connect to localhost:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tododb
spring.datasource.username=root
spring.datasource.password=password
```

### Step 3: Run the Backend
```bash
cd Backend
mvn clean package
java -Dspring.profiles.active=local -jar target/todo-0.0.1-SNAPSHOT.jar
```

Or directly:
```bash
cd Backend
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=local"
```

The backend should start on http://localhost:8080

### Step 4: Run the Frontend
```bash
# Option 1: Using Python (if installed)
cd Frontend
python -m http.server 8081

# Option 2: Using Node.js
cd Frontend
npx http-server -p 8081

# Option 3: Using any other HTTP server
```

Frontend will be available at http://localhost:8081

---

## Verifying the Setup

### 1. Check Backend API
Open a browser or use curl:
```bash
curl http://localhost:8080/api/todos
```

Expected response: `[]` (empty array) or list of todos

### 2. Check Application Logs
Look for this startup message in the console:
```
================================================================================
Todo Application Started Successfully!
Database URL: jdbc:mysql://mysql:3306/tododb (or localhost if local)
Database Username: root
Application Profile(s): 
Server Port: 8080
================================================================================
```

### 3. Test Adding a Todo
In the frontend:
1. Type a todo in the input field
2. Click "Add"
3. You should see:
   - Green notification: "✓ Todo added successfully! (ID: X)"
   - Todo appears in the list

### 4. Check Database Directly
Connect to MySQL and run:
```sql
USE tododb;
SELECT * FROM todo;
```

---

## Common Issues and Solutions

### Issue 1: "Failed to fetch todos" on Frontend

**Possible Causes:**
- Backend not running
- Database connection failed
- CORS issue

**Solutions:**
1. Check backend is running: `curl http://localhost:8080/api/todos`
2. Check logs for database connection errors
3. Verify MySQL is running and accessible

### Issue 2: "HTTP Error 500" when adding a todo

**Possible Causes:**
- Database connection failure
- SQL error

**Solutions:**
1. Check backend console for detailed error message
2. Verify database credentials in application.properties
3. Ensure `tododb` database exists
4. Check MySQL is running

### Issue 3: Todos added but not visible in database

**Possible Causes:**
- Data being saved to H2 database instead of MySQL
- Transaction not committed

**Solutions:**
1. Check logs show "jdbc:mysql" URL (not H2)
2. Run: `SELECT * FROM todo;` in MySQL
3. Restart the application

### Issue 4: Can't connect to MySQL in Docker

**Possible Causes:**
- MySQL container not started
- Network issue

**Solutions:**
1. Check container is running: `docker ps`
2. Check logs: `docker logs <container_name>`
3. Wait for healthcheck: `docker-compose logs mysql`

---

## Logging Configuration

### View Detailed Logs
To see SQL queries being executed:
```
logging.level.full_k8s_project=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
```

This shows:
- Every SQL query
- Parameter bindings
- Results

### Log Levels
- **TRACE**: Very detailed SQL binding information
- **DEBUG**: Application-level debug logs
- **INFO**: General information
- **ERROR**: Errors only

---

## Next Steps

1. **Run the application** with Docker or locally
2. **Monitor the logs** during startup to verify database connection
3. **Add a todo** and verify it appears in the list
4. **Check the database** directly to confirm data is saved
5. **Monitor the browser console** (F12) for any JavaScript errors

If you encounter any issues, the enhanced logging will provide detailed information about what's happening.

