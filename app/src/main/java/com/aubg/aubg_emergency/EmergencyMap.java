package com.aubg.aubg_emergency;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import com.customlbs.library.IndoorsException;
import com.customlbs.library.IndoorsFactory;
import com.customlbs.library.IndoorsLocationListener;
import com.customlbs.library.callbacks.LoadingBuildingStatus;
import com.customlbs.library.model.Building;
import com.customlbs.library.model.Zone;
import com.customlbs.shared.Coordinate;
import com.customlbs.surface.library.DefaultSurfacePainterConfiguration;
import com.customlbs.surface.library.IndoorsSurface;
import com.customlbs.surface.library.IndoorsSurfaceFactory;
import com.customlbs.surface.library.IndoorsSurfaceFragment;
import com.customlbs.surface.library.IndoorsSurfaceInteractionCallback;
import com.customlbs.surface.library.SurfacePainterConfiguration;
import com.customlbs.surface.library.SurfaceState;

public class EmergencyMap extends FragmentActivity implements IndoorsLocationListener {
    Activity myActivity = this;
    private IndoorsSurfaceFragment indoorsFragment;
    private IndoorsSurface surf;
    IndoorsSurfaceFactory.Builder surfaceBuilder;
    boolean zoomedIn = false;
    boolean snapMap = true;
    SharedPreferences myPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPref = getSharedPreferences("emergency_app", MODE_PRIVATE);
        snapMap=myPref.getBoolean("MAP", false);
        IndoorsSurfaceInteractionCallback mycallback = new IndoorsSurfaceInteractionCallback() {
            @Override
            public void onUserInteraction(SurfaceState surfaceState) {
            }
        };

        IndoorsFactory.Builder indoorsBuilder = new IndoorsFactory.Builder();
        surfaceBuilder = new IndoorsSurfaceFactory.Builder();
        try {
            if (indoorsBuilder != null) {
                indoorsBuilder.setContext(this);
                //the following line of code was used for debugging
                indoorsBuilder.setEvaluationMode(true);

                // TODO: replace this with your API-key
                indoorsBuilder.setApiKey("3ebe9204-311f-48c9-af86-0c5753a51b7e");
                // TODO: replace 12345 with the id of the building you uploaded to
                // our cloud using the MMT
                indoorsBuilder.setBuildingId((long) 674291838);
                // callback for indoo.rs-events
                indoorsBuilder.setUserInteractionListener(this);
                if (surfaceBuilder != null) {
                    surfaceBuilder.setIndoorsBuilder(indoorsBuilder);


                    SurfacePainterConfiguration config = DefaultSurfacePainterConfiguration.getConfiguration();
                    config.getZonePaintInnerPaintConfiguration().setColor(Color.rgb(0, 77, 0));
                    config.getZonePaintFramePaintConfiguration().setColor(Color.BLACK);
                    SurfacePainterConfiguration.PaintConfiguration backgroundPaint = new SurfacePainterConfiguration.PaintConfiguration();
                    backgroundPaint.setColor(Color.BLACK);
                    backgroundPaint.setStyle(Paint.Style.valueOf("FILL"));
                    config.setNoTilePaintConfiguration(backgroundPaint);
                    surfaceBuilder.setSurfacePainterConfiguration(config);

                    indoorsFragment = surfaceBuilder.build();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(android.R.id.content, indoorsFragment, "indoors");
                    transaction.commit();

                    surf = new IndoorsSurface(getApplicationContext(), surfaceBuilder.getSurfaceState(), config, mycallback);
                }
            } else {
                Log.d("ERROR: ", "somethign is wrong");
            }
        }
        catch(Exception e){
            Log.d("ERROR: ", e.getLocalizedMessage());

        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        indoorsFragment=null;
        myPref = null;
        //surf = null;
        surfaceBuilder = null;
    }
    @Override
    protected void onStop(){
        super.onStop();
        finish();

    }

    public void positionUpdated(Coordinate userPosition, int accuracy) {

        Coordinate start = new Coordinate(userPosition.x, userPosition.y, 1);
        Coordinate end1 = new Coordinate(17448, 24803, 1);
        Coordinate end2 = new Coordinate(29649, 31153, 1);
        Coordinate end3 = new Coordinate(27333, 5428, 1);
        Coordinate end;
        float factor;
        if (!zoomedIn) {
            factor = 2.3F;
            zoomedIn = true;
        } else factor = 1F;
        surf.zoomToPosition(userPosition.x / 50, userPosition.y / 50, factor);
        if (snapMap&&surf!=null) surf.scrollToPositionAnimated(-userPosition.x / 40, -userPosition.y / 35);
        if ((Math.abs(start.x - end1.x) + Math.abs(start.y - end1.y) < (Math.abs(start.x - end2.x) + Math.abs(start.y - end2.y)) && (Math.abs(start.x - end1.x) + Math.abs(start.y - end1.y) < Math.abs(start.x - end3.x) + Math.abs(start.y - end3.y))))
            end = end1;
        else if ((Math.abs(start.x - end2.x) + Math.abs(start.y - end2.y) < (Math.abs(start.x - end1.x) + Math.abs(start.y - end1.y)) && (Math.abs(start.x - end2.x) + Math.abs(start.y - end2.y) < Math.abs(start.x - end3.x) + Math.abs(start.y - end3.y))))
            end = end2;
        else end = end3;
        if (indoorsFragment != null && end!=null) {
            try{
            indoorsFragment.routeTo(end, true);
            indoorsFragment.updateSurface();
        }
        catch(NullPointerException e){
            Log.d("ERROR: ", e.getLocalizedMessage());
        }
    }
    }

    @Override
    public void loadingBuilding(LoadingBuildingStatus loadingBuildingStatus) {
    }

    public void buildingLoaded(Building building) {
        // indoo.rs SDK successfully loaded the building you requested and
        // calculates a position now
        if(indoorsFragment!=null)indoorsFragment.setShowAllZones(true);
    }
    public void onError(IndoorsException indoorsException) {
        //Toast.makeText(this, indoorsException.getMessage(), Toast.LENGTH_LONG).show();
        Log.e("An error has occured: ", indoorsException.getLocalizedMessage());
    }

    public void changedFloor(int floorLevel, String name) {
        // user changed the floor
    }

    public void leftBuilding(Building building) {
        // user left the building
    }

    public void loadingBuilding(int progress) {
        // indoo.rs is still downloading or parsing the requested building
    }

    public void orientationUpdated(float orientation) {
        // user changed the direction he's heading to
    }

    public void enteredZones(List<Zone> zones) {
        // user entered one or more zones
    }

    @Override
    public void buildingLoadingCanceled() {
        // Loading of building was cancelled
    }
}
