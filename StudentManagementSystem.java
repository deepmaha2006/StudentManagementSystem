import java.io.*;
import java.time.*;
import java.util.*;

public class StudentManagementSystem {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Map<String, Student> STUDENTS = new HashMap<>();
    
    static class Student {
        String id, name;
        Map<String, Double> subjects = new HashMap<>();
        Student(String id, String name) { this.id = id; this.name = name; }
        double getAverage() {
            return subjects.values().stream().mapToDouble(d -> d).average().orElse(0);
        }
        String getGrade() {
            double avg = getAverage();
            if (avg >= 90) return "A+"; if (avg >= 80) return "A";
            if (avg >= 70) return "B"; if (avg >= 60) return "C";
            if (avg >= 50) return "D"; return "F";
        }
    }

    public static void main(String[] args) {
        System.out.println("=== STUDENT MANAGEMENT SYSTEM ===\n");
        while (true) {
            System.out.println("1.Add 2.Update 3.Delete 4.View Result 5.All Results 6.List 7.Exit");
            switch (readInt("Choice: ")) {
                case 1: addStudent(); break; case 2: updateStudent(); break;
                case 3: deleteStudent(); break; case 4: viewResult(); break;
                case 5: allResults(); break; case 6: listStudents(); break;
                case 7: saveReport(); return; default: System.out.println("Invalid choice");
            }
        }
    }

    private static void addStudent() {
        String id = readNonEmpty("Student ID: ");
        if (STUDENTS.containsKey(id)) { System.out.println("ID exists!"); return; }
        Student s = new Student(id, readNonEmpty("Name: "));
        System.out.println("Add subjects (type 'done' to finish):");
        while (true) {
            String sub = readNonEmpty("Subject: ");
            if (sub.equals("done")) break;
            s.subjects.put(sub, readDouble("Mark (0-100): ", 0, 100));
        }
        STUDENTS.put(id, s);
        System.out.println("Student added!");
    }

    private static void updateStudent() {
        Student s = STUDENTS.get(readNonEmpty("Student ID: "));
        if (s == null) { System.out.println("Not found!"); return; }
        
        System.out.println("\nWhat to update?");
        System.out.println("1. Name 2. Subjects/Marks 3. Cancel");
        switch (readInt("Choice: ")) {
            case 1: 
                s.name = readNonEmpty("New name: ");
                System.out.println("Name updated!");
                break;
            case 2:
                updateSubjects(s);
                break;
            case 3:
                System.out.println("Update cancelled.");
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private static void updateSubjects(Student s) {
        while (true) {
            System.out.println("\nCurrent subjects:");
            s.subjects.forEach((sub, mark) -> System.out.printf("  %s: %.2f\n", sub, mark));
            
            System.out.println("\n1.Add/Update 2.Remove 3.Done");
            switch (readInt("Choice: ")) {
                case 1:
                    String sub = readNonEmpty("Subject: ");
                    double mark = readDouble("Mark (0-100): ", 0, 100);
                    s.subjects.put(sub, mark);
                    System.out.println("Subject updated!");
                    break;
                case 2:
                    String removeSub = readNonEmpty("Subject to remove: ");
                    if (s.subjects.remove(removeSub) != null) 
                        System.out.println("Subject removed!");
                    else 
                        System.out.println("Subject not found!");
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void deleteStudent() {
        System.out.println(STUDENTS.remove(readNonEmpty("Student ID: ")) != null ? 
            "Deleted!" : "Not found!");
    }

    private static void viewResult() {
        Student s = STUDENTS.get(readNonEmpty("Student ID: "));
        if (s == null) { System.out.println("Not found!"); return; }
        System.out.println("\nResult: " + s.name + " (" + s.id + ")");
        s.subjects.forEach((sub, mark) -> System.out.printf("  %s: %.2f\n", sub, mark));
        System.out.printf("Average: %.2f | Grade: %s\n", s.getAverage(), s.getGrade());
    }

    private static void allResults() {
        if (STUDENTS.isEmpty()) { System.out.println("No students!"); return; }
        STUDENTS.values().stream()
            .sorted((a,b) -> Double.compare(b.getAverage(), a.getAverage()))
            .forEach(s -> System.out.printf("%s - %s: %.2f (%s)\n", 
                s.id, s.name, s.getAverage(), s.getGrade()));
    }

    private static void listStudents() {
        STUDENTS.values().forEach(s -> System.out.println(s.id + " - " + s.name));
    }

    private static void saveReport() {
        try (BufferedWriter w = new BufferedWriter(new FileWriter("report.txt"))) {
            w.write("Student Report - " + LocalDateTime.now() + "\n\n");
            STUDENTS.values().stream()
                .sorted((a,b) -> Double.compare(b.getAverage(), a.getAverage()))
                .forEach(s -> {
                    try { w.write(String.format("%s - %s: %.2f (%s)\n", 
                        s.id, s.name, s.getAverage(), s.getGrade()));
                    } catch (IOException e) {}
                });
            System.out.println("Report saved to report.txt");
        } catch (IOException e) {
            System.out.println("Error saving report");
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try { return Integer.parseInt(SCANNER.nextLine().trim()); } 
            catch (Exception e) { System.out.println("Enter a valid number!"); }
        }
    }

    private static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double val = Double.parseDouble(SCANNER.nextLine().trim());
                if (val >= min && val <= max) return val;
                System.out.println("Enter between " + min + "-" + max);
            } catch (Exception e) { System.out.println("Enter a valid number!"); }
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();
            if (!input.isEmpty()) return input;
            System.out.println("Cannot be empty!");
        }
    }
}
