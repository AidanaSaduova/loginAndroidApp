package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import io.swagger.client.ApiException;
import io.swagger.client.api.DefaultApi;

import io.swagger.client.model.UserLoginApiForm;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText Username;
    private EditText Password;
    private Button Login;
    private EditText name;
    private EditText passw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Username = (EditText)findViewById(R.id.etUsername);
        Password = (EditText)findViewById(R.id.etPassword);
        Login = (Button)findViewById(R.id.btnLogin);
        Login.setOnClickListener(this);

    }


    private void validate(String username, String password){
        if(username.equals("Admin") && password.equals("1234")){
            //Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            showToast(R.string.loginSuccess);
            //startActivity(intent);

        }else{
            showToast(R.string.loginFailed);
        }
    }


    public void showToast(final int resId){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Toast.makeText(MainActivity.this, resId, Toast.LENGTH_LONG).show();
                } catch (Exception ex) {
                    Log.e("com.myMovie.guru", "Could not show the toast. " + ex.getMessage());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                validate(Username.getText().toString(),Password.getText().toString());
                UserLoginApiForm formToSend = wrapToModel(Username.getText().toString(), Password.getText().toString());
                //validateForm(formToSend);


        }

    }

    private UserLoginApiForm wrapToModel(String username, String password){
        UserLoginApiForm form = new UserLoginApiForm();
        form.setName(username);
        form.setPassword(password);
        return form;
    }


    private void validateForm(final UserLoginApiForm form){
        DefaultApi api = new DefaultApi();
        try {

            if(!api.login(form).getToken().isEmpty()){
                showToast(R.string.loginSuccess);
            }else {
                showToast(R.string.loginFailed);
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ApiException e) {
            e.printStackTrace();
        }

    }


    private class DownloadFilesTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... urls) {
           //TODO: implement Api call
            return null;
        }



        protected void onPostExecute(Void result) {
            //TODO update GUI result with the result of do in background
        }

    }

}
