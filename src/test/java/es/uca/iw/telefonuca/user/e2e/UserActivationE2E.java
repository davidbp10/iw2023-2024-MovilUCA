package es.uca.iw.telefonuca.user.e2e;

import es.uca.iw.telefonuca.user.ObjectMother;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.services.UserManagementService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author ivanruizrube
 */

// La etiqueta @Transactional no funciona con selenium y hay que restaurar
// manualmente el estado de la BD
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserActivationE2E {
    private final String uribase = "http://127.0.0.1:";
    @LocalServerPort
    private int port;
    private WebDriver driver;
    private User testUser;

    @Autowired
    private UserManagementService userManagementService;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();

    }

    @BeforeEach
    void setUp() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox");
        // chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("disable-gpu");
        chromeOptions.addArguments("--window-size=1920,1200");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.quit();
        }

        if (testUser != null && testUser.getId() != null) {
            userManagementService.delete(testUser);
        }
    }

    @Test
    void shouldNotActivateANoExistingUser() {

        // Given
        // a certain user
        testUser = ObjectMother.createTestUser();

        // point the browser to the activation page
        driver.get(uribase + port + "/useractivation");

        // and introduce form data
        driver.findElement(By.id("email")).sendKeys(testUser.getEmail());
        driver.findElement(By.id("secretCode")).sendKeys("key");

        // and press the activate button
        driver.findElement(By.id("activate")).click();

        // and
        WebElement element = driver.findElement(By.id("status"));

        assertThat(element.getText().equals("Ups. El usuario no ha podido ser activado")).isTrue();

    }

    @Test
    void shouldActivateAnExistingUser() {

        // Given
        // a certain user
        testUser = ObjectMother.createTestUser();

        // who is registered
        userManagementService.registerUser(testUser);

        // When

        // point the browser to the activation page
        driver.get(uribase + port + "/useractivation");

        // and introduce form data
        driver.findElement(By.id("email")).sendKeys(testUser.getEmail());
        driver.findElement(By.id("secretCode")).sendKeys(testUser.getRegisterCode());

        // and press the activate button
        driver.findElement(By.id("activate")).click();

        // Then
        WebElement element = driver.findElement(By.id("status"));

        assertThat(element.getText().equals("Enhorabuena. El usuario ha sido activado")).isTrue();

    }
}
