package com.example.sony.client;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.sony.client.models.RequestForRegister;
import com.example.sony.client.models.ServerResponse;
import com.example.sony.client.models.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    private AppCompatButton btn_register;
    private EditText et_email,et_password,et_name,et_id;
    private TextView tv_login;
    private ProgressBar progress;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        initView(view);
        return view;

    }

    private void initView(View view) {
        btn_register = (AppCompatButton) view.findViewById(R.id.btn_register);
        tv_login = (TextView) view.findViewById(R.id.tv_login);
        et_email = (EditText) view.findViewById(R.id.et_email);
        et_name = (EditText)view.findViewById(R.id.et_name);
        et_password = (EditText) view.findViewById(R.id.et_password);
        et_id = (EditText) view.findViewById(R.id.et_id);
        progress = (ProgressBar) view.findViewById(R.id.progress);

        btn_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login:
                goTologin();
                break;
            case R.id.btn_register:
                String name = et_name.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                String worid_id = et_id.getText().toString();
                if(!name.isEmpty()&&!email.isEmpty()&&!password.isEmpty()&&!worid_id.isEmpty()){
                    progress.setVisibility(View.VISIBLE);
                    registerProcess(name,email,password,worid_id);
                }else{
                    Snackbar.make(getView(),"Fields are empty!", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void registerProcess(String name, String email, String password,String work_id) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ClientAPI api = retrofit.create(ClientAPI.class);
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(password);
        user.setWork_id(work_id);
        RequestForRegister request = new RequestForRegister();
        request.setUser(user);
        Call<ServerResponse> response = api.register(request);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                Snackbar.make(getView(),resp.getMessage(),Snackbar.LENGTH_LONG).show();
                progress.setVisibility(View.INVISIBLE);
                goTologin();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG,"failed");
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }

    private void goTologin() {
        Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }
}
