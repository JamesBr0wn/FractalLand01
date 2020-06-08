package com.diudkr.fractalland01;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ZoomControls;

public class MainFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("diudkr", "MainFragment.onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_main_activity01, container, false);
        ZoomControls zoomControl=(ZoomControls)rootView.findViewById(R.id.zoomDetailControls);
        zoomControl.setOnZoomInClickListener(new View.OnClickListener(){
                                                 public void onClick(View arg0){
                                                     // Log.v("diudkr","Zoom in ");
                                                     ViewSettings.getViewSettings().incDetailLevel();
                                                 }
                                             }
        );

        zoomControl.setOnZoomOutClickListener(new View.OnClickListener(){
                                                  public void onClick(View arg0){
                                                      // Log.v("diudkr","Zoom out ");
                                                      ViewSettings.getViewSettings().decDetailLevel();
                                                  }
                                              }
        );
        if (savedInstanceState != null) {
            Log.i("diudkr", "MainFragment.onCreateView: got a saved state!");
        }
        return rootView;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Log.i("diudkr", "MainFragment.onItemSelected: parent="+parent + " view="+view + " pos="+position + " id="+id);
        ViewSettings.getViewSettings().setDrawmethod(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.i("diudkr", "MainFragment.onNothingSelected");
    }

    @Override
    public void onStart() {
        super.onStart();
        // Log.v("diudkr", "MainFragment.onStart");
        // tmp
        Spinner spn;
        spn = (Spinner) ViewSettings.getViewSettings().getMainActivity().findViewById(R.id.spnDrawmethod);
        if (null != spn) {
            ArrayAdapter<String> adapter;
            adapter = new ArrayAdapter<>(ViewSettings.getViewSettings().getMainActivity(),
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter.add(ViewSettings.getViewSettings().getMainActivity().getString(R.string.btnDrawPoints));
            adapter.add(ViewSettings.getViewSettings().getMainActivity().getString(R.string.btnDrawBigPoints));
            adapter.add(ViewSettings.getViewSettings().getMainActivity().getString(R.string.btnDrawLinesX));
            adapter.add(ViewSettings.getViewSettings().getMainActivity().getString(R.string.btnDrawLinesY));
            adapter.add(ViewSettings.getViewSettings().getMainActivity().getString(R.string.btnDrawSquares));
            adapter.add(ViewSettings.getViewSettings().getMainActivity().getString(R.string.btnDrawTriangles));
            spn.setAdapter(adapter);
            // spn.setPromptId(R.string.spnPrompt);
            spn.setOnItemSelectedListener(this);
        }
        LandscapeView lv2 = (LandscapeView) getActivity().findViewById(R.id.landscapeViewTarget);
        lv2.setVisibility(View.GONE); // initially invisible
        ViewSettings.getViewSettings().updateDetailtxt();
        ViewSettings.getViewSettings().updateDrawMethodCtrls();
    }

}
