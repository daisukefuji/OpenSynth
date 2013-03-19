package com.indigo_lab.android.opensynth;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.indigo_lab.android.opensynth.view.ArpeggioView;
import com.indigo_lab.android.opensynth.view.FilterEnvelopeView;
import com.indigo_lab.android.opensynth.view.FilterView;
import com.indigo_lab.android.opensynth.view.ModulationView;
import com.indigo_lab.android.opensynth.view.OscillatorDetailView;
import com.indigo_lab.android.opensynth.view.OscillatorView;
import com.indigo_lab.android.opensynth.view.VolumeEnvelopeView;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ViewPagerAdapter extends PagerAdapter {
    @SuppressWarnings("unchecked")
    private static final Class<? extends View>[] VIEW_CLASSES = new Class[] {
        OscillatorView.class,
        OscillatorDetailView.class,
        ModulationView.class,
        VolumeEnvelopeView.class,
        FilterView.class,
        FilterEnvelopeView.class,
        ArpeggioView.class,
    };

    private Context mContext;
 
    public ViewPagerAdapter(Context context) {
        super();
        mContext = context;
    }
 
    @Override
    public int getCount() {
        return VIEW_CLASSES.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View)object);
    }

    private View createViewFrom(Class<? extends View> clz) throws NoSuchMethodException,
        IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<? extends View> constructor = clz.getConstructor(Context.class);
        return constructor.newInstance(mContext);
    }

    @Override
    public Object instantiateItem(View collection, int position) {
        Class<? extends View> clz = VIEW_CLASSES[position];
        try {
            View v = createViewFrom(clz);
            ((ViewPager) collection).addView(v, 0);
            return v;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("could not create view from " + clz.getSimpleName());
        }
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View)view);
    }
}
