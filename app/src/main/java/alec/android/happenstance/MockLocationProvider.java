package alec.android.happenstance;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.util.Log;


public class MockLocationProvider {
    private String providerName;
    private Context ctx;


    public MockLocationProvider(String name, Context ctx) {
        this.providerName = name;
        this.ctx = ctx;

        LocationManager lm = (LocationManager) ctx.getSystemService(
                Context.LOCATION_SERVICE);
        for (String provider : lm.getAllProviders()) {
            Log.d(this.getClass().getName(), provider);
        }

    }

    public void pushLocation(double lat, double lon) {

        LocationManager lm = (LocationManager) ctx.getSystemService(
                Context.LOCATION_SERVICE);
        lm.addTestProvider(providerName, false, false, false, false, false,
                false, false, Criteria.POWER_LOW, Criteria.ACCURACY_FINE);

        Location mockLocation = new Location(providerName);
        mockLocation.setLatitude(lat);
        mockLocation.setLongitude(lon);
        mockLocation.setAltitude(0);
        mockLocation.setTime(System.currentTimeMillis());
        mockLocation.setElapsedRealtimeNanos(System.nanoTime());
        mockLocation.setAccuracy(Criteria.ACCURACY_FINE);
        lm.setTestProviderStatus(providerName,
                LocationProvider.AVAILABLE,
                null, System.currentTimeMillis());
        lm.setTestProviderEnabled(providerName, true);
        lm.setTestProviderLocation(providerName, mockLocation);
    }

    public void shutdown() {
        LocationManager lm = (LocationManager) ctx.getSystemService(
                Context.LOCATION_SERVICE);
        lm.removeTestProvider(providerName);
    }
}