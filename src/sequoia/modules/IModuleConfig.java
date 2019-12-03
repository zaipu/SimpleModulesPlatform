package sequoia.modules;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2019/10/2 7:35
 * @Email zhongchaoliang@hdu.edu.cn
 * @description:
 */
public interface IModuleConfig {
    public String getName();

    public String getSuperModuleName();

    public String getType();

    public String getAuthor();

    public String getVersion();

    public String getDescription();

    public String getParamValue(String key);
}
