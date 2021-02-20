package sequoia.DSKernel;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import sequoia.DSKernel.model.DeParallelRequestEvent;
import sequoia.DSKernel.model.DeSerialExecuteEvent;
import sequoia.DSKernel.model.IDeEvent;
import sequoia.DSKernel.model.IParallelRequestEventHandler;

public class DeEventExecuteKernel extends PriorityBlockingQueue<IDeEvent> implements Runnable {

    private static final DeEventExecuteKernel DE_EVENT_SCHEDULER = new DeEventExecuteKernel();
    private DeParallelRequestEventHandlerRegistry parallelEventHandlerRegistry;
    private DeSerialEventScheduler deSerialEventScheduler;
    private ThreadPoolExecutor threadPoolExecutor = createAdjustableExecutor(10, Integer.MAX_VALUE, 100L, "DeEventScheduler-run-%d");

    private DeEventExecuteKernel() {
        super();
        deSerialEventScheduler =new DeSerialEventScheduler();
        deSerialEventScheduler.start();
        parallelEventHandlerRegistry = new DeParallelRequestEventHandlerRegistry();
    }

    public static DeEventExecuteKernel getInstance() {
        return DE_EVENT_SCHEDULER;
    }

    /**
     * 删除属于originator对象的所有event
     *
     * @param originator
     */
    public synchronized void removeTasksForOriginator(Object originator) {
        Iterator<IDeEvent> iterator = this.iterator();
        while (iterator.hasNext()) {
            IDeEvent task = iterator.next();
            if (task.getOriginator() == originator) {
                iterator.remove();
            }
        }
    }

    public DeParallelRequestEventHandlerRegistry getParallelEventHandlerRegistry() {
        return parallelEventHandlerRegistry;
    }

    @Override
    public void run() {
        IDeEvent task;
        while (true) {
            try {
                task = take();

                if (task.getEventType() == IDeEvent.EventType.SerialExecuteEvent) {
                    DeSerialExecuteEvent deExecuteEvent = (DeSerialExecuteEvent) task;
                    deSerialEventScheduler.addSerialEventAtLast(deExecuteEvent);
                } else if (task.getEventType() == IDeEvent.EventType.ParallelRequestEvent) {
                    DeParallelRequestEvent requestEvent = (DeParallelRequestEvent) task;
                    threadPoolExecutor.submit(() -> {
                        IParallelRequestEventHandler handler = parallelEventHandlerRegistry.getHandler(requestEvent.getHandlerName());
                             handler.process(requestEvent);
                    });

                  //  switch (requestEvent.getRequestType()) {
                    //    case DeRestRequest:
                    //        threadPoolExecutor.submit(() -> {
                          //      DsOrderCompleteRequestHandler dsOrderCompleteRequestHandler = (DsOrderCompleteRequestHandler) requestHandlerRegistry.getRobEventHandler(requestEvent.getRequestType());
                           //     dsOrderCompleteRequestHandler.process((DsOrderCompleteRequest) requestEvent);
                       //     });
                     //       break;



                   //     default:
                  //          break;
                  //  }
                }
//                else if (task.getEventType() == IDeEvent.EventType.ParallelExecuteEvent) {
//                    DeParallelExecuteEvent executeEvent = (DeParallelExecuteEvent) task;
//                    threadPoolExecutor.execute(() -> {
//                        executeEvent.process();
//                    });
//                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
        private  ThreadPoolExecutor createAdjustableExecutor(int coreSize, int maxSize, long keepAliveTime
            , String nameFormat) {
        return new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1024),
                new ThreadFactoryBuilder().setNameFormat(nameFormat).build(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
