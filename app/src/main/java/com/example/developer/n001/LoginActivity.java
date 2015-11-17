package com.example.developer.n001;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by developer on 11/1/2015.
 */
public class LoginActivity extends Activity {

    private EditText username;
    private EditText password;
    private Button loginB;
    private Button regb;

    private JSONParser jsonParser = new JSONParser();
    private String URL_TAG="http://csprojects.esy.es/mydb/login.php";
    private String REG_TAG="http://csprojects.esy.es/mydb/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        loginB=(Button)findViewById(R.id.loginbtn);
        regb=(Button)findViewById(R.id.reg);

        regb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attaplogin(1);
            }
        });

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attaplogin(0);

            }
        });

    }

    private void attaplogin(int n){
        String userp=username.getText().toString();
        String ps=password.getText().toString();
        if(TextUtils.isEmpty(userp))
        {
            username.setError("can not empty");
            return;
        }
        else if(TextUtils.isEmpty(ps))
        {
            password.setError("can not empty");
            return;
        }
        if (n==0)
             new LoginUserTask(userp,ps).execute();
        else
            new RegisterUserTask(userp,ps).execute();



    }

    private class LoginUserTask extends AsyncTask<Void,Void,Boolean>
    {
        private ProgressDialog mProgressDialog;
        private JSONObject jsonObjectResult=null;
        private String user;
        private String pass;
        private String error;

        public LoginUserTask(String user, String pass) {
            this.user = user;
            this.pass = pass;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog=ProgressDialog.show(LoginActivity.this,"processing ","check username and password",false,false);

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            List<NameValuePair> pairs=new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username",user));
            pairs.add(new BasicNameValuePair("password",pass));
            jsonObjectResult=jsonParser.makeHtttpRequest(URL_TAG,pairs);
            if (jsonObjectResult==null)
            {
                error="error in the connection";
                return false;
            }
            try{
                if(jsonObjectResult.getInt("success")==1)
                return true;
                else
                    error=jsonObjectResult.getString("message");


            }
            catch (Exception e){}
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mProgressDialog.dismiss();
            if (aBoolean){
                Intent i=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
            }else { Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();}
        }

    }

    private class RegisterUserTask extends AsyncTask<Void,Void ,Boolean>
    {
        private String error;
        private JSONObject jsonObjectResult=null;
        private ProgressDialog mProgressDialog;

        private String name;
        private    String pass;

        public RegisterUserTask(String name, String pass) {
            this.name = name;
            this.pass = pass;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog=ProgressDialog.show(LoginActivity.this,"PROCESS..","create new user",false,false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            List<NameValuePair> pairs=new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("username",name));
            pairs.add(new BasicNameValuePair("password",pass));
            jsonObjectResult=jsonParser.makeHtttpRequest(REG_TAG,pairs);
            if (jsonObjectResult==null) {
                error = "error on connection";
                return false;
            }
            try {
                if(jsonObjectResult.getInt("success")==1)
                    return true;
                else
                    error=jsonObjectResult.getString("message");
            }catch (Exception e){}

            return false;
        }



        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mProgressDialog.dismiss();
            if(aBoolean){
                Intent ii=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(ii);

            }else {
                Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();}
        }


    }







}
