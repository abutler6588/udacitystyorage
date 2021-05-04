package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
public class LoginPage {
    @FindBy(id = "inputUsername")
    private WebElement usernameField;
    @FindBy(id = "inputPassword")
    private WebElement passwordField;
    @FindBy(id = "login-button")
    private WebElement loginButton;
    private final WebDriver driver;
    public LoginPage(WebDriver driver)
    {   this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    public void loginUser(String username, String password)
    {
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + username + "';", usernameField);
        ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + password + "';", passwordField);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginButton);
    }
}