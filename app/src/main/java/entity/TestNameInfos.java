package entity;

/**
 * Created by Tian on 2017/11/13.
 * 测验室的名称信息
 */

public class TestNameInfos {
    private int imageId;
    private String testName;
    private String createName;
    private String createDate;
    private String createTime;

    public TestNameInfos(int imageId, String testName, String createName, String createDate, String createTime) {
        this.imageId = imageId;
        this.testName = testName;
        this.createName = createName;
        this.createDate = createDate;
        this.createTime = createTime;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
