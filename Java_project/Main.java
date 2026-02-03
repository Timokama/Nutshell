import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.sql.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Simple Task Manager API
 * Java Backend with PostgreSQL (No Spring Boot, No Maven)
 */
public class Main {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/taskmanager";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "secret123";
    private static final int PORT = 9000;
    
    private static Connection connection;
    
    public static void main(String[] args) {
        try {
            // Initialize database connection
            initializeDatabase();
            
            // Find available port and create HTTP server
            int port = findAvailablePort(9000);
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            
            // Create context for API endpoints
            server.createContext("/api/tasks", new TaskHandler());
            
            // Serve static files (tasks.html)
            server.createContext("/", new StaticFileHandler("tasks.html"));
            
            // Set executor
            server.setExecutor(Executors.newFixedThreadPool(10));
            server.start();
            
            System.out.println("Task Manager API started on http://localhost:" + port);
            System.out.println("Available endpoints:");
            System.out.println("  GET    /api/tasks       - Get all tasks");
            System.out.println("  GET    /api/tasks/{id}   - Get task by ID");
            System.out.println("  POST   /api/tasks        - Create new task");
            System.out.println("  PUT    /api/tasks/{id}   - Update task");
            System.out.println("  DELETE /api/tasks/{id}   - Delete task");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static int findAvailablePort(int startPort) {
        int port = startPort;
        while (port < 10000) {
            try (ServerSocket ss = new ServerSocket(port)) {
                return port;
            } catch (IOException e) {
                port++;
            }
        }
        return port;
    }
    
    /**
     * Initialize database connection and create tables
     */
    private static void initializeDatabase() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to PostgreSQL database");
            
            // Create table if not exists
            createTable();
            
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC driver not found. Add it to classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Create tasks table
     */
    private static void createTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS tasks (
                id SERIAL PRIMARY KEY,
                title VARCHAR(200) NOT NULL,
                description TEXT,
                status VARCHAR(20) DEFAULT 'pending',
                priority VARCHAR(20) DEFAULT 'medium',
                due_date TIMESTAMP,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tasks table ready");
        }
    }
    
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}

/**
 * Handler for Task API endpoints
 */
class TaskHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        
        try {
            switch (method) {
                case "OPTIONS": handleOptions(exchange); break;
                case "GET": handleGet(exchange, path); break;
                case "POST": handlePost(exchange); break;
                case "PUT": handlePut(exchange, path); break;
                case "DELETE": handleDelete(exchange, path); break;
                default: sendJson(exchange, 405, "{\"error\":\"Method not allowed\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendJson(exchange, 500, "{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    private void handleOptions(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        try {
            exchange.sendResponseHeaders(204, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void handleGet(HttpExchange exchange, String path) throws SQLException {
        // Check if path is exactly /api/tasks
        if (path.equals("/api/tasks") || path.equals("/api/tasks/")) {
            // Get all tasks
            List<Task> tasks = Task.getAll();
            sendJson(exchange, 200, Task.toJsonArray(tasks));
        } else {
            // Get task by ID
            String idStr = path.replace("/api/tasks/", "");
            try {
                int id = Integer.parseInt(idStr);
                Task task = Task.getById(id);
                if (task != null) {
                    sendJson(exchange, 200, task.toJson());
                } else {
                    sendJson(exchange, 404, "{\"error\":\"Task not found\"}");
                }
            } catch (NumberFormatException e) {
                sendJson(exchange, 400, "{\"error\":\"Invalid task ID\"}");
            }
        }
    }
    
    private void handlePost(HttpExchange exchange) throws IOException, SQLException {
        String body = readBody(exchange);
        Task task = Task.fromJson(body);
        
        if (task.save()) {
            sendJson(exchange, 201, "{\"message\":\"Task created\",\"task\":" + task.toJson() + "}");
        } else {
            sendJson(exchange, 500, "{\"error\":\"Failed to create task\"}");
        }
    }
    
    private void handlePut(HttpExchange exchange, String path) throws IOException, SQLException {
        String idStr = path.replace("/api/tasks/", "");
        
        try {
            int id = Integer.parseInt(idStr);
            String body = readBody(exchange);
            
            // First check if task exists
            Task existingTask = Task.getById(id);
            if (existingTask == null) {
                sendJson(exchange, 404, "{\"error\":\"Task not found\"}");
                return;
            }
            
            // Parse JSON and only update provided fields
            Task updateData = Task.fromJson(body);
            
            if (updateData.getTitle() != null) existingTask.setTitle(updateData.getTitle());
            if (updateData.getDescription() != null) existingTask.setDescription(updateData.getDescription());
            if (updateData.getStatus() != null) existingTask.setStatus(updateData.getStatus());
            if (updateData.getPriority() != null) existingTask.setPriority(updateData.getPriority());
            
            if (existingTask.update()) {
                sendJson(exchange, 200, "{\"message\":\"Task updated\",\"task\":" + existingTask.toJson() + "}");
            } else {
                sendJson(exchange, 500, "{\"error\":\"Failed to update task\"}");
            }
        } catch (NumberFormatException e) {
            sendJson(exchange, 400, "{\"error\":\"Invalid task ID\"}");
        }
    }
    
    private void handleDelete(HttpExchange exchange, String path) throws SQLException {
        String idStr = path.replace("/api/tasks/", "");
        
        try {
            int id = Integer.parseInt(idStr);
            if (Task.delete(id)) {
                sendJson(exchange, 200, "{\"message\":\"Task deleted\"}");
            } else {
                sendJson(exchange, 404, "{\"error\":\"Task not found\"}");
            }
        } catch (NumberFormatException e) {
            sendJson(exchange, 400, "{\"error\":\"Invalid task ID\"}");
        }
    }
    
    private String readBody(HttpExchange ex) throws IOException {
        try (InputStream is = ex.getRequestBody()) {
            return new String(is.readAllBytes(), "UTF-8");
        }
    }
    
    private void sendJson(HttpExchange ex, int code, String json) {
        // Add CORS headers to allow requests from file:// protocol
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
        ex.getResponseHeaders().set("Content-Type", "application/json");
        try {
            ex.sendResponseHeaders(code, json.getBytes().length);
            try (OutputStream os = ex.getResponseBody()) {
                os.write(json.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Handler for static files (tasks.html)
 */
class StaticFileHandler implements HttpHandler {
    private final String defaultFile;
    
    public StaticFileHandler(String defaultFile) {
        this.defaultFile = defaultFile;
    }
    
    @Override
    public void handle(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        
        // If root path, serve default file
        if (path.equals("/")) {
            path = "/" + defaultFile;
        }
        
        // Remove leading slash for file lookup
        String fileName = path.substring(1);
        
        // Only allow serving tasks.html for security
        if (!fileName.equals("tasks.html")) {
            try {
                exchange.sendResponseHeaders(404, -1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        
        // Get the directory where the class files are located
        String baseDir = System.getProperty("user.dir");
        String filePath = baseDir + File.separator + fileName;
        
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                exchange.sendResponseHeaders(404, -1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        
        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            String contentType = "text/html";
            if (fileName.endsWith(".css")) {
                contentType = "text/css";
            } else if (fileName.endsWith(".js")) {
                contentType = "application/javascript";
            }
            
            exchange.getResponseHeaders().set("Content-Type", contentType);
            exchange.sendResponseHeaders(200, fileBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fileBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                exchange.sendResponseHeaders(500, -1);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}

/**
 * Task Model
 */
class Task {
    private int id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Timestamp dueDate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public Timestamp getDueDate() { return dueDate; }
    public void setDueDate(Timestamp dueDate) { this.dueDate = dueDate; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    
    /**
     * Get all tasks
     */
    public static List<Task> getAll() {
        List<Task> list = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY created_at DESC";
        
        try (Connection c = Main.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(fromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    /**
     * Get task by ID
     */
    public static Task getById(int id) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        
        try (Connection c = Main.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return fromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Create new task
     */
    public boolean save() {
        String sql = "INSERT INTO tasks (title, description, status, priority) VALUES (?, ?, ?, ?)";
        
        try (Connection c = Main.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, status != null ? status : "pending");
            ps.setString(4, priority != null ? priority : "medium");
            
            if (ps.executeUpdate() > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        id = keys.getInt(1);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Update task - only updates non-null fields
     */
    public boolean update() {
        StringBuilder sql = new StringBuilder("UPDATE tasks SET ");
        List<Object> values = new ArrayList<>();
        
        if (title != null) {
            sql.append("title = ?, ");
            values.add(title);
        }
        if (description != null) {
            sql.append("description = ?, ");
            values.add(description);
        }
        if (status != null) {
            sql.append("status = ?, ");
            values.add(status);
        }
        if (priority != null) {
            sql.append("priority = ?, ");
            values.add(priority);
        }
        
        // Remove trailing comma and space
        if (values.isEmpty()) return false;
        sql.setLength(sql.length() - 2);
        
        sql.append(", updated_at = CURRENT_TIMESTAMP WHERE id = ?");
        values.add(id);
        
        try (Connection c = Main.getConnection();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < values.size(); i++) {
                ps.setObject(i + 1, values.get(i));
            }
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete task
     */
    public static boolean delete(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        
        try (Connection c = Main.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Convert to JSON
     */
    public String toJson() {
        return String.format(
            "{\"id\":%d,\"title\":\"%s\",\"description\":\"%s\",\"status\":\"%s\",\"priority\":\"%s\",\"dueDate\":%s,\"createdAt\":\"%s\"}",
            id, escape(title), escape(description), status, priority,
            dueDate != null ? "\"" + dueDate + "\"" : "null",
            createdAt != null ? createdAt.toString() : "null"
        );
    }
    
    /**
     * Convert list to JSON array
     */
    public static String toJsonArray(List<Task> tasks) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(tasks.get(i).toJson());
            if (i < tasks.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Parse from JSON
     */
    public static Task fromJson(String json) {
        Task t = new Task();
        t.title = extract(json, "title");
        t.description = extract(json, "description");
        t.status = extract(json, "status");
        t.priority = extract(json, "priority");
        return t;
    }
    
    private static Task fromResultSet(ResultSet rs) throws SQLException {
        Task t = new Task();
        t.id = rs.getInt("id");
        t.title = rs.getString("title");
        t.description = rs.getString("description");
        t.status = rs.getString("status");
        t.priority = rs.getString("priority");
        t.dueDate = rs.getTimestamp("due_date");
        t.createdAt = rs.getTimestamp("created_at");
        return t;
    }
    
    private static String extract(String json, String key) {
        String p = "\"" + key + "\":";
        int i = json.indexOf(p);
        if (i == -1) return null;
        int start = i + p.length();
        if (start >= json.length()) return null;
        
        char c = json.charAt(start);
        if (c == '"') {
            int end = json.indexOf("\"", start + 1);
            return json.substring(start + 1, end);
        }
        return null;
    }
    
    private static String escape(String s) {
        return s == null ? "" : s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}

