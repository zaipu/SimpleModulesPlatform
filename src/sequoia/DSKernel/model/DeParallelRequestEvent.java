package sequoia.DSKernel.model;

public class DeParallelRequestEvent extends DeAbstractEvent{
    protected String handlerName;

    public DeParallelRequestEvent() {
        super();
        eventType = EventType.ParallelRequestEvent;
    }

    public String getHandlerName() {
        return handlerName;
    }
}
