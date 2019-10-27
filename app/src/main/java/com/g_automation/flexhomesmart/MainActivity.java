package com.g_automation.flexhomesmart;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.TypedValue;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MqttAndroidClient client;
    private String TAG = "MainActivity";
    private PahoMqttClient pahoMqttClient;
    private String clientid = "";
    private Timer myTimer;
    int n_botoes = 20;
    Button[] btn = new Button[n_botoes];

    boolean flag;

    String MENSAGENS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        FloatingActionButton fab = findViewById( R.id.fab );
        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make( view, "Replace with your own action", Snackbar.LENGTH_LONG )
                        .setAction( "Action", null ).show();
            }
        } );
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        NavigationView navigationView = findViewById( R.id.nav_view );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        drawer.addDrawerListener( toggle );
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener( this );

        clientid = "HomeAut2";


        //Get Edit field values from layout GUI
        EditText broke = (EditText) findViewById(R.id.txtbroke);
        EditText usuario = (EditText) findViewById(R.id.txtusuario);
        EditText senha   = (EditText) findViewById(R.id.txtsenha);
        String sbroke    = broke.getText().toString().trim();
        String susuario     = usuario.getText().toString().trim();
        String ssenha     = senha.getText().toString().trim();
        Button Ligar = (Button) findViewById(R.id.btnLigar);
        Button Desliagar = (Button) findViewById(R.id.btnDesligar);

        btn[0] = findViewById(R.id.btn1);

        pahoMqttClient = new PahoMqttClient();
        client = pahoMqttClient.getMqttClient(  getApplicationContext(),                        // Connect to MQTT Broker
                sbroke,
                clientid,
                susuario,
                ssenha
        );



        //Create listener for MQTT messages.
        mqttCallback();

        //Create Timer to report MQTT connection status every 1 second
        myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ScheduleTasks();
            }

        }, 0, 1000);

        Ligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    pahoMqttClient.publishMessage(client, "L", 2, "Envia/sub_do_esp");
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
        Desliagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    pahoMqttClient.publishMessage(client, "D", 2, "Envia/sub_do_esp");
                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });
        btn[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(flag){
                        pahoMqttClient.publishMessage(client, "D", 2, "Envia/sub_do_esp");
                    }else{
                        pahoMqttClient.publishMessage(client, "L", 2, "Envia/sub_do_esp");
                    }

                } catch (MqttException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate( R.menu.main, menu );
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

        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent configmodulos=new Intent(MainActivity.this,Config.class);
            startActivity(configmodulos);

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }
    private void ScheduleTasks()
    {
        //This method is called directly by the timer
        //and runs in the same thread as the timer.

        //We call the method that will work with the UI
        //through the runOnUiThread method.
        this.runOnUiThread(RunScheduledTasks);
    }


    private Runnable RunScheduledTasks = new Runnable() {
        public void run() {
            //This method runs in the same thread as the UI.

            //Check MQTT Connection Status
            TextView status  = (TextView) findViewById(R.id.statusconexao);
            String msg_new="";

            if(pahoMqttClient.mqttAndroidClient.isConnected() ) {
                msg_new = "Connected\r\n";
                status.setTextColor(0xFF00FF00); //Green if connected
                status.setTextSize( TypedValue.COMPLEX_UNIT_SP, 14);

                try {
                    pahoMqttClient.subscribe(client, "Recebe/pub_do_esp", 1);

                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
            else {
                msg_new = "Disconnected\r\n";
                status.setTextColor(0xFFFF0000); //Red if not connected
                status.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            }
            status.setText(msg_new);

        }
    };

    // Called when a subscribed message is received
    protected void mqttCallback() {
        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                //msg("Connection lost...");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                //TextView tvMessage = (TextView) findViewById(R.id.subscribedMsg);
                if(topic.equals("mycustomtopic1")) {
                    //Add custom message handling here (if topic = "mycustomtopic1")
                }
                else if(topic.equals("mycustomtopic2")) {
                    //Add custom message handling here (if topic = "mycustomtopic2")
                }
                else {
                    String msg = "topic: " + topic + "\r\nMessage: " + message.toString() + "\r\n";
                    //tvMessage.append( msg);
                    MENSAGENS = msg;
                    if(MENSAGENS.contains( "Saida Desligada" )){
                        Drawable draw = getResources().getDrawable( R.mipmap.btn_off );
                        btn[0].setCompoundDrawablesWithIntrinsicBounds( null, null, draw, null );
                        flag = false;

                    }else if(MENSAGENS.contains( "Saida Ligada" )){
                        Drawable draw = getResources().getDrawable( R.mipmap.btn_on );
                        btn[0].setCompoundDrawablesWithIntrinsicBounds( null, null, draw, null );
                        flag = true;

                    }
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
    }
}
