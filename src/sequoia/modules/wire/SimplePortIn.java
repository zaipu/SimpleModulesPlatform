package sequoia.modules.wire;

import java.util.ArrayList;

/**
 * @author Zhong Chaoliang
 * @version 0.1
 * @date 2019/10/2 14:15
 * @Email zhongchaoliang@hdu.edu.cn
 * @description:
 */
public class SimplePortIn implements IPort {
    private String link_name;
    private Object value;
    private long update_timestamp = System.currentTimeMillis();
    private long update_duration = 0;
    private boolean bReady = false;
    private String targetModuleName = null;
    private ArrayList<IValueUpdateListener> listeners = new ArrayList<>();

    public SimplePortIn(Target target) {
        this.targetModuleName=target.getT_module();
        this.link_name = target.getName();
        this.update_duration = target.getDuration();
    }

    public String getTargetModuleName() {
        return targetModuleName;
    }

    @Override
    public String getPortName() {
        return link_name;
    }

    @Override
    public PortTypeE getPortType() {
        return PortTypeE.PORT_IN;
    }

    public void setUpdate_duration(long update_duration) {
        this.update_duration = update_duration;
    }

    @Override
    public void setValue(Object v) {
        if (update_duration == 0) {
            value = v;
            bReady = true;
            for (IValueUpdateListener listener : listeners) {
                listener.valueUpdated( value);
            }
        } else if ((System.currentTimeMillis() - update_timestamp) > update_duration) {

            value = v;
            update_timestamp = System.currentTimeMillis();
            bReady = true;

            for (IValueUpdateListener listener : listeners) {
                listener.valueUpdated( value);
            }
        }
    }

    @Override
    public Object getValue() {
        if (bReady) {
            bReady = false;
            return value;
        }
        return null;
    }


    /**
     * 注册一个监听者
     *
     * @param l - ValueUpdateListener
     */

    public void registerListener(IValueUpdateListener l) {
        listeners.add(l);
    }

    public void removeListener(IValueUpdateListener listener) {
        listeners.remove(listener);
    }
}
