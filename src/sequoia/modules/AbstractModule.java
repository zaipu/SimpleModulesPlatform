package sequoia.modules;

import sequoia.modules.wire.IPort;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2019/10/2 7:41
 * @Email zhongchaoliang@hdu.edu.cn
 * @description:
 */
public abstract class AbstractModule implements IModule {
    protected String moduleName;
    protected ModuleConfig moduleConfig;
    protected Thread t_this;
    protected boolean bRun = false;
    protected boolean bPause=false;

    protected HashMap<String, IPort> portsHashMap = new HashMap<>();
    public final AtomicReference<ModuleStateE> moduleState = new AtomicReference<>(ModuleStateE.UNKNOWN);

    public String getModuleType(){
        return moduleConfig.getType();
    }

    @Override
    public ModuleStateE getModuleState() {
        return moduleState.get();
    }

    @Override
    public String getModuleName() {
        return moduleName;
    }

    @Override
    public IPort getPort(String name) {
        return portsHashMap.get(name);
    }

    @Override
    public void addPort(IPort port) throws Exception {
        if (portsHashMap.get(port.getPortName()) != null) {
            //todo:抛出模块名不唯一异常，模块加载失败
            throw new Exception("已存在端口名:" + port.getPortName());
        }
        portsHashMap.put(port.getPortName(), port);
    }

    @Override
    public ModuleConfig getModuleConfig() {
        return moduleConfig;
    }

    protected void sleep(long milli) {
        try {
            Thread.sleep(milli);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void init(IModuleConfig cfg) throws Exception{
        moduleConfig = (ModuleConfig) cfg;
        moduleName = cfg.getName();
        moduleState.set(ModuleStateE.Ready);
    }

    @Override
    public void start() throws Exception {
        moduleState.set(ModuleStateE.Working);
    }

    @Override
    public void stop() throws Exception {
        moduleState.set(ModuleStateE.Stopped);
    }

    @Override
    public void destroy() throws Exception {
        moduleState.set(ModuleStateE.Destroyed);
    }
}
