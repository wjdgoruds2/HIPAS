package com.example.hipas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.android.volley.VolleyLog.TAG;

public class signup extends AppCompatActivity {
    AlertDialog alertDialog;
    EditText userid2, userpassword2, username, email, userpassword2check;
    Button idcheck, sign;
    ImageButton cancel;

    String user_id;
    String url = "https://scv0319.cafe24.com/hipas/hipas_signup.php";

    //private boolean checking = false;
    private boolean validate = false;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        userid2 = (EditText)findViewById(R.id.userid2);
        userpassword2 = (EditText)findViewById(R.id.userpassword2);
        userpassword2check = (EditText)findViewById(R.id.userpassword2check);
        username = (EditText)findViewById(R.id.username);
        email=(EditText)findViewById(R.id.email);
        idcheck = (Button)findViewById(R.id.idcheck);
        sign = (Button)findViewById(R.id.sign);
        cancel = (ImageButton)findViewById(R.id.cancel);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        //Log.d(TAG, msg);
                        Toast.makeText(signup.this, token, Toast.LENGTH_SHORT).show();
                        System.out.println(token);
                    }
                });
        idcheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_id = userid2.getText().toString();
                if(user_id.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signup.this);
                    alertDialog = builder.setMessage("아이디를 입력해주세요.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                }
                else{
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                //Toast.makeText(Signup.this, response, Toast.LENGTH_LONG).show();
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if(success){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(signup.this);
                                    alertDialog = builder.setMessage("사용 가능한 아이디입니다.")
                                            .setPositiveButton("OK", null)
                                            .create();
                                    alertDialog.show();
                                    validate = true;
                                }
                                else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(signup.this);
                                    alertDialog = builder.setMessage("이미 사용중인 아이디입니다.")
                                            .setNegativeButton("OK", null)
                                            .create();
                                    alertDialog.show();
                                    validate = false;
                                    userid2.setText("");
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    };
                    ValidateRequest ValidateRequest = new ValidateRequest(user_id, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(signup.this);
                    queue.add(ValidateRequest);
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.setTitle("Already have id?");
                alertDialog.setMessage("아이디를 가지고 계십니까?");
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
                        //SharedPreferences.Editor editor = shared.edit();
                        //editor.clear();
                        //editor.commit();
                        Intent intent = new Intent(signup.this, MainActivity.class);
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
                        Toast.makeText(signup.this, "Email형식으로 입력하세요", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    if ( !m.matches()){
                        Toast.makeText(signup.this, "Email형식으로 입력하세요", Toast.LENGTH_SHORT).show();
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
                String stoken = token;

                if(sid.isEmpty() || spassword.isEmpty() || sname.isEmpty() || semail.isEmpty()){
                    Toast.makeText(signup.this, "빈칸없이 다 채워주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(!user_id.equals(sid)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signup.this);
                    alertDialog = builder.setMessage("아이디 중복검사를 해주세요.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                }
                else if(!spassword.equals(spasswordcheck)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signup.this);
                    alertDialog = builder.setMessage("비밀번호를 일치하게 입력해주세요.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                }
                else if(validate==false){
                    AlertDialog.Builder builder = new AlertDialog.Builder(signup.this);
                    alertDialog = builder.setMessage("아이디 중복검사를 해주세요.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                }
                else{
                    signup(sid, spassword, sname, semail, stoken);
                    AlertDialog.Builder builder = new AlertDialog.Builder(signup.this);
                    alertDialog = builder.setMessage("회원가입을 축하합니다.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertDialog.show();
                    show();
                }
            }
        });
    }

    public void show(){
        Intent intent = new Intent(signup.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void signup(final String userid, final String userpassword,final String username, final String email, final String token){
        RequestQueue requestQueue = Volley.newRequestQueue(signup.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Hitesh",""+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Hitesh",""+error);
                Toast.makeText(signup.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap = new HashMap<>();
                stringMap.put("userid",userid);
                stringMap.put("userpassword",userpassword);
                stringMap.put("username",username);
                stringMap.put("email",email);
                stringMap.put("token", token);
                return stringMap;
            }
        };
        requestQueue.add(stringRequest);
    }
}
