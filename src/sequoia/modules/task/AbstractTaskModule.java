package sequoia.modules.task;

import sequoia.modules.IModuleConfig;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbstractTaskModule extends AbstractThreadModule {

    private ArrayBlockingQueue<IModuleTask> taskQueue = new ArrayBlockingQueue<>(10, true);

    protected int intervalTimeI = 1000;//2000毫秒，2秒

  //  public static enum ConnectionState {

   //     NO_CONNECTION, // The client has not connected.
   //     FAILED_CONNECTION, // The client tried to connect and failed.
  //      CONNECTED // Client is connected to server.
 //   }

 //   public final AtomicReference<ConnectionState> connState = new AtomicReference<ConnectionState>(ConnectionState.NO_CONNECTION);

  //  public ConnectionState getConnState() {
  //      return connState.get();
  //  }

    @Override
    public void run() {
        while (bRun) {
            if (bPause == true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(AbstractTaskModule.class.getName()).log(Level.SEVERE, null, ex);
                }
                continue;
            }

            IModuleTask task;
            try {
                task = taskQueue.take();
                task.process(this);
            } catch (InterruptedException ex) {
                Logger.getLogger(AbstractTaskModule.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(intervalTimeI);
            } catch (InterruptedException ex) {
                Logger.getLogger(AbstractTaskModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void addTaskLast(IModuleTask iTask) {
        taskQueue.add(iTask);
    }

    public void addClearTask(IModuleTask iTask) {
        taskQueue.clear();
        taskQueue.add(iTask);
    }

    public void clearTasks() {
        taskQueue.clear();
    }

    public int getIntervalTimeI() {
        return intervalTimeI;
    }

    public void setIntervalTimeI(int intervalTimeI) {
        this.intervalTimeI = intervalTimeI;
    }

    @Override
    public void init(IModuleConfig cfg) throws Exception {
        super.init(cfg);

    }

    @Override
    public void start() throws Exception {
        super.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
    }
}
