package edu.nyu.scps.jaxon.arduinoyunandandroid;

import android.os.Bundle;
import android.widget.SeekBar;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by jaxonian on 8/29/15.
 */
public class Thread {

    private ArrayBlockingQueue<Integer> mQueue = new ArrayBlockingQueue<Integer>(100);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mQueue.offer(progress);
            }
        });
    }





}
