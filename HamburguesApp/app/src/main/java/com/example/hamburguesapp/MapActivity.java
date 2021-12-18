package com.example.hamburguesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    ArrayList<OverlayItem> puntos = new ArrayList<OverlayItem>();
    private MapView myOpenMapView;
    private MapController myMapController;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.logo_foreground);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f6a33e")));

        /* ---- necesitamos establecer el valor del agente de usuario en la configuración ------- */
        //pre 5.6
        //OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        //5.6 and newer
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        /* -------------------------------------------------------------------------------------- */

        /*   punto de geolocalizacion de ejemplo */
        GeoPoint Bogota = new GeoPoint(4.6240416, -74.1722346);
        GeoPoint Bogota1 = new GeoPoint(4.5995752, -74.1840371);
        GeoPoint Bogota2 = new GeoPoint(4.6951974, -74.079801);
        GeoPoint Bogota3 = new GeoPoint(4.64387, -74.1206564);
        //GeoPoint Madrid = new GeoPoint(40.416775, -3.70379);
        //GeoPoint Sydney = new GeoPoint(5.71666667, -72.92083333);
        //----------------------------------------------------------------------------------

        myOpenMapView = (MapView) findViewById(R.id.openmapview);
        myOpenMapView.setBuiltInZoomControls(true);

        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setCenter(Bogota);
        myMapController.setZoom(13);

        myOpenMapView.setMultiTouchControls(true);
        puntos.add(new OverlayItem("Bogotá", "Plaza de las Américas", Bogota));
        puntos.add(new OverlayItem("Bogotá", "Centro Mayor", Bogota1));
        puntos.add(new OverlayItem("Bogotá", "Plaza Imperial", Bogota2));
        puntos.add(new OverlayItem("Bogotá", "Multiplaza Centro Comercial", Bogota3));
        refrescaPuntos();
    }

    /**
     * Permite usar el menú
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Personalizar el funcionamiento del botón de compartir en el toolbar
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"La Mejor Comida a Tu Disposición");
        intent.putExtra(Intent.EXTRA_TEXT, "Pronto lo Podrás Descargar en Google Play ♥");
        startActivity(Intent.createChooser(intent, "Compartir"));
        return super.onOptionsItemSelected(item);
    }


    ItemizedIconOverlay.OnItemGestureListener<OverlayItem> tap = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
        @Override
        public boolean onItemLongPress(int arg0, OverlayItem arg1) {
            return false;
        }
        @Override
        public boolean onItemSingleTapUp(int index, OverlayItem item) {
            return true;
        }
    };
    private void refrescaPuntos() {
        myOpenMapView.getOverlays().clear();
        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> tap = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemLongPress(int arg0, OverlayItem arg1) {
                return false;
            }

            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }
        };

        ItemizedOverlayWithFocus<OverlayItem> capa = new ItemizedOverlayWithFocus<>(this, puntos, tap);
        capa.setFocusItemsOnTap(true);
        myOpenMapView.getOverlays().add(capa);
    }

}