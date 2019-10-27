package com.g_automation.flexhomesmart;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Config extends AppCompatActivity {
    Button btnsalvar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_config );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        TextView status  = (TextView) findViewById(R.id.statusconexao);
        EditText broke = (EditText) findViewById(R.id.txtbroke);
        EditText usuario = (EditText) findViewById(R.id.txtusuario);
        EditText senha   = (EditText) findViewById(R.id.txtsenha);
        String msg_new="";

        btnsalvar = findViewById(R.id.btnsalvar);

        btnsalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Configuraçãos salvas", Toast.LENGTH_LONG).show();
                Intent configmodulos=new Intent(Config.this,MainActivity.class);
                startActivity(configmodulos);
                finish();
            }
        });
    }

}
