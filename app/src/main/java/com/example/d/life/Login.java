package com.example.d.life;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );

        final ProgressBar progress =(ProgressBar) findViewById( R.id.lp );
        final EditText etusername = (EditText) findViewById( R.id.etusername );
        final EditText etpassword = (EditText) findViewById( R.id.etpassword );
        final Button blogin = (Button) findViewById( R.id.blogin );

        blogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blogin.setVisibility( View.GONE );
                progress.setVisibility(View.VISIBLE );
                final String username = etusername.getText().toString();
                final String password = etpassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonResponse = new JSONObject( response );
                            boolean success = jsonResponse.getBoolean( "success" );

                            if (success) {
                                String username = jsonResponse.getString( "username" );
                                String password = jsonResponse.getString( "password" );
                                String verify = jsonResponse.getString("verify");
                                String admin = jsonResponse.getString("admin");


                                if(!admin.equals("Y")) {


                                    SharedPreferences loginData = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = loginData.edit();
                                    editor.putString("username", username);
                                    editor.putString("password", password);
                                    editor.putString("verify", verify);
                                    editor.apply();

                                    if(verify.equals("Y"))
                                    {
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        loginData = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                        editor = loginData.edit();
                                        editor.putString("username", null);
                                        editor.apply();

                                        AlertDialog.Builder builder = new AlertDialog.Builder( Login.this );
                                        builder.setMessage( "Please verrify With an Admin" )
                                                .setNegativeButton( "Ok", null )
                                                .create()
                                                .show();
                                        blogin.setVisibility( View.VISIBLE );
                                        progress.setVisibility(View.GONE );

                                    }


                                }
                                else
                                {

                                    Intent intent = new Intent(Login.this, admin.class);
                                    startActivity(intent);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    finish();
                                    Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();

                                }




                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder( Login.this );
                                builder.setMessage( "Login failed" )
                                        .setNegativeButton( "Retry", null )
                                        .create()
                                        .show();
                                blogin.setVisibility( View.VISIBLE );
                                progress.setVisibility(View.GONE );
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                LoginRequest loginRequest = new LoginRequest( username, password, responseListener );
                RequestQueue queue = Volley.newRequestQueue( Login.this );
                queue.add( loginRequest );
            }
        } );

    }
}






