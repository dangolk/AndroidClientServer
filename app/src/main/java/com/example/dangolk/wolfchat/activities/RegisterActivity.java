package com.example.dangolk.wolfchat.activities;

import android.support.v4.app.Fragment;

import com.example.dangolk.wolfchat.fragments.RegisterFragment;

/**
 * Created by dangolk on 11/01/17.
 */

public class RegisterActivity extends BaseFragmentActivity{

    @Override
    Fragment createFragment() {
        return RegisterFragment.newInstance();
    }
}
