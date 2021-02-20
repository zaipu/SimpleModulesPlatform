package sequoia.DSKernel.model;

public interface IDeEvent {

    public static enum EventType {
        ParallelExecuteEvent,
        ParallelRequestEvent,
        SerialExecuteEvent,
        UnKnown
    }

    public EventType getEventType();

    public Object getOriginator();

    public int getPriority();

    public long getEventId();
}
