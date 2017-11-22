package com.example.dangolk.wolfchat.activities;


import android.support.v4.app.Fragment;

import com.example.dangolk.wolfchat.fragments.LoginFragment;

public class LoginActivity extends BaseFragmentActivity {
    @Override
    Fragment createFragment() {
        return LoginFragment.newInstance();
    }
}

