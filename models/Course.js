const mongoose = require("mongoose");

const courseSchema = new mongoose.Schema({
    _id: {
        type: String,
        required: true
    },
    courseName: {
        type: String,
        required: true,
        unique: true,
        trim: true
    },
    department: {
        type: String,
        required: true
    },
    credits: {
        type: Number,
        required: true
    }
}, { collection: "courses" });

module.exports = mongoose.model("Course", courseSchema);
