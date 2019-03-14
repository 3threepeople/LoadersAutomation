package Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Utility {

  private static void submitTicket(WebElement webElement,
                                  WebDriver webDriver,
                                  String tickeknumber,
                                  WebDriverWait wait) {
    webElement.clear();
    webElement.sendKeys(tickeknumber.trim());
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(
        "clickSubmitButton")));
    webDriver.findElement(By.id("clickSubmitButton")).click();
  }

  private static boolean SelectLoader(String radio,
                                     WebDriver driver,
                                     WebDriverWait wait,
                                     JavascriptExecutor executor) {
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(radio)));
      WebElement radiofind = driver.findElement(By.id(radio));
      executor.executeScript("arguments[0].click();", radiofind);
      return true;
    } catch (TimeoutException e) {
      return false;
    }
  }

  public static List<String> getListofJsons(String[] JsonFiles, List<String> Jsons) {
    try {
      for (String JsonFile:JsonFiles) {
        if (JsonFile.endsWith(".json"))
          Jsons.add(JsonFile);
      }
      return Jsons;
    } catch (NullPointerException e) {
      return null;
    }
  }

  public static Boolean ClickLoader(String radio,
                                    String loadername,
                                    WebDriver driver,
                                    WebDriverWait wait,
                                    JavascriptExecutor executor,
                                    Logger logger) {
    if (null != radio) {
      logger.info("Processing Loader: " + loadername);
      if (!(SelectLoader(radio, driver, wait, executor))) {
        logger.warn("Cannot find the loader:" + loadername);
        return false;
      }
      return true;
    } else {
      logger.warn("This Loader is not present in our Configurations:" + loadername);
      return false;
    }
  }

  public static void ClickLoadButton(WebDriver driver,
                                     WebDriverWait wait,
                                     JavascriptExecutor executor) {
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loadToDb")));
    WebElement ele = driver.findElement(By.id("loadToDb"));
    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);
    executor.executeScript("arguments[0].click();", ele);
  }

  public static Boolean IsTicketPopUp(WebDriver driver, WebDriverWait wait, String TicketNumber) {
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
          "//h4[contains(text(),\"Redmine ticket number\")]")));
      WebElement Ticket = driver.findElement(By.id("ticketNumber"));
      submitTicket(Ticket, driver, TicketNumber, wait);
      return true;
    } catch (TimeoutException e) {
      return false;
    }
  }

  public static Boolean IsOverRidePopUp(WebDriver driver, WebDriverWait wait,Logger logger) {
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
          "//h4[contains(text(),\"Data already exists. Do you want to overwrite?\")]")));
      WebElement element = driver.findElement(By.xpath(
          "//button[contains(text(),'YES')]"));
      ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
      return true;
    } catch (TimeoutException e) {
      return false;
    }
    catch (UnhandledAlertException e)
    {
      logger.warn("Alert Seen");
      driver.switchTo().alert().accept();
      return false;
    }
  }

  public static Boolean ClickSuccessfullyConfigured(WebDriver driver,
                                                    WebDriverWait wait,
                                                    Logger logger,
                                                    String Json,
                                                    String loadername) {
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
          "//h4[contains(text(),\"Successfully configured\")]")));
      WebElement OK = driver.findElements(By.xpath("//button[contains(text(),'OK')]"))
          .get(1);
      ((JavascriptExecutor) driver).executeScript("arguments[0].click();", OK);
      logger.info(Json + " is loaded: " + loadername);
      return true;
    }
    catch (TimeoutException e) {
      return false;
    }
    catch (UnhandledAlertException e)
    {
      logger.warn("Alert Seen");
      driver.switchTo().alert().accept();
      return false;
    }
  }

  public static Boolean IsSubmitRolesPopUp(WebDriver driver,WebDriverWait wait,String JsonDirectory,String Json,Logger logger)
  throws IOException{
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
          "//h4[contains(text(),\"Enter roles to configure same data\")]")));
      File TextFile = new File(JsonDirectory + "/" + Json.replace(".json", ".txt"));
      if (TextFile.exists()) {
        BufferedReader br = new BufferedReader(new FileReader(TextFile));
        String roles = br.readLine();
        WebElement SubmitRoles = driver.findElement(By.id("rolesList"));
        SubmitRoles.clear();
        SubmitRoles.sendKeys(roles);
      }
      driver.findElement(By.id("skipButtonForRoles")).click();
      return true;
    }
    catch (TimeoutException e)
    {
      return false;
    }
    catch (UnhandledAlertException e)
    {
      logger.warn("Alert Seen");
      driver.switchTo().alert().accept();
      return false;
    }
  }

  public static Boolean ClickHide(WebDriver driver,WebDriverWait wait,Logger logger)
  {
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(
          "button.btn.btn-primary.btn-xs")));
      WebElement Hide = driver.findElement(By.cssSelector("button.btn.btn-primary.btn-xs"));
      ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Hide);
      return true;
    }
    catch (TimeoutException e) {
      logger.error("Hide/show Button Not Found ");
      return false;
    }
  }

  public static void UploadJson(WebDriver driver,WebDriverWait wait,String JsonDirectory,String Json)
  {
    String toloadpath = JsonDirectory + "/" + Json;
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("file")));
    driver.findElement(By.name("file")).sendKeys(toloadpath);
  }


}