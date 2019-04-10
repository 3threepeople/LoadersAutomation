import static Util.PreConditions.*;
import static Util.Utility.*;
import static Models.Stats.*;

import Models.LoaderCategory;
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

  private static void increment(String JSON1,String JSON2)
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
    logger.info("Started application");

    Stopwatch stopwatch=Stopwatch.createUnstarted();
    LoaderCategory loaderCategory=new LoaderCategory();
    Properties properties=InitializeProperties();

    String[] Tickets = getTickets(properties);
    String[] splitloaders=getLoaders(properties);
    String[] ParentDirectories=getParentDirectories(properties);

    if(!EligibletoContinue(ParentDirectories,splitloaders,Tickets,logger))
    System.exit(0);

    WebDriver driver=InitializeDriver(properties);
    JavascriptExecutor executor = (JavascriptExecutor) driver;
    WebDriverWait wait = new WebDriverWait(driver, 10);

    OpenURLandLogin(properties,driver);
    SendOTP(driver);
    NavigateConfigurations(driver,executor);
    stopwatch.start();

    for (int i = 0; i < splitloaders.length; i++) {
      String loadername = splitloaders[i].trim();
      String JsonDirectory = ParentDirectories[i].trim();
      File f = new File(JsonDirectory);
      String JsonFiles[] = f.list();
      List<String> Jsons = new ArrayList<String>();
      Jsons = getListofJsons(JsonFiles, Jsons);
      if (null==Jsons || Jsons.isEmpty()) {
        logger.warn("JSONS not present in the given FilePath:" + ParentDirectories[i]);
        continue;
      }
      String category = loaderCategory.getCategory(loadername);
      if (!(ClickLoader(category, loadername, driver, wait, executor)))
        continue;
      for (String Json:Jsons) {
        logger.info("Processing Json: " + Json);
        UploadJson(driver,wait,JsonDirectory,Json);
        ClickLoadButton(driver, wait, executor);

        if (IsTicketPopUp(driver, wait, Tickets[i])) {
          if (IsOverRidePopUp(driver, wait)) {
            ClickSuccessfullyConfigured(driver, wait, Json, loadername);
            increment(TOTALJSONS,OVERRIDEDJSONS);
          }
          else {
            ClickSuccessfullyConfigured(driver, wait, Json, loadername);
            increment(TOTALJSONS,VALIDJSONS);
          }
        }
        else if(IsSubmitRolesPopUp(driver, wait, JsonDirectory, Json))
        {
          if (IsTicketPopUp(driver, wait, Tickets[i]))
          {
            if (IsOverRidePopUp(driver, wait)) {
              ClickSuccessfullyConfigured(driver, wait, Json, loadername);
              increment(TOTALJSONS,OVERRIDEDJSONS);
            }
            else {
              ClickSuccessfullyConfigured(driver, wait, Json, loadername);
              increment(TOTALJSONS,VALIDJSONS);
            }
          }
          else {
            logger.warn("JSON may be Improper");
            increment(TOTALJSONS,INVALIDJSONS);
          }
        }
        else {
          logger.warn("JSON:" + Json + " is not loading in " + loadername);
          increment(TOTALJSONS,INVALIDJSONS);
        }
      }
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

