package sequoia.modules;

import sequoia.modules.wire.IPort;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2019/10/2 7:33
 * @Email zhongchaoliang@hdu.edu.cn
 * @description:
 */
public interface IModule {

    public String getModuleName();

    public String getModuleType();

    public ModuleStateE getModuleState();

    public IModuleConfig getModuleConfig();

    public IPort getPort(String portName);

    public void addPort(IPort port) throws Exception;

    public void init(IModuleConfig cfg) throws Exception;

    public void start() throws Exception;

    public void stop() throws Exception;

    public void destroy() throws Exception;
}
