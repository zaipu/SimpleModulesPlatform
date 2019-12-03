package sequoia.modules.wire;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2019/10/3 19:42
 * @Email zhongchaoliang@hdu.edu.cn
 * @description:
 */
public enum PortTypeE {
    UNKNOWN(0, "未知状态"),
    PORT_IN(1, "消息输入口"),
    PORT_OUT(2, "消息输出口");


    public final int CODE;
    public final String text;

    PortTypeE(int code , String text) {
        this.CODE = code;
        this.text = text;
    }

    public static PortTypeE getType (int code) {
        for (PortTypeE type : PortTypeE.values()) {
            if (type.CODE == code) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
