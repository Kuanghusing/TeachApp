package entity;

/**
 * Created by Tian on 2017/10/31.
 */

public class TestResultInfos {
    private int imageUrl;//用户头像
    private String useName;//用户名字
    private int total;//答题的数目
    private int correctTitle;//答对的题数
    private double score;//总得分
    private int errorTitle;//答错的题数

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUseName() {
        return useName;
    }

    public void setUseName(String useName) {
        this.useName = useName;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCorrectTitle() {
        return correctTitle;
    }

    public void setCorrectTitle(int correctTitle) {
        this.correctTitle = correctTitle;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(int errorTitle) {
        this.errorTitle = errorTitle;
    }

    public TestResultInfos(int imageUrl, String useName, int total, int correctTitle, double score, int errorTitle) {
        this.imageUrl = imageUrl;
        this.useName = useName;
        this.total = total;
        this.correctTitle = correctTitle;
        this.score = score;
        this.errorTitle = errorTitle;
    }
}
