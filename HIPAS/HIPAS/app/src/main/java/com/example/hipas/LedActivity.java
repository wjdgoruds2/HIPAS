package com.example.hipas;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class LedActivity extends AppCompatActivity {
    Button led_on,led_off;
    ImageView imageView2, imageView;
    AlertDialog alertDialog;
    int check = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_led );

        led_on = (Button)findViewById(R.id.led_on);
        led_off = (Button)findViewById(R.id.led_off);

        imageView2 = (ImageView)findViewById(R.id.imageView2);
        imageView = (ImageView)findViewById(R.id.imageView);

        imageView.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.INVISIBLE);

        led_on.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(LedActivity.this).create();

                alertDialog.setTitle("LED ON");
                alertDialog.setMessage("LED를 켜시겠습니까?");
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
                        String test = "https://scv0319.cafe24.com/hipas/ledCheck.php?option=1";
                        URLConnector task = new URLConnector(test);

                        imageView.setVisibility(View.INVISIBLE);
                        imageView2.setVisibility(View.VISIBLE);

                        task.start();

                        try {
                            task.join();
                            System.out.println("waiting... for result");
                        } catch (InterruptedException e) {

                        }

                        String result = task.getResult();
                    }
                });
                alertDialog.show();
            }
        } );
        led_off.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(LedActivity.this).create();

                alertDialog.setTitle("LED OFF");
                alertDialog.setMessage("LED를 끄시겠습니까?");
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
                        imageView.setVisibility(View.VISIBLE);
                        imageView2.setVisibility(View.INVISIBLE);

                        String test = "https://scv0319.cafe24.com/hipas/ledCheck.php?option=0";
                        URLConnector task = new URLConnector(test);

                        task.start();

                        try {
                            task.join();
                            System.out.println("waiting... for result");
                        } catch (InterruptedException e) {

                        }
                        String result = task.getResult();
                    }
                });
                alertDialog.show();
            }
        } );
    }

}
