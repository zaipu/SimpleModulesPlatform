package sequoia.DSKernel.model;

public abstract class DeSerialExecuteEvent extends DeAbstractEvent {

    public DeSerialExecuteEvent() {
        super();
        eventType = EventType.SerialExecuteEvent;
    }

    public abstract void process();
}
