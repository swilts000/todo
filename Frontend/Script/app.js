const API_URL = "/api/todos";

function showNotification(message, type) {
const notification = document.getElementById("notification");
notification.textContent = message;
notification.className = `notification ${type} show`;
setTimeout(() => {
notification.className = "notification";
}, 3000);
}

async function loadTodos() {
try {
const res = await fetch(API_URL);
if (!res.ok) throw new Error(`Failed to load todos: ${res.status}`);
const todos = await res.json();
const list = document.getElementById("todoList");
list.innerHTML = "";
todos.forEach(t => {
const li = document.createElement("li");
li.textContent = t.text;
li.onclick = async () => {
try {
const deleteRes = await fetch(`${API_URL}/${t.id}`, { method: "DELETE" });
if (!deleteRes.ok) throw new Error(`Failed to delete: ${deleteRes.status}`);
showNotification("Todo deleted successfully!", "success");
loadTodos();
} catch (error) {
showNotification(`Error deleting todo: ${error.message}`, "error");
}
};
list.appendChild(li);
});
} catch (error) {
showNotification(`Error loading todos: ${error.message}`, "error");
}
}

async function addTodo() {
const input = document.getElementById("todoInput");
const text = input.value.trim();
if (!text) return;
try {
const res = await fetch(API_URL, {
method: "POST",
headers: { "Content-Type": "application/json" },
body: JSON.stringify({ text, done: false })
});
if (!res.ok) throw new Error(`Failed to add todo: ${res.status}`);
showNotification("Todo added successfully!", "success");
input.value = "";
loadTodos();
} catch (error) {
showNotification(`Error adding todo: ${error.message}`, "error");
}
}

// Load todos when the page loads
window.addEventListener('DOMContentLoaded', loadTodos);
