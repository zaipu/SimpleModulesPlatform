package sequoia.modules.wire;



import org.dom4j.Element;
import java.util.ArrayList;
import java.util.List;
import sequoia.modules.IllegalXMLFormatException;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2019/10/2 14:18
 * @Email zhongchaoliang@hdu.edu.cn
 * @description:
 */

public class WireConfig {

    private String srcModuleName;

    private String name;

    private long duration = 0;

    private ArrayList<Target> wireTargets = new ArrayList<>();

    public WireConfig() {
    }

    public WireConfig(Element e) throws IllegalXMLFormatException {
        loadFromXML(e);
    }

    private void loadFromXML(Element e)
            throws IllegalXMLFormatException {
        if (e.getName().equals("Wire")) {

            this.srcModuleName = e.attributeValue("srcModuleName");
            this.name = e.attributeValue("name");
            this.duration = Long.parseLong(e.attributeValue("duration"));

            List l = e.elements("target");
            for (int i = 0; i < l.size(); i++) {
                Element e2 = (Element) l.get(i);
                String t_m = e2.attributeValue("targetModuleName");
                String t_n = e2.attributeValue("name");
                long duration1 = 0;
                String v = e2.attributeValue("duration");
                duration1 = Long.parseLong(v);
                wireTargets.add(new Target(t_m, t_n, duration1));
            }
        } else {
            throw new IllegalXMLFormatException("错误格式，标志应为 ``Wire''.");
        }
    }

    public String getSrcModuleName() {
        return srcModuleName;
    }

    public String getName() {
        return name;
    }

    public long getDuration() {
        return duration;
    }

    public ArrayList<Target> getWireTargets() {
        return wireTargets;
    }
}
