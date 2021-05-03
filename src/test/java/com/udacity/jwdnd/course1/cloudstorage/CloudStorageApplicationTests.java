package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
class CloudStorageApplicationTests {

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
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

	}

//	@AfterEach
//	public void afterEach() {
//		if (this.driver != null) {
//			driver.quit();
//		}
//	}

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
	public void testAuthorizedUserAccess() throws InterruptedException {


		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginUser(username, password);
		assertEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());

		HomePage homePage = new HomePage(driver);
		Thread.sleep(1000);
		homePage.logout();

		driver.get("http://localhost:" + port + "/home");
		assertNotEquals("http://localhost:" + port + "/home", driver.getCurrentUrl());


	}



	@Test
	public void testCreateEditDeleteNote() throws InterruptedException {


		String title = "First Note";
		String description = "This is my first note. ";
		String title2 = "Second Note";
		String description2 = "This is my second note. ";

		LoginPage loginPage = new LoginPage(driver);
		loginPage.loginUser(username, password);

		//create note
		WebDriverWait wait = new WebDriverWait(driver, 120);
		HomePage homePage = new HomePage(driver);
		WebElement notesTab = homePage.getNotesTab();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait.until(ExpectedConditions.elementToBeClickable(notesTab)).click();


		wait.until(ExpectedConditions.elementToBeClickable(By.id("notes-button"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-title"))).sendKeys(title);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("note-description"))).sendKeys(description);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("save-button"))).click();

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		driver.get("http://localhost:" + port + "/home");

		wait.until(ExpectedConditions.elementToBeClickable(notesTab)).click();
		String actualTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("title-display"))).getText();
		String actualDescription = wait.until(ExpectedConditions.elementToBeClickable(By.id("description-display"))).getText();
		assertEquals(title, actualTitle);
		assertEquals(description, actualDescription);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("editNote"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("editNote-title"))).clear();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("editNote-title"))).sendKeys(title2);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("editNote-description"))).clear();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("editNote-description"))).sendKeys(description2);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("editNoteSaveButton"))).click();

		driver.get("http://localhost:" + port + "/home");

		wait.until(ExpectedConditions.elementToBeClickable(notesTab)).click();
		actualTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("title-display"))).getText();
		actualDescription = wait.until(ExpectedConditions.elementToBeClickable(By.id("description-display"))).getText();
		assertEquals(title2, actualTitle);
		assertEquals(description2, actualDescription);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("deleteNote"))).click();

		driver.get("http://localhost:" + port + "/home");

		wait.until(ExpectedConditions.elementToBeClickable(notesTab)).click();

		assertThrows(NoSuchElementException.class, homePage::getDisplayedTitle);
		assertThrows(NoSuchElementException.class, homePage::getDisplayedDescription);
	}

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
		WebDriverWait wait = new WebDriverWait(driver, 200);
		HomePage homePage = new HomePage(driver);
		WebElement credentialsTab = homePage.getCredentialsTab();
		wait.until(ExpectedConditions.elementToBeClickable(credentialsTab)).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.id("creds-button"))).click();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-url"))).sendKeys(url);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-username"))).sendKeys(username);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password"))).sendKeys(password);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("save-cred"))).click();

		driver.get("http://localhost:" + port + "/home");

		wait.until(ExpectedConditions.elementToBeClickable(credentialsTab)).click();
		String actualUrl = wait.until(ExpectedConditions.elementToBeClickable(By.id("url-display"))).getText();
		String actualUsername = wait.until(ExpectedConditions.elementToBeClickable(By.id("username-display"))).getText();
		String actualPassword = wait.until(ExpectedConditions.elementToBeClickable(By.id("password-display"))).getText();
		assertEquals(url, actualUrl);
		assertEquals(username, actualUsername);
		assertNotEquals(password, actualPassword);

		//edit credential
		wait.until(ExpectedConditions.elementToBeClickable(By.id("editCred"))).click();
		String decryptedPassword = wait.until(ExpectedConditions.elementToBeClickable(By.id("editCredential-password"))).getAttribute("value");
		assertEquals(password, decryptedPassword);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("editCredential-url"))).clear();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("editCredential-url"))).sendKeys(url2);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("editCredential-username"))).clear();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("editCredential-username"))).sendKeys(username2);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("editCredential-password"))).clear();
		wait.until(ExpectedConditions.elementToBeClickable(By.id("editCredential-password"))).sendKeys(password2);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("editCredentialSaveButton"))).click();

		driver.get("http://localhost:" + port + "/home");

		wait.until(ExpectedConditions.elementToBeClickable(credentialsTab)).click();
		actualUrl = wait.until(ExpectedConditions.elementToBeClickable(By.id("url-display"))).getText();
		actualUsername = wait.until(ExpectedConditions.elementToBeClickable(By.id("username-display"))).getText();
		actualPassword = wait.until(ExpectedConditions.elementToBeClickable(By.id("password-display"))).getText();
		assertEquals(url2, actualUrl);
		assertEquals(username2, actualUsername);
		assertNotEquals(password2, actualPassword);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("editCred"))).click();
		decryptedPassword = wait.until(ExpectedConditions.elementToBeClickable(By.id("editCredential-password"))).getAttribute("value");
		assertEquals(password2, decryptedPassword);

		wait.until(ExpectedConditions.elementToBeClickable(By.id("close-button"))).click();

		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		wait.until(ExpectedConditions.elementToBeClickable(By.id("deleteCredential"))).click();

		driver.get("http://localhost:" + port + "/home");

		wait.until(ExpectedConditions.elementToBeClickable(credentialsTab)).click();

		assertThrows(NoSuchElementException.class, homePage::getDisplayedUrl);
		assertThrows(NoSuchElementException.class, homePage::getDisplayedUsername);
		assertThrows(NoSuchElementException.class, homePage::getDisplayedPassword);
	}
}