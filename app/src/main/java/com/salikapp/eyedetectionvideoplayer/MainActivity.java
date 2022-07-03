package com.salikapp.eyedetectionvideoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;



import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }


    public void pick(View view) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("video/*");

         startActivityForResult(intent,7);
//        startActivityForResult(intent, REQUEST_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch(requestCode){

            case 7:

                if(resultCode==RESULT_OK){

                    String PathHolder = data.getData().getPath();
                    PathHolder=PathHolder.substring(18);

                    //Toast.makeText(MainActivity.this, PathHolder , Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(MainActivity.this, Video.class);
                    intent.putExtra("message_key",PathHolder);
                    startActivity(intent);

                }
                break;

        }
    }

}
