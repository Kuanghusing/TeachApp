package entity;

/**
 * Created by Tian on 2017/11/14.
 * 在线情况
 */

public class OnlineInfos  {
    private int imageId;
    private String userName;
    private int type;
    public static final int TYPE_ENTER=0;
    public static final int TYPE_EXIT=1;

    public OnlineInfos(int imageId, String userName, int type) {
        this.imageId = imageId;
        this.userName = userName;
        this.type = type;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
