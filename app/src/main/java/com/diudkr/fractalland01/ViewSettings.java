package com.diudkr.fractalland01;

import android.animation.Animator;
import androidx.appcompat.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ViewSettings implements Animator.AnimatorListener {
    private static ViewSettings theViewSettings = null;
    private int iDetailLevel;
    private MainActivity01 theMainActivity = null;
    private int drawmethod; // 0..5
    private boolean hasActiveAnimation;
    private int aniDuration;

    private ViewSettings() {
        super();
        drawmethod = 4;
        hasActiveAnimation = false;
    }

    public int getDrawmethod() {
        return drawmethod;
    }

    public MainActivity01 getMainActivity() {
        return theMainActivity;
    }

    public void addRiver() {
        if (hasActiveAnimation) {
            Log.w("diudkr", "ViewSettings.addRiver: called while animation");
            return;
        }
        LandscapeData.getLandscape().createRiver();
        if (useAnimation()) {
            startTransitionAnimation();
        }
        else {
            updateLandscapeView();
        }
    }

    public void add5River() {
        if (hasActiveAnimation) {
            Log.w("diudkr", "ViewSettings.add5River: called while animation");
            return;
        }
        for (int i=0; i<5; i++) {
            LandscapeData.getLandscape().createRiver();
        }
        if (useAnimation()) {
            startTransitionAnimation();
        }
        else {
            updateLandscapeView();
        }
    }

    public void addOcean() {
        if (hasActiveAnimation) {
            Log.w("diudkr", "ViewSettings.addOcean: called while animation");
            return;
        }
        LandscapeData.getLandscape().floodOcean();
        if (useAnimation()) {
            startTransitionAnimation();
        }
        else {
            updateLandscapeView();
        }
    }

    public void incDrawmethod() {
        if (hasActiveAnimation) {
            Log.w("diudkr", "ViewSettings.incDrawmethod: called while animation");
            return;
        }
        drawmethod++;
        if (drawmethod > 5) {
            drawmethod = 0;
        }
        if (useAnimation()) {
            startTransitionAnimation();
        }
        else {
            updateLandscapeView();
        }
        updateDrawMethodCtrls();
    }

    public void decDrawmethod() {
        if (hasActiveAnimation) {
            Log.w("diudkr", "ViewSettings.decDrawmethod: called while animation");
            return;
        }
        drawmethod--;
        if (drawmethod < 0) {
            drawmethod = 5;
        }
        if (useAnimation()) {
            startTransitionAnimation();
        }
        else {
            updateLandscapeView();
        }
        updateDrawMethodCtrls();
    }

    public void setDrawmethod(int d) {
        if ( d == drawmethod) {
            return;
        }
        if (hasActiveAnimation) {
            Log.w("diudkr", "ViewSettings.setDrawmethod: called while animation");
            return;
        }
        if ( (d<0) || (d>5) ) {
            d=1;
        }
        drawmethod = d;
        if (useAnimation()) {
            startTransitionAnimation();
        }
        else {
            updateLandscapeView();
        }
        updateDrawMethodCtrls();
    }

    public void setTheMainActivity(MainActivity01 theMainActivity) {
        this.theMainActivity = theMainActivity;
        if (theMainActivity != null) {
            int md = theMainActivity.getResources().getInteger(android.R.integer.config_longAnimTime);
            this.aniDuration = (int) (md * 1.5);
        }
        else {
            aniDuration = 5; // just a default
        }
    }

    public int getDetailLevel() {
        return iDetailLevel;
    }

    public void setDetailLevel(int iDetailLevel) {
        this.iDetailLevel = iDetailLevel;
    }

    public void onAnimationStart(Animator animation) {
    }

    public void onAnimationEnd(Animator animation) {
        if (null == theMainActivity) {
            hasActiveAnimation = false;
            return;
        }
        LandscapeView lv2 = (LandscapeView) theMainActivity.findViewById(R.id.landscapeViewTarget);
        if (lv2 == null) {
            hasActiveAnimation = false;
            return;
        }
        lv2.setVisibility(View.GONE);
        LandscapeView lv1 = (LandscapeView) theMainActivity.findViewById(R.id.landscapeViewSource);
        if (null == lv1) {
            hasActiveAnimation = false;
            return;
        }
        lv1.setAlpha(1f);
        updateLandscapeView();
        hasActiveAnimation = false;
    }

    public void onAnimationCancel(Animator animation) {
    }

    public void onAnimationRepeat(Animator animation) {
    }

    private void startTransitionAnimation() {
        // Log.i("diudkr", "startTransitionAnimation");
        LandscapeView lv2 = (LandscapeView) theMainActivity.findViewById(R.id.landscapeViewTarget);
        if (null == lv2) {
            hasActiveAnimation = false;
            return;
        }
        LandscapeView lv1 = (LandscapeView) theMainActivity.findViewById(R.id.landscapeViewSource);
        if (null == lv1) {
            hasActiveAnimation = false;
            return;
        }
        updateLandscapeViewTarget();
        lv2.setAlpha(0f);
        lv2.setVisibility(View.VISIBLE);
        hasActiveAnimation = true;
        lv2.animate().alpha(1f).setDuration(aniDuration).setListener(null);
        lv1.animate().alpha(0f).setDuration(aniDuration).setListener(this);
    }

    private boolean useAnimation() {
        if (null == theMainActivity) return false;
        CheckBox acb = (CheckBox) theMainActivity.findViewById(R.id.chkUseAnimation);
        return acb.isChecked();
    }

    public void incDetailLevel() {
        if (hasActiveAnimation) {
            Log.w("diudkr", "ViewSettings.incDetailLevel: called while animation");
            return;
        }
        if (null == theMainActivity) {
            return;
        }
        iDetailLevel++;
        if(iDetailLevel>LandscapeData.MAX_DETAIL)
        {
            iDetailLevel = LandscapeData.MAX_DETAIL;
            Toast toast = Toast.makeText(theMainActivity, theMainActivity.getString(R.string.txt_max_detail_toast, "" + iDetailLevel), Toast.LENGTH_SHORT);
            toast.show();
        }
        else { // really draw new
            updateDetailtxt();
            if (useAnimation()) {
                startTransitionAnimation();
            }
            else {
                updateLandscapeView();
            }
        }
    }

    public void decDetailLevel() {
        if (hasActiveAnimation) {
            Log.w("diudkr", "ViewSettings.decDetailLevel: called while animation");
            return;
        }
        if (null == theMainActivity) {
            return;
        }
        iDetailLevel--;
        if(iDetailLevel<LandscapeData.MIN_DETAIL)
        {
            iDetailLevel = LandscapeData.MIN_DETAIL;
            Toast toast = Toast.makeText(theMainActivity, theMainActivity.getString(R.string.txt_min_detail_toast), Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            updateDetailtxt();
            if (useAnimation()) {
                startTransitionAnimation();
            }
            else {
                updateLandscapeView();
            }
        }
    }

    public void updateDrawMethodCtrls() {
        // two controls: the Button and the Spinner
        Button bt;
        Spinner spn;
        if (null != theMainActivity) {
            bt = (Button) theMainActivity.findViewById(R.id.btnDrawmethod);
            if (null != bt) {
                String txt = "?";
                switch (drawmethod) {
                    case 0: txt = theMainActivity.getString(R.string.btnDrawPoints); break;
                    case 1: txt = theMainActivity.getString(R.string.btnDrawBigPoints); break;
                    case 2: txt = theMainActivity.getString(R.string.btnDrawLinesX); break;
                    case 3: txt = theMainActivity.getString(R.string.btnDrawLinesY); break;
                    case 4: txt = theMainActivity.getString(R.string.btnDrawSquares); break;
                    case 5: txt = theMainActivity.getString(R.string.btnDrawTriangles); break;
                }
                bt.setText(txt);
            }
            spn = (Spinner) theMainActivity.findViewById(R.id.spnDrawmethod);
            if (null!= spn) {
                spn.setSelection(drawmethod, false);
            }
        }
        else {
            Log.w("diudkr", "ViewSettings.updateDrawMethodBtn: no MainActivity!");
        }
    }

    public void updateDetailtxt() {
        TextView tv;
        if (null != theMainActivity) {
            String txt = theMainActivity.getString(R.string.lblCurrentDetailNr, "" + iDetailLevel, "" + LandscapeData.MAX_DETAIL);
            tv = (TextView) theMainActivity.findViewById(R.id.lblCurrentDetail);
            if (null != tv) {
                tv.setText(txt);
            }
            else {
                ActionBar ab = theMainActivity.getSupportActionBar();
                ab.setSubtitle(txt);
            }
        }
        else {
            Log.w("diudkr", "ViewSettings.updateDetailtxt: no MainActivity!");
        }
    }

    public void updateLandscapeView() {
        if (null != theMainActivity) {
            LandscapeView lv = (LandscapeView) theMainActivity.findViewById(R.id.landscapeView);
            LandscapeView lv2 = (LandscapeView) theMainActivity.findViewById(R.id.landscapeViewSource);
            if (lv != null) {
                lv.invalidate();
                lv2.invalidate();
            }
            else {
                Log.w("diudkr", "ViewSettings.updateLandscapeView: no LandscapeView!");
            }
        }
        else {
            Log.w("diudkr", "ViewSettings.updateLandscapeView: no MainActivity!");
        }
    }

    public void updateLandscapeViewTarget() {
        if (null != theMainActivity) {
            LandscapeView lv2 = (LandscapeView) theMainActivity.findViewById(R.id.landscapeViewTarget);
            if (lv2 != null) {
                lv2.invalidate();
            }
            else {
                Log.w("diudkr", "ViewSettings.updateLandscapeView: no LandscapeView!");
            }
        }
        else {
            Log.w("diudkr", "ViewSettings.updateLandscapeView: no MainActivity!");
        }
    }

    public void toggleAnimation() {
        if (null != theMainActivity) {
            CheckBox cb = (CheckBox) theMainActivity.findViewById(R.id.chkUseAnimation);
            if (cb == null) {
                return;
            }
            boolean b = cb.isChecked();
            if (b) {
                cb.setChecked(false);
            }
            else {
                cb.setChecked(true);
            }
        }
        else {
            Log.w("diudkr", "ViewSettings.toggleAnimation: no MainActivity!");
        }
    }

    public static ViewSettings getViewSettings() {
        if (theViewSettings == null) {
            theViewSettings = new ViewSettings();
        }
        return theViewSettings;
    }

}
