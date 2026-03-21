import com.sun.net.httpserver.HttpServer;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentFeedbackFormTest {

    private static HttpServer server;
    private static String baseUrl;
    private static Path rootDir;

    private WebDriver driver;

    @BeforeAll
    static void startServer() throws IOException {
        rootDir = Paths.get("").toAbsolutePath().normalize();
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);

        server.createContext("/", exchange -> {
            String requestPath = exchange.getRequestURI().getPath();
            if (requestPath.equals("/")) {
                requestPath = "/index.html";
            }

            Path target = rootDir.resolve(requestPath.substring(1)).normalize();
            if (!target.startsWith(rootDir) || !Files.exists(target) || Files.isDirectory(target)) {
                byte[] response = "Not Found".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(404, response.length);
                exchange.getResponseBody().write(response);
                exchange.close();
                return;
            }

            String contentType = guessContentType(target);
            exchange.getResponseHeaders().add("Content-Type", contentType);
            exchange.sendResponseHeaders(200, Files.size(target));
            try (InputStream in = Files.newInputStream(target)) {
                in.transferTo(exchange.getResponseBody());
            }
            exchange.close();
        });

        server.start();
        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort() + "/index.html";
    }

    @AfterAll
    static void stopServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    @BeforeEach
    void setupDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        String headless = System.getenv().getOrDefault("HEADLESS", "true").toLowerCase();
        if ("true".equals(headless)) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1366,900");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        driver = new ChromeDriver(options);
    }

    @AfterEach
    void teardownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void checkFormPageOpensSuccessfully() {
        driver.get(baseUrl);

        assertTrue(driver.getTitle().contains("Student Feedback Registration Form"));
        assertTrue(driver.findElement(By.id("form-heading")).isDisplayed());
    }

    @Test
    void enterValidDataAndVerifySuccessfulSubmission() {
        driver.get(baseUrl);
        fillValidData();

        driver.findElement(By.id("submitBtn")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(d -> d.findElement(By.id("successMessage")).getText().contains("Feedback submitted successfully."));
        assertTrue(driver.findElement(By.id("successMessage")).getText().contains("Feedback submitted successfully."));
    }

    @Test
    void leaveMandatoryFieldsBlankAndCheckErrorMessages() {
        driver.get(baseUrl);

        driver.findElement(By.id("submitBtn")).click();

        assertEquals("Student Name is required.", driver.findElement(By.id("nameError")).getText());
        assertEquals("Enter a valid email address.", driver.findElement(By.id("emailError")).getText());
        assertEquals("Mobile number must contain exactly 10 digits.", driver.findElement(By.id("mobileError")).getText());
        assertEquals("Please select a department.", driver.findElement(By.id("departmentError")).getText());
        assertEquals("Please select a gender.", driver.findElement(By.id("genderError")).getText());
        assertEquals("Feedback comments cannot be blank.", driver.findElement(By.id("commentsError")).getText());
    }

    @Test
    void enterInvalidEmailAndVerifyValidation() {
        driver.get(baseUrl);

        driver.findElement(By.id("studentName")).sendKeys("Ravi Kumar");
        driver.findElement(By.id("email")).sendKeys("invalid-email");
        driver.findElement(By.id("mobile")).sendKeys("9876543210");
        new Select(driver.findElement(By.id("department"))).selectByVisibleText("Computer Science");
        driver.findElement(By.cssSelector("input[name='gender'][value='Male']")).click();
        driver.findElement(By.id("comments")).sendKeys("This feedback has enough words to satisfy the minimum words requirement.");

        driver.findElement(By.id("submitBtn")).click();

        assertEquals("Enter a valid email address.", driver.findElement(By.id("emailError")).getText());
    }

    @Test
    void enterInvalidMobileAndVerifyValidation() {
        driver.get(baseUrl);

        driver.findElement(By.id("studentName")).sendKeys("Ravi Kumar");
        driver.findElement(By.id("email")).sendKeys("ravi.kumar@example.com");
        driver.findElement(By.id("mobile")).sendKeys("98AB543210");
        new Select(driver.findElement(By.id("department"))).selectByVisibleText("Computer Science");
        driver.findElement(By.cssSelector("input[name='gender'][value='Male']")).click();
        driver.findElement(By.id("comments")).sendKeys("This feedback has enough words to satisfy the minimum words requirement.");

        driver.findElement(By.id("submitBtn")).click();

        assertEquals("Mobile number must contain exactly 10 digits.", driver.findElement(By.id("mobileError")).getText());
    }

    @Test
    void checkDropdownSelectionWorksProperly() {
        driver.get(baseUrl);

        Select department = new Select(driver.findElement(By.id("department")));
        department.selectByVisibleText("Electronics");

        assertEquals("Electronics", department.getFirstSelectedOption().getText());
    }

    @Test
    void checkSubmitAndResetButtonsWork() {
        driver.get(baseUrl);

        WebElement studentName = driver.findElement(By.id("studentName"));
        studentName.sendKeys("Asha Sharma");

        driver.findElement(By.id("resetBtn")).click();

        assertEquals("", studentName.getDomProperty("value"));
    }

    private void fillValidData() {
        driver.findElement(By.id("studentName")).sendKeys("Ravi Kumar");
        driver.findElement(By.id("email")).sendKeys("ravi.kumar@example.com");
        driver.findElement(By.id("mobile")).sendKeys("9876543210");
        new Select(driver.findElement(By.id("department"))).selectByVisibleText("Computer Science");
        driver.findElement(By.cssSelector("input[name='gender'][value='Male']")).click();
        driver.findElement(By.id("comments")).sendKeys(
                "The faculty is supportive and labs are excellent for practical learning sessions every week."
        );
    }

    private static String guessContentType(Path file) {
        String name = file.getFileName().toString().toLowerCase();
        if (name.endsWith(".html")) {
            return "text/html; charset=utf-8";
        }
        if (name.endsWith(".css")) {
            return "text/css; charset=utf-8";
        }
        if (name.endsWith(".js")) {
            return "application/javascript; charset=utf-8";
        }
        return "application/octet-stream";
    }
}
