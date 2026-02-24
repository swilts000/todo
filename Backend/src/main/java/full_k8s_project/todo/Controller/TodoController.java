package full_k8s_project.todo.Controller;

import full_k8s_project.todo.Model.Todo;
import full_k8s_project.todo.Repository.TodoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
@CrossOrigin
public class TodoController {
        private static final Logger logger = LoggerFactory.getLogger(TodoController.class);
        private final TodoRepository repo;

        public TodoController(TodoRepository repo) {
            this.repo = repo;
        }

        @GetMapping
        public ResponseEntity<?> getAll() {
            try {
                logger.info("Fetching all todos from database");
                List<Todo> todos = repo.findAll();
                logger.info("Successfully fetched {} todos", todos.size());
                return ResponseEntity.ok(todos);
            } catch (Exception e) {
                logger.error("Error fetching todos", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch todos: " + e.getMessage()));
            }
        }

        @PostMapping
        public ResponseEntity<?> create(@RequestBody Todo todo) {
            try {
                logger.info("Creating new todo with text: {}", todo.getText());
                if (todo.getText() == null || todo.getText().trim().isEmpty()) {
                    logger.warn("Attempted to create todo with empty text");
                    return ResponseEntity.badRequest()
                        .body(Map.of("error", "Todo text cannot be empty"));
                }
                Todo saved = repo.save(todo);
                logger.info("Successfully saved todo with ID: {}", saved.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(saved);
            } catch (Exception e) {
                logger.error("Error creating todo", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create todo: " + e.getMessage()));
            }
        }

        @PutMapping("/{id}")
        public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Todo todo) {
            try {
                logger.info("Updating todo with ID: {}", id);
                todo.setId(id);
                Todo updated = repo.save(todo);
                logger.info("Successfully updated todo with ID: {}", id);
                return ResponseEntity.ok(updated);
            } catch (Exception e) {
                logger.error("Error updating todo with ID: {}", id, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to update todo: " + e.getMessage()));
            }
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> delete(@PathVariable Long id) {
            try {
                logger.info("Deleting todo with ID: {}", id);
                repo.deleteById(id);
                logger.info("Successfully deleted todo with ID: {}", id);
                return ResponseEntity.ok(Map.of("message", "Todo deleted successfully"));
            } catch (Exception e) {
                logger.error("Error deleting todo with ID: {}", id, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete todo: " + e.getMessage()));
            }
        }
}


