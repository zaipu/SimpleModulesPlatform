package sequoia.DSKernel.model;

public class DeParallelRequestEventResult {
    protected  long eventId;  //与请求相同
    public boolean isSuccess;
    public String msg;

    public DeParallelRequestEventResult(long eventId) {
        this.eventId = eventId;
    }
}
