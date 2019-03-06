import Models.RadioButton;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import sun.security.krb5.internal.Ticket;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.io.File;

public class Application {
    public static void main(String[] args) throws IOException, InterruptedException {


        RadioButton radioButton = new RadioButton();

        Properties Prop = new Properties();
        FileInputStream fis = new FileInputStream("/Users/shridharnraykar/IdeaProjects/LoadersAutomation/src/main/resources/Properties");
        Prop.load(fis);

        Scanner key = new Scanner(System.in);

        System.setProperty("webdriver.chrome.driver", "/Users/shridharnraykar/Downloads/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().fullscreen();

        driver.get(Prop.getProperty("URL"));
        driver.findElement(By.name("username")).sendKeys(Prop.getProperty("username"));
        Thread.sleep(2000);
        driver.findElement(By.name("password")).sendKeys(Prop.getProperty("password"));
        Thread.sleep(2000);
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


        String jsonpath = Prop.getProperty("jsonpath");
        String[] ParentDirectories = jsonpath.split(",");

        String loaders = Prop.getProperty("loader");
        String[] splitloaders = loaders.split(",");

        if (ParentDirectories.length != splitloaders.length) {
            System.out.println("The number of Directories and Loaders are not same...Please Check it:");
            System.exit(0);
        }


        for (int i = 0; i < splitloaders.length; i++) {

            String loadername = splitloaders[i];

            String JsonDirectory = ParentDirectories[i];

            File f = new File(JsonDirectory);
            String JsonFiles[] = f.list();

            List<String> Jsons = new ArrayList<String>();

            for (int k = 0; k < JsonFiles.length; k++) {
                if (JsonFiles[k].endsWith(".json"))
                    Jsons.add(JsonFiles[k]);
            }

            String radio = radioButton.getRadio(loadername);

            WebElement radiofind = driver.findElement(By.id(radio));
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", radiofind);

            for (int m = 0; m < Jsons.size(); m++) {
                String toloadpath = JsonDirectory + "/" + Jsons.get(m);

                driver.findElement(By.name("file")).sendKeys(toloadpath);
                Thread.sleep(5000);

                WebElement ele = driver.findElement(By.id("loadToDb"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);

                executor = (JavascriptExecutor) driver;
                executor.executeScript("arguments[0].click();", ele);
                Thread.sleep(1000);

                String redmine = driver.findElement(By.xpath("//h4[contains(text(),\"Redmine ticket number\")]")).getText();
                //System.out.println(redmine);

                if (redmine.equals("Redmine ticket number")) {
                    WebElement Ticket = driver.findElement(By.id("ticketNumber"));
                    Ticket.clear();
                    Ticket.sendKeys("9999");
                    Thread.sleep(3000);
                    driver.findElement(By.id("clickSubmitButton")).click();
                    Thread.sleep(3000);

                    String override = driver.findElement(By.xpath("//h4[contains(text(),\"Data already exists. Do you want to overwrite?\")]")).getText();
                    //System.out.println(override);
                    if (override.equals("Data already exists. Do you want to overwrite?")) {
                        WebElement element = driver.findElement(By.xpath("//button[contains(text(),'YES')]"));
                        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
                        Thread.sleep(2000);
                    }

                    WebElement OK = driver.findElements(By.xpath("//button[contains(text(),'OK')]")).get(1);
                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", OK);

                }
                else
                {
                    System.out.println("Invalid JSON: " + toloadpath);
                }


            }

        }
            try {
                WebElement Hide = driver.findElement(By.cssSelector("button.btn.btn-primary.btn-xs"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", Hide);
            } catch (Exception e) {
                System.out.println("Exception seen - " + e);
            }

            Thread.sleep(1000);

        }
    }

