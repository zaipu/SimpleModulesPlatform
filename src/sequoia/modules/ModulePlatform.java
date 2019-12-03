package sequoia.modules;


import org.dom4j.DocumentException;
import sequoia.modules.wire.PortIn;
import sequoia.modules.wire.PortOut;
import sequoia.modules.wire.Target;
import sequoia.modules.wire.WireConfig;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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
        //   ArrayList<ModuleConfig> configs = platformConfig.getModuleConfigs();
        //解析配置文件
   /*     for (ModuleConfig moduleConfig : configs) {
            Object config = loadConfig(moduleConfig);
            config_hash.put(moduleConfig.getName(), config);

            //把配置添加到配置组
            String type = moduleConfig.getType();
            if (!type.equalsIgnoreCase("group")) {
                GroupConfigImpl groupModuleImpl = (GroupConfigImpl) config_hash.get(type);
                groupModuleImpl.addChildModule(moduleConfig.getName());
            }
        }
*/
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

        //分析并构建Links
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

    /*
    public Object loadConfig(ModuleConfig moduleConfig) throws Exception {
        String config_Name = moduleConfig.getName();
        String className = moduleConfig.getClassName();
        if (module_hash.get(config_Name) != null) {
            throw new Exception("配置ID:" + config_Name + "重复。");
        }
        if (className == null || className.length() == 0) {
            throw new Exception("配置加载失败,原因是配置错误:config名为" + config_Name + "的classname为空！");
        }
        //创建配置对象
        Object m = Class.forName(className).newInstance();
        Field[] fields = m.getClass().getDeclaredFields();
        HashMap<String, String> params = moduleConfig.getParams();
        for (Map.Entry entry : params.entrySet()) {
            String key = entry.getKey().toString();
            String value = entry.getValue().toString();
            if ("&".equals(value.substring(0, 1))) {
                for (Field field : fields) {
                    if (field.getName().equals(key)) {
                        if (config_hash.get(value.substring(1)) != null) {
                            setBean(m, field, config_hash.get(value.substring(1)));
                        } else {
                            //                        log.info(config_Name + "注入" + field.getName() + "失败," + value.substring(1) + "配置不存在");
                        }
                    }
                }
            } else {
                for (Field field : fields) {
                    if (field.getName().equals(key)) {
                        setter(m, field, entry.getValue());
                    }
                }
            }
        }

        return m;
    }
*/

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
            /**
             * 获取当前类包括父类的所有字段
             */
            /*
            List<Field> fields = new ArrayList<>();
            Class clazz = m.getClass();
            while(clazz != null){
                fields.addAll(Arrays.asList(clazz .getDeclaredFields()));
                clazz = clazz.getSuperclass(); //得到父类,然后赋给自己
            }

            HashMap<String, String> params = moduleConfig.getParams();
            for (Map.Entry entry : params.entrySet()) {
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                if ("&".equals(value.substring(0, 1))) {
                    for (Field field : fields) {
                        if (field.getName().equals(key)) {
                            if (config_hash.get(value.substring(1)) != null) {
                                setBean(m, field, config_hash.get(value.substring(1)));
                            } else {
                                //                           log.info(module_Name + "注入" + field.getName() + "失败," + value.substring(1) + "模块不存在");
                            }
                        }
                    }
                } else {
                    for (Field field : fields) {
                        if (field.getName().equals(key)) {
                            setter(m, field, entry.getValue());
                        }
                    }
                }
            }
*/
            return (IModule) m;
        } else {
            throw new Exception("创建模块失败, 模块名为: " + module_Name);
        }
    }


    /*
    public void updateModule(Object o, ModuleConfig moduleConfig) throws Exception {
        String module_Name = moduleConfig.getName();
        String className = moduleConfig.getClassName();
        if (module_hash.get(module_Name) == null) {
            throw new Exception("模块ID:" + module_Name + "不存在。");
        }
        if (className == null || className.length() == 0) {
            throw new Exception("模块加载失败,原因是配置错误:module名为" + module_Name + "的classname为空！");
        }
        //创建模块对象
        Object m = module_hash.get(module_Name);
        if (m instanceof IModule) {
            Field[] fields = o.getClass().getDeclaredFields();
            HashMap<String, String> params = moduleConfig.getParams();
            for (Map.Entry entry : params.entrySet()) {
                String key = entry.getKey().toString();
                String value = entry.getValue().toString();
                if ("&".equals(value.substring(0, 1))) {
                    for (Field field : fields) {
                        if (field.getName().equals(key)) {
                            if (config_hash.get(value.substring(1)) != null) {
                                setBean(o, field, config_hash.get(value.substring(1)));
                            } else {
                                //                              log.info(module_Name + "注入" + field.getName() + "失败," + value.substring(1) + "模块不存在");
                            }
                        }
                    }
                } else {
                    for (Field field : fields) {
                        if (field.getName().equals(key)) {
                            setter(o, field, entry.getValue());
                        }
                    }
                }
            }
        } else {
            throw new Exception("创建模块失败, 模块名为: " + module_Name);
        }
    }

    private void setter(Object o, Field field, Object v) {
        try {
            if (field.isAccessible()) {
                field.set(o, typeChange(field.getType().getName(), v.toString()));
            } else {
                field.setAccessible(true);
                field.set(o, typeChange(field.getType().getName(), v.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setBean(Object o, Field field, Object v) {
        try {
            if (field.isAccessible()) {
                field.set(o, v);
            } else {
                field.setAccessible(true);
                field.set(o, v);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object typeChange(String type, String str) {
        if ("int".equals(type) || "java.lang.Integer".equals(type)) {
            return Integer.parseInt(str);
        }

        if ("char".equals(type) || "java.lang.Character".equals(type)) {
            return str.charAt(0);
        }

        if ("long".equals(type) || "java.lang.Long".equals(type)) {
            return Long.parseLong(str);
        }

        if ("float".equals(type) || "java.lang.Float".equals(type)) {
            return Float.parseFloat(str);
        }

        if ("boolean".equals(type) || "java.lang.Boolean".equals(type)) {
            return Boolean.parseBoolean(str);
        }

        if ("short".equals(type) || "java.lang.Short".equals(type)) {
            return Short.parseShort(str);
        }

        if ("byte".equals(type) || "java.lang.Byte".equals(type)) {
            return Byte.parseByte(str);
        }

        if ("double".equals(type) || "Double".equals(type)) {
            return Double.parseDouble(str);
        }

        return str;
    }

    */

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
