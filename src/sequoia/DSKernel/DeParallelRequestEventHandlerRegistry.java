package sequoia.DSKernel;



import java.util.concurrent.ConcurrentHashMap;
import sequoia.DSKernel.model.IParallelRequestEventHandler;

public class DeParallelRequestEventHandlerRegistry {
    private ConcurrentHashMap<String, IParallelRequestEventHandler> hashMap;

    public DeParallelRequestEventHandlerRegistry() {
        hashMap =new ConcurrentHashMap<>();
    }

    public void addHandler(String handlerName, IParallelRequestEventHandler requestHandler){
        hashMap.put(handlerName, requestHandler);
    }

    public IParallelRequestEventHandler getHandler(String handlerName){
        return hashMap.get(handlerName);
    }

    public IParallelRequestEventHandler removeHandler(String handlerName){
        return hashMap.remove(handlerName);
    }
}
