require("dotenv").config();

const express = require("express");
const cors = require("cors");
const path = require("path");
const connectDB = require("./config/database");

const app = express();
app.use(cors());
app.use(express.json());

app.get("/api/health", (req, res) => {
    res.json({ ok: true, node: process.version });
});

app.use("/api", async (req, res, next) => {
    try {
        await connectDB();
        next();
    } catch (err) {
        res.status(500).json({ error: "Database connection failed", detail: err.message });
    }
});

app.use(express.static(path.join(__dirname, "public")));
app.use("/api/students", require("./routes/studentRoutes"));
app.use("/api/courses", require("./routes/courseRoutes"));

app.use("/api", (req, res) => res.status(404).json({ message: "API route not found" }));
app.get("*", (req, res) => res.sendFile(path.join(__dirname, "public", "index.html")));

if (require.main === module) {
    const PORT = process.env.PORT || 5000;
    app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
}

module.exports = app;
