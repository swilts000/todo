# Quick Commands - Get Started Now! 🚀

## Option 1: Run with Docker (Easiest)

```bash
# Navigate to project
cd C:\Users\xwilt\OneDrive\Desktop\Kubernetes__Project\todo

# Start everything
docker-compose up --build
```

**Then open:**
- Frontend: http://localhost:8081
- Backend API: http://localhost:8080/api/todos

**Done!** You should see:
- No errors on startup
- Logs showing database connection to "mysql"
- Frontend loads successfully
- Can add todos with success notifications

---

## Option 2: Run Locally (Development)

### Step 1: Ensure MySQL is Running
```bash
# Windows - if you have MySQL Service installed
net start MySQL80

# Or via Docker container
docker run --name mysql-local -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=tododb -d mysql:8.0
```

### Step 2: Build Backend
```bash
cd C:\Users\xwilt\OneDrive\Desktop\Kubernetes__Project\todo\Backend
mvn clean package -DskipTests
```

### Step 3: Run Backend
```bash
# From Backend directory
java -Dspring.profiles.active=local -jar target/todo-0.0.1-SNAPSHOT.jar
```

**Expected Output:**
```
================================================================================
Todo Application Started Successfully!
Database URL: jdbc:mysql://localhost:3306/tododb
Database Username: root
================================================================================
```

### Step 4: Run Frontend (in another terminal)
```bash
cd C:\Users\xwilt\OneDrive\Desktop\Kubernetes__Project\todo\Frontend

# Option A: Python (if installed)
python -m http.server 8081

# Option B: Node.js (if installed)
npx http-server -p 8081

# Option C: Any other HTTP server on port 8081
```

**Then open:** http://localhost:8081

---

## Test Commands

### Check Backend Health
```bash
curl http://localhost:8080/api/todos
```
**Expected:** `[]` (empty list) or list of todos

### Add a Todo via API
```bash
curl -X POST http://localhost:8080/api/todos ^
  -H "Content-Type: application/json" ^
  -d "{\"text\": \"Test todo\", \"done\": false}"
```
**Expected Response (201 Created):**
```json
{
  "id": 1,
  "text": "Test todo",
  "done": false
}
```

### Check Database
```bash
# Connect to MySQL
mysql -u root -ppassword tododb

# Inside MySQL:
SELECT * FROM todo;
```

---

## Verify Success

When you add a todo in the browser, you should see:

1. **Green Notification:**
   ```
   ✓ Todo added successfully! (ID: 1)
   ```

2. **Todo in List:**
   ```
   - Test todo
   ```

3. **In Database:**
   ```sql
   mysql> SELECT * FROM todo;
   +----+-----------+------+
   | id | text      | done |
   +----+-----------+------+
   |  1 | Test todo |    0 |
   +----+-----------+------+
   ```

✅ **Success!** Data is being saved to MySQL!

---

## Troubleshooting Quick Fixes

### "Cannot GET /api/todos"
- ❌ Backend not running
- ✅ Run backend first

### "No todos yet. Add one!"
- ✅ This is normal! Database is empty
- Add a todo to test

### Red error notification when adding
1. Check backend logs for database error
2. Verify MySQL is running
3. Verify database `tododb` exists

### "Connection refused"
- ❌ MySQL not running
- ✅ Start MySQL:
  ```bash
  # Docker
  docker run -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -e MYSQL_DATABASE=tododb -d mysql:8.0
  
  # Or Windows service
  net start MySQL80
  ```

### Application won't start
- Check logs for errors
- Verify correct Java version (17+)
- Try: `mvn clean package -DskipTests`

---

## Common Issues & Solutions

| Problem | Solution |
|---------|----------|
| Port 8080 already in use | Change in `application.properties`: `server.port=8081` |
| Port 8081 already in use | Use different port: `python -m http.server 9000` |
| MySQL password wrong | Check `docker-compose.yml` or `application-local.properties` |
| Database not found | Run: `CREATE DATABASE tododb;` |
| Can't connect to MySQL | Wait 20 seconds for container startup |
| Java not found | Install Java 17+ from oracle.com |

---

## Check Logs

### Docker
```bash
# View all logs
docker-compose logs

# View only backend logs
docker-compose logs backend

# View only MySQL logs
docker-compose logs mysql

# Follow logs in real-time
docker-compose logs -f backend
```

### Local Terminal
Logs print directly to console where you started the app.

Look for:
- ✅ "Todo Application Started Successfully!"
- ✅ "Successfully saved todo with ID:"
- ❌ "Error" (indicates problems)

---

## Files to Check

If things go wrong, check these logs:

1. **Backend Console** - Real-time application logs
2. **Browser Console** (F12) - JavaScript errors
3. **Browser Network Tab** (F12) - API requests/responses
4. **MySQL Logs** (Docker) - Database errors

---

## Still Having Issues?

1. **Read:** `SOLUTION-SUMMARY.md` - Overview of changes
2. **Read:** `TROUBLESHOOTING.md` - Detailed solutions
3. **Read:** `TESTING.md` - Step-by-step testing
4. **Check:** Backend logs for specific errors
5. **Check:** Browser console (F12) for JavaScript errors

---

## Success Indicators ✅

You'll know it's working when:

1. ✅ Backend starts without "connection refused" errors
2. ✅ Frontend loads with "No todos yet" message
3. ✅ Adding todo shows green notification
4. ✅ Todo appears in list
5. ✅ Todo exists in database (checked via MySQL)
6. ✅ Deleting todo works
7. ✅ No red error notifications

**If all 7 are true, you're done!** 🎉

---

## What Changed

**Frontend:**
- Better error messages from backend
- Success notifications show Todo ID
- Clear green/red feedback

**Backend:**
- All operations logged
- Proper error handling
- Database connection info on startup
- Works locally and in Docker

**Database:**
- No changes needed
- Automatically created by Hibernate
- Data persists between restarts

---

## Duration

- **Docker setup:** 2-3 minutes
- **Local setup:** 5-10 minutes
- **First test:** 1 minute
- **Total:** Less than 15 minutes!

Now go build and test! 🚀

