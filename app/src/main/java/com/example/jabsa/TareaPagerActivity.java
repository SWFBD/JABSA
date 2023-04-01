package com.example.jabsa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.jabsa.model.Tarea;
import com.example.jabsa.model.TareaLab;

import java.util.List;
import java.util.UUID;

public class TareaPagerActivity extends AppCompatActivity implements TareaFragment.Callbacks{

    private static final String EXTRA_TAREA_ID = "tarea_id";

    private ViewPager mViewPager;
    private List<Tarea> mTareas;

    public static Intent newIntent(Context packageContext, UUID tareaId){
        Intent intent = new Intent(packageContext, TareaPagerActivity.class);
        intent.putExtra(EXTRA_TAREA_ID, tareaId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Mensaje", "TareaPagerActivity onCreate");
        setContentView(R.layout.activity_tarea_pager);

        UUID tareaId = (UUID)getIntent().getSerializableExtra(EXTRA_TAREA_ID);

        mViewPager = (ViewPager) findViewById(R.id.tarea_view_pager);
        mTareas = TareaLab.get(this).getTareas();

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                Tarea tarea = mTareas.get(position);
                return TareaFragment.newInstance(tarea.getmId());
            }

            @Override
            public int getCount() {
                return mTareas.size();
            }
        });
        for(int i = 0; i < mTareas.size(); i++){
            if(mTareas.get(i).getmId().equals(tareaId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }

    @Override
    public void onTareaUpdated(Tarea tarea) {

    }
}
