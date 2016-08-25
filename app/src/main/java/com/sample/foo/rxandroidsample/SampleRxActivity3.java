package com.sample.foo.rxandroidsample;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sample.foo.rxandroidsample.databinding.ActivitySampleRx3Binding;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SampleRxActivity3 extends AppCompatActivity {
    ActivitySampleRx3Binding rx3Binding;
    Observable<String[]> myObservable;
    Observer<String> myObserver;
    int mCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Activity 3");
        createObserver();

        rx3Binding = DataBindingUtil.setContentView(this, R.layout.activity_sample_rx3);

        rx3Binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rx3Binding.button.setEnabled(false);

                Observable.from(rx3Binding.editText.getText().toString().split("\n"))
                        .subscribeOn(Schedulers.io())
                        .map(new Func1<String, String>() {
                            @Override
                            public String call(String morphedString) {
                                try {
                                    Thread.sleep(2000);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return mCounter++ + ". " + morphedString;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(myObserver);
            }
        });
    }

    private void createObserver() {
        myObserver = new Observer<String>() {

            @Override
            public void onCompleted() {
                rx3Binding.button.setEnabled(true);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String string) {
                rx3Binding.textView.setText(rx3Binding.textView.getText() + "\n" + string);
            }
        };

    }
}
