package com.example.jabsa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.jabsa.model.Tarea;

public class TareaListActivity extends SingleFragmentActivity implements TareaListFragment.Callbacks,
TareaFragment.Callbacks{

    @Override
    protected Fragment createFragment() {
        Log.i("Mensaje", "TareaListActivity");
        return new TareaListFragment();
    }

    @Override
    protected int getLayoutRedId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onTareaSelected(Tarea tarea) {
        if(findViewById(R.id.detail_fragment_container) == null){
            Intent intent = TareaPagerActivity.newIntent(this, tarea.getmId());
            startActivity(intent);
        }else{
            Fragment newDetail = TareaFragment.newInstance(tarea.getmId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }


    @Override
    public void onTareaUpdated(Tarea tarea) {
        TareaListFragment listFragment = (TareaListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }
}