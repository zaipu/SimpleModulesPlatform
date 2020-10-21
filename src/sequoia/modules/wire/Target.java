package sequoia.modules.wire;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2019/10/2 14:16
 * @Email zhongchaoliang@hdu.edu.cn
 * @description:
 */

public class Target {
    private String t_module;
    private String name;
    private long duration = 0;

    public Target() {
    }

    public Target(String t_module, String t_name,  long duration) {
        this.t_module = t_module;
        this.name = t_name;
        this.duration = duration;
    }

    public String getT_module() {
        return t_module;
    }

    public void setT_module(String t_module) {
        this.t_module = t_module;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }
}
