package exam.shidongming.me.mytest;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PagerFragment1 extends Fragment implements MyListView.ILoadListener {

    private static boolean sd;
    private MyListViewAdapter mAdapter;
    public static List mList = new ArrayList();
    private List myList = new ArrayList();
    private View view;
    private MyListView listView;

    private Handler handler = new Handler();
    private Handler mHandler = new Handler();

    private ScrollView scrollView;
    private ImageView imView;

    int a=0;

    public static void getImageList(List list,boolean x){
        mList = list;
        sd = x;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAdapter = new MyListViewAdapter(activity,R.layout.lv_item1,myList,true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment1,container,false);
        listView = (MyListView) view.findViewById(R.id.iv_ListView);
        listView.setInterface(this);
        listView.setAdapter(mAdapter);
        listView.setonRefreshListener(new MyListView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < mList.size(); i++) {
                            myList.add(mList.get(i));
                        }
                        listView.onRefreshComplete();
                        mAdapter.notifyDataSetChanged();
                    }
                },3000);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetImageOrContent imageShow = (GetImageOrContent) myList.get(position - 1);
                Show.getBitmapOrContent(imageShow, null,false);
                Show show = new Show();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.add(R.id.myShow, show);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mList.size(); i++) {
                    myList.add(mList.get(i));
                }
                mAdapter.notifyDataSetChanged();
            }
        }, 1000);

        return view;
    }

    @Override
     public void onLoad() {
        final String[] url = new String[]{"http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=2",
                "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=3"
                , "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=4"
                , "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=5"
                , "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=6"
                , "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=7"
                , "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=7"
                , "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=8"
                , "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=9"};
        if (a < 8) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    new AsyncTask<String, Integer, Void>() {

                        @Override
                        protected Void doInBackground(String... params) {
                            String Url = params[a];
                            HttpClient httpClient = new DefaultHttpClient();
                            HttpGet httpGet = new HttpGet(Url);
                            HttpResponse httpResponse = null;
                            try {
                                httpResponse = httpClient.execute(httpGet);
                                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                                    HttpEntity httpEntity = httpResponse.getEntity();
                                    final String read = EntityUtils.toString(httpEntity, "utf-8");
                                    parseJson(read);
                                    a++;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            mAdapter.notifyDataSetChanged();
                            listView.loadComplete();
                        }
                    }.execute(url);
                }
            }).start();
        }
    }

    private void parseJson(String jsonData){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            String comments = jsonObject.getString("comments");
                parseImageJson(comments);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseImageJson(String comments) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(comments);
            for ( int i = 0;i<4;i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("comment_author");
                String time = jsonObject.getString("comment_date");
                String dz = jsonObject.getString("vote_positive");
                String cp = jsonObject.getString("vote_negative");
                String tc = jsonObject.getString("comment_approved");
                JSONArray urlArray = jsonObject.getJSONArray("pics");
                String imageUrl = urlArray.getString(0);
                Bitmap bm =  downLoad(imageUrl);
                GetImageOrContent myGetImageOrContent = new GetImageOrContent(title, time
                        , dz, cp, tc, bm,0, null);
                myList.add(myGetImageOrContent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Bitmap downLoad(String url) {
        Bitmap mbitmap=null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = null;
            httpResponse = httpClient.execute(httpGet);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                InputStream inputStream = httpEntity.getContent();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                mbitmap = BitmapFactory.decodeStream(bufferedInputStream);
                inputStream.close();
                bufferedInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mbitmap;
    }
}
