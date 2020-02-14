package com.samir.googlemaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.samir.googlemaps.model.LatitudeLongitude;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private AutoCompleteTextView etCity;
    private Button btnSearch;
    private List<LatitudeLongitude> latlonglist;
    Marker markerName;
    CameraUpdate center, zoom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        etCity = findViewById(R.id.etCity);
        btnSearch = findViewById(R.id.btnSearch);

        fillArrayListAndSetAdapter();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etCity.getText().toString())) {
                    etCity.setError("please enter a place's name");
                    return;

                }
                int position = SearchArrayList(etCity.getText().toString());
                if (position > -1)
                    loadMap(position);
                else
                    Toast.makeText(SearchActivity.this, "location not found: "
                            + etCity.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        center = CameraUpdateFactory.newLatLng(new LatLng(27.7172453, 85.3239605 ));
        zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    public void loadMap(int position) {

        if(markerName!= null){
            markerName.remove();
        }
        double latitude = latlonglist.get(position).getLat();
        double longitute = latlonglist.get(position).getLon();
        String marker = latlonglist.get(position).getMarker();
        center= CameraUpdateFactory.newLatLng(new LatLng(latitude,longitute));
        zoom = CameraUpdateFactory.zoomTo(17);
        markerName = mMap.addMarker(new MarkerOptions().position( new LatLng(latitude,longitute) )
                .title(marker));
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.getUiSettings().setZoomControlsEnabled(true);

    }

    private  void fillArrayListAndSetAdapter(){

        latlonglist = new ArrayList<>();
        latlonglist.add(new LatitudeLongitude(27.7134481, 85.3241922, "NagPokhari"));
        latlonglist.add(new LatitudeLongitude(27.7181749, 85.3173212, "Narayanhiti Palace"));
        latlonglist.add(new LatitudeLongitude(27.7127827, 85.3265391, "Hotel Brihaspati"));

        String [] data = new String[latlonglist.size()];

        for( int i = 0; i<data.length; i++ ){
            data[i] = latlonglist.get(i).getMarker();

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                SearchActivity.this,
                android.R.Layout.simple_list_item_1,
                data
        );
        etCity.setAdapter(adapter);
        etCity.setThreshold(1);



    }
    public int SearchArrayList(String name){
        for(int i = 0; i<latlonglist.size(); i++) {
           if (latlonglist.get(i).getMarker().contains(name)){
               return i;

           }
        }return -1;

    }

}
