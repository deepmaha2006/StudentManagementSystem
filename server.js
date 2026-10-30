require("dotenv").config();

const express  = require("express");
const cors     = require("cors");
const path     = require("path");
const connectDB = require("./config/database");

const app = express();

app.use(cors());
app.use(express.json());

// Ensure DB is connected before every request (uses cached connection)
app.use(async (req, res, next) => {
    try {
        await connectDB();
        next();
    } catch (err) {
        res.status(500).json({ error: "Database connection failed" });
    }
});

// Serve frontend static files
app.use(express.static(path.join(__dirname, "public")));

// API routes
app.use("/api/students", require("./routes/studentRoutes"));
app.use("/api/courses",  require("./routes/courseRoutes"));

// API 404
app.use("/api", (req, res) => {
    res.status(404).json({ message: "API route not found" });
});

// Frontend SPA fallback
app.get("*", (req, res) => {
    res.sendFile(path.join(__dirname, "public", "index.html"));
});

// For local development
if (require.main === module) {
    const PORT = process.env.PORT || 5000;
    app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
}

module.exports = app;
