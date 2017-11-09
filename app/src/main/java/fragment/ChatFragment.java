package fragment;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.news.teachapp.FullImageActivity;
import com.news.teachapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import adapter.ChatAdapter;
import adapter.CommonFragmentPagerAdapter;
import entity.FullImageInfo;
import entity.MessageInfo;
import utils.Constants;
import utils.GlobalOnItemClickManagerUtils;
import utils.MediaManager;
import widget.EmotionInputDetector;
import widget.NoScrollViewPager;

/**
 * Created by Tian on 2017/10/30.
 * 聊天室
 */

public class ChatFragment extends Fragment {
    private EasyRecyclerView chat_list;//聊天列表---》EasyRecyclerView
    private ImageView emotion_voice;//语音
    private EditText edit_text;//输入框
    private TextView voice_text;//按住语音说话的文本
    private ImageView emotion_button;//表情按钮
    private ImageView emotion_add;//添加按钮
    private Button btn_send;//发送按钮
    private LinearLayout emotion_layout; //显示表情
    private NoScrollViewPager noscroll_viewpager;

    private List<Fragment> fragmentList;  //存放表情、照片、拍摄的
    private View view;
    private CommonFragmentPagerAdapter adapter;
    public static EmotionInputDetector mDetector;  //输入框管理类
    private LinearLayoutManager layoutManager;
    private ChatAdapter chatAdapter;  //聊天适配器
    private List<MessageInfo> messageInfos;
    private long lastTime = 0;
    //录音相关
    int animationRes = 0;
    int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_chat, container, false);
        }
        initViews();
        initData();
        return view;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在onCreate方法注册EventBus
        EventBus.getDefault().register(this);
    }

    private void initViews() {
        chat_list = view.findViewById(R.id.chat_list);
        emotion_voice = view.findViewById(R.id.emotion_voice);
        edit_text = view.findViewById(R.id.edit_text);
        voice_text = view.findViewById(R.id.voice_text);
        emotion_button = view.findViewById(R.id.emotion_button);
        emotion_add = view.findViewById(R.id.emotion_add);
        btn_send = view.findViewById(R.id.btn_send);
        emotion_layout = view.findViewById(R.id.emotion_layout);
        noscroll_viewpager = view.findViewById(R.id.noscroll_viewpager);
    }

    private void initData() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new ChatEmotionFragment()); //表情的fragment
        fragmentList.add(new ChatFunctionFragment());//图片，拍照的fragment
        adapter = new CommonFragmentPagerAdapter(getFragmentManager(), fragmentList);
        noscroll_viewpager.setAdapter(adapter);
        noscroll_viewpager.setCurrentItem(0);
        mDetector = EmotionInputDetector.with(getActivity())
                .setEmotionView(emotion_layout)
                .setViewPager(noscroll_viewpager)
                .bindToContent(chat_list)
                .bindToEditText(edit_text)
                .bindToEmotionButton(emotion_button)
                .bindToAddButton(emotion_add)
                .bindToSendButton(btn_send)
                .bindToVoiceButton(emotion_voice)
                .bindToVoiceText(voice_text)
                .build();
         /*点击表情管理监听事件*/
        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(getActivity());
        globalOnItemClickListener.attachToEditText(edit_text);

        chatAdapter = new ChatAdapter(getContext());
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        /* 设置布局管理器方法，实现RecylerView布局里面的内容显示方式；有三种：LinearLayoutManager：线性布局管理器
        StaggeredGridLayoutManager: 错列网格布局管理器
        GridLayoutManager：网格布局管理器*/
        chat_list.setLayoutManager(layoutManager);
        chat_list.setAdapter(chatAdapter);
        chat_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    /*正在滚动*/
                    case RecyclerView.SCROLL_STATE_IDLE:
                        //清除当前mHandler消息队列
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        chatAdapter.notifyDataSetChanged();
                        break;
                    //正在被外部拖拽,一般为用户正在用手指滚动
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        mDetector.hideEmotionLayout(false);
                        mDetector.hideSoftInput();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        chatAdapter.addItemClickListener(itemClickListener);
        LoadData();//聊天数据
    }


    /**
     * item点击事件
     */
    private ChatAdapter.onItemClickListener itemClickListener = new ChatAdapter.onItemClickListener() {
        @Override
        public void onHeaderClick(int position) {
            Toast.makeText(getContext(), "onHeaderClick", Toast.LENGTH_SHORT).show();
        }
        /*点击图片*/
        public void onImageClick(View view, int position) {
            int location[] = new int[2];
            view.getLocationOnScreen(location);
            FullImageInfo fullImageInfo = new FullImageInfo();
            fullImageInfo.setLocationX(location[0]);
            fullImageInfo.setLocationY(location[1]);
            fullImageInfo.setWidth(view.getWidth());
            fullImageInfo.setHeight(view.getHeight());
            fullImageInfo.setImageUrl(messageInfos.get(position).getImageUrl());
            EventBus.getDefault().postSticky(fullImageInfo);
            startActivity(new Intent(getContext(), FullImageActivity.class));
            // overridePendingTransition(0, 0);  动画
        }
        /*点击发送的语音*/
        @Override
        public void onVoiceClick(final ImageView imageView, final int position) {
            if (animView != null) {
                animView.setImageResource(res);
                animView = null;
            }
            switch (messageInfos.get(position).getType()) {
                case 1:
                    animationRes = R.drawable.voice_left;
                    res = R.mipmap.icon_voice_left3;
                    break;
                case 2:
                    animationRes = R.drawable.voice_right;
                    res = R.mipmap.icon_voice_right3;
                    break;
            }
            animView = imageView;
            animView.setImageResource(animationRes);
            animationDrawable = (AnimationDrawable) imageView.getDrawable();
            animationDrawable.start();
            /*播放语音*/
            MediaManager.playSound(messageInfos.get(position).getFilePath(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animView.setImageResource(res);
                }
            });
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final MessageInfo messageInfo) {
        /*用户发送消息*/
        messageInfo.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
        messageInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo.setSendSate(Constants.CHAT_ITEM_SENDING);
        long time = System.currentTimeMillis()- lastTime;
        if(time > 60* 1000){
            messageInfo.setTime(System.currentTimeMillis()+"");
        }
        lastTime = System.currentTimeMillis();
        messageInfos.add(messageInfo);
        chatAdapter.add(messageInfo);
        chat_list.scrollToPosition(chatAdapter.getCount() - 1);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                messageInfo.setSendSate(Constants.CHAT_ITEM_SEND_SUCCESS);
                chatAdapter.notifyDataSetChanged();
            }
        }, 2000);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                /*模拟消息回复*/
                MessageInfo message = new MessageInfo();
                message.setContent("这是模拟消息回复");
                message.setType(Constants.CHAT_ITEM_TYPE_LEFT);
                message.setHeader("http://tupian.enterdesk.com/2014/mxy/11/2/1/12.jpg");
                long time = System.currentTimeMillis()- lastTime;
                if(time > 60* 1000){
                    message.setTime(String.valueOf(System.currentTimeMillis()));
                }
                lastTime = System.currentTimeMillis();
                messageInfos.add(message);
                chatAdapter.add(message);
                chat_list.scrollToPosition(chatAdapter.getCount() - 1);
            }
        }, 3000);
    }

  /*  public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }*/


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }

    /**
     * 构造聊天数据
     */
    private void LoadData() {
        messageInfos = new ArrayList<>();

        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setContent("你好，欢迎使用Rance的聊天界面框架");
        messageInfo.setType(Constants.CHAT_ITEM_TYPE_LEFT);
        messageInfo.setHeader("http://tupian.enterdesk.com/2014/mxy/11/2/1/12.jpg");
        messageInfos.add(messageInfo);

        MessageInfo messageInfo1 = new MessageInfo();
        messageInfo1.setFilePath("http://www.trueme.net/bb_midi/welcome.wav");
        messageInfo1.setVoiceTime(3000);
        messageInfo1.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo1.setSendSate(Constants.CHAT_ITEM_SEND_SUCCESS);
        messageInfo1.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
        messageInfos.add(messageInfo1);

        MessageInfo messageInfo2 = new MessageInfo();
        messageInfo2.setImageUrl("http://img4.imgtn.bdimg.com/it/u=1800788429,176707229&fm=21&gp=0.jpg");
        messageInfo2.setType(Constants.CHAT_ITEM_TYPE_LEFT);
        messageInfo2.setHeader("http://tupian.enterdesk.com/2014/mxy/11/2/1/12.jpg");
        messageInfos.add(messageInfo2);

        MessageInfo messageInfo3 = new MessageInfo();
        messageInfo3.setContent("[微笑][色][色][色]");
        messageInfo3.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo3.setSendSate(Constants.CHAT_ITEM_SEND_ERROR);
        messageInfo3.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
        messageInfos.add(messageInfo3);

        chatAdapter.addAll(messageInfos);
    }


}
