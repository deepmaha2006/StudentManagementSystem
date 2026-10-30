const express = require("express");
const router = express.Router();

const {
    createStudent,
    getStudents,
    getStudent,
    updateStudent,
    deleteStudent,
    coursePerformance
} = require("../controllers/studentController");

router.post("/", createStudent);
router.get("/", getStudents);
router.get("/reports/course-performance", coursePerformance);
router.get("/:id", getStudent);
router.put("/:id", updateStudent);
router.delete("/:id", deleteStudent);

module.exports = router;
