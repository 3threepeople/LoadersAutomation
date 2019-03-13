import static Util.PreConditions.*;
import static Util.Utility.*;

import Models.RadioButton;
import Models.Stats;
import com.google.common.base.Stopwatch;
import java.io.*;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import java.util.*;
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
    Properties Properties = new Properties();
    FileInputStream fis = new FileInputStream(PropertiesPath);
    Properties.load(fis);

    String[] Tickets = getTickets(Properties);
    String[] splitloaders=getLoaders(Properties);
    String[] ParentDirectories=getParentDirectories(Properties);

    if(!EligibletoContinue(ParentDirectories,splitloaders,Tickets,logger))
    System.exit(0);

    WebDriver driver=InitializeDriver();
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    WebDriverWait wait = new WebDriverWait(driver, 10);

    OpenURLandLogin(Properties,driver);
    Thread.sleep(2000);
    SendOTP(driver);
    NavigateConfigurations(driver);
    stopwatch.start();

    for (int i = 0; i < splitloaders.length; i++) {
      String loadername = splitloaders[i].trim();
      String JsonDirectory = ParentDirectories[i].trim();
      File f = new File(JsonDirectory);
      String JsonFiles[] = f.list();
      List<String> Jsons = new ArrayList<String>();

      Jsons = getListofJsons(JsonFiles, Jsons);
      if (Jsons == null || Jsons.isEmpty()) {
        logger.warn("JSONS not present in the given FilePath:" + ParentDirectories[i]);
        continue;
      }
      String radio = radioButton.getRadio(loadername);
      if (!(ClickLoader(radio, loadername, driver, wait, executor, logger)))
        continue;

      for (int m = 0; m < Jsons.size(); m++) {
        logger.info("Processing Json: " + Jsons.get(m));
        UploadJson(driver,wait,JsonDirectory,Jsons.get(m));
        ClickLoadButton(driver, wait, executor);
        if (IsTicketPopUp(driver, wait, Tickets[i])) {
          if (IsOverRidePopUp(driver, wait, logger)) {
            ClickSuccessfullyConfigured(driver, wait, logger, Jsons.get(m), loadername);
            TotalJsons++;
            OverridedJsons++;
            continue;
          } else {
            ClickSuccessfullyConfigured(driver, wait, logger, Jsons.get(m), loadername);
            TotalJsons++;
            ValidJsons++;
            continue;
          }
        } else if (IsSubmitRolesPopUp(driver, wait, JsonDirectory, Jsons.get(m), logger)) {
          if (IsOverRidePopUp(driver, wait, logger)) {
            ClickSuccessfullyConfigured(driver, wait, logger, Jsons.get(m), loadername);
            TotalJsons++;
            OverridedJsons++;
            continue;
          } else if (ClickSuccessfullyConfigured(driver, wait, logger, Jsons.get(m), loadername)) {
            TotalJsons++;
            ValidJsons++;
            continue;
          } else {
            logger.warn("JSON may be Improper");
            TotalJsons++;
            InvalidJsons++;
            continue;
          }
        } else {
          logger.warn("JSON:" + Jsons.get(m) + " is not loading in " + loadername);
          TotalJsons++;
          InvalidJsons++;
          continue;
        }
      }
      ClickHide(driver,wait,logger);
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

