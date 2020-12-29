/*
 * File:ModulePlatformTest.java  
 * Date:2019-4-30   
 * Encoding:UTF-8  
 * Author:Zhong Chaoliang  
 * Email:zhongchaoliang@hdu.edu.cn
 * Version 1.0
 */
package test;

import java.io.FileInputStream;
import java.util.ArrayList;
import sequoia.modules.IModule;
import sequoia.modules.IModuleGroup;
import sequoia.modules.ModulePlatform;

/**
 *
 * @author Zhong Chaoliang
 */
public class ModulePlatformTest {

    private static ModulePlatform platform;

    /**
     * @param args 
     */
    public static void main(String[] args) {
      
        System.out.println(360/0.000171661376953125);
        
        
        System.out.println("创建平台. ");
        
        try {
            String fileString = System.getProperty("user.dir") + System.getProperty("file.separator") + "conf" + System.getProperty("file.separator") + "MMPlatform.xml";
            System.out.println(fileString);
            platform = ModulePlatform.getInstance();
            platform.ConfigPlatformXMLFile(new FileInputStream(fileString));
            platform.booting();
            platform.modulesStart();
            
            //测试模块组输出
            IModuleGroup groupModuleImpl = (IModuleGroup) platform.getModule("network_group");
            ArrayList<String> list = groupModuleImpl.getChildModuleNames();
            for (String string : list) {
                System.out.println(string);
            }
            
            IModule iModule=platform.getModule("customer");
            System.out.println("moduleplatformtest.ModulePlatformTest.main();"+iModule.getModuleConfig().getSuperModuleName());
            
            //启动平台线程，在业务场景下，无需启动该平台线程，只需持有platform对象就好。
            platform.startPlatform();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
}
