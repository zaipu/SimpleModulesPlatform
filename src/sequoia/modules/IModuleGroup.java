package sequoia.modules;

import java.util.ArrayList;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2020/10/6 19:37
 * @email zhongchaoliang@hdu.edu.cn
 * @description
 */
public interface IModuleGroup extends IModule{
    public ArrayList<String> getChildModuleNames();

    public void addChildModule(String moduleName);

    public void removeChildModule(String moduleName);
}