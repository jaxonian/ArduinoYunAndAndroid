package edu.nyu.scps.jaxon.arduinoyunandandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

     // BEGIN THREAD
        private static Thread sNetworkThreadSend = null;
        private final Runnable mNetworkRunnableSend = new Runnable() {
            @Override
            public void run() {
                String urlBase = "http://"+ARDUINO_IP_ADDRESS+"/arduino/analog/13/";
                String url;
                try {
                    while(!mStop.get()){
                        int val = mQueue.take();
                        if(val >= 0){
                            HttpClient httpClient = new DefaultHttpClient();
                            url = urlBase.concat(String.valueOf(val));
                            HttpResponse response = httpClient.execute(new HttpGet(url));
                        }
                    }
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                sNetworkThreadSend = null;
            }
        };

        private AtomicBoolean mStop = new AtomicBoolean(false);

        @Override
        protected void onStart() {
            mStop.set(false);
            if(sNetworkThreadSend == null){
                sNetworkThreadSend = new Thread(mNetworkRunnableSend);
                sNetworkThreadSend.start();
            }
            if(sNetworkThreadReceive == null){
                sNetworkThreadReceive = new Thread(mNetworkRunnableReceive);
                sNetworkThreadReceive.start();
            }
            super.onStart();
        }

        @Override
        protected void onStop() {
            mStop.set(true);
            mQueue.clear();
            if(sNetworkThreadSend != null) sNetworkThreadSend.interrupt();
            if(sNetworkThreadReceive != null) sNetworkThreadReceive.interrupt();
            super.onStop();
        }


        //  END THREAD

    }//  END ON CREATE




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mQueue.clear();
        mQueue.offer(progress);
    }
