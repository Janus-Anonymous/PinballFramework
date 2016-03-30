package com.mobicom.pinballframework;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.pinball.PinballRequest;
import com.android.pinball.PinballRequest;
import com.android.pinball.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        /**
         * This example is just for testing.
         * It's not the formal usage of Pinball.
         *
         */
        PinballFramework pf = PinballFramework.getInstance(this);
        String url;
        final ImageView imageView = (ImageView) findViewById(R.id.image);
        url = "http://img02.tooopen.com/images/20160125/tooopen_sy_155368082418.jpg";
        PinballRequest imageRequest = pf.createRequest(url, PinballRequest.ACTIVE, "New Image Request");
        imageRequest.setShouldCache(false);
        imageRequest.setEndTime(imageRequest.getEndTime() + 1000);
        pf.putRequest(imageRequest, new Response.Listener<byte[]>() {
            /**
             * Do something with call back response in onResponse() method.
             * @param response
             */
            @Override
            public void onResponse(byte[] response) {
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(response, 0, response.length));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
