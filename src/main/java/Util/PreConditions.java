package Util;

import com.google.common.base.Strings;
import com.opencsv.CSVReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class PreConditions {
  static Logger logger=Logger.getLogger(PreConditions.class);

  public static Properties InitializeProperties() throws IOException {
    String PropertiesPath = System.getProperty("props.path");
    Properties Properties = new Properties();
    FileInputStream fis = new FileInputStream(PropertiesPath);
    Properties.load(fis);
    return Properties;
  }

  public static void NavigateConfigurations(WebDriver driver, JavascriptExecutor executor) throws InterruptedException {
    driver.findElement(By.className("fi-page-edit")).click();
    WebElement Configurations=driver.findElement(By.xpath("//p[contains(text(),\"Configurations\")]"));
    executor.executeScript("arguments[0].click();", Configurations);
  }

  public static void SendOTP(WebDriver driver) {
    Scanner key=new Scanner(System.in);
    System.out.println("Enter OTP:");
    String otp = key.nextLine();
    driver.findElement(By.name("htmlKey")).sendKeys(otp);
    driver.findElement(By.cssSelector("button.button")).click();
  }

  public static WebDriver InitializeDriver(Properties properties) {
    String ChromeDriverPath = System.getProperty("chromedriver.path");
    System.setProperty("webdriver.chrome.driver", ChromeDriverPath);
    WebDriver driver;
    if("headless".equals(properties.getProperty("BrowserName"))) {
      ChromeOptions chromeOptions = new ChromeOptions();
      chromeOptions.setHeadless(true);
      chromeOptions.addArguments("window-size=1920,1080");
      chromeOptions.addArguments("--no-sandbox");
      chromeOptions.addArguments("--whitelisted-ips='localhost'");
      driver=new ChromeDriver(chromeOptions);
    }
    else
      driver = new ChromeDriver();

    driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    return driver;
  }

  public static List<List<String>> getcsvproperties() throws IOException {
    CSVReader reader =new CSVReader(new FileReader(System.getProperty("csv.path")),'\t');
    List<List<String>> csvproperties=new LinkedList<>();
    List<String> Loaders=new ArrayList<>();
    List<String> Jsonpaths=new ArrayList<>();
    List<String> Tickets=new ArrayList<>();

    String[] row;
    Boolean skipheader=false;
    while (null!=(row=reader.readNext())){
      if(!Strings.isNullOrEmpty(row[0]) && !Strings.isNullOrEmpty(row[1]) && !Strings.isNullOrEmpty(row[2])) {
        if (skipheader) {
          Loaders.add(row[0]);
          Jsonpaths.add(row[1]);
          Tickets.add(row[2]);
        } else {
          skipheader = true;
        }
      }
      else {
        logger.error("There is null or empty value in CSV");
        System.exit(0);
      }
    }
    csvproperties.add(Loaders);
    csvproperties.add(Jsonpaths);
    csvproperties.add(Tickets);
    return csvproperties;
    }

  public static void OpenURLandLogin(Properties properties,WebDriver driver){
    driver.get(properties.getProperty("URL"));
    driver.findElement(By.name("username")).sendKeys(properties.getProperty("username"));
    driver.findElement(By.name("password")).sendKeys(properties.getProperty("password"));
    driver.findElement(By.className("button")).click();
  }

}
