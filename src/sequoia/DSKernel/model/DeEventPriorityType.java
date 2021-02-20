package sequoia.DSKernel.model;

public enum DeEventPriorityType {

    DEFAULT_PRIORITY(1, "默认优先级，一般性任务的优先级"),
    SYSTEM_PRIORITY(10, "系统层级的紧急任务优先级，比如充电任务，指令任务（紧急暂停）"),
    UNKNOWN(0, "未知优先级，最低");

    public final int CODE;
    public final String text;

    DeEventPriorityType(int code, String text) {
        this.CODE = code;
        this.text = text;
    }

    public static DeEventPriorityType getType(int code) {
        for (DeEventPriorityType type : DeEventPriorityType.values()) {
            if (type.CODE == code) {
                return type;
            }
        }
        return UNKNOWN;
    }

    public static int getDefaultPriority(){
        return DEFAULT_PRIORITY.CODE;
    }

    public static int getSystemPriority(){
        return SYSTEM_PRIORITY.CODE;
    }
}
