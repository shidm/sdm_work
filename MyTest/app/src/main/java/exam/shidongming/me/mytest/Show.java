package exam.shidongming.me.mytest;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

public class Show extends Fragment {

    private ScrollView scrollView;
    private ImageView imageView;
    private static GetImageOrContent get;
    private TextView textView;
    private static boolean isMyContent;
    private static String myContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.show, container, false);
        scrollView = (ScrollView) view.findViewById(R.id.my_ScrollView);
        textView = (TextView) view.findViewById(R.id.large_text);
        imageView = (ImageView) view.findViewById(R.id.large_image);

        if (!isMyContent) {
            scrollView.setVisibility(View.GONE);
            imageView.setImageBitmap(get.myBitmap);
        }else {
            imageView.setVisibility(View.GONE);
            textView.setText(myContent);
        }
        return view;
    }

    public static void getBitmapOrContent(GetImageOrContent getImageOrContent,String content
            ,boolean isContent){

        get = getImageOrContent;
        isMyContent =isContent;
        myContent = content;
    }
}
