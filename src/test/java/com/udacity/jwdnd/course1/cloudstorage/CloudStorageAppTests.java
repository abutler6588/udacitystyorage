package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageAppTests {

    @LocalServerPort
    private Integer port;

    private static WebDriver driver;
    private String username;
    private String password;

    @BeforeAll
    public static void beforeAll()
    {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @AfterAll
    public static void afterAll()
    {
        driver.quit();
    }

    @BeforeEach
    public void beforeEach()
    {
        username = "abutler202188";
        password = "Password1*";
        driver.get("http://localhost:" + port + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        signupPage.signupUser("Janet", "Jackson", username, password);
        driver.get("http://localhost:" + port + "/login");
    }
    //	@AfterEach
//	public void afterEach() {
//		if (this.driver != null) {
//			driver.quit();
//		}
//	}

    /**
     * User Rubric - Tests for User Signup, Login, and Unauthorized Access Restrictions.
     */

    @Test
    public void testUnauthorizedUserAccess()
    {
        driver.get("http://localhost:" + port + "/signup");
        assertEquals("http://localhost:" + port + "/signup", driver.getCurrentUrl());
        driver.get("http://localhost:" + port + "/login");
        assertEquals("http://localhost:" + port + "/login", driver.getCurrentUrl());
        driver.get("http://localhost:" + port + "/home");
        assertNotEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());
    }


    @Test
    public void testAuthorizedUser() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginUser(username, password);
        assertEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());
        HomePage homePage = new HomePage(driver);
        homePage.logout();
        driver.get("http://localhost:" + port + "/home");
        assertNotEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());
    }

    /**
     * Note Rubric - Tests for Note Creation, Viewing, Editing, and Deletion.
     */

    @Test
    public void testCreateEditDeleteNote() {

        String title = "First Note";
        String description = "This is my first note.";
        String title2 = "Second Note";
        String description2 = "This is my second note.";

        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginUser(username, password);

        //note creation
        WebDriverWait wait = new WebDriverWait(driver, 4);
        HomePage homePage = new HomePage(driver);
        WebElement notesTab = homePage.getNotesTab();
        wait.until(ExpectedConditions.elementToBeClickable(notesTab));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", notesTab);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAddNewNote")));
        WebElement noteButton = driver.findElement(By.id("btnAddNewNote"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", noteButton);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
        WebElement noteTitle = driver.findElement(By.id("note-title"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + title + "';", noteTitle);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-description")));
        WebElement noteDescription = driver.findElement(By.id("note-description"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + description + "';", noteDescription);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSaveChanges")));
        WebElement saveButton = driver.findElement(By.id("btnSaveChanges"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveButton);

        driver.get("http://localhost:" + port + "/home");

        wait.until(ExpectedConditions.elementToBeClickable(notesTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", notesTab);
        String actualTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("tableNoteTitle"))).getText();
        String actualDescription = wait.until(ExpectedConditions.elementToBeClickable(By.id("tableNoteDescription"))).getText();
        assertEquals(title, actualTitle);
        assertEquals(description, actualDescription);

        //modify note
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btnEditNote")));
        WebElement editNoteButton = driver.findElement(By.id("btnEditNote"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editNoteButton);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title")));
        WebElement editNoteTitle = driver.findElement(By.id("note-title"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + title2 + "';", editNoteTitle);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("note-description")));
        WebElement editNoteDescription = driver.findElement(By.id("note-description"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + description2 + "';", editNoteDescription);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btnSaveChanges")));
        WebElement editNoteSaveButton = driver.findElement(By.id("btnSaveChanges"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editNoteSaveButton);

        driver.get("http://localhost:" + port + "/home");

        wait.until(ExpectedConditions.elementToBeClickable(notesTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", notesTab);
        actualTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("tableNoteTitle"))).getText();
        actualDescription = wait.until(ExpectedConditions.elementToBeClickable(By.id("tableNoteDescription"))).getText();
        assertEquals(title2, actualTitle);
        assertEquals(description2, actualDescription);

        //note deletion
        wait.until(ExpectedConditions.elementToBeClickable(By.id("ancDeleteNote")));
        WebElement deleteNoteButton = driver.findElement(By.id("ancDeleteNote"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteNoteButton);

        driver.get("http://localhost:" + port + "/home");

        wait.until(ExpectedConditions.elementToBeClickable(notesTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", notesTab);
        assertThrows(NoSuchElementException.class, homePage::getDisplayedTitle);
        assertThrows(NoSuchElementException.class, homePage::getDisplayedDescription);
    }

    /**
     * Credential Rubric - Tests for Credential Creation, Viewing, Editing, and Deletion.
     */

    @Test
    public void testCreateEditDeleteCredential()
    {
        String url = "www.google.com";
        String url2 = "localhost:8080/home";
        String username2 = "Abutler2021";
        String password2 = "ab3288";
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginUser(username, password);

        //create credential
        WebDriverWait wait = new WebDriverWait(driver, 4);
        HomePage homePage = new HomePage(driver);
        WebElement credentialsTab = homePage.getCredentialsTab();
        wait.until(ExpectedConditions.elementToBeClickable(credentialsTab));

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", credentialsTab);
//		wait.until(ExpectedConditions.elementToBeClickable(By.id("creds-button"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btnAddNewCredential")));
        WebElement credentialButton = driver.findElement(By.id("btnAddNewCredential"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", credentialButton);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
        WebElement credentialUrl = driver.findElement(By.id("credential-url"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + url + "';", credentialUrl);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-username")));
        WebElement credentialUsername = driver.findElement(By.id("credential-username"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + username + "';", credentialUsername);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password")));
        WebElement credentialPassword = driver.findElement(By.id("credential-password"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + password + "';", credentialPassword);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btnCredentialSaveChanges")));
        WebElement saveCredential = driver.findElement(By.id("btnCredentialSaveChanges"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", saveCredential);

        driver.get("http://localhost:" + port + "/home");

        wait.until(ExpectedConditions.elementToBeClickable(credentialsTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", credentialsTab);
        String actualUrl = wait.until(ExpectedConditions.elementToBeClickable(By.id("tblCredentialUrl"))).getText();
        String actualUsername = wait.until(ExpectedConditions.elementToBeClickable(By.id("tblCredentialUsername"))).getText();
        String actualPassword = wait.until(ExpectedConditions.elementToBeClickable(By.id("tblCredentialPassword"))).getText();
        assertEquals(url, actualUrl);
        assertEquals(username, actualUsername);
        assertNotEquals(password, actualPassword);

        //edit credential
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btnEditCredential")));
        WebElement editCredentialButton = driver.findElement(By.id("btnEditCredential"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editCredentialButton);
        String decryptedPassword = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password"))).getAttribute("value");
        assertEquals(password, decryptedPassword);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url")));
        WebElement editCredentialUrl = driver.findElement(By.id("credential-url"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + url2 + "';", editCredentialUrl);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-username")));
        WebElement editCredentialUsername = driver.findElement(By.id("credential-username"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + username2 + "';", editCredentialUsername);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password")));
        WebElement editCredentialPassword = driver.findElement(By.id("credential-password"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + password2 + "';", editCredentialPassword);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btnCredentialSaveChanges")));
        WebElement editCredentialSaveButton = driver.findElement(By.id("btnCredentialSaveChanges"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editCredentialSaveButton);

        driver.get("http://localhost:" + port + "/home");

        wait.until(ExpectedConditions.elementToBeClickable(credentialsTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", credentialsTab);
        actualUrl = wait.until(ExpectedConditions.elementToBeClickable(By.id("tblCredentialUrl"))).getText();
        actualUsername = wait.until(ExpectedConditions.elementToBeClickable(By.id("tblCredentialUsername"))).getText();
        actualPassword = wait.until(ExpectedConditions.elementToBeClickable(By.id("tblCredentialPassword"))).getText();
        assertEquals(url2, actualUrl);
        assertEquals(username2, actualUsername);
        assertNotEquals(password2, actualPassword);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("btnEditCredential")));
        editCredentialButton = driver.findElement(By.id("btnEditCredential"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", editCredentialButton);
        decryptedPassword = wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password"))).getAttribute("value");
        assertEquals(password2, decryptedPassword);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("close-button")));
        WebElement closeCredentialButton = driver.findElement(By.id("close-button"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", closeCredentialButton);

        //delete credential
        wait.until(ExpectedConditions.elementToBeClickable(By.id("aDeleteCredential")));
        WebElement deleteCredentialButton = driver.findElement(By.id("aDeleteCredential"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", deleteCredentialButton);

        driver.get("http://localhost:" + port + "/home");

        wait.until(ExpectedConditions.elementToBeClickable(credentialsTab));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", credentialsTab);
        assertThrows(NoSuchElementException.class, homePage::getDisplayedUrl);
        assertThrows(NoSuchElementException.class, homePage::getDisplayedUsername);
        assertThrows(NoSuchElementException.class, homePage::getDisplayedPassword);
    }
}
