const student = {
  regNo: 2305000921,
  name: "Elysee NIYIBIZI",
  age: 20,
  courses: [
    "Advanced Web Development",
    "Advanced Java",
    "Mobile Application Development",
  ],
  isEnrolled: true,
  school: "University of Kigali",
  department: "Computer Science",
  year: 3,
  gpa: 4.1,
  hobbies: ["Coding", "Gaming", "Traveling"],
};

document.write("Student Name: " + student.name + "<br>");
document.write("Student Age: " + student.age + "<br>");
document.write(
  "Student Courses: " + student.courses.slice(0, 1).join(", ") + "<br>",
);
document.write("Student GPA: " + student.gpa + "<br>");
document.write("Student Hobbies: " + student.hobbies.join(", ") + "<br>");
document.write("Student School: " + student.school + "<br>");
document.write("Student Department: " + student.department + "<br>");
document.write("Student Year: " + student.year + "<br>");
