package exam.shidongming.me.mytest;

import android.graphics.Bitmap;

public class GetImageOrContent {

    public String myTitle,myTime,myDz,myCp,myTc,myContent;
    public int myId;
    public Bitmap myBitmap;

    public GetImageOrContent(String title, String time
            , String dz, String cp, String tc, Bitmap bitmap,int id,String content){
        this.myTitle = title;
        this.myTime = time;
        this.myDz = dz;
        this.myCp = cp;
        this.myTc = tc;
        this.myBitmap = bitmap;
        this.myContent = content;
        this.myId = id;
    }
}
