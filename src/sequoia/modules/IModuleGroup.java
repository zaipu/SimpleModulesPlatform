/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sequoia.modules;

import java.util.ArrayList;

/**
 *
 * @author Zhong Chaoliang  <zhongchaoliang@hdu.edu.cn>
 * @date 2019-12-2 17:23:33
 */
public interface IModuleGroup extends IModule{
    public ArrayList<String> getChildModuleNames();

    public void addChildModule(String moduleName);

    public void removeChildModule(String moduleName);
}
