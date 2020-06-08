package com.diudkr.fractalland01;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class LandscapeView extends View {

    private Path rect;
    private Path linepath;
    private Path riverpath;
    private Path oceanpath;
    private Path lakepath;
    private Paint cpaintbg;
    private Paint cpaintborder;
    private Paint cpaintlines;
    // private Paint cpaintlinesW;
    private Paint cpaintriver;
    private Paint cpaintocean;
    private Paint cpaintlake;

    public LandscapeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        doInit();
    }

    public LandscapeView(Context context) {
        super(context);
        doInit();
    }

    public LandscapeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        doInit();
    }

    public float transformToOutputX(int x, int y, int screenx) {
        // the screenx-2 means, that the last point is also visible in x direction
        float minx = (float) LandscapeData.getLandscape().getProjectionMinMax().getMinX();
        float maxx = (float) LandscapeData.getLandscape().getProjectionMinMax().getMaxX();
        float modelx = (float) LandscapeData.getLandscape().getProjectionX(x,y);
        return ( modelx - minx) * (screenx-2) / (maxx - minx);
    }

    public float transformToOutputY(int x, int y, int screeny) {
        // the screeny-2 means, that the last point is also visible in y direction
        float miny = (float) LandscapeData.getLandscape().getProjectionMinMax().getMinY();
        float maxy = (float) LandscapeData.getLandscape().getProjectionMinMax().getMaxY();
        float modely = (float) LandscapeData.getLandscape().getProjectionY(x,y);
        return screeny - (( modely - miny) * (screeny-2) / (maxy - miny));
    }

    private void drawSmallPoints(int delta, int wx, int wy) {
        float sx, sy;
        int ix, iy;
        for (int x = 0; x <= LandscapeData.MAX_ARRAYDIM; x += delta) {
            for (int y = 0; y <= LandscapeData.MAX_ARRAYDIM; y += delta) {
                ix = x;
                iy = y;
                sx = transformToOutputX(ix, iy, wx);
                sy = transformToOutputY(ix, iy, wy);
                // canvas.drawPoint(sx, sy, cpaintlines);
                linepath.moveTo(sx-1,sy-1);
                linepath.lineTo(sx+1,sy+1);
            }
        }
    }

    private void drawBigPoints(int delta, int wx, int wy) {
        float sx, sy;
        int ix, iy;
        // float iz;
        for (int x = 0; x <= LandscapeData.MAX_ARRAYDIM; x += delta) {
            for (int y = 0; y <= LandscapeData.MAX_ARRAYDIM; y += delta) {
                // Inbound mapping
                ix = x;
                iy = y;
                sx = transformToOutputX(ix, iy, wx);
                sy = transformToOutputY(ix, iy, wy);
                // canvas.drawLine(sx, sy, sx + 2, sy + 2, cpaintlines); // +2 makes it appear bigger...
                linepath.moveTo(sx-1,sy-1);
                linepath.lineTo(sx+1,sy+1);
                linepath.moveTo(sx-1,sy+1);
                linepath.lineTo(sx+1,sy-1);
            }
        }
    }

    private void drawX(int delta, int wx, int wy) {
        float sx0, sy0;
        int ix0, iy0;
        // float iz0;
        float sx1, sy1;
        int ix1, iy1;
        // float iz1;
        for (int y = 0; y <= LandscapeData.MAX_ARRAYDIM; y += delta) {
            for (int x = 0; x < LandscapeData.MAX_ARRAYDIM; x += delta) {
                if (x == 0) {
                    ix0 = x;
                    iy0 = y;
                    sx0 = transformToOutputX(ix0, iy0, wx);
                    sy0 = transformToOutputY(ix0,iy0, wy);
                    linepath.moveTo(sx0, sy0);
                }
                ix1 = (x + delta);
                iy1 = y;
                sx1 = transformToOutputX(ix1, iy1, wx);
                sy1 = transformToOutputY(ix1, iy1, wy);
                linepath.lineTo(sx1, sy1);
                // canvas.drawLine(sx0,sy0,sx1,sy1,cpaintlines);
            }
        }
        // canvas.drawPath(linepath, cpaintlines);
        for (int x = 0; x <= LandscapeData.MAX_ARRAYDIM; x += LandscapeData.pow2(LandscapeData.MAX_DETAIL)) {
            for (int y = 0; y < LandscapeData.MAX_ARRAYDIM; y += delta) {
                if (y == 0) {
                    ix0 = x;
                    iy0 = y;
                    sx0 = transformToOutputX(ix0, iy0, wx);
                    sy0 = transformToOutputY(ix0, iy0, wy);
                    linepath.moveTo(sx0, sy0);
                }
                ix1 = x;
                iy1 = y + delta;
                sx1 = transformToOutputX(ix1, iy1, wx);
                sy1 = transformToOutputY(ix1, iy1, wy);
                // canvas.drawLine(sx0, sy0, sx1, sy1, cpaintlines);
                linepath.lineTo(sx1, sy1);
            }
        }
    }

    private void drawY(int delta, int wx, int wy) {
        float sx0, sy0;
        int ix0, iy0;
        // float iz0;
        float sx2, sy2;
        int ix2, iy2;
        // float iz2;
        for (int x = 0; x <= LandscapeData.MAX_ARRAYDIM; x += delta) {
            for (int y = 0; y < LandscapeData.MAX_ARRAYDIM; y += delta) {
                if (y == 0) {
                    ix0 = x;
                    iy0 = y;
                    sx0 = transformToOutputX(ix0, iy0, wx);
                    sy0 = transformToOutputY(ix0, iy0, wy);
                    linepath.moveTo(sx0, sy0);
                }
                ix2 = x;
                iy2 = y + delta;
                sx2 = transformToOutputX(ix2, iy2 , wx);
                sy2 = transformToOutputY(ix2, iy2, wy);
                // canvas.drawLine(sx0,sy0,sx2,sy2,cpaintlines);
                linepath.lineTo(sx2, sy2);
            }
        }
        for (int y = 0; y <= LandscapeData.MAX_ARRAYDIM; y += LandscapeData.pow2(LandscapeData.MAX_DETAIL)) {
            for (int x = 0; x < LandscapeData.MAX_ARRAYDIM; x += delta) {
                if (x == 0) {
                    ix0 = x;
                    iy0 = y;
                    sx0 = transformToOutputX(ix0, iy0, wx);
                    sy0 = transformToOutputY(ix0, iy0, wy);
                    linepath.moveTo(sx0, sy0);
                }
                ix2 = x + delta;
                iy2 = y;
                sx2 = transformToOutputX(ix2, iy2, wx);
                sy2 = transformToOutputY(ix2, iy2, wy);
                // canvas.drawLine(sx0, sy0, sx2, sy2, cpaintlines);
                linepath.lineTo(sx2, sy2);
            }
        }
    }

    private void drawCross(int delta, int wx, int wy) {
        float sx0,sy0;
        int ix0, iy0;
        float sx3,sy3;
        int ix3, iy3;
        for (int yl=LandscapeData.MAX_ARRAYDIM; yl>=0; yl -= delta) {
            boolean cont = true;
            int cur_x=0, cur_y=yl; // start coord of cross line
            int xc, yc; // end coord of cross line
            ix0 = cur_x;
            iy0 = cur_y;
            sx0 = transformToOutputX(ix0, iy0, wx);
            sy0 = transformToOutputY(ix0, iy0, wy);
            linepath.moveTo(sx0, sy0);
            while (cont) {
                xc=cur_x+delta;
                yc=cur_y+delta;
                if ( (xc<=LandscapeData.MAX_ARRAYDIM) && (yc<=LandscapeData.MAX_ARRAYDIM)) {
                    ix3 = xc;
                    iy3 = yc;
                    sx3 = transformToOutputX(ix3, iy3, wx);
                    sy3 = transformToOutputY(ix3, iy3, wy);
                    linepath.lineTo(sx3, sy3);
                    cur_x += delta;
                    cur_y += delta;
                }
                else {
                    cont = false;
                }
            }
        }
        for (int xl=delta; xl<LandscapeData.MAX_ARRAYDIM; xl += delta) {
            boolean cont = true;
            int cur_x=xl, cur_y=0;
            int xc, yc;
            while (cont) {
                xc=cur_x+delta;
                yc=cur_y+delta;
                ix0 = cur_x;
                iy0 = cur_y;
                sx0 = transformToOutputX(ix0, iy0, wx);
                sy0 = transformToOutputY(ix0, iy0, wy);
                linepath.moveTo(sx0, sy0);
                if ( (xc<=LandscapeData.MAX_ARRAYDIM) && (yc<=LandscapeData.MAX_ARRAYDIM)) {
                    ix3 = xc;
                    iy3 = yc;
                    sx3 = transformToOutputX(ix3, iy3, wx);
                    sy3 = transformToOutputY(ix3, iy3, wy);
                    linepath.lineTo(sx3, sy3);
                    cur_x += delta;
                    cur_y += delta;
                }
                else {
                    cont = false;
                }
            }
        }
    }

    private void drawRiver(int wx, int wy) {
        float sx0,sy0;
        int ix0, iy0;
        float sx1,sy1;
        int ix1, iy1;
        for (int x=0; x<=LandscapeData.MAX_ARRAYDIM; x++) {
            for (int y=0; y<=LandscapeData.MAX_ARRAYDIM; y++) {
                if (LandscapeData.getLandscape().getPointInfo(x, y).state == 1) { // start of River
                    LandscapeData.River theRiver = LandscapeData.getLandscape().getPointInfo(x, y).river;
                    if ((theRiver != null) && (theRiver.next != null)) {
                        ix0 = theRiver.x;
                        iy0 = theRiver.y;
                        sx0 = transformToOutputX(ix0, iy0, wx);
                        sy0 = transformToOutputY(ix0, iy0, wy);
                        riverpath.moveTo(sx0, sy0);
                    }
                    while ((theRiver != null) && (theRiver.next != null)) {
                        ix1 = theRiver.next.x;
                        iy1 = theRiver.next.y;
                        sx1 = transformToOutputX(ix1, iy1, wx);
                        sy1 = transformToOutputY(ix1, iy1, wy);
                        riverpath.lineTo(sx1, sy1);
                        theRiver = theRiver.next;
                    }
                }
            }
        }
    }

    private void drawLake(int wx, int wy) {
        float sx0,sy0;
        int ix0, iy0;
        for (int x=0; x<=LandscapeData.MAX_ARRAYDIM; x++) {
            for (int y=0; y<=LandscapeData.MAX_ARRAYDIM; y++) {
                if (LandscapeData.getLandscape().getPointInfo(x, y).state == 3) {
                    ix0 = x;
                    iy0 = y;
                    sx0 = transformToOutputX(ix0, iy0, wx);
                    sy0 = transformToOutputY(ix0, iy0, wy);
                    lakepath.moveTo(sx0-2, sy0);
                    lakepath.lineTo(sx0+2, sy0);
                }
            }
        }
    }

    private void drawOcean(int wx, int wy) {
        float sx0,sy0;
        int ix0, iy0;
        float sx1,sy1;
        int ix1, iy1;
        int tox;
        for (int y=0; y<=LandscapeData.MAX_ARRAYDIM; y++) {
            for (int x=0; x<=LandscapeData.MAX_ARRAYDIM; x++) {
                if (LandscapeData.getLandscape().getPointInfo(x, y).state == 4) {
                    tox = x;
                    while ( (tox < LandscapeData.MAX_ARRAYDIM) && (LandscapeData.getLandscape().getPointInfo(tox+1,y).state == 4)) {
                        tox++;
                    }
                    ix0 = x;
                    iy0 = y;
                    sx0 = transformToOutputX(ix0, iy0, wx);
                    sy0 = transformToOutputY(ix0, iy0, wy);
                    ix1 = tox;
                    iy1 = y;
                    sx1 = transformToOutputX(ix1, iy1, wx);
                    sy1 = transformToOutputY(ix1, iy1, wy);
                    oceanpath.moveTo(sx0-2, sy0);
                    oceanpath.lineTo(sx1+2, sy1);
                    x = tox;  // maybe only one point
                }
            }
        }
    }

    protected void onDraw(Canvas canvas) {
        //Draw rectangle;
        int wx = this.getWidth();
        int wy = this.getHeight();
        rect.reset();
        rect.addRect(0, 0, wx, wy, Path.Direction.CW);
        if (isInEditMode()) {
            cpaintbg.setColor(Color.GREEN);
            canvas.drawPath(rect, cpaintbg);
            // canvas.drawPaint(cpaintbg); // Preview Pane doesnt want this
            return;
        }

        int id = this.getId();
        if (id == R.id.landscapeView) { // just the background view
            // Points, Lines, ...
            canvas.drawPath(rect, cpaintbg);
            canvas.drawLine(0, 0, wx - 2, 0, cpaintborder);
            canvas.drawLine(0, 0, 0, wy - 2, cpaintborder);
            canvas.drawLine(0, wy - 2, wx - 2, wy - 2, cpaintborder);
            canvas.drawLine(wx - 2, 0, wx - 2, wy - 2, cpaintborder);
            return;
        }

        // draw wireframe (for source and target view)
        int drawmethod = ViewSettings.getViewSettings().getDrawmethod();
        int delta = LandscapeData.MAX_ARRAYDIM / LandscapeData.pow2(ViewSettings.getViewSettings().getDetailLevel());

        if (drawmethod == 0) { // small points
            linepath.reset();
            drawSmallPoints(delta, wx, wy);
            canvas.drawPath(linepath, cpaintlines);
        }

        if (drawmethod == 1) { // big points
            linepath.reset();
            drawBigPoints(delta, wx, wy);
            canvas.drawPath(linepath, cpaintlines);
        }

        if (drawmethod == 2) { // Lines X
            linepath.reset();
            drawX(delta, wx, wy);
            canvas.drawPath(linepath, cpaintlines);
        }

        if (drawmethod == 3) { // Lines Y
            linepath.reset();
            drawY(delta, wx, wy);
            canvas.drawPath(linepath, cpaintlines);
        }

        if (drawmethod == 4) { // 3d Squares
            linepath.reset();
            drawX(delta, wx, wy);
            drawY(delta, wx, wy);
            canvas.drawPath(linepath, cpaintlines);
        }

        if (drawmethod == 5) { // 3d Triangles
            linepath.reset();
            drawX(delta, wx, wy);
            drawY(delta, wx, wy);
            drawCross(delta, wx, wy);
            canvas.drawPath(linepath, cpaintlines);
        }

        boolean hasWater = LandscapeData.getLandscape().hasWater();
        if (hasWater) {
            riverpath.reset();
            drawRiver(wx, wy);
            canvas.drawPath(riverpath, cpaintriver);

            lakepath.reset();
            drawLake(wx, wy);
            canvas.drawPath(lakepath, cpaintlake);
        }

        boolean hasOcean = LandscapeData.getLandscape().hasOcean();
        if (hasOcean) {
            oceanpath.reset();
            drawOcean(wx, wy);
            canvas.drawPath(oceanpath, cpaintocean);
        }

    }

    public void doInit() {
        // used colors
        int lakecolor = Color.rgb(0,120,225);
        int rivercolor = Color.rgb(0,170,255);
        int oceancolor = Color.rgb(0,0,255);
        int linecolor = Color.WHITE;
        // int landcolor = Color.GREEN;

        rect = new Path();
        linepath = new Path();
        riverpath = new Path();
        lakepath = new Path();
        oceanpath = new Path();

        cpaintbg = new Paint();
        cpaintbg.setColor(Color.GRAY);

        cpaintborder = new Paint();
        cpaintborder.setColor(Color.CYAN);
        cpaintborder.setStyle(Paint.Style.STROKE);
        cpaintborder.setAntiAlias(false);

        // if no water is in landscape -> white
        cpaintlines = new Paint();
        cpaintlines.setColor(linecolor);
        cpaintlines.setStyle(Paint.Style.STROKE);
        cpaintlines.setAntiAlias(false);

        // river
        cpaintriver = new Paint();
        cpaintriver.setColor(rivercolor);
        cpaintriver.setStyle(Paint.Style.STROKE);
        cpaintriver.setStrokeWidth(4f);
        cpaintriver.setAntiAlias(false);

        // lake
        cpaintlake = new Paint();
        cpaintlake.setColor(lakecolor);
        cpaintlake.setStyle(Paint.Style.STROKE);
        cpaintlake.setStrokeWidth(4f);
        cpaintlake.setAntiAlias(false);

        // ocean
        cpaintocean = new Paint();
        cpaintocean.setColor(oceancolor);
        cpaintocean.setStyle(Paint.Style.STROKE);
        cpaintocean.setStrokeWidth(4f);
        cpaintocean.setAntiAlias(false);

    }

}