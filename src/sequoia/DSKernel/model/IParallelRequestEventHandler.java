package sequoia.DSKernel.model;

public interface IParallelRequestEventHandler<T> {
    public void process(T t);
}
