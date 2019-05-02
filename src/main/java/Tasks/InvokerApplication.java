package Tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.log4j.Logger;

public class InvokerApplication {

  public static void main(String[] args) throws InterruptedException {

    Logger logger=Logger.getLogger(InvokerApplication.class);
    Scanner key = new Scanner(System.in);                                  //to take input from console
    Scheduler scheduler = new Scheduler();                                 //Instanstiated scheduler

    while (true) {
      System.out.println("1.Scheduled Jobs");
      System.out.println("2.adhoc task");
      Integer option = Integer.parseInt(key.nextLine());                   //option to considered whether the task is of multiple jobs
      if (1==option) {                                                     // or single job

        scheduler.createpool(5);
        List<Task> tasklist = new ArrayList<>();
        logger.info("Enter the number of tasks:");
        Integer tasks = Integer.parseInt(key.nextLine());
        for (int i = 1; i <= tasks; i++) {
          logger.info("Enter the number for the task " + i + ":");    //Until what number the primenumbers should find
          Integer templatevariable = Integer.parseInt(key.nextLine());
          tasklist.add(new Task(templatevariable, new ArrayList()));

        }
        scheduler.createpool(5);                              //Sending ThreadPool Size for scheduler by which
        //it can execute multiple tasks
        if (!tasklist.isEmpty()) {                                     //if the task is empty
          scheduler.scheduledtasks(tasklist);
          scheduler.runtask();
        }
        int index = 1;
        for (Task task : tasklist) {
          logger.info("Primenumbers for the task " + index + ":" + task.primenumbers);
          index++;
        }
      }
      else {
        logger.info("Enter the number for the task:");
        Integer templatevariable = Integer.parseInt(key.nextLine());
        Task task=new Task(templatevariable, new ArrayList());
        scheduler.executeadhoc(task);
        Thread.sleep(1000);
        logger.info("Primenumbers for the task:" + task.primenumbers);
      }
    }
  }

}
