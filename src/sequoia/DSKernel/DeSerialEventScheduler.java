package sequoia.DSKernel;


import sequoia.modules.task.AbstractTaskModule;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import sequoia.DSKernel.model.DeSerialExecuteEvent;

public class DeSerialEventScheduler implements Runnable{

    private ArrayBlockingQueue<DeSerialExecuteEvent> queue = new ArrayBlockingQueue(10, true);
    private Thread t_this;
    private boolean bRun = false;
    private boolean bPause = false;

    public DeSerialEventScheduler() {

    }


    public void start() {
        if (t_this == null) {
            t_this = new Thread(this);
            bRun = true;
            t_this.start();
        } else {
            bPause = false;
        }
    }

    public void stop() {
        this.bPause = true;
    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (bRun) {
            if (bPause == true) {
                sleep(2000);
                continue;
            }
            try {
                DeSerialExecuteEvent event = (DeSerialExecuteEvent)this.queue.take();
                event.process();
            } catch (InterruptedException var4) {
                Logger.getLogger(AbstractTaskModule.class.getName()).log(Level.SEVERE, (String)null, var4);
            }
        }
    }

    public boolean isPause() {
        return bPause;
    }

    public void addSerialEventAtLast(DeSerialExecuteEvent event) {
        this.queue.add(event);
    }

    public void addClearEvent(DeSerialExecuteEvent event) {
        this.queue.clear();
        this.queue.add(event);
    }

    public void clearEventQueue() {
        this.queue.clear();
    }



}
