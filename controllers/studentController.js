const Student = require("../models/Student");

// CREATE
exports.createStudent = async (req, res) => {
    try {
        const student = await Student.create(req.body);
        res.status(201).json(student);
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
};

// READ ALL
exports.getStudents = async (req, res) => {
    try {
        const students = await Student.find().sort({ studentId: 1 });
        res.json(students);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

// READ ONE
exports.getStudent = async (req, res) => {
    try {
        const student = await Student.findOne({
            studentId: Number(req.params.id)
        });

        if (!student) {
            return res.status(404).json({ message: "Student not found" });
        }

        res.json(student);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

// UPDATE
exports.updateStudent = async (req, res) => {
    try {
        const student = await Student.findOneAndUpdate(
            { studentId: Number(req.params.id) },
            { $set: req.body },
            { new: true, runValidators: true }
        );

        if (!student) {
            return res.status(404).json({ message: "Student not found" });
        }

        res.json(student);
    } catch (error) {
        res.status(400).json({ error: error.message });
    }
};

// DELETE
exports.deleteStudent = async (req, res) => {
    try {
        const student = await Student.findOneAndDelete({
            studentId: Number(req.params.id)
        });

        if (!student) {
            return res.status(404).json({ message: "Student not found" });
        }

        res.json({ message: "Student deleted successfully" });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

// COURSE PERFORMANCE REPORT
exports.coursePerformance = async (req, res) => {
    try {
        const report = await Student.aggregate([
            {
                $group: {
                    _id: "$courseId",
                    totalStudents: { $sum: 1 },
                    averageMarks: { $avg: "$marks" }
                }
            },
            {
                $lookup: {
                    from: "courses",
                    localField: "_id",
                    foreignField: "_id",
                    as: "course"
                }
            },
            { $unwind: "$course" },
            {
                $project: {
                    _id: 0,
                    courseId: "$_id",
                    courseName: "$course.courseName",
                    totalStudents: 1,
                    averageMarks: { $round: ["$averageMarks", 2] }
                }
            }
        ]);

        res.json(report);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};
