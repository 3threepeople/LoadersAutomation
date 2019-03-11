import Models.RadioButton;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.io.File;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Application {
  public static void main(String[] args) throws IOException, InterruptedException {

    RadioButton radioButton = new RadioButton();
    String PropertiesPath = System.getProperty("props.path");
    Scanner key = new Scanner(System.in);
    Properties Properties = new Properties();

    FileInputStream fis = new FileInputStream(PropertiesPath);
    Properties.load(fis);

    String jsonpath = Properties.getProperty("jsonpath");
    String[] ParentDirectories = jsonpath.split(",");

    String loaders = Properties.getProperty("loaders");
    String[] splitloaders = loaders.split(",");

    String splittickets = Properties.getProperty("Tickets");
    String[] Tickets = splittickets.split(",");

    if (ParentDirectories.length != splitloaders.length) {
      System.out.println("The number of Directories and Loaders are not same...Please Check it:");
      System.exit(0);
    }
    if (ParentDirectories.length != Tickets.length) {
      System.out.println("The Number of Tickets is not equal to Number of Loaders");
      System.exit(0);
    }

    String ChromeDriverPath = System.getProperty("chromedriver.path");
    System.setProperty("webdriver.chrome.driver", ChromeDriverPath);
    WebDriver driver = new ChromeDriver();
    driver.manage().window().fullscreen();
    driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    WebDriverWait wait = new WebDriverWait(driver, 20);

    driver.get(Properties.getProperty("URL"));
    driver.findElement(By.name("username")).sendKeys(Properties.getProperty("username"));
    driver.findElement(By.name("password")).sendKeys(Properties.getProperty("password"));
    driver.findElement(By.className("button")).click();
    Thread.sleep(2000);

    System.out.println("Enter OTP:");
    String otp = key.nextLine();

    driver.findElement(By.name("htmlKey")).sendKeys(otp);
    driver.findElement(By.cssSelector("button.button")).click();
    Thread.sleep(2000);

    driver.findElement(By.className("fi-page-edit")).click();
    Thread.sleep(3000);
    driver.findElement(By.className("admin-item")).click();
    Thread.sleep(3000);

    for (int i = 0; i < splitloaders.length; i++) {
      String loadername = splitloaders[i].trim();
      String JsonDirectory = ParentDirectories[i].trim();
      File f = new File(JsonDirectory);
      String JsonFiles[] = f.list();
      List<String> Jsons = new ArrayList<String>();

      try {
        for (int k = 0; k < JsonFiles.length; k++) {
          if (JsonFiles[k].endsWith(".json"))
            Jsons.add(JsonFiles[k]);
        }
      } catch (NullPointerException e) {
        System.out.println("JSONS not present in the given FilePath:" + ParentDirectories[i]);
        continue;
      }

      String radio = radioButton.getRadio(loadername);
      if (null != radio) {
        try {
          wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(radio)));
          WebElement radiofind = driver.findElement(By.id(radio));
          executor.executeScript("arguments[0].click();", radiofind);
        } catch (TimeoutException e) {
          System.out.println("Cannot find the loader:" + loadername);
          continue;
        }
      }
      else {
        System.out.println("This Loader is not present in our Configurations:" + loadername);
        System.out.println(radioButton.Radio.keySet());
        continue;
      }

      for (int m = 0; m < Jsons.size(); m++) {
        String toloadpath = JsonDirectory + "/" + Jsons.get(m);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("file")));
        driver.findElement(By.name("file")).sendKeys(toloadpath);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("loadToDb")));
        WebElement ele = driver.findElement(By.id("loadToDb"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);
        executor = (JavascriptExecutor) driver;
        executor.executeScript("arguments[0].click();", ele);

        try {
          try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                "//h4[contains(text(),\"Redmine ticket number\")]")));
              WebElement Ticket = driver.findElement(By.id("ticketNumber"));
              Ticket.clear();
              Ticket.sendKeys(Tickets[i].trim());
              wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(
                  "clickSubmitButton")));
              driver.findElement(By.id("clickSubmitButton")).click();
              try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//h4[contains(text(),\"Data already exists. Do you want to overwrite?\")]")));
                  WebElement element = driver.findElement(By.xpath(
                      "//button[contains(text(),'YES')]"));
                  ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                  }
              catch (TimeoutException e1) {
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//h4[contains(text(),\"Successfully configured\")]")));
                WebElement OK = driver.findElements(By.xpath("//button[contains(text(),'OK')]"))
                    .get(1);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", OK);
                System.out.println(Jsons.get(m) + " is loaded");
                  }
              wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                  "//h4[contains(text(),\"Successfully configured\")]")));
              WebElement OK = driver.findElements(By.xpath("//button[contains(text(),'OK')]"))
                  .get(1);
              ((JavascriptExecutor) driver).executeScript("arguments[0].click();", OK);
              System.out.println(Jsons.get(m) + " is loaded in "+loadername);
          }
          catch (TimeoutException e) {
            try {
              wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                  "//h4[contains(text(),\"Enter roles to configure same data\")]")));
              driver.findElement(By.id("skipButtonForRoles")).click();
              try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//h4[contains(text(),\"Redmine ticket number\")]")));
              }
              catch (TimeoutException e1) {
                System.out.println("Current status JSON May be Improper");
                continue;
              }
              WebElement Ticket = driver.findElement(By.id("ticketNumber"));
              Ticket.clear();
              Ticket.sendKeys(Tickets[i]);
              try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(
                    "clickSubmitButton")));
                driver.findElement(By.id("clickSubmitButton")).click();
              }
              catch (TimeoutException e2) {
                System.out.println("Submit button not found of roles");
                continue;
              }
              try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//h4[contains(text(),\"Data already exists. Do you want to overwrite?\")]")));
              }
              catch (TimeoutException e3) {
                wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                    "//h4[contains(text(),\"Successfully configured\")]")));
                WebElement OK = driver.findElements(By.xpath("//button[contains(text(),'OK')]"))
                    .get(1);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", OK);
                System.out.println(Jsons.get(m) + " is loaded in "+loadername);
                continue;
              }
                WebElement element = driver.findElement(By.xpath(
                    "//button[contains(text(),'YES')]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
              wait.until(ExpectedConditions.elementToBeClickable(By.xpath(
                  "//h4[contains(text(),\"Successfully configured\")]")));
              WebElement OK = driver.findElements(By.xpath("//button[contains(text(),'OK')]"))
                  .get(1);
              ((JavascriptExecutor) driver).executeScript("arguments[0].click();", OK);
              System.out.println(Jsons.get(m) + " is loaded");
            }
            catch (TimeoutException e3) {
              System.out.println("JSON is not loading " + Jsons.get(m));
              continue;
            }
          }
        }
        catch (UnhandledAlertException e)
        {
          System.out.println("Alert seen");
          driver.switchTo().alert().accept();
        }
        try {
          wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn.btn-primary.btn-xs")));
          WebElement Hide = driver.findElement(By.cssSelector("button.btn.btn-primary.btn-xs"));
          ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Hide);
        } catch (TimeoutException e) {
          System.out.println("Hide/show Button Not Found ");
        }
        Thread.sleep(1000);
      }
    }
    driver.quit();
  }
}
