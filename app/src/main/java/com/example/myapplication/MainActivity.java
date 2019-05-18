package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.service.UserClient;

import io.swagger.client.model.UserLoginApiForm;
import io.swagger.client.model.UserViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //initialize a variables for a screen form
    private EditText Username;
    private EditText Password;
    private Button Login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //declare variables and bind it with xml
        Username = findViewById(R.id.etUsername);
        Password = findViewById(R.id.etPassword);
        Login = findViewById(R.id.btnLogin);

        //initialize Onclick listener for Login button
        Login.setOnClickListener(this);

    }


    //a method to test inputs and validation on the view
    private void validate(String username, String password){
        if(username.equals("Admin") && password.equals("1234")){
            showToast(R.string.loginSuccess);
        }else{
            showToast(R.string.loginFailed);
        }
    }


    //Show status messages on screen (success or fail)
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


    //Specify a login logic per click
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
                //call test method
                //validate(Username.getText().toString(),Password.getText().toString());
                final UserLoginApiForm form = wrapToModel(Username.getText().toString(), Password.getText().toString());
                sendNetworkRequest(form);
        }
    }


    private void sendNetworkRequest(UserLoginApiForm form) {
        //create retrofit instance
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:9000/api/")
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        //get client and call an object for the request
        UserClient client = retrofit.create(UserClient.class);
        Call<UserViewModel> call = client.login(form);

        call.enqueue(new Callback<UserViewModel>() {
            @Override
            public void onResponse(Call<UserViewModel> call, Response<UserViewModel> response) {
                Toast.makeText(MainActivity.this,"Success, User-token:"+response.body().getToken(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UserViewModel> call, Throwable t) {
                Toast.makeText(MainActivity.this,"something went wrong :(", Toast.LENGTH_SHORT).show();
            }
        });
    }


    //Helper method, to wrap typed Username and password to the swagger Model
    private UserLoginApiForm wrapToModel(String username, String password){
        UserLoginApiForm form = new UserLoginApiForm();
        form.setName(username);
        form.setPassword(password);
        return form;
    }


    // 1st try to use Default api from swagger, added it in the Thread as recommended in the docs
    //result: failed to handle threads -> The application may be doing too much work on its main thread.

   /* private void validateForm(final UserLoginApiForm form){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                DefaultApi apiInstance = new DefaultApi();
                try {
                    UserViewModel result = apiInstance.login(form);
                    System.out.println(result);
                } catch (ApiException e) {
                    System.err.println("Exception when calling DefaultApi#login");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }

        });

    }*/

    // 2nd try is to handle the Default api in a Async task
    //result: also fail, put the timer to wait execution, but doesn't react
    /*private class checkLogin extends AsyncTask<String, Void, UserViewModel> {
        protected UserViewModel doInBackground(String... urls) {
           //TODO: implement Api call
            UserLoginApiForm user = wrapToModel(Username.getText().toString(), Password.getText().toString());
            DefaultApi apiInstance = new DefaultApi();
            UserViewModel result = new UserViewModel();
            try {
                result = apiInstance.login(user);
                Log.d("Result",result.getToken());

            } catch (TimeoutException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ApiException e) {
                e.printStackTrace();
            }
            Log.d("Result", result.getToken());
            return result;

        }

        protected void onPostExecute(UserViewModel result) {
            //TODO update GUI result with the result of do in background
            if(result.getToken().isEmpty()){
                showToast(R.string.loginFailed);
            } else{
                showToast(R.string.loginSuccess);
            }
        }

    }*/

}
