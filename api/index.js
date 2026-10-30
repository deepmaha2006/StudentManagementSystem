require("dotenv").config();

const express = require("express");
const cors = require("cors");
const connectDB = require("../config/database");

const app = express();
app.use(cors());
app.use(express.json());

// Health check
app.get("/api/health", (req, res) => {
    res.json({ ok: true, node: process.version });
});

// DB middleware for API routes
app.use("/api", async (req, res, next) => {
    try {
        await connectDB();
        next();
    } catch (err) {
        console.error("DB error:", err.message);
        res.status(500).json({ error: "Database connection failed", detail: err.message });
    }
});

app.use("/api/students", require("../routes/studentRoutes"));
app.use("/api/courses", require("../routes/courseRoutes"));

app.use("/api", (req, res) => {
    res.status(404).json({ message: "API route not found" });
});

module.exports = app;
