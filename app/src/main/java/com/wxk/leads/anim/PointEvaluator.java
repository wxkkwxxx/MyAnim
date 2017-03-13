package com.wxk.leads.anim;

import android.animation.TypeEvaluator;

/**
 * Created by Administrator on 2017/3/12 0012.
 */

public class PointEvaluator implements TypeEvaluator{
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {

        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;

        float x = startPoint.getX() + fraction * (endPoint.getX() - startPoint.getX());
        float y = startPoint.getY() + fraction * (endPoint.getY() - startPoint.getY());

        Point point = new Point(x, y);
        return point;
    }
}
