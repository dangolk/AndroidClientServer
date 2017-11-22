package com.example.dangolk.wolfchat.fragments;

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
import com.example.dangolk.wolfchat.activities.BaseFragmentActivity;
import com.example.dangolk.wolfchat.services.LiveAccountServices;
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

public class RegisterFragment extends BaseFragment{

    @BindView(R2.id.fragment_register_userName)
    EditText mUsernameEt;

    @BindView(R2.id.fragment_register_userEmail)
    EditText mUserEmailEt;

    @BindView(R2.id.fragment_register_userPassword)
    EditText mUserPasswordEt;

    @BindView(R2.id.fragment_register_registerButton)
    Button mRegisterButton;

    @BindView(R2.id.fragment_register_loginButton)
    Button mLoginButton;

    private Unbinder mUnbinder;

    private Socket mSocket;

    private LiveAccountServices mLiveAccountServices;


    public static RegisterFragment newInstance(){
        return new RegisterFragment();
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            mSocket = IO.socket(Constants.IP_LOCAL_HOST_DORM);
        } catch (URISyntaxException e) {
            Toast.makeText(getActivity(), "Connection to server failed", Toast.LENGTH_LONG).show();
            Log.i(RegisterFragment.class.getSimpleName(), e.getMessage());
            e.printStackTrace();
        }

        mSocket.connect();

        mLiveAccountServices = LiveAccountServices.getInstance();

    }

    @OnClick(R2.id.fragment_register_registerButton)
    public void setmRegisterButton(){
        mCompositeSubscription.add(mLiveAccountServices.sendRegistrationInfo(
                mUsernameEt, mUserEmailEt, mUserPasswordEt, mSocket
        ));
    }

    @OnClick(R2.id.fragment_register_loginButton)
    public void setmLoginButton(){
        getActivity().finish();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_register, container, false);
        mUnbinder = ButterKnife.bind(this, rootview);
        return rootview;
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
