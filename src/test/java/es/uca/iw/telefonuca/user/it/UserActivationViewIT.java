package es.uca.iw.telefonuca.user.it;

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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author ivanruizrube
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserActivationViewIT {

    private final String uribase = "http://127.0.0.1:";
    @LocalServerPort
    private int port;
    private WebDriver driver;

    @MockBean
    private UserManagementService userManagementService;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("disable-gpu");
        chromeOptions.addArguments("--window-size=1920,1200");

        driver = new ChromeDriver(chromeOptions);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }

    @AfterEach
    void teardown() {
        if (driver != null) {
            driver.close();

            driver.quit();
        }
    }

    @Test
    void shouldShowFailureMessageWhenUserIsNotActivated() {

        // Given
        // a certain user
        User testUser = ObjectMother.createTestUser();

        // and the service is stubbed for the activateUser method
        given(userManagementService.activateUser(anyString(), anyString())).willReturn(false);

        // When
        // point the browser to the activation page
        driver.get(uribase + port + "/useractivation");

        // and introduce form data
        WebElement email = driver.findElement(By.id("email"));
        email.sendKeys(testUser.getEmail());
        WebElement secretCode = driver.findElement(By.id("secretCode"));
        secretCode.sendKeys("key");

        // and press the activate button
        driver.findElement(By.id("activate")).click();

        // Then
        WebElement element = driver.findElement(By.id("status"));
        assertThat(element.getText().equals("Ups. El usuario no ha podido ser activado")).isTrue();

        // and
        verify(userManagementService, times(1)).activateUser(anyString(), anyString());

    }

    @Test
    void shouldShowSuccessMessageWhenUserIsActivated() {

        // Given
        // a certain user
        User testUser = ObjectMother.createTestUser();

        // and the service is stubbed for the activateUser method
        given(userManagementService.activateUser(anyString(), anyString())).willReturn(true);

        // When

        // point the browser to the activation page
        driver.get(uribase + port + "/useractivation");

        // and introduce form data
        WebElement email = driver.findElement(By.id("email"));
        email.sendKeys(testUser.getEmail());
        WebElement secretCode = driver.findElement(By.id("secretCode"));
        secretCode.sendKeys("key");

        // and press the activate button
        driver.findElement(By.id("activate")).click();

        // Then
        WebElement element = driver.findElement(By.id("status"));
        assertThat(element.getText().equals("Enhorabuena. El usuario ha sido activado")).isTrue();

        // and
        // verify(userManagementService, times(1)).activateUser(anyString(),
        // anyString());

    }

}
