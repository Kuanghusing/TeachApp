package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.news.teachapp.R;

import java.util.ArrayList;
import java.util.List;

import adapter.EmotionGridViewAdapter;
import adapter.EmotionPagerAdapter;
import base.BaseFragment;
import base.MyApplication;
import butterknife.Bind;
import butterknife.ButterKnife;
import utils.EmotionUtils;
import utils.GlobalOnItemClickManagerUtils;
import utils.Utils;
import widget.IndicatorView;

/**
 *表情按钮显示的Fragment
 */
public class ChatEmotionFragment extends BaseFragment {
    @Bind(R.id.fragment_chat_vp)
    ViewPager fragmentChatVp;//存放表情
    @Bind(R.id.fragment_chat_group)
    IndicatorView fragmentChatGroup;//IndicatorView自定义表情底部小圆点的指示器
    private View rootView;
    private EmotionPagerAdapter emotionPagerAdapter;//表情的适配器
    private static final String TAG="ChatEmotionFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_chat_emotion, container, false);
            ButterKnife.bind(this, rootView);
            initWidget();
        }
        return rootView;
    }

    private void initWidget() {
        //监听view pager每一页改变时
        fragmentChatVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int oldPagerPos = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //页面移动时切换指示器
                fragmentChatGroup.playByStartPointToNext(oldPagerPos, position);
                oldPagerPos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initEmotion();//初始化表情
    }

    /**
     * 初始化表情面板
     * 思路：获取表情的总数，按每行存放7个表情，动态计算出每个表情所占的宽度大小（包含间距），
     *      而每个表情的高与宽应该是相等的，这里我们约定只存放3行
     *      每个面板最多存放7*3=21个表情，再减去一个删除键，即每个面板包含20个表情
     *      根据表情总数，循环创建多个容量为20的List，存放表情，对于大小不满20进行特殊
     *      处理即可。
     */
    private void initEmotion() {
        // 获取屏幕宽度
        int screenWidth = MyApplication.screenWidth;
        Log.v(TAG,screenWidth+"");
        // item的间距，dp转为dip,表情之间的间距
        int spacing = Utils.dp2px(getActivity(), 12);
        // 动态计算item的宽度和高度，每个表情的宽度
        int itemWidth = (screenWidth - spacing * 8) / 7;
        //动态计算gridview的总高度，三行
        int gvHeight = itemWidth * 3 + spacing * 6;
        Log.v(TAG,gvHeight+"gvHeight");
        List<GridView> emotionViews = new ArrayList<>();
        List<String> emotionNames = new ArrayList<>();
        // 遍历所有的表情的key
        for (String emojiName : EmotionUtils.EMOTION_STATIC_MAP.keySet()) {
            emotionNames.add(emojiName);
            if (emojiName!=null){

            }
            // 每20个表情作为一组,同时添加到ViewPager对应的view集合中
            if (emotionNames.size() == 23) {
                GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
                emotionViews.add(gv);
                // 添加完一组表情,重新创建一个表情名字集合
                emotionNames = new ArrayList<>();
            }
        }

        // 判断最后是否有不足23个表情的剩余情况
        if (emotionNames.size() > 0) {
            GridView gv = createEmotionGridView(emotionNames, screenWidth, spacing, itemWidth, gvHeight);
            emotionViews.add(gv);
        }

        //初始化指示器
        fragmentChatGroup.initIndicator(emotionViews.size());
        // 将多个GridView添加显示到ViewPager中
        emotionPagerAdapter = new EmotionPagerAdapter(emotionViews);
        Log.v("123456789",emotionViews.toString());
        fragmentChatVp.setAdapter(emotionPagerAdapter);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, gvHeight);
        fragmentChatVp.setLayoutParams(params);


    }

    /**
     * 创建显示表情的GridView
     */
    private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
        // 创建GridView
        GridView gv = new GridView(getActivity());
        //设置点击背景透明
        gv.setSelector(android.R.color.transparent);
        //设置8列
        gv.setNumColumns(8);
        gv.setPadding(padding, padding, padding, padding);
        gv.setHorizontalSpacing(padding);
        gv.setVerticalSpacing(padding * 2);
       // gv.setBackgroundColor(Color.BLUE);
        Log.v("123",gvWidth+"");
        Log.v("123",gvHeight+"");
        //设置GridView的宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gv.setLayoutParams(params);
        // 给GridView设置表情图片
        EmotionGridViewAdapter adapter = new EmotionGridViewAdapter(getActivity(), emotionNames, itemWidth);
        gv.setAdapter(adapter);
        //设置全局点击事件
        gv.setOnItemClickListener(GlobalOnItemClickManagerUtils.getInstance(getActivity()).getOnItemClickListener());
        return gv;
    }

}
