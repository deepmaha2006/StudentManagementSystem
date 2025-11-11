import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StudentManagementSystem {

    private static final Scanner SCANNER = new Scanner(System.in);

    // Student Model
    private static final class Student {
        private final String studentId;
        private String fullName;
        private final Map<String, Double> subjectToMark; // subject -> mark (0-100)

        Student(String studentId, String fullName) {
            this.studentId = studentId;
            this.fullName = fullName;
            this.subjectToMark = new HashMap<>();
        }

        String getStudentId() { return studentId; }
        String getFullName() { return fullName; }
        void setFullName(String fullName) { this.fullName = fullName; }
        Map<String, Double> getSubjectToMark() { return subjectToMark; }
        double getTotalMarks() {
            double total = 0.0;
            for (double mark : subjectToMark.values()) total += mark;
            return total;
        }
        double getAverageMarks() {
            if (subjectToMark.isEmpty()) return 0.0;
            return getTotalMarks() / subjectToMark.size();
        }
        String getGrade() {
            double avg = getAverageMarks();
            if (avg >= 90) return "A+";
            if (avg >= 80) return "A";
            if (avg >= 70) return "B";
            if (avg >= 60) return "C";
            if (avg >= 50) return "D";
            return "F";
        }
    }

    // Repository for managing students
    private static final class Repository {
        private final Map<String, Student> idToStudent = new HashMap<>();
        boolean exists(String studentId) { return idToStudent.containsKey(studentId); }
        void add(Student student) { idToStudent.put(student.getStudentId(), student); }
        Optional<Student> get(String studentId) { return Optional.ofNullable(idToStudent.get(studentId)); }
        boolean remove(String studentId) { return idToStudent.remove(studentId) != null; }
        List<Student> listAllSorted() {
            List<Student> list = new ArrayList<>(idToStudent.values());
            list.sort(Comparator.comparing(Student::getStudentId));
            return list;
        }
    }

    private static final Repository REPO = new Repository();

    public static void main(String[] args) {
        System.out.println("==== Student Result Management System ====");
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readInt("Choose an option: ");
            switch (choice) {
                case 1: handleAddStudent(); break;
                case 2: handleUpdateStudent(); break;
                case 3: handleDeleteStudent(); break;
                case 4: handleViewIndividualResult(); break;
                case 5: handleViewClassResults(); break;
                case 6: handleListStudents(); break;
                case 7: generateExitReport(); running = false; break;
                default: System.out.println("Invalid choice. Please try again.");
            }
            System.out.println();
        }
        System.out.println("Goodbye!");
    }

    private static void printMenu() {
        System.out.println("\nMenu:");
        System.out.println("  1) Add Student");
        System.out.println("  2) Update Student");
        System.out.println("  3) Delete Student");
        System.out.println("  4) View Individual Result");
        System.out.println("  5) View Class Results");
        System.out.println("  6) List Students");
        System.out.println("  7) Save & Exit (Generate Report)");
    }

    private static void handleAddStudent() {
        System.out.println("\n-- Add Student --");
        String id = readNonEmptyString("Enter Student ID: ");
        if (REPO.exists(id)) {
            System.out.println("A student with this ID already exists.");
            return;
        }
        String name = readNonEmptyString("Enter Full Name: ");
        Student s = new Student(id, name);
        System.out.println("Enter subjects and marks (0-100). Type 'done' as subject to finish.");
        while (true) {
            String subject = readString("  Subject name: ");
            if (subject.trim().equalsIgnoreCase("done")) break;
            double mark = readDoubleInRange("  Mark (0-100): ", 0, 100);
            s.getSubjectToMark().put(subject.trim(), mark);
        }
        REPO.add(s);
        System.out.println("Student added successfully.");
    }

    private static void handleUpdateStudent() {
        System.out.println("\n-- Update Student --");
        String id = readNonEmptyString("Enter Student ID to update: ");
        Optional<Student> opt = REPO.get(id);
        if (!opt.isPresent()) {
            System.out.println("Student not found.");
            return;
        }
        Student s = opt.get();
        System.out.println("Selected: " + s.getStudentId() + " - " + s.getFullName());
        System.out.println("What would you like to update?");
        System.out.println("  1) Name");
        System.out.println("  2) Subjects/Marks");
        System.out.println("  3) Cancel");
        int choice = readInt("Choose: ");
        if (choice == 1) {
            String newName = readNonEmptyString("Enter new full name: ");
            s.setFullName(newName);
            System.out.println("Name updated.");
        } else if (choice == 2) {
            updateSubjectsAndMarks(s);
        } else {
            System.out.println("Update cancelled.");
        }
    }

    private static void updateSubjectsAndMarks(Student s) {
        boolean editing = true;
        while (editing) {
            System.out.println("\nSubjects for " + s.getFullName() + ":");
            if (s.getSubjectToMark().isEmpty()) {
                System.out.println("  (none)");
            } else {
                for (Map.Entry<String, Double> e : s.getSubjectToMark().entrySet()) {
                    System.out.println("  - " + e.getKey() + ": " + e.getValue());
                }
            }
            System.out.println("Options:");
            System.out.println("  1) Add/Update subject");
            System.out.println("  2) Remove subject");
            System.out.println("  3) Done");
            int ch = readInt("Choose: ");
            if (ch == 1) {
                String subject = readNonEmptyString("  Subject: ");
                double mark = readDoubleInRange("  Mark (0-100): ", 0, 100);
                s.getSubjectToMark().put(subject.trim(), mark);
                System.out.println("  Saved.");
            } else if (ch == 2) {
                String subject = readNonEmptyString("  Subject to remove: ");
                Double removed = s.getSubjectToMark().remove(subject.trim());
                if (removed == null) {
                    System.out.println("  Subject not found.");
                } else {
                    System.out.println("  Removed.");
                }
            } else {
                editing = false;
            }
        }
    }

    private static void handleDeleteStudent() {
        System.out.println("\n-- Delete Student --");
        String id = readNonEmptyString("Enter Student ID to delete: ");
        boolean removed = REPO.remove(id);
        if (removed) {
            System.out.println("Student removed.");
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void handleViewIndividualResult() {
        System.out.println("\n-- Individual Result --");
        String id = readNonEmptyString("Enter Student ID: ");
        Optional<Student> opt = REPO.get(id);
        if (!opt.isPresent()) {
            System.out.println("Student not found.");
            return;
        }
        printStudentReport(opt.get());
    }

    private static void handleViewClassResults() {
        System.out.println("\n-- Class Results --");
        List<Student> list = REPO.listAllSorted();
        if (list.isEmpty()) {
            System.out.println("No students available.");
            return;
        }
        printClassTable(list);
    }

    private static void handleListStudents() {
        System.out.println("\n-- Students --");
        List<Student> list = REPO.listAllSorted();
        if (list.isEmpty()) {
            System.out.println("No students available.");
            return;
        }
        for (Student s : list) {
            System.out.println(s.getStudentId() + " - " + s.getFullName());
        }
    }

    private static void printStudentReport(Student s) {
        System.out.println("\nResult Report for: " + s.getFullName() + " (" + s.getStudentId() + ")");
        if (s.getSubjectToMark().isEmpty()) {
            System.out.println("No subjects recorded.");
        } else {
            int maxSubjectLen = 0;
            for (String sub : s.getSubjectToMark().keySet()) maxSubjectLen = Math.max(maxSubjectLen, sub.length());
            String fmt = "  %1$-" + (maxSubjectLen + 2) + "s %2$6.2f";
            System.out.println("Subjects and Marks:");
            for (Map.Entry<String, Double> e : s.getSubjectToMark().entrySet()) {
                System.out.println(String.format(Locale.US, fmt, e.getKey(), e.getValue()));
            }
        }
        System.out.printf(Locale.US, "Total: %.2f\n", s.getTotalMarks());
        System.out.printf(Locale.US, "Average: %.2f\n", s.getAverageMarks());
        System.out.println("Grade: " + s.getGrade());
    }

    private static void printClassTable(List<Student> students) {
        List<Student> sorted = new ArrayList<>(students);
        sorted.sort(Comparator.comparingDouble(Student::getAverageMarks).reversed());
        int idLen = Math.max(6, sorted.stream().map(s -> s.getStudentId().length()).max(Integer::compareTo).orElse(6));
        int nameLen = Math.max(10, sorted.stream().map(s -> s.getFullName().length()).max(Integer::compareTo).orElse(10));
        String headerFmt = "%-" + idLen + "s  %-" + nameLen + "s  %8s  %8s  %-3s";
        String rowFmt = "%-" + idLen + "s  %-" + nameLen + "s  %8.2f  %8.2f  %-3s";
        System.out.println(String.format(headerFmt, "ID", "Name", "Total", "Average", "Grd"));
        System.out.println(repeat('-', idLen + nameLen + 2 + 8 + 2 + 8 + 2 + 3));
        for (Student s : sorted) {
            System.out.println(String.format(Locale.US, rowFmt, s.getStudentId(), s.getFullName(), s.getTotalMarks(), s.getAverageMarks(), s.getGrade()));
        }
    }

    private static void generateExitReport() {
        List<Student> students = REPO.listAllSorted();
        StringBuilder report = new StringBuilder();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        report.append("Student Result Management System - Report\n");
        report.append("Generated: ").append(timestamp).append("\n\n");
        report.append("Summary\n");
        report.append("- Total students: ").append(students.size()).append("\n");

        if (students.isEmpty()) {
            report.append("No data available.\n");
        } else {
            double classTotal = 0.0;
            int numStudents = students.size();
            int gradeAPlus = 0, gradeA = 0, gradeB = 0, gradeC = 0, gradeD = 0, gradeF = 0;
            Map<String, List<Double>> subjectToMarks = new HashMap<>();

            for (Student s : students) {
                classTotal += s.getAverageMarks();
                switch (s.getGrade()) {
                    case "A+": gradeAPlus++; break;
                    case "A": gradeA++; break;
                    case "B": gradeB++; break;
                    case "C": gradeC++; break;
                    case "D": gradeD++; break;
                    default: gradeF++; break;
                }
                for (Map.Entry<String, Double> e : s.getSubjectToMark().entrySet()) {
                    subjectToMarks.computeIfAbsent(e.getKey(), k -> new ArrayList<>()).add(e.getValue());
                }
            }

            double classAverageOfAverages = classTotal / Math.max(1, numStudents);
            List<Student> sortedByAvg = new ArrayList<>(students);
            sortedByAvg.sort(Comparator.comparingDouble(Student::getAverageMarks).reversed());
            Student top = sortedByAvg.get(0);

            report.append(String.format(Locale.US, "- Class average (of averages): %.2f\n", classAverageOfAverages));
            report.append("- Grade distribution: ")
                 .append("A+=").append(gradeAPlus).append(", ")
                 .append("A=").append(gradeA).append(", ")
                 .append("B=").append(gradeB).append(", ")
                 .append("C=").append(gradeC).append(", ")
                 .append("D=").append(gradeD).append(", ")
                 .append("F=").append(gradeF).append("\n");
            report.append(String.format(Locale.US, "- Top student: %s (%s) Avg=%.2f Grade=%s\n\n",
                    top.getFullName(), top.getStudentId(), top.getAverageMarks(), top.getGrade()));

            if (!subjectToMarks.isEmpty()) {
                report.append("Subject Averages\n");
                List<String> subjects = new ArrayList<>(subjectToMarks.keySet());
                Collections.sort(subjects);
                for (String subject : subjects) {
                    List<Double> marks = subjectToMarks.get(subject);
                    double sum = 0.0; for (double m : marks) sum += m;
                    double avg = sum / Math.max(1, marks.size());
                    report.append(String.format(Locale.US, "- %s: %.2f (n=%d)\n", subject, avg, marks.size()));
                }
                report.append("\n");
            }
            report.append("Class Results\n");
            String table = buildClassTable(students);
            report.append(table);
        }

        String reportContent = report.toString();
        System.out.println("\n=== Final Report ===\n");
        System.out.println(reportContent);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter("student_report.txt"))) {
            bw.write(reportContent);
            System.out.println("Report saved to 'student_report.txt'.");
        } catch (IOException e) {
            System.out.println("Failed to save report: " + e.getMessage());
        }
    }

    private static String buildClassTable(List<Student> list) {
        List<Student> sorted = new ArrayList<>(list);
        sorted.sort(Comparator.comparingDouble(Student::getAverageMarks).reversed());
        int idLen = Math.max(6, sorted.stream().map(s -> s.getStudentId().length()).max(Integer::compareTo).orElse(6));
        int nameLen = Math.max(10, sorted.stream().map(s -> s.getFullName().length()).max(Integer::compareTo).orElse(10));
        String headerFmt = "%-" + idLen + "s  %-" + nameLen + "s  %8s  %8s  %-3s";
        String rowFmt = "%-" + idLen + "s  %-" + nameLen + "s  %8.2f  %8.2f  %-3s";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(headerFmt, "ID", "Name", "Total", "Average", "Grd")).append('\n');
        sb.append(repeat('-', idLen + nameLen + 2 + 8 + 2 + 8 + 2 + 3)).append('\n');
        for (Student s : sorted) {
            sb.append(String.format(Locale.US, rowFmt, s.getStudentId(), s.getFullName(), s.getTotalMarks(), s.getAverageMarks(), s.getGrade())).append('\n');
        }
        return sb.toString();
    }

    // Utility methods for input
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readDoubleInRange(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine();
            try {
                double val = Double.parseDouble(line.trim());
                if (val < min || val > max) {
                    System.out.println("Value must be between " + min + " and " + max + ".");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine();
            if (line != null && !line.trim().isEmpty()) return line.trim();
            System.out.println("Input cannot be empty.");
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        String line = SCANNER.nextLine();
        return line == null ? "" : line;
    }

    private static String repeat(char c, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) sb.append(c);
        return sb.toString();
    }
}
