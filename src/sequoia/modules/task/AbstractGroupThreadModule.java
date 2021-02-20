/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sequoia.modules.task;

import java.util.ArrayList;
import sequoia.modules.IModuleGroup;

/**
 *
 * @author Zhong Chaoliang  <zhongchaoliang@hdu.edu.cn>
 * @date 2021-1-6 11:08:40
 */
public abstract class AbstractGroupThreadModule extends AbstractThreadModule implements IModuleGroup{

    protected ArrayList<String> childModuleNames = new ArrayList<>();

    public ArrayList<String> getChildModuleNames() {
        return childModuleNames;
    }

    public void addChildModule(String moduleName) {
        this.childModuleNames.add(moduleName);
    }

    public void removeChildModule(String moduleName) {
        this.childModuleNames.remove(moduleName);
    }

}