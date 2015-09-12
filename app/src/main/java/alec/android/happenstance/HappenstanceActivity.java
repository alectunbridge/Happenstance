package alec.android.happenstance;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class HappenstanceActivity extends AppCompatActivity {

    private static final String PROVIDER_NAME = LocationManager.GPS_PROVIDER;
    private static final String LOG_TAG = "mockLocationTest";
    private static final double START_LATITUDE = 53.470836;
    private static final double END_LATITUDE = 53.489276;
    private static final double POSITION_INCREMENT = 0.001;
    private static final double START_LONGITUDE = -2.261829;
    private static final double END_LONGITUDE = -2.217883;
    private MockLocationProvider mock;
    private ScheduledExecutorService scheduleTaskExecutor;
    private List<LatLong> locations;
    private int locationIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws SecurityException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happenstance);

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

        buildListOfLocations();

        scheduleTaskExecutor = Executors.newScheduledThreadPool(1);

        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                LatLong nextLocation = getNextLocation();
                Log.d(LOG_TAG,"Pushing location: " + nextLocation);
                mock.pushLocation(nextLocation);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    protected void onDestroy() {
        mock.shutdown();
        scheduleTaskExecutor.shutdown();
        super.onDestroy();
    }


    private void buildListOfLocations() {
        locations = new ArrayList<>();
        for(double latitude=START_LATITUDE; latitude<=END_LATITUDE; latitude+=POSITION_INCREMENT){
            for(double longitude=START_LONGITUDE; longitude<=END_LONGITUDE; longitude+=POSITION_INCREMENT){
                LatLong latLong = new LatLong(latitude,longitude);
                locations.add(latLong);
                Log.i(LOG_TAG, latLong.toString() );
            }
        }
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

    private LatLong getNextLocation() {
        locationIndex++;
        locationIndex %= locations.size();
        return locations.get(locationIndex);
    }
}
