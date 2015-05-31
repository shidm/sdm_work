package exam.shidongming.me.mytest;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyListViewAdapter extends ArrayAdapter {
    

    private int resourceId;
    private boolean isImage=false;

    public MyListViewAdapter(Context context, int resource, List objects,boolean is) {
        super(context, resource, objects);
        resourceId = resource;
        isImage = is;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        GetImageOrContent mGetImageOrContent= (GetImageOrContent) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_image);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_cp = (TextView) convertView.findViewById(R.id.tv_cp);
            viewHolder.tv_dz = (TextView) convertView.findViewById(R.id.tv_dz);
            viewHolder.tv_tc = (TextView) convertView.findViewById(R.id.tv_tc);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!isImage) {
            convertView.findViewById(R.id.iv_image).setVisibility(View.GONE);
            viewHolder.tv_content.setText(mGetImageOrContent.myContent);
        }else {
            convertView.findViewById(R.id.tv_content).setVisibility(View.GONE);
            if(mGetImageOrContent.myId==0) {
                viewHolder.imageView.setImageBitmap(mGetImageOrContent.myBitmap);
            }else {
                viewHolder.imageView.setImageResource(mGetImageOrContent.myId);
            }
        }

        viewHolder.tv_cp.setText(mGetImageOrContent.myCp);
        viewHolder.tv_dz.setText(mGetImageOrContent.myDz);
        viewHolder.tv_tc.setText(mGetImageOrContent.myTc);
        viewHolder.tv_time.setText(mGetImageOrContent.myTime);
        viewHolder.tv_title.setText(mGetImageOrContent.myTitle);
        return convertView;
    }

    class ViewHolder {
        ImageView imageView ;
        TextView tv_dz,tv_cp,tv_tc,tv_content,tv_time,tv_title;
    }
}
