const studentTableBody = document.querySelector("#student-table-body");
const statusText = document.querySelector("#status-text");
const totalCount = document.querySelector("#total-count");
const departmentCount = document.querySelector("#department-count");
const filterInput = document.querySelector("#filter-input");
const refreshButton = document.querySelector("#refresh-button");
const errorBox = document.querySelector("#error-box");
const updatedAt = document.querySelector("#updated-at");

const API_ENDPOINT = "/api/v1/public/students";

const formatDate = (dateString) => {
  if (!dateString) {
    return "—";
  }
  const date = new Date(dateString);
  if (Number.isNaN(date.getTime())) {
    return dateString;
  }
  return date.toLocaleDateString(undefined, {
    year: "numeric",
    month: "short",
    day: "numeric",
  });
};

const setStatus = (message) => {
  statusText.textContent = message;
};

const renderRows = (students) => {
  studentTableBody.innerHTML = "";

  if (!students.length) { studentTableBody.innerHTML =
                               '<tr><td colspan="6" class="notice">No students found for the current filter.</td></tr>';
                             return;
                           }

                           students.forEach((student) => {
                             const row = document.createElement("tr");
                             row.innerHTML = `
                               <td>${student.id}</td>
                               <td>${student.firstName} ${student.lastName}</td>
                               <td>${student.email}</td>
                               <td>${student.department ?? "—"}</td>
                               <td>${student.phoneNumber ?? "—"}</td>
                                     <td>${formatDate(student.enrollmentDate)}</td>
                                   `;
                                   studentTableBody.appendChild(row);
                                 });
                               };

                               const updateStats = (students) => {
                                 totalCount.textContent = students.length;
                                 const departments = new Set(
                                   students
                                     .map((student) => student.department)
                                     .filter((department) => department && department.trim() !== "")
                                 );
                                 departmentCount.textContent = departments.size;
                               };

                               const filterStudents = (students, term) => {
                                 if (!term) {
                                   return students;
                                 }
                                 const lowerTerm = term.toLowerCase();
                                 return students.filter((student) =>
                                   [student.firstName, student.lastName, student.email, student.department]
                                     .filter(Boolean)
                                     .some((value) => value.toLowerCase().includes(lowerTerm))
                                 );
                               };

                               const loadStudents = async () => {
                                 setStatus("Loading students from the backend API...");
                                 errorBox.hidden = true;

                                 try {
                                   const response = await fetch(API_ENDPOINT);
                                   if (!response.ok) {
                                     throw new Error(`Request failed with status ${response.status}`);
                                   }
                                   const students = await response.json();
                                   const filtered = filterStudents(students, filterInput.value.trim());

                                   updateStats(students);
                                   renderRows(filtered);
                                   updatedAt.textContent = new Date().toLocaleTimeString();
                                   setStatus("Data loaded from backend.");
                                 } catch (error) {
                                   errorBox.hidden = false;
                                   errorBox.textContent = `Unable to load students. ${error.message}`;
                                   setStatus("Backend unavailable.");
                                   studentTableBody.innerHTML = "";
                                 }
                               };

                               filterInput.addEventListener("input", () => {
                                 loadStudents();
                               });

                               refreshButton.addEventListener("click", () => {
                                 loadStudents();
                               });

                               loadStudents();