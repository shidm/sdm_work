package exam.shidongming.me.mytest;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PagerFragment3 extends Fragment  {

    private View view;
    private MyListViewAdapter myAdapter;
    private List myList = new ArrayList();
    private MyListView listView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        readContacts(activity);
        myAdapter = new MyListViewAdapter(activity,R.layout.lv_item1,myList,false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment3,container,false);
        listView = (MyListView) view.findViewById(R.id.my_dhb);
        listView.setAdapter(myAdapter);
        listView.setonRefreshListener(new MyListView.OnRefreshListener() {

            @Override
            public void onRefresh() {

                listView.onRefreshComplete();
                myAdapter.notifyDataSetChanged();
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
        return view;
    }

    private void readContacts(Activity activity){

        Cursor cursor = null;
        try {
            cursor = activity.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null,null,null);
            String s = String.valueOf(cursor.moveToNext());
            String time = String.valueOf(System.currentTimeMillis());
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                String number = cursor.getString(cursor.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                GetImageOrContent gioc = new GetImageOrContent(name,time,"电话","就是电话","妈蛋",null,0,number);
                myList.add(gioc);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(cursor!=null){
                cursor.close();
            }
        }
    }
}
