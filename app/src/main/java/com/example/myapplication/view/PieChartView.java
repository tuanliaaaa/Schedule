package com.example.myapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class PieChartView extends View {

    private Paint paint;
    private RectF rectF;
    private List<Float> values;
    private List<Integer> colors;
    private List<String> labels;

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        rectF = new RectF();
        values = new ArrayList<>();
        colors = new ArrayList<>();
        labels = new ArrayList<>();
    }

    public void setValues(List<Float> values) {
        this.values = values;
        invalidate(); // Yêu cầu vẽ lại biểu đồ khi dữ liệu thay đổi
    }

    public void setColors(List<Integer> colors) {
        this.colors = colors;
        invalidate(); // Yêu cầu vẽ lại biểu đồ khi dữ liệu thay đổi
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
        invalidate(); // Yêu cầu vẽ lại biểu đồ khi dữ liệu thay đổi
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (values == null || colors == null || labels == null || values.size() != colors.size() || values.size() != labels.size()) {
            return; // Nếu dữ liệu chưa được cung cấp hoặc số lượng phần tử không khớp, không vẽ gì cả
        }

        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;
        float radius = Math.min(centerX, centerY) * 0.9f;
        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        float total = 0;
        for (float value : values) {
            total += value;
        }

        float startAngle = 0;
        for (int i = 0; i < values.size(); i++) {
            paint.setColor(colors.get(i));
            float percentage = (values.get(i) / total) * 100;
            String formattedPercentage = String.format("%.2f", percentage);
            canvas.drawArc(rectF, startAngle, (values.get(i) / total) * 360, true, paint);
            drawLabel(canvas, formattedPercentage + "%", startAngle + ((values.get(i) / total) * 360) / 2, centerX, centerY, radius);
            startAngle += (values.get(i) / total) * 360;
        }
    }

    private void drawLabel(Canvas canvas, String label, float angle, float centerX, float centerY, float radius) {
        float labelRadius = radius * 0.7f;
        float labelX = centerX + labelRadius * (float) Math.cos(Math.toRadians(angle));
        float labelY = centerY + labelRadius * (float) Math.sin(Math.toRadians(angle));
        paint.setColor(Color.BLACK);
        paint.setTextSize(30);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(label, labelX, labelY, paint);
    }
}
