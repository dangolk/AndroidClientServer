package com.example.dangolk.wolfchat.services;

import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.Pack200;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

import io.socket.client.Socket;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.util.functions.Func1;

/**
 * Created by dangolk on 11/02/17.
 */

public class LiveAccountServices {
    public static LiveAccountServices mLiveAccountServices;

    private final int USER_ERROR_EMPTY_PASSWORD = 1;
    private final int USER_ERROR_EMPTY_EMAIL = 2;
    private final int USER_ERROR_EMPTY_USERNAME = 3;
    private final int USER_ERROR_PASSWORD_SHORT = 4;
    private final int USER_ERROR_EMAIL_BAD_FORMAT = 5;

    private final int SERVER_SUCCESS = 6;
    private final int SERVER_FAILURE = 7;




    public static LiveAccountServices getInstance(){
        if(mLiveAccountServices == null){
            mLiveAccountServices = new LiveAccountServices();
        }
        return mLiveAccountServices;
    }

    public Subscription sendRegistrationInfo(final EditText userNameEt, final EditText userEmailEt,
                                             final EditText userPasswordEt, final Socket socket){
        List<String> userDetails = new ArrayList<>();
        userDetails.add(userNameEt.getText().toString());
        userDetails.add(userEmailEt.getText().toString());
        userDetails.add(userPasswordEt.getText().toString());

        Observable<List<String>> userDetailsObservable =  Observable.just(userDetails);

        return userDetailsObservable
                .subscribeOn(Schedulers.io())
                .map(new Func1<List<String>, Integer>() {
                    @Override
                    public Integer call(List<String> strings) {

                        String userName = strings.get(0);
                        String userEmail = strings.get(1);
                        String userPassword = strings.get(2);

                        if(userName.isEmpty()){
                            return USER_ERROR_EMPTY_USERNAME;
                        }
                        else if (userEmail.isEmpty()){
                            return USER_ERROR_EMPTY_EMAIL;
                        }
                        else if (userPassword.isEmpty()){
                            return USER_ERROR_EMPTY_PASSWORD;
                        }
                        else if (userPassword.length() < 6){
                            return USER_ERROR_PASSWORD_SHORT;
                        }
                        else if (!isEmailValid(userEmail)) {
                            return USER_ERROR_EMAIL_BAD_FORMAT;
                        }
                        else{
                            JSONObject sendData = new JSONObject();
                            try{
                                sendData.put("email", userEmail);
                                sendData.put("userName", userName);
                                sendData.put("password", userPassword);

                                socket.emit("userData", sendData);

                                return SERVER_SUCCESS;

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i(LiveAccountServices.class.getSimpleName(),
                                        e.getMessage());
                                return SERVER_FAILURE;
                            }

                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        if(integer.equals(USER_ERROR_EMPTY_EMAIL)){
                            userEmailEt.setError("Email Address can't be empty.");
                        }
                        else if (integer.equals(USER_ERROR_EMAIL_BAD_FORMAT)){
                            userEmailEt.setError("Please check your email format.");
                        }
                        else if (integer.equals(USER_ERROR_EMPTY_PASSWORD)){
                            userPasswordEt.setError("Password can't be blank.");
                        }
                        else if (integer.equals(USER_ERROR_PASSWORD_SHORT)){
                            userPasswordEt.setError("Password must be at least 6 characters long");
                        }
                        else if (integer.equals(USER_ERROR_EMPTY_USERNAME)){
                            userNameEt.setError("Username can't be be empty.");
                        }
                    }
                });
    }

    private boolean isEmailValid(CharSequence email){
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
