package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NotePage {

    @FindBy(id="nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id="addnote-button")
    private WebElement addNoteButton;

    @FindBy(id="editnote-button")
    private WebElement editNoteButton;

    @FindBy(id="deletenote-button")
    private WebElement deleteNoteButton;

    @FindBy(id="note-title")
    private WebElement noteTitleInput;

    @FindBy(id="note-description")
    private WebElement noteDescriptionInput;

    @FindBy(id="notesubmit-button")
    private WebElement noteSubmitButton;

    @FindBy(id="notetitle-display")
    private WebElement noteTitleDisplay;

    @FindBy(id="notedescription-display")
    private WebElement noteDescriptionDisplay;

    public NotePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void addNote(WebDriver driver, String title, String description){

        clickNotesTab(driver);
        clickAddNewNote(driver);

        writeAddInputTitle(driver,title);
        writeAddDescriptionTitle(driver,description);

        clickSubmitNote(driver);

    }

    public void editNote(WebDriver driver, String title, String description){
        clickNotesTab(driver);
        clickEditNote(driver);

        writeEditInputTitle(driver,title);
        writeEditDescriptionTitle(driver,description);

        clickSubmitNote(driver);
    }

    public void deleteNote(WebDriver driver){
        clickNotesTab(driver);

        clickDeleteNote(driver);
    }

    public void clickNotesTab(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try{
            wait.until(ExpectedConditions.visibilityOf(notesTab)).click();
        }catch (TimeoutException ex){
            System.out.println("clickNotesTab Error : " + ex);
        }

    }

    public void clickAddNewNote(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try{
            wait.until(ExpectedConditions.visibilityOf(addNoteButton)).click();
        }catch (TimeoutException ex){
            System.out.println("clickAddNewNote Error : " + ex);
        }
    }

    public void clickEditNote(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try{
            wait.until(ExpectedConditions.visibilityOf(editNoteButton)).click();
        }catch (TimeoutException ex){
            System.out.println("clickEditNote Error : " + ex);
        }

    }

    public void clickDeleteNote(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try{
            wait.until(ExpectedConditions.visibilityOf(deleteNoteButton)).click();
        }catch (TimeoutException ex){
            System.out.println("clickDeleteNote Error : " + ex);
        }
    }

    public void clickSubmitNote(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try{
            wait.until(ExpectedConditions.visibilityOf(noteSubmitButton)).click();
        }catch (TimeoutException ex){
            System.out.println("clickSubmitNote Error : " + ex);
        }
    }

    public void writeAddInputTitle(WebDriver driver,String title){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try{
            wait.until(ExpectedConditions.visibilityOf(noteTitleInput));
        }catch (TimeoutException ex){
            System.out.println("writeAddInputTitle Error : " + ex);
        }

        noteTitleInput.sendKeys(title);
    }

    public void writeAddDescriptionTitle(WebDriver driver,String title){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try{
            wait.until(ExpectedConditions.visibilityOf(noteDescriptionInput));
        }catch (TimeoutException ex){
            System.out.println("writeAddDescriptionTitle Error : " + ex);
        }

        noteDescriptionInput.sendKeys(title);
    }

    public void writeEditInputTitle(WebDriver driver,String title){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try{
            wait.until(ExpectedConditions.visibilityOf(noteTitleInput));
        }catch (TimeoutException ex){
            System.out.println("writeEditInputTitle Error : " + ex);
        }

        noteTitleInput.clear();
        noteTitleInput.sendKeys(title);
    }

    public void writeEditDescriptionTitle(WebDriver driver,String title){
        WebDriverWait wait = new WebDriverWait(driver, 10);
        try{
            wait.until(ExpectedConditions.visibilityOf(noteDescriptionInput));
        }catch (TimeoutException ex){
            System.out.println("writeEditDescriptionTitle Error : " + ex);
        }

        noteDescriptionInput.clear();
        noteDescriptionInput.sendKeys(title);
    }

    public String getNoteTitle() {
        try{
            return noteTitleDisplay.getText();
        } catch (NoSuchElementException e){
            return null;
        }

    }

    public String getNoteDescription() {
        try{
            return noteDescriptionDisplay.getText();
        } catch (NoSuchElementException e){
            return null;
        }
    }

}
