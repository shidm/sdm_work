package exam.shidongming.me.mytest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sdm on 2015/5/5.
 */
public class Login extends Activity implements View.OnClickListener {
    private EditText zh, mm;
    private Button zc, dl;
    private CheckBox remember_password;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        openService();
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        zh = (EditText) findViewById(R.id.zh);
        mm = (EditText) findViewById(R.id.mm);
        zc = (Button) findViewById(R.id.zc);
        dl = (Button) findViewById(R.id.dl);
        remember_password = (CheckBox) findViewById(R.id.remember_password);
        zc.setOnClickListener(this);
        dl.setOnClickListener(this);
        boolean remember = pref.getBoolean("remember_pw",false);
        Log.d("remember", String.valueOf(remember));
        if(remember){
            String mzh = pref.getString("zh","");
            String mmm = pref.getString("mm","");
            zh.setText(mzh);
            mm.setText(mmm);
            remember_password.setChecked(true);
        }
    }

    private void openService() {
        Intent uintent = new Intent(this,MyIntentService.class);
        startService(uintent);
    }

    @Override
    public void onClick(View v) {
        editor = pref.edit();
        switch (v.getId()){
            case R.id.zc:
                String zhx = zh.getText().toString();
                String mmx = mm.getText().toString();
                editor.putString("zhx", zhx);
                editor.putString("mmx", mmx);
                editor.commit();
                break;
            case R.id.dl:
                String mzh = zh.getText().toString();
                String mmm = mm.getText().toString();
                String xzh = pref.getString("zhx","");
                String xmm = pref.getString("mmx","");
                if (mzh.equals(xzh) && mmm.equals(xmm)) {

                    if (remember_password.isChecked()) {
                        editor.putString("zh", mzh);
                        editor.putString("mm", mmm);
                        editor.putBoolean("remember_pw", true);
                    } else {
                        editor.clear();
                    }
                    editor.commit();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Login.this,"accout or password is invalid",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
