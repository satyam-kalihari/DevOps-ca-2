what what to do # Student Feedback Registration Form

This project satisfies all 5 requested subtasks using HTML, CSS, JavaScript, Selenium, and Jenkins.

## Project Structure

- `index.html` - Student Feedback Registration Form (HTML)
- `styles.css` - External CSS styling
- `script.js` - JavaScript validation logic
- `pom.xml` - Maven dependencies and test build setup
- `src/test/java/StudentFeedbackFormTest.java` - Selenium automation tests in Java (JUnit 5)
- `Jenkinsfile` - Jenkins CI pipeline for Selenium tests

## Sub Task Coverage

1. Create Registration Form using HTML

- Includes fields: Student Name, Email ID, Mobile Number, Department, Gender, Feedback Comments
- Includes Submit and Reset buttons

2. Use of CSS

- External CSS in `styles.css`
- Internal CSS in `<style>` block inside `index.html`

3. Use of JavaScript Validation

- Student Name non-empty
- Valid email format
- Mobile number digits only (exactly 10)
- Gender selection required
- Department selection required
- Feedback comments required with at least 10 words

4. Use of Selenium

- Test page opens successfully
- Valid data submission success
- Blank mandatory field errors
- Invalid email validation
- Invalid mobile validation
- Department dropdown selection
- Submit and Reset behavior

5. Jenkins Automation

- Jenkinsfile to run `mvn clean test` for Selenium test execution
- JUnit XML reports generated and archived from Surefire output

## How to Run Locally (Windows)

1. Ensure prerequisites are installed:

- Java 17+
- Maven 3.9+
- Google Chrome

```powershell
java -version
mvn -version
```

2. Run Selenium tests (Java + JUnit):

```powershell
mvn -B clean test
```

3. Open the form manually in a browser:

- Double-click `index.html` or serve via:

```powershell
python -m http.server 8000
```

Then open `http://localhost:8000/index.html`.

## Jenkins Setup Steps (for screenshots)

1. Install Jenkins and open dashboard (`http://localhost:8080` usually).
2. Create a **New Item** -> **Pipeline**.
3. Link repository:

- If GitHub: set repository URL and credentials.
- If local folder: use Jenkins workspace + copy project files.

4. In pipeline config, use **Pipeline script from SCM** and point to this repo.
5. Ensure Jenkins has JDK + Maven tools configured in **Global Tool Configuration**.
6. Execute **Build Now**.
7. Open build results and test report.

## One-Pass Screenshot Capture Plan

Use this exact sequence once, start to finish:

1. Open `index.html` in browser and capture full form UI.
2. In editor, open `index.html` and capture the internal `<style>` block.
3. In editor, open `styles.css` and capture external CSS.
4. In browser, click Submit with blank fields and capture error messages.
5. In browser, enter invalid email and capture email validation message.
6. In browser, enter invalid mobile and capture mobile validation message.
7. In browser, enter fully valid data and capture success message.
8. In terminal, run `mvn -B clean test` and capture:

- command being run
- final `BUILD SUCCESS` summary
- test count summary

9. In editor, open `src/test/java/StudentFeedbackFormTest.java` and capture test methods.
10. Open Jenkins dashboard and capture home page.
11. Open your Jenkins job config and capture SCM + pipeline settings.
12. Click **Build Now** and capture build queue/running state.
13. Capture completed build result page.
14. Open console output and capture `mvn -B clean test` with pass/fail details.
15. Open test result page and capture JUnit report.

## Screenshot Checklist (Important)

Take screenshots for your submission in this order:

1. Form UI page open in browser (show all input fields and buttons).
2. Internal + external CSS evidence:

- `index.html` showing `<style>` block
- `styles.css` file open

3. JavaScript validation demo:

- blank submission showing errors
- invalid email showing error
- invalid mobile showing error
- success message after valid submission

4. Selenium tests:

- terminal with `mvn -B clean test` command and passing tests summary
- test file (`src/test/java/StudentFeedbackFormTest.java`) open in editor

5. Jenkins:

- Jenkins dashboard
- job configuration page
- build execution in progress/completed
- successful (or failed) build result page with console output
- test report (`target/surefire-reports/*.xml`) or Jenkins test result screen

## Notes

- Selenium browser driver is managed automatically by WebDriverManager.
- If you want to see browser UI while tests run, set:

```powershell
$env:HEADLESS="false"
mvn -B test
```
# DevOps-ca-2
