package Tasks;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class Scheduler{
  private ScheduledExecutorService scheduledThreadPool;

  public void createpool(int poolsize){
    this.scheduledThreadPool= Executors.newScheduledThreadPool(poolsize);
  }

  public void scheduledtasks(List<Task> taskList){
    for(Task task:taskList)
      scheduledThreadPool.schedule(task, 2, TimeUnit.SECONDS);
  }

  public void runtask(){
    scheduledThreadPool.shutdown();
    while(!scheduledThreadPool.isTerminated()){
    }
  }

  public void executeadhoc(Task task){
    task.start();
  }
}