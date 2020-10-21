package sequoia.modules.wire;





import java.util.List;
import sequoia.modules.IModule;
import sequoia.modules.ModulePlatform;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2019/10/2 14:15
 * @Email zhongchaoliang@hdu.edu.cn
 * @description:
 */
public class SimplePortOut  implements IPort{
    private String portName;
    private String srcModuleName = null;
    private long update_timestamp = System.currentTimeMillis();
    private long update_duration = 0;
    private WireConfig wireConfig = null;

    public SimplePortOut(WireConfig wireConfig) {
        this.wireConfig = wireConfig;
        this.srcModuleName= wireConfig.getSrcModuleName();
        this.portName = wireConfig.getName();
        this.update_duration = wireConfig.getDuration();
    }

    public String getSrcModuleName() {
        return srcModuleName;
    }

    @Override
    public String getPortName() {
        return portName;
    }

    @Override
    public PortTypeE getPortType() {
        return PortTypeE.PORT_OUT;
    }

    @Override
    public void setValue( Object v) {
        if (update_duration == 0) {
            updateValue(v);
        } else {
            if ((System.currentTimeMillis() - update_timestamp) > update_duration) {
                update_timestamp = System.currentTimeMillis();
                updateValue(v);
            }
        }
    }

    private void updateValue( Object v) {
        /*查找目标模块及其输入口 */
        ModulePlatform platform = ModulePlatform.getInstance();

        List<Target> list = wireConfig.getWireTargets();
        for (Target target : list) {
            IModule m = platform.getModule(target.getT_module());
            m.getPort(target.getName()).setValue(v);
        }
    }

    @Override
    public Object getValue() {
        //输出口不需要
        return null;
    }

    public long getUpdate_duration() {
        return update_duration;
    }

    public void setUpdate_duration(long update_duration) {
        this.update_duration = update_duration;
    }

}
