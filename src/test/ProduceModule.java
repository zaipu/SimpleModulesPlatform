/*
 * File:ProduceModule.java  
 * Date:2019-4-30   
 * Encoding:UTF-8  
 * Author:Zhong Chaoliang  
 * Email:zhongchaoliang@hdu.edu.cn
 * Version 1.0
 */
package test;

import sequoia.modules.AbstractModule;
import sequoia.modules.IModuleConfig;
import sequoia.modules.ModuleConfig;
import sequoia.modules.ModuleStateE;
import sequoia.modules.wire.SimplePortOut;

/**
 *
 * @author Zhong Chaoliang
 */
public class ProduceModule extends AbstractModule implements Runnable {

    private Thread t_this;
    private boolean bRun = false;
    private boolean bPause = false;

    @Override
    public void init(IModuleConfig cfg) throws Exception {
        moduleConfig = (ModuleConfig) cfg;
        moduleName = moduleConfig.getName();

        moduleState.set(ModuleStateE.Ready);
    }

    @Override
    public void start() throws Exception {
        if (t_this == null) {
            t_this = new Thread(this);
            bRun = true;
            bPause = false;
            t_this.start();
        } else {
            bPause = false;
        }

        moduleState.set(ModuleStateE.Working);
    }

    @Override
    public void stop() throws Exception {
        bPause = true;
        moduleState.set(ModuleStateE.Stopped);
    }

    @Override
    public void run() {
        while (bRun) {
            if (bPause == true) {
                sleep(300);
                continue;
            }
            SimplePortOut outPort = (SimplePortOut) getPort("produce.data");
            outPort.setValue("test");
            sleep(300);
        }
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
        moduleState.set(ModuleStateE.Destroyed);
    }

}
