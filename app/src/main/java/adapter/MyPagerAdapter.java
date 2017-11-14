package adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.news.teachapp.R;

import java.util.List;

import player.TxVideoPlayerController;
import player.VideoPlayer;



/**
 * Created by Tian on 2017/11/6.
 */

public class MyPagerAdapter extends PagerAdapter {
    private List<String> listTitles;
    private Context context;
    private VideoPlayer mNiceVideoPlayer;
    private TxVideoPlayerController controller;

    public MyPagerAdapter(Context context,List<String> listTitles){
        this.listTitles=listTitles;
        this.context = context;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view=View.inflate(context, R.layout.fragment_titles,null);
        mNiceVideoPlayer = (VideoPlayer) view.findViewById(R.id.video);
        mNiceVideoPlayer.setPlayerType(VideoPlayer.TYPE_IJK); // IjkPlayer or MediaPlayer
        String url="http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4";
        mNiceVideoPlayer.setUp(url,null);
         controller= new TxVideoPlayerController(context);
        controller.setTitle("Beautiful China...");
        controller.setLength(117000);
        Glide.with(context)
                .load("http://imgsrc.baidu.com/image/c0%3Dshijue%2C0%2C0%2C245%2C40/sign=304dee3ab299a9012f38537575fc600e/91529822720e0cf3f8b77cd50046f21fbe09aa5f.jpg")
                .placeholder(R.mipmap.img_default)
                .crossFade()
                .into(controller.imageView());
        mNiceVideoPlayer.setController(controller);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       // super.destroyItem(container, position, object);
    }

    @Override
    public int getCount() {
        return listTitles.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitles.get(position);
    }
}
