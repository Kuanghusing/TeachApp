package adapter.holder;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.news.teachapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import adapter.ChatAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import entity.MessageInfo;
import utils.Constants;
import utils.Utils;
import widget.BubbleImageView;
import widget.GifTextView;

/**
 *发送消息的ViewHolder
 */
public class ChatSendViewHolder extends BaseViewHolder<MessageInfo> {

    @Bind(R.id.chat_item_date)
    TextView chatItemDate;  //发送的时间
    @Bind(R.id.chat_item_header)
    ImageView chatItemHeader;  //头像
    @Bind(R.id.chat_item_content_text)
    GifTextView chatItemContentText;  //发送的内容
    @Bind(R.id.chat_item_content_image)
    BubbleImageView chatItemContentImage;  //发送的图片
    @Bind(R.id.chat_item_fail)
    ImageView chatItemFail;   //发送失败
    @Bind(R.id.chat_item_progress)
    ProgressBar chatItemProgress;  //发送中
    @Bind(R.id.chat_item_voice)
    ImageView chatItemVoice;  //录音的图片
    @Bind(R.id.chat_item_layout_content)
    LinearLayout chatItemLayoutContent;   //气泡的图片
    @Bind(R.id.chat_item_voice_time)
    TextView chatItemVoiceTime;  //录音的时间长度
    private ChatAdapter.onItemClickListener onItemClickListener;   //聊天适配器的item点击监听
    private Handler handler;

    public ChatSendViewHolder(ViewGroup parent, ChatAdapter.onItemClickListener onItemClickListener, Handler handler) {
        super(parent, R.layout.item_chat_send);
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
        this.handler = handler;
    }


    @Override
    public void setData(MessageInfo data) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        if (data.getTime() != null){
            Log.v(Constants.TAG,format.format(new Date(Long.parseLong(data.getTime()+"") )));
            chatItemDate.setText(format.format(new Date(Long.parseLong(data.getTime()+""))));
        }
        Glide.with(getContext()).load(data.getHeader()).into(chatItemHeader);
        chatItemHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onHeaderClick(getDataPosition());
            }
        });
        if (data.getContent() != null) {
            chatItemContentText.setSpanText(handler, data.getContent(), true);
            chatItemVoice.setVisibility(View.GONE);
            chatItemContentText.setVisibility(View.VISIBLE);
            chatItemLayoutContent.setVisibility(View.VISIBLE);
            chatItemVoiceTime.setVisibility(View.GONE);
            chatItemContentImage.setVisibility(View.GONE);
        } else if (data.getImageUrl() != null) {
            chatItemVoice.setVisibility(View.GONE);
            chatItemLayoutContent.setVisibility(View.GONE);
            chatItemVoiceTime.setVisibility(View.GONE);
            chatItemContentText.setVisibility(View.GONE);
            chatItemContentImage.setVisibility(View.VISIBLE);
            Glide.with(getContext()).load(data.getImageUrl()).into(chatItemContentImage);
            chatItemContentImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onImageClick(chatItemContentImage, getDataPosition());
                }
            });
        } else if (data.getFilePath() != null) {
            chatItemVoice.setVisibility(View.VISIBLE);
            chatItemLayoutContent.setVisibility(View.VISIBLE);
            chatItemContentText.setVisibility(View.GONE);
            chatItemVoiceTime.setVisibility(View.VISIBLE);
            chatItemContentImage.setVisibility(View.GONE);
            chatItemVoiceTime.setText(Utils.formatTime(data.getVoiceTime()));
            chatItemLayoutContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onVoiceClick(chatItemVoice, getDataPosition());
                }
            });
        }
        switch (data.getSendSate()) {
            case Constants.CHAT_ITEM_SENDING:  //发送中
                chatItemProgress.setVisibility(View.VISIBLE);
                chatItemFail.setVisibility(View.GONE);
                break;
            case Constants.CHAT_ITEM_SEND_ERROR:  //发送失败
                chatItemProgress.setVisibility(View.GONE);
                chatItemFail.setVisibility(View.VISIBLE);
                break;
            case Constants.CHAT_ITEM_SEND_SUCCESS:  //发送成功
                chatItemProgress.setVisibility(View.GONE);
                chatItemFail.setVisibility(View.GONE);
                break;
        }
    }
}
