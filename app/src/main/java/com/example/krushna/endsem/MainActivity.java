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
import android.support.v4.content.ContextCompat;
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

    String type ;
    String name ;
    String mob_no;
    String veh_no;



    boolean show = true;

    private GoogleMap mMap;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference amb_reference = database.getReference("ambulance_live_locations");
    DatabaseReference amb_reference1 = database.getReference("ambulance_live_locations/amb_live");
    DatabaseReference pol_reference = database.getReference("police_live_locations");
    DatabaseReference pol_reference1 = database.getReference("police_live_locations/pol_live");
    DatabaseReference fire_reference = database.getReference("fire_live_locations");
    DatabaseReference approachingLableRefrence = database.getReference(String.format("/vehicles"));

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
                    final Bitmap ambulance = BitmapFactory.decodeResource(getResources(), R.mipmap.amb72);

                    mMap.addMarker(new MarkerOptions().position(new LatLng(34, 151))
                            .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(police))));
                    // mTextMessage.setText(R.string.title_ambulance);

                    approachingLableRefrence.child("Ambulance").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                           mMap.clear();

                            /*LocationCoordinates lc = dataSnapshot.getValue(LocationCoordinates.class);
                            Log.d("abcdef", lc.getLat() + "" + "," + lc.getLon());

                            mMap.addMarker(new MarkerOptions().position(new LatLng(lc.getLat(), lc.getLon()))
                                    .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(fire))));*/
                            //Log.d("firee", dataSnapshot.get);
                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                             MainLogin login = ds.getValue(MainLogin.class);
                             LocationCoordinates lc = login.getLc();
                                mMap.addMarker(new MarkerOptions().position(new LatLng(lc.getLat(), lc.getLon()))
                                        .icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createBitmap(ambulance))));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    SmartLocation.with(MainActivity.this).location().oneFix().start(new OnLocationUpdatedListener() {
                        @Override
                        public void onLocationUpdated(Location location) {
                            LocationCoordinates lc1 = new LocationCoordinates();
                            lc1.setLat(location.getLatitude());
                            lc1.setLon(location.getLongitude());
                            findNearestVehicle(type, lc1, new VehicleLoader() {
                                @Override
                                public void nearestVehicleFound(final MainLogin vehicleInfo) {

                                    Log.d("Helpp", vehicleInfo.getName()+"\nmobile no-->" + vehicleInfo.getMob_no());
                                    /*mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                        @Override
                                        public void onMapClick(LatLng latLng) {
                                            mMap.addMarker(new MarkerOptions().position(latLng))
                                                    .setTitle("Name-->" + vehicleInfo.getName()+
                                                    "\nMobile no-->" + vehicleInfo.getMob_no()+
                                                    "\nVehicle No-->" + vehicleInfo.getVeh_no());
                                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                @Override
                                                public void onInfoWindowClick(Marker marker) {
                                                    getDirections(marker);
                                                }
                                            });
                                        }
                                    });*/

                                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker marker) {
                                            marker.setTitle(vehicleInfo.getMob_no());
                                            return false;
                                        }
                                    });
                                }

                                @Override
                                public void noVehicleFound() {

                                }

                                @Override
                                public void errorOccurred(Throwable throwable) {

                                }
                            });
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

        type = getIntent().getExtras().getString("type");
         name = getIntent().getExtras().getString("name");
         mob_no=getIntent().getExtras().getString("mob");
         veh_no=getIntent().getExtras().getString("veh_no");

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
                        ml.setLable("NA");
                        ConstraintLayout cl = findViewById(R.id.container);
                        Snackbar.make(cl,location.getLatitude() +"," + location.getLatitude(),Snackbar.LENGTH_SHORT).setAction("Action",null).show();
                       // amb_reference.child(mob_no).setValue(ml);
                        //pol_reference.child("pol_live").setValue(lc);
                        approachingLableRefrence.child(type).child(mob_no).setValue(ml);
                        updateApproachingLable(type,mob_no,lc);

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


        if (locations != null && locations.size() > 0) {
           mMap.clear();

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


    public void updateApproachingLable(String type, String phoneNumber, LocationCoordinates locationCoordinates) {
        DatabaseReference approachingLableRefrence = database.getReference(String.format("/vehicles/%s/%s/lable/",type,phoneNumber));

        LocationDataHolder[] squareLocations = {
                new LocationDataHolder(21.170640,79.069690, "Katol Sq."),
        };


        for (LocationDataHolder squareLocation : squareLocations) {

            float distanceFromSquare = distFrom(locationCoordinates.getLat(), locationCoordinates.getLon(), squareLocation.getLat(), squareLocation.getLon());//In meters
            if (distanceFromSquare <= 2000) {
                //Ambulance is Apporoaching square
                approachingLableRefrence.setValue(String.format("%s Approaching %s",type,squareLocation.getName()));
            } else {
                //Ambulance is NOT Apporoaching square
                approachingLableRefrence.setValue("NA");
            }
        }
    }


    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; // meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
                * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }


    public class LocationDataHolder{
        double lat;
        double lon;
        String name;

        public LocationDataHolder(double lat, double lon, String name) {
            this.lat = lat;
            this.lon = lon;
            this.name = name;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public interface VehicleLoader {
        void nearestVehicleFound(MainLogin vehicleInfo);

        void noVehicleFound();

        void errorOccurred(Throwable throwable);
    }


    public void findNearestVehicle(String type, final LocationCoordinates userLocation, final VehicleLoader vehicleLoader) {
        DatabaseReference vehicles = database.getReference(String.format("/vehicles/%s/", type));

        vehicles.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Log.d("inside dv", "here");

                    float shortestDistanceFound = 2000;
                    MainLogin vehicleFiltered = null;

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        Log.d("inside dv", "here");

                        MainLogin vehicle = ds.getValue(MainLogin.class);

                        if (distFrom(userLocation.getLat(), userLocation.getLon(), vehicle.getLc().getLat(), vehicle.getLc().getLon()) < shortestDistanceFound) {
                            vehicleFiltered = vehicle;
                        }
                    }

                    if (vehicleFiltered != null) {
                        vehicleLoader.nearestVehicleFound(vehicleFiltered);
                    } else {
                        vehicleLoader.noVehicleFound();
                    }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("onCancelled", databaseError.getMessage());
                vehicleLoader.errorOccurred(new Throwable(databaseError.getMessage()));
            }
        });


    }

}
