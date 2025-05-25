<h1 align="left">📝 Project Title: University Timetable Generator</h1>

<h2 align="left">📌 Overview</h2>
<p>
The University Timetable Generator is a Java-based desktop application that helps automate the process of managing and generating academic timetables for a university. The system enables the admin to handle departments, faculty, courses (papers), and generate weekly timetables while avoiding scheduling conflicts. Built using Java Swing and connected to a MySQL database, it eliminates manual scheduling errors and saves time.
</p>

<h2>✅ Features</h2>
<ul>
  <li><strong>Login / Signup System</strong>
    <ul>
      <li>Secure login for administrators</li>
      <li>User authentication and role-based access</li>
    </ul>
  </li>
  <li><strong>Manage Departments</strong>
    <ul>
      <li>Add, edit, and delete departments</li>
      <li>Associate departments with specific semesters and courses</li>
    </ul>
  </li>
  <li><strong>Manage Faculty</strong>
    <ul>
      <li>Add, update, and remove faculty details</li>
      <li>Assign faculty to departments and papers</li>
    </ul>
  </li>
  <li><strong>Manage Papers (Subjects)</strong>
    <ul>
      <li>Add and update subject/course information</li>
      <li>Link papers to specific faculty and departments</li>
    </ul>
  </li>
  <li><strong>Generate Timetable</strong>
    <ul>
      <li>Generate weekly class schedules</li>
      <li>Resolve conflicts automatically</li>
    </ul>
  </li>
  <li><strong>View Timetable</strong>
    <ul>
      <li>View timetable by department, faculty, or semester</li>
      <li>Export or print timetable for use</li>
    </ul>
  </li>
  <li><strong>Database Integration</strong>
    <ul>
      <li>All data stored and retrieved using JDBC connection to MySQL</li>
    </ul>
  </li>
</ul>

<h2>🛠️ Software Used</h2>
<table>
  <tr><th>Component</th><th>Tool/Technology</th></tr>
  <tr><td>Programming Language</td><td>Java</td></tr>
  <tr><td>GUI Framework</td><td>Java Swing</td></tr>
  <tr><td>IDE</td><td>Eclipse IDE</td></tr>
  <tr><td>Database</td><td>MySQL Workbench</td></tr>
  <tr><td>Database Connector</td><td>MySQL JDBC Driver</td></tr>
  <tr><td>Libraries</td><td>Swing, AWT, JDBC</td></tr>
  <tr><td>OS Compatibility</td><td>Windows</td></tr>
</table>

<h2>📁 Project Structure</h2>
<pre>
UniversityTimetableGenerator/
├── bin/
│   └── database/
│   └── gui/
├── src/
│   └── database/
│   └── gui/
├── resources/
│   └── images/
│       ├── login.jpg
│       ├── main_menu.jpg
│       ├── manage_dept.jpg
│       ├── manage_faculty.jpg
│       ├── manage_paper.jpg
│       ├── view_timetable.jpg
│       ├── generate_timetable.jpg
├── timetable.sql
├── UniversityTimetableGenerator.jar
├── Final_University_Timetable_Generator.exe
└── README.md
</pre>

<h2>📸 Screenshots</h2>

<h3>🔐 Login Page</h3>

![login](https://github.com/user-attachments/assets/cc7c84d3-0fd2-4b98-ae0f-a16f6ac498cd)

<h3>➡️ After Clicking Login</h3>

![after_click_login](https://github.com/user-attachments/assets/e8312ec5-d43b-4f1f-844b-3c7062917781)

<h3>🏢 Manage Departments</h3>

![manage_dept](https://github.com/user-attachments/assets/4d28d999-e0dc-4f4a-a7da-912529e952f4)

<h3>👩‍🏫 Manage Faculty</h3>

![manage_faculty](https://github.com/user-attachments/assets/e4d1e4d1-49b8-4dd0-880c-1f439d663390)

<h3>📘 Manage Paper</h3>

![manage_paper](https://github.com/user-attachments/assets/ba36e32a-3c90-4fed-9cc9-665685b814de)

<h3>📅 View Timetable</h3>

![view_timetable](https://github.com/user-attachments/assets/e4bcf99a-f9f1-4637-a1bb-d92e41e81fa9)

<h3>⚙️ Generate Timetable</h3>

![generate_timetable](https://github.com/user-attachments/assets/6e04f65f-8881-4d6d-babb-aa40e57d4e29)

<h2>📘 Usage Instructions</h2>
<ol>
  <li>Open the project in Eclipse IDE.</li>
  <li>Make sure MySQL server is running and import <code>timetable.sql</code> into MySQL Workbench.</li>
  <li>Configure database connection in <code>DatabaseConnection.java</code> with correct URL, username, and password.</li>
  <li>Build the project and run <code>LoginPage.java</code> to start the application.</li>
  <li>Use the GUI to manage departments, faculty, papers, and generate/view timetables.</li>
  <li>Executable JAR and EXE files are provided for quick access without Eclipse.</li>
  <li>After click JAR and EXE files Click view raw and then open files</li>
  <li>Run the projects</li>
</ol>

---

<h2>👥 Contributors</h2>

- 🧑‍💻 **Ankana Mondal** – Design and Development of Overall Structure  
 www.linkedin.com/in/ankanamondal
---

<p align="center">🎓 Developed as part of MCA curriculum project – University of Engineering and Management, Kolkata.</p>
