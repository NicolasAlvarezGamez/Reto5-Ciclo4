package com.example.hamburguesapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ProductosUsuarioActvity extends AppCompatActivity {

    private DbHelper dbHelper;
    private ArrayList<Combo> listaCombos;
    private GridView gridView;
    ActionBar actionBar;

    public ArrayList<Combo> llenarLista(Cursor cursor){
        ArrayList<Combo> list = new ArrayList<>();
        if(cursor.getCount() == 0){
            return list;
        }else{
            StringBuffer buffer = new StringBuffer();
            while(cursor.moveToNext()){
                byte[] image = cursor.getBlob(2);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                Combo combo = new Combo(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getBlob(2)
                );
                list.add(combo);
            }
            return list;
        }
    }

    public Bitmap byteToBitmap(byte[] image){
        return BitmapFactory.decodeByteArray(image, 0 , image.length);
    }
    public byte[] imageViewToByte(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String imageViewToString(ImageView imageView){
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        String result = java.util.Base64.getEncoder().encodeToString(byteArray);
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public byte[] stringToByte(String string){
        byte[] result = java.util.Base64.getDecoder().decode(string);
        return result;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_usuario_actvity);

        actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.mipmap.logo_foreground);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f6a33e")));

        dbHelper = new DbHelper(getApplicationContext());
        gridView = (GridView) findViewById(R.id.grid_view);

        Cursor cursor = dbHelper.getCombo();
        listaCombos = llenarLista(cursor);
        ComboAdapter comboAdapter = new ComboAdapter(getApplicationContext(), listaCombos);
        gridView.setAdapter(comboAdapter);

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
}