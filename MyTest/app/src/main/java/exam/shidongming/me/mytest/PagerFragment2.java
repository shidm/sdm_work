package exam.shidongming.me.mytest;

import android.app.Activity;
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
import android.widget.ListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PagerFragment2 extends Fragment implements MyListView.ILoadListener {

    private static List mList;
    private MyListViewAdapter myAdapter;
    private List myList = new ArrayList();
    private MyListView listView;
    private String theContent1,theContent,theContent2;
    private Handler handler = new Handler();
    private Handler mHandler = new Handler();

    int a =0;

    private View view;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        it();
        myAdapter = new MyListViewAdapter(activity,R.layout.lv_item1,myList,false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment2,container,false);
        listView = (MyListView) view.findViewById(R.id.tv_ListView);
        listView.setInterface(this);
        listView.setAdapter(myAdapter);
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
                        myAdapter.notifyDataSetChanged();
                    }
                }, 3000);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GetImageOrContent contentShow = (GetImageOrContent) myList.get(position - 1);
                String myContent = contentShow.myContent;
                Show.getBitmapOrContent(null, myContent, true);
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
                myAdapter.notifyDataSetChanged();
            }
        }, 2000);
        return view;
    }

    public static void getContentList(List list){
        mList = list;
    }

    @Override
    public void onLoad() {
        final String[] url = new String[]{"http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_duan_comments&page=2"
                , "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_duan_comments&page=3"
                , "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_duan_comments&page=4",
                "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_duan_comments&page=5"};

        if (a < 4) {
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
                            myAdapter.notifyDataSetChanged();
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
            parseArryJson(comments);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseArryJson(String jsonData) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonData);
            for (int i = 0;i<jsonArray.length();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String content = jsonObject.getString("comment_content");
                String title = jsonObject.getString("comment_author");
                String time = jsonObject.getString("comment_date");
                String dz = jsonObject.getString("vote_positive");
                String cp = jsonObject.getString("vote_negative");
                String tc = jsonObject.getString("comment_approved");
                GetImageOrContent myGetImageOrContent = new GetImageOrContent(title, time
                        , dz, cp, tc, null, 0,content);
                myList.add(myGetImageOrContent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
