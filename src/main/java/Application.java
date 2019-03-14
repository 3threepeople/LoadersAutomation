import static Util.PreConditions.*;
import static Util.Utility.*;
import static Models.Stats.*;

import Models.RadioButton;
import com.google.common.base.Stopwatch;
import java.io.*;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import java.util.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import org.apache.log4j.Logger;

public class Application {
  private static int TotalJsons=0;
  private static int InvalidJsons=0;
  private static int OverridedJsons=0;
  private static int ValidJsons=0;

  public static void increment(String JSON1,String JSON2)
  {
    if(JSON1.equals(TOTALJSONS))
      TotalJsons++;
    if(JSON2.equals(OVERRIDEDJSONS))
      OverridedJsons++;
    if(JSON2.equals(VALIDJSONS))
      ValidJsons++;
    if(JSON2.equals(INVALIDJSONS))
      InvalidJsons++;
  }
  public static void main(String[] args) throws IOException, InterruptedException {

    Logger logger = Logger.getLogger(Application.class);
    Stopwatch stopwatch=Stopwatch.createUnstarted();
    logger.info("Started application");

    RadioButton radioButton = new RadioButton();
    Properties Properties=InitializeProperties();

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
      for (String Json:Jsons) {
        logger.info("Processing Json: " + Json);
        UploadJson(driver,wait,JsonDirectory,Json);
        ClickLoadButton(driver, wait, executor);

        if (IsTicketPopUp(driver, wait, Tickets[i])) {
          if (IsOverRidePopUp(driver, wait, logger)) {
            ClickSuccessfullyConfigured(driver, wait, logger, Json, loadername);
            increment(TOTALJSONS,OVERRIDEDJSONS);
            continue;
          }
          else {
            ClickSuccessfullyConfigured(driver, wait, logger, Json, loadername);
            increment(TOTALJSONS,VALIDJSONS);
            continue;
          }
        }
        else if(IsSubmitRolesPopUp(driver, wait, JsonDirectory, Json, logger))
        {
          if (IsTicketPopUp(driver, wait, Tickets[i]))
          {
            if (IsOverRidePopUp(driver, wait, logger)) {
              ClickSuccessfullyConfigured(driver, wait, logger, Json, loadername);
              increment(TOTALJSONS,OVERRIDEDJSONS);
              continue;
            }
            else {
              ClickSuccessfullyConfigured(driver, wait, logger, Json, loadername);
              increment(TOTALJSONS,VALIDJSONS);
            }
          }
          else {
            logger.warn("JSON may be Improper");
            increment(TOTALJSONS,INVALIDJSONS);
            continue;
          }
        }
        else {
          logger.warn("JSON:" + Json + " is not loading in " + loadername);
          increment(TOTALJSONS,INVALIDJSONS);
          continue;
        }
      }
      ClickHide(driver,wait,logger);
    }
      stopwatch.stop();
      logger.info(TOTALJSONS+":"+TotalJsons);
      logger.info(VALIDJSONS+":"+ValidJsons);
      logger.info(OVERRIDEDJSONS+":"+OverridedJsons);
      logger.info(INVALIDJSONS+":"+InvalidJsons);
      logger.info(TOTALTIMETAKEN+":"+stopwatch.elapsed(TimeUnit.SECONDS)+" secs");
      driver.quit();
  }
}

