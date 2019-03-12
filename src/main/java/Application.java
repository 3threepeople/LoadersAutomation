import Models.RadioButton;
import Models.Stats;
import com.google.common.base.Stopwatch;
import java.io.*;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import org.apache.log4j.Logger;

public class Application {
  public static void main(String[] args) throws IOException, InterruptedException {

    Logger logger = Logger.getLogger(Application.class);
    Stopwatch stopwatch=Stopwatch.createUnstarted();
    logger.info("Started application");
    Integer TotalJsons=0;
    Integer InvalidJsons=0;
    Integer OverridedJsons=0;
    Integer ValidJsons=0;

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
      logger.error("The number of Directories and Loaders are not same");
      System.exit(0);
    }
    if (ParentDirectories.length != Tickets.length) {
      logger.error("The Number of Tickets is not equal to Number of Loaders");
      System.exit(0);
    }

    stopwatch.start();
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
        logger.warn("JSONS not present in the given FilePath:" + ParentDirectories[i]);
        continue;
      }

      String radio = radioButton.getRadio(loadername);
      if (null != radio) {
        try {
          logger.info("Processing loader: " + loadername);
          wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(radio)));
          WebElement radiofind = driver.findElement(By.id(radio));
          executor.executeScript("arguments[0].click();", radiofind);
        } catch (TimeoutException e) {
          logger.warn("Cannot find the loader:" + loadername);
          continue;
        }
      }
      else {
        logger.warn("This Loader is not present in our Configurations:" + loadername);
        continue;
      }

      for (int m = 0; m < Jsons.size(); m++) {
        logger.info("Processing Json: " + Jsons.get(m));
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
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//h4[contains(text(),\"Successfully configured\")]")));
                WebElement OK = driver.findElements(By.xpath("//button[contains(text(),'OK')]"))
                    .get(1);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", OK);
                logger.info(Jsons.get(m) + " is loaded: "+ loadername);
                TotalJsons++;
                ValidJsons++;
                continue;
                  }
              wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                  "//h4[contains(text(),\"Successfully configured\")]")));
              WebElement OK = driver.findElements(By.xpath("//button[contains(text(),'OK')]"))
                  .get(1);
              ((JavascriptExecutor) driver).executeScript("arguments[0].click();", OK);
              logger.info(Jsons.get(m) + " is loaded in "+loadername);
              TotalJsons++;
              OverridedJsons++;
              continue;
          }
          catch (TimeoutException e) {
            try {
              wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                  "//h4[contains(text(),\"Enter roles to configure same data\")]")));
              File TextFile= new File(JsonDirectory+"/"+Jsons.get(m).replace(".json",".txt"));
              if(TextFile.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(TextFile));
                String roles = "";
                roles = br.readLine();
                WebElement SubmitRoles = driver.findElement(By.id("rolesList"));
                SubmitRoles.clear();
                SubmitRoles.sendKeys(roles);
              }
              driver.findElement(By.id("skipButtonForRoles")).click();
              try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//h4[contains(text(),\"Redmine ticket number\")]")));
              }
              catch (TimeoutException e1) {
                logger.warn("JSON May be Improper");
                TotalJsons++;
                InvalidJsons++;
                continue;
              }
              WebElement Ticket = driver.findElement(By.id("ticketNumber"));
              Ticket.clear();
              Ticket.sendKeys(Tickets[i]);
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(
                    "clickSubmitButton")));
                driver.findElement(By.id("clickSubmitButton")).click();
              try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//h4[contains(text(),\"Data already exists. Do you want to overwrite?\")]")));
              }
              catch (TimeoutException e3) {
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                    "//h4[contains(text(),\"Successfully configured\")]")));
                WebElement OK = driver.findElements(By.xpath("//button[contains(text(),'OK')]"))
                    .get(1);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", OK);
                logger.info(Jsons.get(m) + " is loaded in "+loadername);
                ValidJsons++;
                TotalJsons++;
                continue;
              }
                WebElement element = driver.findElement(By.xpath(
                    "//button[contains(text(),'YES')]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
              wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                  "//h4[contains(text(),\"Successfully configured\")]")));
              WebElement OK = driver.findElements(By.xpath("//button[contains(text(),'OK')]"))
                  .get(1);
              ((JavascriptExecutor) driver).executeScript("arguments[0].click();", OK);
              logger.info(Jsons.get(m) + " is loaded in "+loadername);
              OverridedJsons++;
              ValidJsons++;
              continue;
            }
            catch (TimeoutException e3) {
              logger.warn("JSON is not loading " + Jsons.get(m));
              TotalJsons++;
              InvalidJsons++;
              continue;
            }
          }
        }
        catch (UnhandledAlertException e)
        {
          logger.warn("Alert Seen");
          TotalJsons++;
          InvalidJsons++;
          driver.switchTo().alert().accept();
        }
        }
      try {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button.btn.btn-primary.btn-xs")));
        WebElement Hide = driver.findElement(By.cssSelector("button.btn.btn-primary.btn-xs"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Hide);
      }
      catch (TimeoutException e) {
        logger.error("Hide/show Button Not Found ");
      }
    }
    stopwatch.stop();
    logger.info(Stats.TOTALJSONS+":"+TotalJsons);
    logger.info(Stats.VALIDJSONS+":"+ValidJsons);
    logger.info(Stats.OVERRIDEDJSONS+":"+OverridedJsons);
    logger.info(Stats.INVALIDJSONS+":"+InvalidJsons);
    logger.info(Stats.TOTALTIMETAKEN+":"+stopwatch.elapsed(TimeUnit.SECONDS)+" secs");
    driver.quit();
  }
}
