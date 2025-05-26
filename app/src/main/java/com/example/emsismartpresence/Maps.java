package com.example.emsismartpresence;
import android.util.Log;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Maps extends Fragment implements OnMapReadyCallback {

    private FloatingActionButton btnMyLocation;

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LatLng currentLocationLatLng;

    private AutoCompleteTextView searchInput;
    private ImageButton btnSearch;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        searchInput = view.findViewById(R.id.search_location);
        btnSearch = view.findViewById(R.id.btn_search);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        // Suggestions simples
        String[] suggestions = {"Casablanca", "Rabat", "Marrakech", "Fès", "Agadir"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, suggestions);
        searchInput.setAdapter(adapter);

        // Recherche au clic sur le bouton loupe
        btnSearch.setOnClickListener(v -> {
            String locationName = searchInput.getText().toString();
            if (!locationName.isEmpty()) {
                geoLocateAndDrawRoute(locationName);
            } else {
                Toast.makeText(getContext(), "Veuillez saisir un lieu", Toast.LENGTH_SHORT).show();
            }
        });

        btnMyLocation = view.findViewById(R.id.btn_my_location);
        btnMyLocation.setOnClickListener(v -> {
            if (currentLocationLatLng != null && mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, 17));
            } else {
                Toast.makeText(getContext(), "Localisation non disponible", Toast.LENGTH_SHORT).show();
            }
        });


        // Recherche au clic touche entrée sur clavier
        searchInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH
                    || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String locationName = v.getText().toString();
                if (!locationName.isEmpty()) {
                    geoLocateAndDrawRoute(locationName);
                }
                return true;
            }
            return false;
        });

        return view;
    }

    private void geoLocateAndDrawRoute(String locationName) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            if (addresses == null || addresses.isEmpty()) {
                Toast.makeText(getContext(), "Adresse non trouvée", Toast.LENGTH_SHORT).show();
                return;
            }
            Address address = addresses.get(0);
            LatLng destination = new LatLng(address.getLatitude(), address.getLongitude());
            drawRoute(currentLocationLatLng, destination);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Erreur lors de la recherche", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        mMap.setMyLocationEnabled(true);
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;

                for (Location location : locationResult.getLocations()) {
                    currentLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(currentLocationLatLng).title("Ma position"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, 17));
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private void drawRoute(LatLng origin, LatLng destination) {
        if (origin == null) {
            Toast.makeText(getContext(), "Localisation actuelle non disponible", Toast.LENGTH_SHORT).show();
            return;
        }


        Log.d("Maps", "Origine lat/lng : " + origin.latitude + ", " + origin.longitude);
        Log.d("Maps", "Destination lat/lng : " + destination.latitude + ", " + destination.longitude);


        String apiKey =  "AIzaSyC4krHghio4uqRxvDduPSYoH57qv7zU22A"; // Remplace par ta vraie clé API
        String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                "origin=" + origin.latitude + "," + origin.longitude +
                "&destination=" + destination.latitude + "," + destination.longitude +
                "&key=" + apiKey;

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Erreur de requête", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        JSONArray routes = json.getJSONArray("routes");

                        if (routes.length() > 0) {
                            JSONObject route = routes.getJSONObject(0);
                            JSONObject leg = route.getJSONArray("legs").getJSONObject(0);

                            String duration = leg.getJSONObject("duration").getString("text");

                            JSONArray steps = leg.getJSONArray("steps");

                            List<LatLng> path = new ArrayList<>();

                            for (int i = 0; i < steps.length(); i++) {
                                String polyline = steps.getJSONObject(i)
                                        .getJSONObject("polyline")
                                        .getString("points");
                                path.addAll(PolyUtil.decode(polyline));
                            }

                            requireActivity().runOnUiThread(() -> {
                                mMap.clear();
                                mMap.addMarker(new MarkerOptions().position(origin).title("Ma position"));
                                PolylineOptions options = new PolylineOptions()
                                        .addAll(path)
                                        .width(12)
                                        .color(Color.BLUE)
                                        .geodesic(true);
                                mMap.addPolyline(options);

                                mMap.addMarker(new MarkerOptions().position(destination).title("Destination"));
                                Toast.makeText(getContext(), "Durée estimée : " + duration, Toast.LENGTH_LONG).show();
                            });
                        } else {
                            requireActivity().runOnUiThread(() ->
                                    Toast.makeText(getContext(), "Aucun itinéraire trouvé", Toast.LENGTH_SHORT).show());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Erreur de traitement des données", Toast.LENGTH_SHORT).show());
                    }
                } else {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Erreur dans la réponse", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }
}
