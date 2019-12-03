package sequoia.modules.wire;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2019/10/2 14:09
 * @Email zhongchaoliang@hdu.edu.cn
 * @description:
 */
public interface IPort {

    public String getPortName();

    public PortTypeE getPortType();

    /**
     *
     * @param value
     */
    public void setValue(Object value);

    public Object getValue();
}
