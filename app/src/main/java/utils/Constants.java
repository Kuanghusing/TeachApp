package utils;

/**
 *聊天的状态
 */
public class Constants {
    public static final String TAG = "teachApp";
    /** 0x001-接受消息  0x002-发送消息**/
    public static final int CHAT_ITEM_TYPE_LEFT = 0x001;
    public static final int CHAT_ITEM_TYPE_RIGHT = 0x002;
    /** 0x003-发送中  0x004-发送失败  0x005-发送成功**/
    public static final int CHAT_ITEM_SENDING = 0x003;
    public static final int CHAT_ITEM_SEND_ERROR = 0x004;
    public static final int CHAT_ITEM_SEND_SUCCESS = 0x005;
}
