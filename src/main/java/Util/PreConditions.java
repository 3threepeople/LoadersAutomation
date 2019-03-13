package Util;

import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class PreConditions {

  public static void NavigateConfigurations(WebDriver driver) throws InterruptedException {
    driver.findElement(By.cssSelector("button.button")).click();
    Thread.sleep(2000);
    driver.findElement(By.className("fi-page-edit")).click();
    Thread.sleep(3000);
    driver.findElement(By.className("admin-item")).click();
    Thread.sleep(3000);
  }

  public static void SendOTP(WebDriver driver)
  {
    Scanner key=new Scanner(System.in);
    System.out.println("Enter OTP:");
    String otp = key.nextLine();
    driver.findElement(By.name("htmlKey")).sendKeys(otp);
  }

  public static WebDriver InitializeDriver()
  {
    String ChromeDriverPath = System.getProperty("chromedriver.path");
    System.setProperty("webdriver.chrome.driver", ChromeDriverPath);
    WebDriver driver = new ChromeDriver();
    driver.manage().window().fullscreen();
    driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    return driver;
  }

  public static String[] getTickets(Properties properties)
  {
    String splittickets = properties.getProperty("Tickets");
    String[] Tickets = splittickets.split(",");
    return Tickets;
  }

  public static String[] getLoaders(Properties properties)
  {
    String loaders = properties.getProperty("loaders");
    String[] splitloaders = loaders.split(",");
    return splitloaders;
  }

  public static String[] getParentDirectories(Properties properties)
  {
    String jsonpath = properties.getProperty("jsonpath");
    String[] ParentDirectories = jsonpath.split(",");
    return ParentDirectories;
  }

  public static void OpenURLandLogin(Properties properties,WebDriver driver)
  {
    driver.get(properties.getProperty("URL"));
    driver.findElement(By.name("username")).sendKeys(properties.getProperty("username"));
    driver.findElement(By.name("password")).sendKeys(properties.getProperty("password"));
    driver.findElement(By.className("button")).click();
  }

  public static boolean EligibletoContinue(String[] ParentDirectories, String[] splitloaders, String[] Tickets, Logger logger)
  {
    if (ParentDirectories.length != splitloaders.length) {
      logger.error("The number of Directories and Loaders are not same");
      return false;
    }
    if (ParentDirectories.length != Tickets.length) {
      logger.error("The Number of Tickets is not equal to Number of Loaders");
      return false;
    }
    return true;
  }
}
