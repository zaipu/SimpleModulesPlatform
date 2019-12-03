package sequoia.modules;


import org.dom4j.DocumentException;
import sequoia.modules.wire.PortIn;
import sequoia.modules.wire.PortOut;
import sequoia.modules.wire.Target;
import sequoia.modules.wire.WireConfig;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2019/10/2 7:39
 * @Email zhongchaoliang@hdu.edu.cn
 * @description:
 */
public class ModulePlatform implements Runnable {

    private static ConcurrentHashMap<String, IModule> module_hash = new ConcurrentHashMap<>();  ////存储所有的模块对象
    private static HashMap<String, IModuleConfig> config_hash = new HashMap<>();  ////存储所有的配置对象
    private static String platform_name; //平台名
    private static PlatformConfig platformConfig = null;
    private Thread thisThread;
    private final static ModulePlatform instance = new ModulePlatform();
  

    private ModulePlatform() {
    }

    /**
     * 单例，返回模块平台对象
     *
     * @return
     */
    public static ModulePlatform getInstance() {
        return instance;
    }

    /**
     * 获取模块存储map
     *
     * @return
     */
    public ConcurrentHashMap<String, IModule> getModule_hash() {
        return module_hash;
    }

    /**
     * 获取配置存储map
     *
     * @return
     */
    public HashMap<String, IModuleConfig> getConfig_hash() {
        return config_hash;
    }

    public PlatformConfig getPlatformConfig() {
        return platformConfig;
    }

    /**
     * 获取平台设置参数
     *
     * @param name
     * @return
     */
    public String getPlatformSettingByName(String name) {
        return platformConfig.getParaSetByName(name);
    }

    /**
     * 解析XML配置文件
     *
     * @param file
     * @throws IllegalXMLFormatException
     * @throws DocumentException
     */
    public void ConfigPlatformXMLFile(InputStream file) throws IllegalXMLFormatException, DocumentException {
        platformConfig = new PlatformConfig();
        platformConfig.loadFromXML(file);
        platform_name = platformConfig.getModulePlatformName();
    }

    /**
     * 根据xml配置文件构建各个模块对象和连接关系
     *
     * @throws Exception
     */
    public void booting() throws Exception {

        ArrayList<ModuleConfig> moduleConfigs = platformConfig.getPlatModuleConfigs();

        //创建所有模块, 并调用模块的init方法进行一些初始化动作
        for (ModuleConfig moduleConfig : moduleConfigs) {
            IModule module = loadModule(moduleConfig);
            module.init(moduleConfig);
            module_hash.put(moduleConfig.getName(), module);
        }

        //把模块添加到模块组
        for (Iterator<IModule> it = module_hash.values().iterator(); it.hasNext(); ) {
            IModule module = it.next();
            String fatherModuleName = module.getModuleConfig().getSuperModuleName();
            if (!fatherModuleName.equalsIgnoreCase("NULL")) {
                IModule iModule = module_hash.get(module.getModuleConfig().getSuperModuleName());
                if (iModule instanceof GroupModule) {
                    GroupModule abstractGroupModule = (GroupModule) iModule;
                    abstractGroupModule.addChildModule(module.getModuleName());
                }
            }
        }

        //分析并构建Wires
        ArrayList<WireConfig> wireConfigs = platformConfig.getWireConfigs();
        for (WireConfig wireConfig : wireConfigs) {
            PortOut simplePortOut = new PortOut(wireConfig);
            getModule(wireConfig.getSrcModuleName()).addPort(simplePortOut);

            ArrayList<Target> targets = wireConfig.getWireTargets();
            for (Target target : targets) {
                PortIn simplePortIn = new PortIn(target);
                getModule(target.getT_module()).addPort(simplePortIn);
            }
        }
    }

  

    public IModule loadModule(ModuleConfig moduleConfig) throws Exception {
        String module_Name = moduleConfig.getName();
        String className = moduleConfig.getClassName();
        if (module_hash.get(module_Name) != null) {
            //todo:抛出模块名不唯一异常，模块加载失败
            throw new Exception("模块ID:" + module_Name + "重复。");
        }
        if (className == null || className.length() == 0) {
            throw new Exception("模块加载失败,原因是配置错误:module名为" + module_Name + "的classname为空！");
        }
        //创建模块对象
        Object m = Class.forName(className).newInstance();
        if (m instanceof IModule) {
            return (IModule) m;
        } else {
            throw new Exception("创建模块失败, 模块名为: " + module_Name);
        }
    }



    /**
     * 调用模块的stop()和destroy()方法，注销各个模块
     *
     * @throws Exception
     */
    public void modulesDestroy() throws Exception {
        for (IModule module : module_hash.values()) {
            module.stop();
        }
        for (IModule module : module_hash.values()) {
            module.destroy();
        }
    }

    /**
     * 调用各个模块的start()方法，启动各个模块
     *
     * @throws Exception
     */
    public void modulesStart() throws Exception {
        //启动模块
        for (IModule module : module_hash.values()) {
            module.start();
        }
    }

    /**
     * @return 平台名
     */
    public String getPlatform_name() {
        return platform_name;
    }

    /**
     * 通过连接name获取连接配置对象
     *
     * @param name 连接名称
     * @return LinkConfig
     */
    public WireConfig getLinkConfig(String name) {
        //  HashMap wcs = platformConfig.getConnectionsHashMap();
        for (WireConfig wireConfig : platformConfig.getWireConfigs()) {
            if (wireConfig.getName().equalsIgnoreCase(name)) {
                return wireConfig;
            }
        }
        return null;
    }

    /**
     * 通过模块name获取模块对象
     *
     * @param module_name
     * @return 模块对象
     */
    public IModule getModule(String module_name) {
        //    return module_hash.get(module_name) instanceof IModule ? (IModule) module_hash.get(module_name) : null;
        return module_hash.get(module_name);
    }

    /**
     * 通过模块name获取模块对象
     *
     * @param clazz 类
     * @return 模块对象
     */
//    public <T> List<T> getModuleByClass(Class<T> clazz) {
//        return (List<T>)module_hash.entrySet().stream().filter(
//                moduleSet ->clazz.isAssignableFrom(moduleSet.getValue().getClass())
//        ).map(moduleSet -> moduleSet.getValue()).collect(Collectors.toList());
//    }

    /**
     * 测试模块平台时调用该方法，启动ModulePlatform线程。其余情况不要调用该方法，请自行维护ModulePlatform的实例对象。
     */
    public void startPlatform() {
        thisThread = new Thread(this);
        thisThread.start();
    }

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        System.out.print(">>");
        try {
            while ((line = br.readLine()) != null) {
                if (line.equals("quit")) {
                    try {
                        this.modulesDestroy();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    System.exit(0);
                    break;
                } else //showHelp();
                {
                    System.out.print(">>");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private IModule instsnceof(Object module) {
//        return (IModule) module;
//    }
}
