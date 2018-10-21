package com.example.krushna.endsem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, RoutingListener {

    List<LatLng> locations = new ArrayList<>();
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorPrimary, R.color.colorPrimaryDark, R.color.red, R.color.colorAccent, R.color.primary_dark_material_light};

    String type = getIntent().getExtras().getString("type");
    String name = getIntent().getExtras().getString("name");
    String mob_no=getIntent().getExtras().getString("mob");
    String veh_no=getIntent().getExtras().getString("veh_no");
    boolean show = true;

    private GoogleMap mMap;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference amb_reference = database.getReference("ambulance_live_locations");
    DatabaseReference amb_reference1 = database.getReference("ambulance_live_locations/amb_live");
    DatabaseReference pol_reference = database.getReference("police_live_locations");
    DatabaseReference pol_reference1 = database.getReference("police_live_locations/pol_live");
    DatabaseReference fire_reference = database.getReference("fire_live_locations");

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            switch (item.getItemId()) {
                case R.id.navigation_police:
                    // mTextMessage.setText(R.string.title_police);
                    return true;
                case R.id.navigation_ambulance: {

                    show = false;
                    final Bitmap police = BitmapFactory.decodeResource(getResources(), R.mipmap.police72);
                    final Bitmap fire = BitmapFactory.decodeResource(getResources(), R.mipmap.fire72);

                    mMap.addMarker(new MarkerOptions().position(new LatLng(34, 151))
                            .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(police))));
                    // mTextMessage.setText(R.string.title_ambulance);

                    amb_reference1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                           // mMap.clear();

                            LocationCoordinates lc = dataSnapshot.getValue(LocationCoordinates.class);
                            Log.d("abcdef", lc.getLat() + "" + "," + lc.getLon());

                            mMap.addMarker(new MarkerOptions().position(new LatLng(lc.getLat(), lc.getLon()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(fire))));

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    //return true;
                }
                case R.id.navigation_fire:
                    //mTextMessage.setText(R.string.title_fire);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        Bitmap police = BitmapFactory.decodeResource(getResources(), R.mipmap.police72);
        Bitmap fire = BitmapFactory.decodeResource(getResources(), R.mipmap.fire72);

        mMap.addMarker(new MarkerOptions().position(new LatLng(34, 151))
                .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(police))));

        mMap.addMarker(new MarkerOptions().position(new LatLng(-34, 151))
                .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(fire))));


        SmartLocation.with(this).location().config(LocationParams.NAVIGATION)
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(Location location) {
                       // Toast.makeText(MainActivity.this, location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();

                        LocationCoordinates lc = new LocationCoordinates(location.getLatitude(), location.getLongitude());
                        MainLogin ml = new MainLogin();
                        ml.setMob_no(mob_no);
                        ml.setLc(lc);
                        ml.setName(name);
                        ml.setVeh_no(veh_no);
                        ml.setType(type);
                        ConstraintLayout cl = findViewById(R.id.container);
                        Snackbar.make(cl,location.getLatitude() +"," + location.getLatitude(),Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                        amb_reference.child(mob_no).setValue(ml);
                        pol_reference.child("pol_live").setValue(lc);

                        locations.add(new LatLng(location.getLatitude(), location.getLongitude()));
                        if (show)
                            drawPath(lc);
                    }
                });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng))
                        .setTitle("Get Directions");
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        getDirections(marker);
                    }
                });
            }
        });

    }

    private void getDirections(Marker mark){

        LatLng end = new LatLng(mark.getPosition().latitude,mark.getPosition().longitude);
        LatLng waypoint = new LatLng(21.131180, 79.062398);
        LatLng start = new LatLng(21.177259, 79.061562);

        Routing routing = new Routing.Builder()
                .key("AIzaSyB7Hf1QFt5zQhSzAwssBSqY2Z9rbEot_7g")
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(start, end)
                .build();
        routing.execute();
    }


    private void drawPath(LocationCoordinates lc) {

        /*LatLng start = new LatLng(lc.getLat(), lc.getLon());
        LatLng waypoint = new LatLng(21.131180, 79.062398);
        LatLng end = new LatLng(27.338603, 88.614466);*/

       /* Routing routing = new Routing.Builder()
                .key("AIzaSyB7Hf1QFt5zQhSzAwssBSqY2Z9rbEot_7g")
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(this)
                .waypoints(start, end)
                .build();
        routing.execute();*/

        if (locations != null && locations.size() > 0) {
           // mMap.clear();

            Bitmap ambulance = BitmapFactory.decodeResource(getResources(), R.mipmap.amb72);

            Location p = new Location("prev");
            Location c = new Location("curr");

            try {

                p.setLatitude(locations.get(locations.size() - 2).latitude);
                p.setLongitude(locations.get(locations.size() - 2).longitude);


                c.setLatitude(locations.get(locations.size() - 1).latitude);
                c.setLongitude(locations.get(locations.size() - 1).longitude);
            } catch (Exception e) {
            }

            float bearing = p.bearingTo(c);


            mMap.addMarker(new MarkerOptions().position(locations.get(locations.size() - 1))
                    .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(ambulance)))
                    .rotation(bearing)
                    .flat(true));


            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .add(locations.toArray(new LatLng[]{}))
                    .width(15)
                    .color(Color.parseColor("#3E82F7")));


        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        System.out.println();
        e.printStackTrace();
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int i) {
        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int j = 0; j < route.size(); j++) {

            //In case of more than 5 alternative routes
            int colorIndex = j % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + j * 3);
            polyOptions.addAll(route.get(j).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);
            }
    }

    @Override
    public void onRoutingCancelled() {

    }
}
