package com.diudkr.fractalland01;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.core.view.GestureDetectorCompat;
// import android.support.v7.app.ActionBarActivity;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity01 extends AppCompatActivity {

    private static final int SWIPE_DELTA = 200;
    private static final int SWIPE_VELOCITY = 100;

    private GestureDetectorCompat mDetector;
    private PopupWindow mPopupWindow;
    private Context mContext;
    private FrameLayout mMainContainer;

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            // Log.d("diudkr","onDown returning true: " + event.toString());
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // Log.d("diudkr", "OnF XV = " + velocityX + " YV = " + velocityY);
            if ( Math.abs(e1.getY() - e2.getY()) > Math.abs(e1.getX() - e2.getX())) { // bigger swipe in Y dir
                if ((e1.getY() - e2.getY()) > SWIPE_DELTA && Math.abs(velocityY) > SWIPE_VELOCITY) {
                    ViewSettings.getViewSettings().incDetailLevel();
                    return true;
                }
                if ((e2.getY() - e1.getY()) > SWIPE_DELTA && Math.abs(velocityY) > SWIPE_VELOCITY) {
                    ViewSettings.getViewSettings().decDetailLevel();
                    return true;
                }
            }
            else {
                if (e1.getX() - e2.getX() > SWIPE_DELTA && Math.abs(velocityX) > SWIPE_VELOCITY) {
                    ViewSettings.getViewSettings().decDrawmethod();
                    return true;
                }
                if (e2.getX() - e1.getX() > SWIPE_DELTA && Math.abs(velocityX) > SWIPE_VELOCITY) {
                    ViewSettings.getViewSettings().incDrawmethod();
                    return true;
                }
            }
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // return super.onTouchEvent(event);
        boolean b = this.mDetector.onTouchEvent(event);
        return b || super.onTouchEvent(event);
    }

    private void doDataInit() {
        LandscapeData.getLandscape().calculateLandscape();
    }

    public void addRiver(View v) {
        ViewSettings.getViewSettings().add5River();
    }

    public void addOcean(View v) {
        ViewSettings.getViewSettings().addOcean();
    }

    public void startNew(View v) {
        doDataInit();
        ViewSettings.getViewSettings().updateDetailtxt();
        ViewSettings.getViewSettings().updateLandscapeView();
    }

    public void changeDrawmethod(View v) {
        ViewSettings.getViewSettings().incDrawmethod();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) { // this is the _real_ constructor...
        super.onCreate(savedInstanceState);
        ViewSettings.getViewSettings().setTheMainActivity(null); // not ready yet

        Log.i("diudkr", "MainActivity01.onCreate " + savedInstanceState);

        this.setContentView(R.layout.activity_main_activity01);
        mContext = getApplicationContext();

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        if (savedInstanceState == null) { // new start. This is not null in case of device rotation
            this.doDataInit();
            ViewSettings.getViewSettings().setDetailLevel(6);  // 6 as non-trivial start value
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_container, new MainFragment())
                    .commit();
        }
        ViewSettings.getViewSettings().setTheMainActivity(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.i("diudkr", "MainActivity01.onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main_activity01, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // Log.i("diudkr", "MainActivity01.onOptionsItemSelected mi = " + item);

        //noinspection SimplifiableIfStatement
        if (id == R.id.mni_new) {
            startNew(null);
            return true;
        }
        if (id == R.id.mni_about) {
            showPopup();
            return true;
        }
        if (id == R.id.mni_screenshot) {
            this.makeScreenshot();
            return true;
        }
        if (id == R.id.mni_close) {
            this.finishAffinity();
            return true;
        }
        /*
        //noinspection SimplifiableIfStatement
        if (id == R.id.mni_5rivers) {
            ViewSettings.getViewSettings().add5River();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.mni_toggle_animation) {
            ViewSettings.getViewSettings().toggleAnimation();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.mni_inc_detail) {
            ViewSettings.getViewSettings().incDetailLevel();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.mni_dec_detail) {
            ViewSettings.getViewSettings().decDetailLevel();
            return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    void makeScreenshot() {
        Log.i("diudkr", "MainActivity01.makeScreenshot1");
        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        Log.i("diudkr", "MainActivity01.makeScreenshot2 " + rootView);
        View screenView = rootView.getRootView();
        Log.i("diudkr", "MainActivity01.makeScreenshot3 " + screenView);
        Boolean oldcachestate = screenView.isDrawingCacheEnabled();
        Log.i("diudkr", "MainActivity01.makeScreenshot3.5 isDrawingCacheEnabled() = " + oldcachestate);
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        Log.i("diudkr", "MainActivity01.makeScreenshot4 " + bitmap);
        screenView.setDrawingCacheEnabled(oldcachestate);
        // write
        // String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Screenshots";
        File file = new File(getExternalFilesDir(null), "DemoFile.png");
        Log.i("diudkr", "MainActivity01.makeScreenshot5 " + file.getAbsolutePath());
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("diudkr", "MainActivity01.makeScreenshot6 ");
    }

    void showPopup() {
        Log.i("diudkr", "MainActivity01.showPopup");

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the popup view
        // View popupView = inflater.inflate(R.layout.aboutpopup,null);
        View popupView = inflater.inflate(R.layout.aboutpopup,null);

        // Initialize a new instance of popup window
        mPopupWindow = new PopupWindow(
                popupView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        // Get a reference for the custom view close button
        Button closeButton = (Button) popupView.findViewById(R.id.btn_close_about);

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });

        // Finally, show the popup window at the center location of root relative layout
        mMainContainer = (FrameLayout) findViewById(R.id.main_container);
        mPopupWindow.showAtLocation(mMainContainer, Gravity.CENTER,0,0);
    }
}
