package sequoia.modules;

import java.util.ArrayList;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2019/10/2 7:43
 * @Email zhongchaoliang@hdu.edu.cn
 * @description:
 */
public class GroupModule extends AbstractModule implements IModuleGroup{

    protected ArrayList<String> childModuleNames = new ArrayList<>();

    @Override
    public ArrayList<String> getChildModuleNames() {
        return childModuleNames;
    }

    @Override
    public void addChildModule(String moduleName) {
        this.childModuleNames.add(moduleName);
    }

    @Override
    public void removeChildModule(String moduleName) {
        this.childModuleNames.remove(moduleName);
    }

    
}
