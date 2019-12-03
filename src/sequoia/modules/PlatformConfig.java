package sequoia.modules;

import java.io.File;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import sequoia.modules.wire.WireConfig;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2019/10/2 7:39
 * @Email zhongchaoliang@hdu.edu.cn
 * @description:
 */
public class PlatformConfig {

    private String modulePlatformName;
    private ArrayList<ModuleConfig> platModuleConfigs = new ArrayList<ModuleConfig>();
    private ArrayList<WireConfig> wireConfigs = new ArrayList<>();
    private HashMap<String, String> paraSetMap = new HashMap<String, String>();

    protected void loadFromXML(InputStream r) throws IllegalXMLFormatException, DocumentException {
        SAXReader sAXReader = new SAXReader();
        Document xmlDoc = sAXReader.read(r);
        parserDocument(xmlDoc);
    }

    protected void loadFromXML(File file) throws IllegalXMLFormatException, DocumentException {
        SAXReader sAXReader = new SAXReader();
        Document xmlDoc = sAXReader.read(file);
        parserDocument(xmlDoc);
    }

    protected void loadFromXML(Reader reader) throws IllegalXMLFormatException, DocumentException {
        SAXReader sAXReader = new SAXReader();
        Document xmlDoc = sAXReader.read(reader);
        parserDocument(xmlDoc);
    }
   

    protected String getParaSetByName(String name) {
        return paraSetMap.get(name);
    }

    protected String getModulePlatformName() {
        return modulePlatformName;
    }

    protected ArrayList<ModuleConfig> getPlatModuleConfigs() {
        return platModuleConfigs;
    }


    protected ArrayList<WireConfig> getWireConfigs() {
        return wireConfigs;
    }


    protected HashMap<String, String> getPlatParaMap() {
        return paraSetMap;
    }


    private void parserDocument(Document xmlDoc) throws IllegalXMLFormatException {
        Element root = xmlDoc.getRootElement();

        if (root.getName().equalsIgnoreCase("ModulePlatform")) {
            modulePlatformName = root.attributeValue("name");
//获得配置信息集

            //获得模块集
            Element modules = root.element("Modules");
            String modulesRootName = modules.attributeValue("name");

            //解析modules目录下直接放置的模块
            List<Element> eleModule_list = modules.elements("Module");
            if (eleModule_list != null) {
                for (int j = 0; j < eleModule_list.size(); j++) {
                    Element em = eleModule_list.get(j);
                    //创建单个模块配置文件
                    ModuleConfig moduleSingle = new ModuleConfig();
                    moduleSingle.processXML(em);
                    moduleSingle.setSuperModuleName(modulesRootName);
                    platModuleConfigs.add(moduleSingle);
                }
            }

            //递归解析ModuleGroup
            List<Element> moduleGroup_list = modules.elements("ModuleGroup");
            if (moduleGroup_list != null) {
                for (int i = 0; i < moduleGroup_list.size(); i++) {
                    Element eGroup = moduleGroup_list.get(i);
                    ParserModuleGroup(eGroup, modulesRootName);
                }
            }

            Element platformSettingE = root.element("PlatformSettings");

            if (platformSettingE != null) {
                List sett_paramL = platformSettingE.elements("param");
                for (int i = 0; i < sett_paramL.size(); i++) {
                    Element e2 = (Element) sett_paramL.get(i);
                    String key = e2.attributeValue("name");
                    String val = e2.attributeValue("value");
                    paraSetMap.put(key, val);
                }
            }

            Element links_E = root.element("Wires");
            if (links_E != null) {
                List collection_list = links_E.elements("Wire");
                for (int i = 0; i < collection_list.size(); i++) {
                    Element linke = (Element) collection_list.get(i);
                    WireConfig wc = new WireConfig(linke);
                    wireConfigs.add(wc);
                }
            }

        } else {
            throw new IllegalXMLFormatException("没有元素 ``ModulePlatform'', 请检查XML文件.");
        }
    }

    private void ParserModuleGroup(Element groupElement, String superModuleName) throws IllegalXMLFormatException {
        //创建组模块配置文件
        ModuleConfig moduleGroupConfig = new ModuleConfig();
        moduleGroupConfig.processXML(groupElement);
        moduleGroupConfig.setSuperModuleName(superModuleName);
        platModuleConfigs.add(moduleGroupConfig);

        List<Element> eleModule_list = groupElement.elements("Module");
        if (eleModule_list != null) {
            for (int j = 0; j < eleModule_list.size(); j++) {
                Element em = eleModule_list.get(j);
                //创建单个模块配置文件
                ModuleConfig moduleSingle = new ModuleConfig();
                moduleSingle.processXML(em);
                moduleSingle.setSuperModuleName(moduleGroupConfig.getName());
                platModuleConfigs.add(moduleSingle);
            }
        }

        List<Element> moduleGroup_list = groupElement.elements("ModuleGroup");
        if (moduleGroup_list != null) {
            for (int i = 0; i < moduleGroup_list.size(); i++) {
                Element eGroup = moduleGroup_list.get(i);
                ParserModuleGroup(eGroup, moduleGroupConfig.getName());
            }
        }
    }
}
