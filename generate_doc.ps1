$desktopPath = [Environment]::GetFolderPath("Desktop")
$filePath = Join-Path $desktopPath "GIFT_DEVISON_Test_Cases.doc"

$html = @"
<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:w="urn:schemas-microsoft-com:office:word" xmlns="http://www.w3.org/TR/REC-html40">
<head>
<meta charset="utf-8">
<title>TEST CASE TEMPLATE</title>
<style>
body { font-family: Arial, sans-serif; margin: 40px; color: #14284B; }
h1 { text-align: center; color: #082B63; font-size: 18pt; }
h2 { text-align: center; color: #647CA6; font-size: 13pt; margin-bottom: 20px; }
h3 { color: #173B70; margin-top: 30px; border-bottom: 2px solid #173B70; padding-bottom: 4px; font-size: 12pt; }
p { font-size: 11pt; line-height: 1.5; }
table { width: 100%; border-collapse: collapse; margin-top: 10px; margin-bottom: 20px; }
th, td { border: 1px solid #082B63; padding: 8px 12px; font-size: 11pt; text-align: left; vertical-align: top; }
th { background-color: #F5F8FC; color: #082B63; width: 28%; font-weight: bold; }
td { width: 72%; }
</style>
</head>
<body>
<h1>TEST CASE TEMPLATE</h1>
<h2>Mobile Applications Development Project – 4th Year Test Design Submission</h2>
<p><b>Instructions:</b> Your test cases are based on the updated project documentation and UI created. Each feature has at least one positive test case and one negative test case. Test Case IDs follow the format <code>MODULE_FEATURE_NNN</code>.</p>
<hr/>

<h3>Test Case 1: Successful User Account Creation / Login (Positive)</h3>
<table>
<tr><th>Field</th><th>Details</th></tr>
<tr><td><b>Test Case ID</b></td><td>AUTH_LOGIN_001</td></tr>
<tr><td><b>Description</b></td><td>Verify that a student can successfully create an account or log in by entering a valid Full Name (First & Last Name) and optional email.</td></tr>
<tr><td><b>Test Type</b></td><td>Positive</td></tr>
<tr><td><b>Priority</b></td><td>High</td></tr>
<tr><td><b>Prerequisites</b></td><td>App is launched on fresh install or after session reset.</td></tr>
<tr><td><b>Test Data</b></td><td>Name: "Alex Johnson", Email: "alex@university.edu", Password: "Password123"</td></tr>
<tr><td><b>Test Steps</b></td><td>1. Launch the Student Study Planner app.<br>2. View the Create Account / Log In screen.<br>3. Enter "Alex Johnson" in the Full Name field.<br>4. Enter password and confirmation.<br>5. Tap "Create Account".</td></tr>
<tr><td><b>Expected Result</b></td><td>The app validates that the name contains both first and last name, saves the profile in the Room database, and transitions to the Dashboard.</td></tr>
<tr><td><b>Post-conditions</b></td><td>User session is active; user profile is persisted locally.</td></tr>
<tr><td><b>Status</b></td><td></td></tr>
</table>

<h3>Test Case 2: Reject Account Creation with Single Name Only (Negative)</h3>
<table>
<tr><th>Field</th><th>Details</th></tr>
<tr><td><b>Test Case ID</b></td><td>AUTH_LOGIN_002</td></tr>
<tr><td><b>Description</b></td><td>Verify that the app rejects account creation when the user enters only a single name (missing last name).</td></tr>
<tr><td><b>Test Type</b></td><td>Negative</td></tr>
<tr><td><b>Priority</b></td><td>High</td></tr>
<tr><td><b>Prerequisites</b></td><td>Create Account screen is open.</td></tr>
<tr><td><b>Test Data</b></td><td>Name: "Alex" (Single name only)</td></tr>
<tr><td><b>Test Steps</b></td><td>1. Enter "Alex" in the Full Name field.<br>2. Fill in password and tap "Create Account".</td></tr>
<tr><td><b>Expected Result</b></td><td>The app rejects registration and displays error message: "Please enter both your first name and last name (Full Name)."</td></tr>
<tr><td><b>Post-conditions</b></td><td>Account creation is prevented.</td></tr>
<tr><td><b>Status</b></td><td></td></tr>
</table>

<h3>Test Case 3: Add New Course (Positive)</h3>
<table>
<tr><th>Field</th><th>Details</th></tr>
<tr><td><b>Test Case ID</b></td><td>COURSE_ADD_001</td></tr>
<tr><td><b>Description</b></td><td>Verify that a student can successfully add a new course with valid details.</td></tr>
<tr><td><b>Test Type</b></td><td>Positive</td></tr>
<tr><td><b>Priority</b></td><td>High</td></tr>
<tr><td><b>Prerequisites</b></td><td>User is logged in and on the Courses screen.</td></tr>
<tr><td><b>Test Data</b></td><td>Name: "Data Structures", Code: "CS201", Instructor: "Dr. Smith", Room: "Lab 3B", Credits: 3</td></tr>
<tr><td><b>Test Steps</b></td><td>1. Navigate to the Courses screen.<br>2. Tap the Floating Action Button (+).<br>3. Enter course details and tap "Add".</td></tr>
<tr><td><b>Expected Result</b></td><td>The new course appears in the list inside a white card with Navy Blue course code and Navy Blue action icons.</td></tr>
<tr><td><b>Post-conditions</b></td><td>Course is saved successfully in the Room database.</td></tr>
<tr><td><b>Status</b></td><td></td></tr>
</table>

<h3>Test Case 4: Add Course with Blank Name (Negative)</h3>
<table>
<tr><th>Field</th><th>Details</th></tr>
<tr><td><b>Test Case ID</b></td><td>COURSE_ADD_002</td></tr>
<tr><td><b>Description</b></td><td>Verify that course creation fails with a validation error when the course name is blank.</td></tr>
<tr><td><b>Test Type</b></td><td>Negative</td></tr>
<tr><td><b>Priority</b></td><td>Medium</td></tr>
<tr><td><b>Prerequisites</b></td><td>Add Course dialog is open.</td></tr>
<tr><td><b>Test Data</b></td><td>Name: "" (Empty)</td></tr>
<tr><td><b>Test Steps</b></td><td>1. Open Add Course dialog, leave name blank, and tap "Add".</td></tr>
<tr><td><b>Expected Result</b></td><td>Error message "Course name is required" is displayed and dialog remains open.</td></tr>
<tr><td><b>Post-conditions</b></td><td>No course is added.</td></tr>
<tr><td><b>Status</b></td><td></td></tr>
</table>

<h3>Test Case 5: Add Task Under Course (Positive)</h3>
<table>
<tr><th>Field</th><th>Details</th></tr>
<tr><td><b>Test Case ID</b></td><td>TASK_ADD_001</td></tr>
<tr><td><b>Description</b></td><td>Verify that a student can successfully add a task under a specific course.</td></tr>
<tr><td><b>Test Type</b></td><td>Positive</td></tr>
<tr><td><b>Priority</b></td><td>High</td></tr>
<tr><td><b>Prerequisites</b></td><td>At least one course exists.</td></tr>
<tr><td><b>Test Data</b></td><td>Title: "Binary Trees Assignment", Priority: "HIGH"</td></tr>
<tr><td><b>Test Steps</b></td><td>1. Go to Courses screen.<br>2. Tap "Add Task to [Course]".<br>3. Enter task details and tap "Add".</td></tr>
<tr><td><b>Expected Result</b></td><td>Task is created and listed under the course's To-Do List section.</td></tr>
<tr><td><b>Post-conditions</b></td><td>Task saved with valid courseId.</td></tr>
<tr><td><b>Status</b></td><td></td></tr>
</table>

<h3>Test Case 6: Attempt Task / Exam / Schedule Creation Without Course (Negative)</h3>
<table>
<tr><th>Field</th><th>Details</th></tr>
<tr><td><b>Test Case ID</b></td><td>CONSTRAINT_ADD_001</td></tr>
<tr><td><b>Description</b></td><td>Verify that tasks, exams, and study schedules cannot be created unless a course has been added first.</td></tr>
<tr><td><b>Test Type</b></td><td>Negative</td></tr>
<tr><td><b>Priority</b></td><td>High</td></tr>
<tr><td><b>Prerequisites</b></td><td>Zero courses exist in the database.</td></tr>
<tr><td><b>Test Data</b></td><td>N/A</td></tr>
<tr><td><b>Test Steps</b></td><td>1. Navigate to Exams or Study Schedule or Tasks when zero courses exist.<br>2. Attempt to add a record.</td></tr>
<tr><td><b>Expected Result</b></td><td>App displays warning: "You must add at least one course before..." and prevents record creation.</td></tr>
<tr><td><b>Post-conditions</b></td><td>Referential integrity strictly maintained.</td></tr>
<tr><td><b>Status</b></td><td></td></tr>
</table>

<h3>Test Case 7: Toggle Task Completion (Positive)</h3>
<table>
<tr><th>Field</th><th>Details</th></tr>
<tr><td><b>Test Case ID</b></td><td>TASK_COMPLETE_001</td></tr>
<tr><td><b>Description</b></td><td>Verify that checking off a task moves it to Completed Tasks with line-through styling.</td></tr>
<tr><td><b>Test Type</b></td><td>Positive</td></tr>
<tr><td><b>Priority</b></td><td>Medium</td></tr>
<tr><td><b>Prerequisites</b></td><td>A task exists in To-Do list.</td></tr>
<tr><td><b>Test Data</b></td><td>Task title</td></tr>
<tr><td><b>Test Steps</b></td><td>1. Tap checkbox next to task.</td></tr>
<tr><td><b>Expected Result</b></td><td>Task moves to Completed Tasks section with strikethrough.</td></tr>
<tr><td><b>Post-conditions</b></td><td>isCompleted updated to true in DB.</td></tr>
<tr><td><b>Status</b></td><td></td></tr>
</table>

<h3>Test Case 8: Add Exam with Course Constraint (Positive)</h3>
<table>
<tr><th>Field</th><th>Details</th></tr>
<tr><td><b>Test Case ID</b></td><td>EXAM_ADD_001</td></tr>
<tr><td><b>Description</b></td><td>Verify that a student can schedule an exam linked to an existing course.</td></tr>
<tr><td><b>Test Type</b></td><td>Positive</td></tr>
<tr><td><b>Priority</b></td><td>High</td></tr>
<tr><td><b>Prerequisites</b></td><td>At least one course exists.</td></tr>
<tr><td><b>Test Data</b></td><td>Title: "Midterm Exam", Location: "Hall A"</td></tr>
<tr><td><b>Test Steps</b></td><td>1. Go to Exams screen, tap +, select course, enter title, and save.</td></tr>
<tr><td><b>Expected Result</b></td><td>Exam appears in the list.</td></tr>
<tr><td><b>Post-conditions</b></td><td>Exam saved with valid courseId.</td></tr>
<tr><td><b>Status</b></td><td></td></tr>
</table>

<h3>Test Case 9: Add Study Schedule with Course Constraint (Positive)</h3>
<table>
<tr><th>Field</th><th>Details</th></tr>
<tr><td><b>Test Case ID</b></td><td>SCH_ADD_001</td></tr>
<tr><td><b>Description</b></td><td>Verify that a student can add a study schedule linked to an existing course.</td></tr>
<tr><td><b>Test Type</b></td><td>Positive</td></tr>
<tr><td><b>Priority</b></td><td>High</td></tr>
<tr><td><b>Prerequisites</b></td><td>At least one course exists.</td></tr>
<tr><td><b>Test Data</b></td><td>Day: Monday, Time: 09:00 - 10:30</td></tr>
<tr><td><b>Test Steps</b></td><td>1. Navigate to Schedule screen, tap +, select course, day, and time, then save.</td></tr>
<tr><td><b>Expected Result</b></td><td>Schedule appears in the weekly timetable.</td></tr>
<tr><td><b>Post-conditions</b></td><td>Schedule saved with valid courseId.</td></tr>
<tr><td><b>Status</b></td><td></td></tr>
</table>

<h3>Test Case 10: Create Reminder & Trigger Notification (Positive)</h3>
<table>
<tr><th>Field</th><th>Details</th></tr>
<tr><td><b>Test Case ID</b></td><td>ALERT_ADD_001</td></tr>
<tr><td><b>Description</b></td><td>Verify that creating a reminder under Alerts triggers a system notification.</td></tr>
<tr><td><b>Test Type</b></td><td>Positive</td></tr>
<tr><td><b>Priority</b></td><td>Medium</td></tr>
<tr><td><b>Prerequisites</b></td><td>Notification permissions granted.</td></tr>
<tr><td><b>Test Data</b></td><td>Title: "Submit Essay"</td></tr>
<tr><td><b>Test Steps</b></td><td>1. Go to Alerts screen, tap +, enter title, and save.</td></tr>
<tr><td><b>Expected Result</b></td><td>Reminder appears in list and notification is posted.</td></tr>
<tr><td><b>Post-conditions</b></td><td>Reminder saved in DB.</td></tr>
<tr><td><b>Status</b></td><td></td></tr>
</table>

</body>
</html>
"@

Set-Content -Path $filePath -Value $html -Encoding UTF8
Write-Host "Word document successfully generated at $filePath"
