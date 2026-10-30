const mongoose = require("mongoose");

const studentSchema = new mongoose.Schema({
    studentId: {
        type: Number,
        required: true,
        unique: true
    },
    name: {
        type: String,
        required: true,
        trim: true
    },
    age: {
        type: Number,
        required: true,
        min: 1
    },
    courseId: {
        type: String,
        required: true,
        trim: true
    },
    marks: {
        type: Number,
        required: true,
        min: 0,
        max: 100
    }
}, { collection: "students" });

module.exports = mongoose.model("Student", studentSchema);
