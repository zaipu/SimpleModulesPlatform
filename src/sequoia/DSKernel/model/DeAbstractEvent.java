package sequoia.DSKernel.model;

public class DeAbstractEvent implements IDeEvent,Comparable{
    private  long eventId;  //该Id自增长
    private static long eventSequencer;
    protected Object originator;
    protected int priority=DeEventPriorityType.getDefaultPriority();
    protected EventType eventType;

    public DeAbstractEvent() {
        this.eventId = eventSequencer++;
    }


    public Object getOriginator() {
        return originator;
    }

    public int getPriority() {
        return priority;
    }

    public long getEventId(){
        return eventId;
    }

    protected void setDefaultPriority(){
        priority= DeEventPriorityType.getDefaultPriority();
    }

    protected void setSystemPriority(){
        priority= DeEventPriorityType.getSystemPriority();
    }

    @Override
    public EventType getEventType() {
        return eventType;
    }

    @Override
    public int compareTo(Object object) {
        IDeEvent event = (IDeEvent) object;
        if (this.getPriority() == event.getPriority()) {
            return 0;
        } else {
            return this.getPriority() - event.getPriority();
        }
    }
}
