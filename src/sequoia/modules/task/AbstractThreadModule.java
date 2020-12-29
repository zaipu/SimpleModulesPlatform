package sequoia.modules.task;

import sequoia.modules.AbstractModule;

public abstract class AbstractThreadModule extends AbstractModule implements Runnable {
    protected Thread t_this;
    protected boolean bRun = false;
    protected boolean bPause = false;

    public AbstractThreadModule() {
        super();
    }


    @Override
    public void start() throws Exception {
        if (t_this == null) {
            t_this = new Thread(this);
            bRun = true;
            t_this.start();
        } else {
            bPause = false;
        }
        super.start();
    }

    @Override
    public void stop() throws Exception {
        bPause = true;
        super.stop();
    }


    @Override
    public void destroy() throws Exception {
        bRun = false;
        int i = 0;
        while (t_this.isAlive() && i < 5) {
            t_this.interrupt();
            sleep(50);
            i++;
        }
        if (t_this.isAlive()) {
            throw new Exception("无法停止模块线程运行: " + moduleName);
        }
        super.destroy();
    }

}
