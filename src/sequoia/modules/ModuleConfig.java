package sequoia.modules;

import org.dom4j.Element;


import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2019/10/2 7:36
 * @Email zhongchaoliang@hdu.edu.cn
 * @description:
 */
public class ModuleConfig implements IModuleConfig {

    private String name;
    private String className;
    private String description;
    private String version;
    private String author;
    private String type;
    private String superModuleName = "NULL";
    private HashMap<String, String> params = new HashMap<String, String>();

    public ModuleConfig() {
    }

    public void processXML(Element ele) throws IllegalXMLFormatException {
        if (ele.getName().equals("ModuleGroup")
                ||ele.getName().equals("Module")) {
            this.name = ele.attributeValue("name");
            this.type = ele.attributeValue("type");
            this.className = ele.attributeValue("class");
            this.description = ele.attributeValue("description");
            this.version = ele.attributeValue("version");
            this.author = ele.attributeValue("author");

            List lst = ele.elements("param");
            if (lst != null) {
                for (int i = 0; i < lst.size(); i++) {
                    Element e2 = (Element) lst.get(i);
                    String key = e2.attributeValue("name");
                    String val;
                    if(e2.attributeValue("ref") != null){
                        //标记这是一个reference而不是普通数值
                        val = "&"+e2.attributeValue("ref");
                    }else {
                        val = e2.attributeValue("value");
                    }
                    params.put(key, val);
                }
            }

        } else {
            throw new IllegalXMLFormatException("XML 文件开头非 ``Module''或者 ``ModuleGroup''");
        }
    }


    public Element toXMLElement(Element element){
        Element e = element.addElement("module");

        e.addAttribute("name", name);
        e.addAttribute("type", type);
        e.addAttribute("class", className);
        if(description != null)
            e.addAttribute("description", description);
        if(version != null)
            e.addAttribute("version", version);
        if(author != null)
            e.addAttribute("author", author);

        Iterator iterator = params.keySet().iterator();
        while(iterator.hasNext())
        {
            Element e2 =e.addElement("param");
            String key = (String) iterator.next();
            String val =  params.get(key);
            e2.addAttribute("name", key);
            e2.addAttribute("value", val);

        }
        return e;
    }


    public String getParamValue(String key) {
        return (String) params.get(key);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getSuperModuleName() {
        return superModuleName;
    }

    public void setSuperModuleName(String superModuleName) {
        this.superModuleName = superModuleName;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
    protected void CopyVariables(ModuleConfig mc) {
        mc.setAuthor(this.getAuthor());
        mc.setClassName(this.getClassName());
        mc.setDescription(this.getDescription());
        mc.setName(this.getName());
        mc.setParams(this.getParams());
        mc.setSuperModuleName(this.getSuperModuleName());
        mc.setType(this.getType());
        mc.setVersion(this.getVersion());
    }
*/
}
