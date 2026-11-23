# TEST CASE TEMPLATE
**Mobile Applications Development Project**
**4th Year – Test Design Submission**

---

## 1. Instructions
* Your test cases are based on the updated project documentation and UI created.
* Each feature has at least one positive test case and one negative test case.
* Test Case IDs follow the format `MODULE_FEATURE_NNN`.
* Status field is left blank for test execution.

---

## TEST CASES

### Test Case 1: Successful User Account Creation / Login (Positive)
| Field | Details |
| :--- | :--- |
| **Test Case ID** | `AUTH_LOGIN_001` |
| **Description** | Verify that a student can successfully create an account or log in by entering a valid Full Name (First & Last Name) and optional email. |
| **Test Type** | Positive |
| **Priority** | High |
| **Prerequisites** | App is launched on fresh install or after session reset. |
| **Test Data** | Name: "Alex Johnson", Email: "alex@university.edu", Password: "Password123" |
| **Test Steps** | 1. Launch the Student Study Planner app.<br>2. View the Create Account / Log In screen.<br>3. Enter "Alex Johnson" in the Full Name field.<br>4. Enter password and confirmation.<br>5. Tap "Create Account". |
| **Expected Result** | The app validates that the name contains both first and last name, saves the profile in the Room database, and transitions to the Dashboard. |
| **Post-conditions** | User session is active; user profile is persisted locally. |
| **Status** | |

---

### Test Case 2: Reject Account Creation with Single Name Only (Negative)
| Field | Details |
| :--- | :--- |
| **Test Case ID** | `AUTH_LOGIN_002` |
| **Description** | Verify that the app rejects account creation when the user enters only a single name (missing last name). |
| **Test Type** | Negative |
| **Priority** | High |
| **Prerequisites** | Create Account screen is open. |
| **Test Data** | Name: "Alex" (Single name only) |
| **Test Steps** | 1. Enter "Alex" in the Full Name field.<br>2. Fill in password and tap "Create Account". |
| **Expected Result** | The app rejects registration and displays error message: *"Please enter both your first name and last name (Full Name)."* |
| **Post-conditions** | Account creation is prevented. |
| **Status** | |

---

### Test Case 3: Add New Course (Positive)
| Field | Details |
| :--- | :--- |
| **Test Case ID** | `COURSE_ADD_001` |
| **Description** | Verify that a student can successfully add a new course with valid details. |
| **Test Type** | Positive |
| **Priority** | High |
| **Prerequisites** | User is logged in and on the Courses screen. |
| **Test Data** | Name: "Data Structures", Code: "CS201", Instructor: "Dr. Smith", Room: "Lab 3B", Credits: 3 |
| **Test Steps** | 1. Navigate to the Courses screen.<br>2. Tap the Floating Action Button (`+`).<br>3. Enter course details and tap "Add". |
| **Expected Result** | The new course appears in the list inside a white card with Navy Blue course code and Navy Blue action icons. |
| **Post-conditions** | Course is saved successfully in the Room database. |
| **Status** | |

---

### Test Case 4: Add Course with Blank Name (Negative)
| Field | Details |
| :--- | :--- |
| **Test Case ID** | `COURSE_ADD_002` |
| **Description** | Verify that course creation fails with a validation error when the course name is blank. |
| **Test Type** | Negative |
| **Priority** | Medium |
| **Prerequisites** | Add Course dialog is open. |
| **Test Data** | Name: `""` (Empty) |
| **Test Steps** | 1. Open Add Course dialog, leave name blank, and tap "Add". |
| **Expected Result** | Error message *"Course name is required"* is displayed and dialog remains open. |
| **Post-conditions** | No course is added. |
| **Status** | |

---

### Test Case 5: Add Task Under Course (Positive)
| Field | Details |
| :--- | :--- |
| **Test Case ID** | `TASK_ADD_001` |
| **Description** | Verify that a student can successfully add a task under a specific course. |
| **Test Type** | Positive |
| **Priority** | High |
| **Prerequisites** | At least one course exists. |
| **Test Data** | Title: "Binary Trees Assignment", Priority: "HIGH" |
| **Test Steps** | 1. Go to Courses screen.<br>2. Tap "Add Task to [Course]".<br>3. Enter task details and tap "Add". |
| **Expected Result** | Task is created and listed under the course's To-Do List section. |
| **Post-conditions** | Task saved with valid `courseId`. |
| **Status** | |

---

### Test Case 6: Attempt Task / Exam / Schedule Creation Without Course (Negative)
| Field | Details |
| :--- | :--- |
| **Test Case ID** | `CONSTRAINT_ADD_001` |
| **Description** | Verify that tasks, exams, and study schedules cannot be created unless a course has been added first. |
| **Test Type** | Negative |
| **Priority** | High |
| **Prerequisites** | Zero courses exist in the database. |
| **Test Data** | N/A |
| **Test Steps** | 1. Navigate to Exams or Study Schedule or Tasks when zero courses exist.<br>2. Attempt to add a record. |
| **Expected Result** | App displays warning: *"You must add at least one course before..."* and prevents record creation. |
| **Post-conditions** | Referential integrity strictly maintained. |
| **Status** | |

---

### Test Case 7: Toggle Task Completion (Positive)
| Field | Details |
| :--- | :--- |
| **Test Case ID** | `TASK_COMPLETE_001` |
| **Description** | Verify that checking off a task moves it to Completed Tasks with line-through styling. |
| **Test Type** | Positive |
| **Priority** | Medium |
| **Prerequisites** | A task exists in To-Do list. |
| **Test Data** | Task title |
| **Test Steps** | 1. Tap checkbox next to task. |
| **Expected Result** | Task moves to Completed Tasks section with strikethrough. |
| **Post-conditions** | `isCompleted` updated to `true` in DB. |
| **Status** | |

---

### Test Case 8: Add Exam with Course Constraint (Positive)
| Field | Details |
| :--- | :--- |
| **Test Case ID** | `EXAM_ADD_001` |
| **Description** | Verify that a student can schedule an exam linked to an existing course. |
| **Test Type** | Positive |
| **Priority** | High |
| **Prerequisites** | At least one course exists. |
| **Test Data** | Title: "Midterm Exam", Location: "Hall A" |
| **Test Steps** | 1. Go to Exams screen, tap `+`, select course, enter title, and save. |
| **Expected Result** | Exam appears in the list. |
| **Post-conditions** | Exam saved with valid `courseId`. |
| **Status** | |

---

### Test Case 9: Add Study Schedule with Course Constraint (Positive)
| Field | Details |
| :--- | :--- |
| **Test Case ID** | `SCH_ADD_001` |
| **Description** | Verify that a student can add a study schedule linked to an existing course. |
| **Test Type** | Positive |
| **Priority** | High |
| **Prerequisites** | At least one course exists. |
| **Test Data** | Day: Monday, Time: 09:00 - 10:30 |
| **Test Steps** | 1. Go to Schedule screen, tap `+`, select course, day, and time, then save. |
| **Expected Result** | Schedule appears in the weekly timetable. |
| **Post-conditions** | Schedule saved with valid `courseId`. |
| **Status** | |

---

### Test Case 10: Create Reminder & Trigger Notification (Positive)
| Field | Details |
| :--- | :--- |
| **Test Case ID** | `ALERT_ADD_001` |
| **Description** | Verify that creating a reminder under Alerts triggers a system notification. |
| **Test Type** | Positive |
| **Priority** | Medium |
| **Prerequisites** | Notification permissions granted. |
| **Test Data** | Title: "Submit Essay" |
| **Test Steps** | 1. Go to Alerts screen, tap `+`, enter title, and save. |
| **Expected Result** | Reminder appears in list and notification is posted. |
| **Post-conditions** | Reminder saved in DB. |
| **Status** | |
