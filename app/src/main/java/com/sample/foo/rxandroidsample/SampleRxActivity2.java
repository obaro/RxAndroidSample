package com.sample.foo.rxandroidsample;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sample.foo.rxandroidsample.databinding.ActivitySampleRx2Binding;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SampleRxActivity2 extends AppCompatActivity {
    ActivitySampleRx2Binding rx2Binding;
    Observable<String> myObservable;
    Observer<String> myObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Activity 2");
        createObservableAndObserver();

        rx2Binding = DataBindingUtil.setContentView(this, R.layout.activity_sample_rx2);

        rx2Binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rx2Binding.button.setEnabled(false);
                myObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(myObserver);
            }
        });
    }

    private void createObservableAndObserver() {
        myObservable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> sub) {
                        try {
                            Thread.sleep(3000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String[] strings = rx2Binding.editText.getText().toString().split("\n");
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < strings.length; i++) {
                            builder.append((i+1) + ". " + strings[i] + "\n");
                        }
                        sub.onNext(builder.toString());
                        sub.onCompleted();
                    }
                }
        );

        myObserver = new Observer<String>() {

            @Override
            public void onCompleted() {
                rx2Binding.button.setEnabled(true);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String string) {
                ((TextView) findViewById(R.id.textView)).setText(string);
            }
        };

    }
}
