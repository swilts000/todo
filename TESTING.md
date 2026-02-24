# Testing the Todo Application

## Quick Test with Docker

### 1. Start Everything
```bash
cd C:\Users\xwilt\OneDrive\Desktop\Kubernetes__Project\todo
docker-compose up --build
```

Wait for the output:
```
backend_1   | ================================================================================
backend_1   | Todo Application Started Successfully!
backend_1   | Database URL: jdbc:mysql://mysql:3306/tododb
backend_1   | Database Username: root
backend_1   | ================================================================================
```

### 2. Test the API

#### Get all todos (should be empty initially)
```bash
curl http://localhost:8080/api/todos
```

Response:
```json
[]
```

#### Add a todo
```bash
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{"text": "Buy groceries", "done": false}'
```

Expected Response (201 Created):
```json
{
  "id": 1,
  "text": "Buy groceries",
  "done": false
}
```

If you see an error like `"error": "Failed to create todo: ..."`, check:
1. Backend logs for database connection issues
2. MySQL is running: `docker ps`
3. Database exists: `docker exec <mysql_container> mysql -u root -ppassword -e "SHOW DATABASES;"`

#### Get all todos (should now have 1)
```bash
curl http://localhost:8080/api/todos
```

Response:
```json
[
  {
    "id": 1,
    "text": "Buy groceries",
    "done": false
  }
]
```

#### Update a todo
```bash
curl -X PUT http://localhost:8080/api/todos/1 \
  -H "Content-Type: application/json" \
  -d '{"text": "Buy groceries", "done": true}'
```

Expected Response:
```json
{
  "id": 1,
  "text": "Buy groceries",
  "done": true
}
```

#### Delete a todo
```bash
curl -X DELETE http://localhost:8080/api/todos/1
```

Expected Response:
```json
{
  "message": "Todo deleted successfully"
}
```

### 3. Test the Frontend

1. Open http://localhost:8081 in your browser
2. Type a todo: "Test todo"
3. Click "Add"
4. You should see:
   - Green notification: "✓ Todo added successfully! (ID: 1)"
   - Todo appears in the list
5. Click the todo to delete it
6. You should see:
   - Green notification: "Todo deleted successfully!"
   - Todo disappears from the list

### 4. Verify Data in Database

```bash
# Connect to MySQL inside Docker
docker exec -it <mysql_container_id> mysql -u root -ppassword tododb

# Inside MySQL shell:
SELECT * FROM todo;
```

---

## Testing Specific Scenarios

### Scenario 1: Connection Success
**What to look for:**
- Logs show database URL without errors
- API requests return data
- No "connection refused" errors

**Frontend Indication:**
- Todos load without error messages
- Notifications appear when adding/deleting

### Scenario 2: Database Error
**What to look for:**
- Backend logs show connection errors
- API returns 500 with error message

**Frontend Indication:**
- Red notification shows error details
- Example: "Error adding todo: Connection refused"

### Scenario 3: Empty Database
**What to look for:**
- API returns empty array: `[]`
- Frontend shows: "No todos yet. Add one!"

**Database Query:**
```sql
SELECT COUNT(*) FROM todo;
-- Should return 0
```

---

## Browser Developer Tools (F12)

### Network Tab
1. Open DevTools (F12)
2. Go to "Network" tab
3. Add a todo
4. Look for requests to `/api/todos`
5. Check response:
   - Success: Status 201, shows saved todo with ID
   - Error: Status 500, shows error message

### Console Tab
Look for logs like:
- `[SUCCESS] Todo added successfully! (ID: 1)`
- `[ERROR] Error adding todo: ...`

---

## Troubleshooting Test Results

### If todos don't save:
1. Check backend console for errors
2. Verify MySQL container is running
3. Verify database `tododb` exists
4. Check network connectivity: `docker network ls`

### If todos save but don't appear in list:
1. Check API GET request in Network tab
2. Verify database has data: `SELECT * FROM todo;`
3. Check browser console for JavaScript errors

### If notifications don't appear:
1. Check HTML has `<div id="notification">` element
2. Check CSS file is loaded (Network tab)
3. Check `showNotification()` is being called (Console tab)

---

## Performance Testing

### Add multiple todos
```bash
for i in {1..10}; do
  curl -X POST http://localhost:8080/api/todos \
    -H "Content-Type: application/json" \
    -d "{\"text\": \"Todo $i\", \"done\": false}"
done
```

Then verify all appear in the UI and database.

### Check database size
```bash
docker exec <mysql_container_id> mysql -u root -ppassword tododb -e "SELECT COUNT(*) as total_todos FROM todo;"
```

---

## Success Criteria

✅ **All tests pass when:**
1. Frontend loads without errors
2. Adding a todo shows success notification
3. Todo appears in the list
4. Todo is visible in the database
5. Deleting a todo works and updates the list
6. No JavaScript errors in console
7. No SQL errors in backend logs

If any test fails, check the TROUBLESHOOTING.md guide for solutions.

