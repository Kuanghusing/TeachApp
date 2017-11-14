package entity;

/**
 * Created by Tian on 2017/11/13.
 * 班级名称
 */

public class ClassNameInfos {
    private int type;
    private String className;
    public static final int TYPE_CHECK=0;
    public static final int TYPE_NOCHECK=1;

    public ClassNameInfos(int type, String className) {
        this.type = type;
        this.className = className;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
