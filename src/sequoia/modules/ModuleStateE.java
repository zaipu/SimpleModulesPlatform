package sequoia.modules;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2019/10/3 15:57
 * @Email zhongchaoliang@hdu.edu.cn
 * @description:
 */
public enum ModuleStateE {
    UNKNOWN(0, "未知状态"),
    Ready(1, "准备好"),
    Working(2, "正在工作"),
    Stopped(3, "停止"),
    Destroyed(4, "已销毁"),
    Error(5, "出错");

    /**
     * 模块状态
     * Ready：模块初始化成功，进入准备工作状态
     * Working： 模型处在工作状态中
     * Stopped： 停止模块运行，资源没被释放
     * Destroyed: 模块被销毁，资源被释放
     * Error: 模块处在异常情况
     */

    public final int CODE;
    public final String text;

    ModuleStateE(int code , String text) {
        this.CODE = code;
        this.text = text;
    }

    public static ModuleStateE getType (int code) {
        for (ModuleStateE type : ModuleStateE.values()) {
            if (type.CODE == code) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
