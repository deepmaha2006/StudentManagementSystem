const express = require("express");
const app = express();

app.get("/api/health", (req, res) => {
    res.json({ ok: true, node: process.version });
});

app.get("*", (req, res) => {
    res.json({ message: "Student Management API" });
});

if (require.main === module) {
    app.listen(process.env.PORT || 5000, () => console.log("Running"));
}

module.exports = app;
