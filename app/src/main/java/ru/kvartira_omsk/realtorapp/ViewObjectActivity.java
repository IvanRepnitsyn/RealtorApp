package ru.kvartira_omsk.realtorapp;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewDebug;
import android.widget.TextView;
import android.widget.Toast;


import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;
import java.util.List;

/**
 * Created by ivanr on 03.04.2018.
 */

public class ViewObjectActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private static final int LAYOUT = R.layout.view_object_activity;
    private DBWork mDbHelper;
    private SliderLayout sliderLayout;
    private Long mRowId;
    private Toolbar toolbar;

    private MenuItem homeMenuItem;
    private MenuItem selectorMenuItem;

    private TextView textViewNameObject;
    private TextView textViewNovostroy;
    private TextView textViewPriceSale;
    private TextView textViewAddress;
    private TextView textViewNumberRooms;
    private TextView textViewFloor;
    private TextView textViewMaterial;
    private TextView textViewAllSquare;
    private TextView textViewLiveSquare;
    private TextView textViewKitchenSquare;
    private TextView textViewTypePlan;
    private TextView textViewRepair;
    private TextView textViewBathroom;
    private TextView textViewBalcony;
    private TextView textViewWindows;
    private TextView textViewViewFromWindows;
    private TextView textViewIpoteka;
    private TextView textViewClearSale;
    private TextView textViewChange;
    private TextView textViewAddInfo;
    private TextView textViewPriceClient;

    //private HashMap<

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppDefault);
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        mDbHelper = new DBWork(this);

        sliderLayout = (SliderLayout) findViewById(R.id.sliderViewObjectLayout);

        textViewNameObject = (TextView) findViewById(R.id.tvNameObject);
        textViewNovostroy = (TextView) findViewById(R.id.tvNovostroy);
        textViewPriceSale = (TextView) findViewById(R.id.tvPriceSale);
        textViewAddress = (TextView) findViewById(R.id.tvAddress);
        textViewNumberRooms = (TextView) findViewById(R.id.tvNumberRooms);
        textViewFloor = (TextView) findViewById(R.id.tvFloor);
        textViewMaterial = (TextView) findViewById(R.id.tvMaterial);
        textViewAllSquare = (TextView) findViewById(R.id.tvAllSquare);
        textViewLiveSquare = (TextView) findViewById(R.id.tvLiveSquare);
        textViewKitchenSquare = (TextView) findViewById(R.id.tvKitchenSquare);
        textViewTypePlan = (TextView) findViewById(R.id.tvTypePlan);
        textViewRepair = (TextView) findViewById(R.id.tvRepair);
        textViewBathroom = (TextView) findViewById(R.id.tvBathroom);
        textViewBalcony = (TextView) findViewById(R.id.tvBalcony);
        textViewWindows = (TextView) findViewById(R.id.tvWindows);
        textViewViewFromWindows = (TextView) findViewById(R.id.tvVievfromwindows);
        textViewIpoteka = (TextView) findViewById(R.id.tvIpoteka);
        textViewClearSale = (TextView) findViewById(R.id.tvClearSale);
        textViewChange = (TextView) findViewById(R.id.tvChange);
        textViewAddInfo = (TextView) findViewById(R.id.tvAddInfo);
        textViewPriceClient = (TextView) findViewById(R.id.tvPriceClient);


        Bundle extras = getIntent().getExtras();

        mRowId = (savedInstanceState == null) ? null
                : (Long) savedInstanceState
                .getSerializable(DBWork.COLUMN_ID);
        if (extras != null) {
            Toast.makeText(this, "Get extras object1 OK", Toast.LENGTH_LONG).show();
            mRowId = extras.getLong("idviewobject");
            //Toast.makeText(this, "ID Object "+mRowId, Toast.LENGTH_LONG).show();
            //PhoneNmb = extras.getString("callphone");
            //if (PhoneNmb != null) etPhoneClient.setText(PhoneNmb);

        }
        Toast.makeText(this, "ID Object " + mRowId, Toast.LENGTH_LONG).show();

        List<String> namePhotoObject = mDbHelper.getObjectPhotobyIdObject(Long.toString(mRowId));

        HashMap<String,String> sliderImages = new HashMap<String, String>();
        for (int i=0; i< namePhotoObject.size(); i++ ) {
            String nameImage = String.valueOf(i);
            sliderImages.put(nameImage,"file://" + Environment.getExternalStorageDirectory().getPath() + "/RealtorAppPhotos/"+namePhotoObject.get(i));
            //Toast.makeText(this, "/sdcard/RealtorAppPhotos/"+namePhotoObject.get(i), Toast.LENGTH_LONG).show();
        }

        for (String name : sliderImages.keySet()) {

            TextSliderView textSliderView = new TextSliderView(this);
            textSliderView
                    .description(name)
                    .image(sliderImages.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);
            sliderLayout.addSlider(textSliderView);
            Toast.makeText(this, sliderImages.get(name), Toast.LENGTH_LONG).show();
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(5000);
        sliderLayout.addOnPageChangeListener(this);

        initToolbar();

        Cursor object = mDbHelper.getObject(mRowId);
        startManagingCursor(object);
        /*String nameObj = object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_NAMEOBJECT));*/
        textViewNameObject.setText(object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_NAMEOBJECT)));
        int idNewbuild = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_NEWBUILD));
        String strNovostroy = null;
        if (idNewbuild == 1) {
            //textViewNovostroy.setText("новостройка");
            strNovostroy = "новостройка";
            textViewNovostroy.setTextColor(Color.GREEN);
        } else {
            //textViewNovostroy.setText("вторичное жилье");
            strNovostroy = "вторичное жилье";
            textViewNovostroy.setTextColor(Color.BLUE);
        }
        String strYearConstruction = object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_YEARCONSTRUCTION));
        String strEnd = null;
        if (strYearConstruction.isEmpty()) {
            strEnd = "";
        } else {
            strEnd = " год";
        }
        textViewNovostroy.setText(strNovostroy + " " + strYearConstruction + strEnd);

        textViewPriceSale.setText(object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_PRICESALE)));
        textViewAddress.setText(object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_OBJECTADDRESS)));
        textViewNumberRooms.setText(object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_NUMBERROOM)));
        textViewFloor.setText(object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_FLOOR)) + "/" + object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_ALLFLOOR)));
        textViewAllSquare.setText(object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_ALLSQUARE)));
        textViewLiveSquare.setText(object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_LIVESQUARE)));
        textViewKitchenSquare.setText(object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_KITCHENSQUARE)));
        Toast.makeText(this, object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_FLOOR)) + "/" + object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_ALLFLOOR)), Toast.LENGTH_LONG).show();

        int intMaterial = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_MATERIAL));
        String strMaterial = null;
        switch (intMaterial) {
            case 1:
                strMaterial = "кирпичный";
                break;
            case 2:
                strMaterial = "панельный";
                break;
            case 3:
                strMaterial = "монолитный";
                break;
            case 4:
                strMaterial = "монолитно-кирпичный";
                break;
            case 5:
                strMaterial = "рубленный";
                break;
            case 6:
                strMaterial = "брусовой";
                break;
            case 7:
                strMaterial = "каркасно-насыпной";
                break;
            case 8:
                strMaterial = "прочее";
                break;
        }
        textViewMaterial.setText(strMaterial);

        int intTypePlan = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_TYPYPLAN));
        String strTypePlan = null;
        switch (intTypePlan) {
            case 1:
                strTypePlan = "изолированные комнаты";
                break;
            case 2:
                strTypePlan = "смежные комнаты";
                break;
            case 3:
                strTypePlan = "смежно-изолированные комнаты";
                break;
            case 4:
                strTypePlan = "свободная планировка";
                break;
            case 5:
                strTypePlan = "студия";
                break;
        }
        textViewTypePlan.setText(strTypePlan);

        int intRepair = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_REPAIRS));
        String strRepair = null;
        switch (intRepair) {
            case 1:
                strRepair = "евроремонт";
                break;
            case 2:
                strRepair = "дизайнерский ремонт";
                break;
            case 3:
                strRepair = "требует ремонта";
                break;
            case 4:
                strRepair = "ремонт от застройщика";
                break;
            case 5:
                strRepair = "чистовая отделка";
                break;
            case 6:
                strRepair = "черновая отделка";
                break;
        }
        textViewRepair.setText(strRepair);

        int intBathroom = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_BATHROOM));
        String strBathroom = null;
        switch (intBathroom) {
            case 1:
                strBathroom = "раздельный";
                break;
            case 2:
                strBathroom = "совмещенный";
                break;
            case 3:
                strBathroom = "нет";
                break;
            case 4:
                strBathroom = "более одного санузла";
                break;
        }
        textViewBathroom.setText(strBathroom);

        int intBalcony = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_BALCONY));
        String strBalcony = null;
        switch (intBalcony) {
            case 1:
                strBalcony = "балкон";
                break;
            case 2:
                strBalcony = "лоджия";
                break;
            case 3:
                strBalcony = "нет";
                break;
        }
        textViewBalcony.setText(strBalcony);

        int intWindows = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_WINDOWS));
        String strWindows = null;
        switch (intWindows) {
            case 1:
                strWindows = "деревянные";
                break;
            case 2:
                strWindows = "пластиковые (ПВХ)";
                break;
        }
        textViewWindows.setText(strWindows);

        int intViewFromWindows = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_VIEWFROMWINDOWS));
        String strViewFromWindows = null;
        switch (intViewFromWindows) {
            case 1:
                strViewFromWindows = "во двор";
                break;
            case 2:
                strViewFromWindows = "на улицу";
                break;
            case 3:
                strViewFromWindows = "во двор и на улицу";
                break;
        }
        textViewViewFromWindows.setText(strViewFromWindows);

        int idConditiondeal = object.getInt(object.getColumnIndexOrThrow(DBWork.COLUMN_CONDITIONDEAL));
        switch (idConditiondeal) {
            case 0:
                textViewIpoteka.setPaintFlags(textViewIpoteka.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                textViewClearSale.setPaintFlags(textViewClearSale.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                textViewChange.setPaintFlags(textViewChange.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                break;
            case 1:
                textViewClearSale.setPaintFlags(textViewClearSale.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                textViewChange.setPaintFlags(textViewChange.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                break;
            case 2:
                textViewIpoteka.setPaintFlags(textViewIpoteka.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                textViewChange.setPaintFlags(textViewChange.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                break;
            case 3:
                textViewChange.setPaintFlags(textViewChange.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                break;
            case 4:
                textViewIpoteka.setPaintFlags(textViewIpoteka.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                textViewClearSale.setPaintFlags(textViewClearSale.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                break;
            case 5:
                textViewClearSale.setPaintFlags(textViewClearSale.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                break;
            case 6:
                textViewIpoteka.setPaintFlags(textViewIpoteka.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                break;
            case 7:
                break;
        }

        textViewAddInfo.setText(object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_ADDINFO)));
        textViewPriceClient.setText(object.getString(object
                .getColumnIndexOrThrow(DBWork.COLUMN_PRICECLIENT)));

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbarview, menu);
        homeMenuItem = menu.findItem(android.R.id.home);
        selectorMenuItem = menu.findItem(R.id.action_selector);
        //okMenuItem = menu.findItem(R.id.action_ok);
        //delMenuItem = menu.findItem(R.id.action_delete);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //setToolbarAttributes();
        selectorMenuItem.setVisible(false);
        return true;
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_viewobject);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*String nameObj = mDbHelper.getObject(mRowId).getString(mDbHelper.getObject(mRowId)
                .getColumnIndexOrThrow(DBWork.COLUMN_NAMEOBJECT));*/
        getSupportActionBar().setTitle("");


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
