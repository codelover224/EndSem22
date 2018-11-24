package com.example.krushna.endsem;

import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class BottomSheet extends BottomSheetDialogFragment implements MainActivity.VehicleLoader{

    TextView name1,mobileNo,veh_no;
    Button btn;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){

        View v = inflater.inflate(R.layout.bottom_sheet,container,false);

        name1=v.findViewById(R.id.bs_tv_name);
        mobileNo=v.findViewById(R.id.bs_tv_mobnpo);
        veh_no=v.findViewById(R.id.bs_tv_vehno);
        btn = v.findViewById(R.id.bs_btn);

        //String type = getIntent().getExtras().getString("type");

        SmartLocation.with(getContext()).location().oneFix().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                LocationCoordinates lc1 = new LocationCoordinates();
                lc1.setLat(location.getLatitude());
                lc1.setLon(location.getLongitude());
                findNearestVehicle("Ambulance", lc1, new MainActivity.VehicleLoader() {
                    @Override
                    public void nearestVehicleFound(final MainLogin vehicleInfo) {

                        Log.d("Helpp", vehicleInfo.getName()+"\nmobile no-->" + vehicleInfo.getMob_no()+"\nveh no-->" + vehicleInfo.getVeh_no());
                        name1.setText(vehicleInfo.getName());
                        mobileNo.setText(vehicleInfo.getMob_no());
                        veh_no.setText(vehicleInfo.getVeh_no());
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                                } else {
                                    builder = new AlertDialog.Builder(getContext());
                                }
                                builder.setTitle("Confirm Booking?")
                                        .setMessage("Are you sure you want to book this ambulance??")
                                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                DatabaseReference book = database.getReference("/vehicles/bookedVehicles/" + vehicleInfo.getMob_no());
                                                DatabaseReference vehicles = database.getReference(String.format("/vehicles/%s/%s", "Ambulance",vehicleInfo.getMob_no()));
                                                vehicles.child("isBooked").setValue(true);
                                                book.setValue(vehicleInfo);
                                                database.getReference("/vehicles/Ambulance/").child("" + vehicleInfo.getMob_no()).removeValue();
                                                dismiss();
                                            }
                                        })
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // do nothing
                                                dialog.cancel();
                                                dismiss();
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                                /*DatabaseReference vehicles = database.getReference(String.format("/vehicles/%s/%s", "Ambulance",vehicleInfo.getMob_no()));
                                vehicles.child("isBooked").setValue(true);
                                dismiss();*/
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

        return v;
    }

    @Override
    public void nearestVehicleFound(MainLogin vehicleInfo) {

    }

    @Override
    public void noVehicleFound() {

    }

    @Override
    public void errorOccurred(Throwable throwable) {

    }

    public void findNearestVehicle(String type, final LocationCoordinates userLocation, final MainActivity.VehicleLoader vehicleLoader) {
        DatabaseReference vehicles = database.getReference(String.format("/vehicles/%s/", type));

        vehicles.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                float shortestDistanceFound = 2000;
                MainLogin vehicleFiltered = null;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

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
}
