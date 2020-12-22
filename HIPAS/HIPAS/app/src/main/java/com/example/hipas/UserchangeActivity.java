package com.example.hipas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserchangeActivity extends AppCompatActivity {
    AlertDialog alertDialog;
    SharedPreferences shared;

    TextView userid2, username;
    EditText userpassword2, email, userpassword2check;
    Button sign;
    ImageButton cancel;

    String user_id;
    String url = "https://scv0319.cafe24.com/hipas/hipas_userchange.php";

    //private boolean checking = false;
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userchange);

        userid2 = (TextView) findViewById(R.id.userid2);
        userpassword2 = (EditText)findViewById(R.id.userpassword2);
        userpassword2check = (EditText)findViewById(R.id.userpassword2check);
        username = (TextView)findViewById(R.id.username);
        email=(EditText)findViewById(R.id.email);
        sign = (Button)findViewById(R.id.sign);
        cancel = (ImageButton)findViewById(R.id.cancel);

        shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        final String user_id = shared.getString("userid", "");
        final String user_pw = shared.getString("userpassword", "");
        final String user_name = shared.getString("username", "");

        userid2.setText(user_id);
        username.setText(user_name);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(UserchangeActivity.this).create();
                alertDialog.setTitle("회원정보수정 취소");
                alertDialog.setMessage("회원정보수정을 취소하겠습니까?");
                alertDialog.setCancelable(false);
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = shared.edit();
                        editor.clear();
                        editor.commit();
                        Intent intent = new Intent(UserchangeActivity.this, MainpageActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Pattern p = Pattern.compile("^[a-zA-X0-9]@[a-zA-Z0-9].[a-zA-Z0-9]");
                Matcher m = p.matcher((email).getText().toString());

                if(hasFocus) {
                    if ( !m.matches()){
                        Toast.makeText(UserchangeActivity.this, "Email형식으로 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if ( !m.matches()){
                        Toast.makeText(UserchangeActivity.this, "Email형식으로 입력하세요", Toast.LENGTH_SHORT).show();
                        email.setText("");
                    }
                }
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sid = userid2.getText().toString();
                String spassword = userpassword2.getText().toString();
                String spasswordcheck = userpassword2check.getText().toString();
                String sname = username.getText().toString();
                String semail = email.getText().toString();

                if(sid.isEmpty() || spassword.isEmpty() || sname.isEmpty() || semail.isEmpty()){
                    Toast.makeText(UserchangeActivity.this, "빈칸없이 다 채워주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(!spassword.equals(spasswordcheck)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserchangeActivity.this);
                    alertDialog = builder.setMessage("비밀번호를 일치하게 입력해주세요.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                }
                else{
                    signup(sid, spassword, sname, semail);
                    SharedPreferences preferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.putString("userid",sid);
                    editor.putString("userpassword",spassword);
                    editor.putString("username",sname);
                    editor.putString("email",semail);
                    alertDialog = new AlertDialog.Builder(UserchangeActivity.this).create();
                    alertDialog.setTitle("회원정보 수정");
                    alertDialog.setMessage("회원정보수정를 수정하셨습니다.\n다시 로그인 해주세요.");

                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = shared.edit();
                            editor.clear();
                            editor.commit();
                            Intent intent = new Intent(UserchangeActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                    onPause();
                    show();
                }
            }
        });
    }

    public void show(){
        Intent intent = new Intent(UserchangeActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void signup(final String userid, final String userpassword,final String username, final String email){
        RequestQueue requestQueue = Volley.newRequestQueue(UserchangeActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Hitesh",""+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Hitesh",""+error);
                Toast.makeText(UserchangeActivity.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap = new HashMap<>();
                stringMap.put("userid",userid);
                stringMap.put("userpassword",userpassword);
                stringMap.put("username",username);
                stringMap.put("email",email);
                return stringMap;
            }
        };
        requestQueue.add(stringRequest);
    }
}
