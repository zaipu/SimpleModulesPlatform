/*
 * File:CustomerModule.java  
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
import sequoia.modules.wire.IValueUpdateListener;
import sequoia.modules.wire.SimplePortIn;



/**
 *
 * @author Zhong Chaoliang
 */
public class CustomerModule extends AbstractModule implements Runnable, IValueUpdateListener {

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

            SimplePortIn linkInPort = (SimplePortIn) getPort("customer.datain");
            linkInPort.registerListener(this);
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
            SimplePortIn inPort = (SimplePortIn) getPort("customer.datain");
            String string = (String) inPort.getValue();
            System.out.println("linkInPort.getValue():" + string);
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
            throw new Exception("无法停止模块: " + moduleName);
        }
        moduleState.set(ModuleStateE.Destroyed);
    }

    @Override
    public void valueUpdated(Object object) {
        System.out.println("inPort listener():" + (String) object);
    }

}
