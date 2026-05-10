package com.example.sensors.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LineChartView extends View {

    private final List<Float> values = new ArrayList<>();
    private final int maxValues = 80;

    private final Paint axisPaint = new Paint();
    private final Paint graphPaint = new Paint();
    private final Paint textPaint = new Paint();

    public LineChartView(Context context) {
        super(context);

        axisPaint.setColor(Color.LTGRAY);
        axisPaint.setStrokeWidth(3);

        graphPaint.setColor(Color.rgb(37, 99, 235));
        graphPaint.setStrokeWidth(5);
        graphPaint.setStyle(Paint.Style.STROKE);

        textPaint.setColor(Color.DKGRAY);
        textPaint.setTextSize(30);
    }

    public void addValue(float value) {

        if (values.size() >= maxValues) {
            values.remove(0);
        }

        values.add(value);

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        canvas.drawLine(
                50,
                height - 50,
                width - 20,
                height - 50,
                axisPaint
        );

        canvas.drawLine(
                50,
                20,
                50,
                height - 50,
                axisPaint
        );

        if (values.size() < 2) {
            canvas.drawText(
                    "En attente des données...",
                    80,
                    height / 2,
                    textPaint
            );
            return;
        }

        float min = Float.MAX_VALUE;
        float max = -Float.MAX_VALUE;

        for (float value : values) {
            min = Math.min(min, value);
            max = Math.max(max, value);
        }

        if (max == min) {
            max = min + 1;
        }

        Path path = new Path();

        for (int i = 0; i < values.size(); i++) {

            float x = 50 + i * ((width - 100f) / (maxValues - 1));

            float normalized =
                    (values.get(i) - min) / (max - min);

            float y =
                    height - 50
                            - normalized * (height - 100);

            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }

        canvas.drawPath(path, graphPaint);

        canvas.drawText(
                "Min : " + min + " | Max : " + max,
                70,
                40,
                textPaint
        );
    }
}