package alec.android.happenstance;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class HappenstanceActivity extends AppCompatActivity {

    private static final String PROVIDER_NAME =  LocationManager.GPS_PROVIDER;
    private static final String LOG_TAG = "mockLocationTest";
    private MockLocationProvider mock;
    private ScheduledExecutorService scheduleTaskExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws SecurityException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mock = new MockLocationProvider(PROVIDER_NAME, this);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);


        lm.requestLocationUpdates(PROVIDER_NAME, 0, 0, new LocationListener() {
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }

            @Override
            public void onLocationChanged(Location location) {
            }
        });


        scheduleTaskExecutor= Executors.newScheduledThreadPool(1);

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (Math.random() > 0.5) {
                    mock.pushLocation(53.4667, -2.2333);
                } else {
                    mock.pushLocation(53.480194, -2.251025);
                }
            }
        }, 0, 5, TimeUnit.MINUTES);
    }


    protected void onDestroy() {
        mock.shutdown();
        scheduleTaskExecutor.shutdown();
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my, menu);
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
