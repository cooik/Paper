package com.example.sony.client;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView tv_name,tv_workid;
    private SharedPreferences pref;
    private AppCompatButton btn_logout,btn_back,btn_paper;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initViews(view);
        return view;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        pref = getActivity().getPreferences(0);
        tv_name.setText("姓名 : "+pref.getString(Constants.NAME,""));
        tv_workid.setText("工号 : "+pref.getString(Constants.WORK_ID,""));
    }

    private void initViews(View view) {
        tv_name = (TextView) view.findViewById(R.id.tv_profile_name);
        tv_workid = (TextView) view.findViewById(R.id.tv_profile_workid);
        btn_back = (AppCompatButton) view.findViewById(R.id.btn_back);
        btn_logout = (AppCompatButton) view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_paper = (AppCompatButton) view.findViewById(R.id.btn_mypaper);
        btn_paper.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_logout:
                logout();
                break;
            case R.id.btn_back:
                back();
                break;
            case R.id.btn_mypaper:
                paper();
        }
    }

    private void paper() {
        Fragment login = new UserPapaersFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }


    private void back() {
        Fragment content = new ContentFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,content);
        ft.commit();
    }

    private void logout() {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(Constants.IS_LOGGED_IN,false);
        editor.putString(Constants.EMAIL,"");
        editor.putString(Constants.WORK_ID,"");
        editor.apply();
        goToLogin();
    }

    private void goToLogin() {
        Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }
}
