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

public class MainpageActivity extends AppCompatActivity {
    AlertDialog alertDialog;

    TextView username;
    Button  logout, changeinfo, gas, temp, led;
    SharedPreferences shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        username = (TextView)findViewById(R.id.username);
        changeinfo=(Button)findViewById(R.id.changeinfo);
        logout= (Button) findViewById(R.id.logout);
        temp = (Button) findViewById(R.id.temp);
        gas = (Button)findViewById(R.id.gas);
        led = (Button)findViewById(R.id.led);

        shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        final String user_id = shared.getString("userid", "");
        final String user_pw = shared.getString("userpassword", "");
        final String user_name = shared.getString("username", "");

        //username.setText(user_name+"님");

        changeinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(MainpageActivity.this).create();

                alertDialog.setTitle("회원정보 수정");
                alertDialog.setMessage("회원정보를 수정하시겠습니까?");
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
                        Intent intent = new Intent(MainpageActivity.this, UserchangeActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(MainpageActivity.this).create();

                alertDialog.setTitle("로그아웃");
                alertDialog.setMessage("로그아웃 하시겠습니까?");
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
                        Intent intent = new Intent(MainpageActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                SharedPreferences.Editor editor = shared.edit();
                editor.clear();
                editor.commit();
                alertDialog.show();
            }
        });

        gas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(MainpageActivity.this).create();

                alertDialog.setTitle("Gas 확인");
                alertDialog.setMessage("Gas 데이터를 확인하시겠습니까?");
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
                        Intent intent = new Intent(MainpageActivity.this, GasActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(MainpageActivity.this).create();

                alertDialog.setTitle("온도 확인");
                alertDialog.setMessage("온도 데이터를 확인하시겠습니까?");
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
                        Intent intent = new Intent(MainpageActivity.this, TempActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        led.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(MainpageActivity.this).create();

                alertDialog.setTitle("LED 점검");
                alertDialog.setMessage("LED 점검하시겠습니까?");
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
                        Intent intent = new Intent(MainpageActivity.this, LedActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        } );
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
