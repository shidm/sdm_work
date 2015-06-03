package exam.shidongming.me.mytest;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MyIntentService extends IntentService {

    private List mlist = new ArrayList();
    private List mylist = new ArrayList();
    private static String url1 = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_duan_comments&page=1";
    private static String url2 = "http://jandan.net/?oxwlxojflwblxbsapi=jandan.get_ooxx_comments&page=1";
    private static String[] url = new String[]{url1,url2 };
    private boolean isContent;

    public MyIntentService() {
        super("MyIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new AsyncTask<String,Integer,Void>(){

                    @Override
                    protected Void doInBackground(String... params) {
                        for (int i=0;i<2;i++) {
                            if (i==0) {
                                isContent=true;
                            }else {
                                isContent=false;
                            }
                            String Url = params[i];
                            HttpURLConnection connection = null;
                            try {
                                URL url1 = new URL(Url);
                                connection = (HttpURLConnection) url1.openConnection();
                                connection.setRequestMethod("GET");
                                connection.setConnectTimeout(2000);
                                connection.setReadTimeout(2000);
                                InputStream inputStream = connection.getInputStream();
                                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                                StringBuilder builder = new StringBuilder();
                                String line = null;
                                if((line = reader.readLine())!=null){
                                    builder.append(line);
                                }

                                String s = builder.toString();
                                parseJson(s);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                    }
                }.execute(url);
            };
        }).start();
    }

    private void parseJson(String jsonData){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonData);
            String comments = jsonObject.getString("comments");
            if (isContent) {
                parseArryJson(comments);
            }else {
                parseImageJson(comments);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseImageJson(String comments) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(comments);
            for (int i = 0;i<7;i++) {
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
                mlist.add(myGetImageOrContent);
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
                mylist.add(myGetImageOrContent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

