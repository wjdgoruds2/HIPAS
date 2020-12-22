package com.example.hipas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    AlertDialog alertDialog;

    EditText userid, userpassword;       //login.xml에서 id = userid, id = userpassword를 가져오는 변수
    Button loginbutton, signupbutton;   //login.xml에서 버튼 가져오는 변수
    // TextView findinfo;                         //login.xml에서 아이디/비밀번호 찾기 텍스트 가져오는 변수

    String user_id, user_password;  //userid와 userpassword의 값을 저장하는 변수
    String url;    //서버 기본 주소 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
/*        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(!task.isSuccessful()){
                            Log.w("FCM Log", "getInstancdId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d("FCM Log", "FCM 토근: "+token) ;
                        Toast.makeText(MainActivity.this, token, Toast.LENGTH_SHORT).show();
                    }
                });*/
        userid = (EditText)findViewById(R.id.userid);
        userpassword = (EditText)findViewById(R.id.userpassword);
        loginbutton = (Button) findViewById(R.id.LoginButton);
        signupbutton = (Button) findViewById(R.id.SignupButton);

        SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        user_id = shared.getString("userid", "0");
        user_password = shared.getString("userpassword", "0");


        //  findinfo= (TextView)findViewById(R.id.findinfo);
        //로그인 버튼 클릭시 발생 이벤트
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_id = userid.getText().toString();
                user_password = userpassword.getText().toString();

                if(user_id.isEmpty()){
                    Toast.makeText(MainActivity.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(user_password.isEmpty()){
                    Toast.makeText(MainActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    login(user_id, user_password);
                }
            }
        });

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, signup.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void save(){
        SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        user_id = shared.getString("userid", "0");
        user_password = shared.getString("userpassword", "0");
        if(!user_id.isEmpty()  && !user_password.isEmpty()){
            login2(user_id, user_password);
        }
    }

    public void login(final String userid, final String userpassword){
        url = "https://scv0319.cafe24.com/hipas/hipas_login.php?userid="+userid+"&userpassword="+userpassword+"";
        Log.i("Hiteshurl", ""+url);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String userid2 = jsonObject1.getString("userid");
                    String userpassword2 = jsonObject1.getString("userpassword");
                    String username2 = jsonObject1.getString("username");
                    SharedPreferences shared = getSharedPreferences("Mypref",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("userid",userid2);
                    editor.putString("userpassword",userpassword2);
                    editor.putString("username", username2);
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this,MainpageActivity.class);
                    startActivity(intent);
                }catch (JSONException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    alertDialog = builder.setMessage("ID가 존재하지 않거나 비밀번호가 일치하지않습니다.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("HiteshURLerror",""+error);
            }
        });
        requestQueue.add(stringRequest);
    }



    public void login2(final String userid, final String userpassword){
        url = "https://scv0319.cafe24.com/hipas/hipas_login.php?userid="+userid+"&userpassword="+userpassword+"";
        Log.i("Hiteshurl", ""+url);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String userid2 = jsonObject1.getString("userid");
                    String userpassword2 = jsonObject1.getString("userpassword");
                    String username2 = jsonObject1.getString("username");
                    SharedPreferences shared = getSharedPreferences("Mypref",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("userid",userid2);
                    editor.putString("userpassword",userpassword2);
                    editor.putString("username", username2);
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this,MainpageActivity.class);
                    startActivity(intent);
                }catch (JSONException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    alertDialog = builder.setMessage("로그인을 해주세요.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("HiteshURLerror",""+error);
            }
        });
        requestQueue.add(stringRequest);
    }
}
