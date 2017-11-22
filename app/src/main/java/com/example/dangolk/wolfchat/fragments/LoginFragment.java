package com.example.dangolk.wolfchat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dangolk.wolfchat.R;
import com.example.dangolk.wolfchat.R2;
import com.example.dangolk.wolfchat.activities.RegisterActivity;
import com.example.dangolk.wolfchat.utils.Constants;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by dangolk on 11/01/17.
 */

public class LoginFragment extends BaseFragment {

    @BindView(R2.id.fragment_login_userEmail)
    EditText mUserEmailEt;

    @BindView(R2.id.fragment_login_userPassword)
    EditText mUserPasswordEt;

    @BindView(R2.id.fragment_login_login_button)
    Button mLoginButton;

    @BindView(R2.id.fragment_login_register_button)
    Button mRegisterButton;

    private Unbinder mUnbinder;

    private Socket mSocket;

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            mSocket = IO.socket(Constants.IP_LOCAL_HOST_DORM);
        } catch (URISyntaxException e) {
            Toast.makeText(getActivity(), "Connection to server failed", Toast.LENGTH_LONG).show();
            Log.i(LoginFragment.class.getSimpleName(), e.getMessage());
            e.printStackTrace();
        }

        mSocket.connect();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R2.id.fragment_login_register_button)
    public void setmRegisterButton(){
        startActivity(new Intent(getActivity(), RegisterActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
