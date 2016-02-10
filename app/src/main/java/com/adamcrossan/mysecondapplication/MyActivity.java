package com.adamcrossan.mysecondapplication;

import android.bluetooth.BluetoothAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class MyActivity extends AppCompatActivity {


    private GoogleApiClient client;
    public final static String EXTRA_MESSAGE = "com.adamcrossan.mysecondapplication.MESSAGE";
    public String message ;
   final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my, menu);
        return true;
    }


   /* public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }*/

    public void turnOn(View view) {
        if (!mBluetoothAdapter.isEnabled())
        {
            mBluetoothAdapter.enable();
            message = "Bluetooth is on";
        }
        else {
            mBluetoothAdapter.disable();
            message = "Bluetooth is off";
        }


        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void Finish(View view) {
        finish();
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

    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, "My Page", Uri.parse("http://host/path"),
                Uri.parse("android-app://com.adamcrossan.mysecondapplication/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mBluetoothAdapter.isEnabled())
        {
            mBluetoothAdapter.disable();
        }

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, "My Page", Uri.parse("http://host/path"),
                Uri.parse("android-app://com.adamcrossan.mysecondapplication/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}
