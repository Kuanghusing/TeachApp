package entity;

/**
 * Created by Tian on 2017/10/31.
 * 消息列表
 */

public class MessageInfo {
    private int type;  //1.接受    2.发送
    private int sendSate;   //1.发送中   2.发送错误   3.发送成功
    private String content;  //发送的内容
    private String filePath;  //文件路径
    private String time; //发送、接收的时间
    private String header;  //头像
    private String imageUrl; //图片的url
    private String msgId;  //图片的id
    private long voiceTime;  //语音的时间长度

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSendSate() {
        return sendSate;
    }

    public void setSendSate(int sendSate) {
        this.sendSate = sendSate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public long getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(long voiceTime) {
        this.voiceTime = voiceTime;
    }

    @Override
    public String toString() {
        return "MessageInfo{" +
                "type=" + type +
                ", sendSate=" + sendSate +
                ", content='" + content + '\'' +
                ", filePath='" + filePath + '\'' +
                ", time='" + time + '\'' +
                ", header='" + header + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", msgId='" + msgId + '\'' +
                ", voiceTime=" + voiceTime +
                '}';
    }
}
