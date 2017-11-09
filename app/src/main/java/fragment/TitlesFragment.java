package fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.news.teachapp.R;

/**
 * Created by Tian on 2017/11/6.
 */

public class TitlesFragment extends Fragment implements View.OnClickListener{
    ImageView iv_photo;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_titles,container,false);
        iv_photo=view.findViewById(R.id.iv_photo);
        iv_photo.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_photo:
                Toast.makeText(getContext(), "你点击了我", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
