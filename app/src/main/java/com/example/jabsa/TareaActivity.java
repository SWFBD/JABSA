package com.example.jabsa;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.UUID;

public class TareaActivity extends SingleFragmentActivity{

    private static final String EXTRA_TAREA_ID = "tarea_id";
    @Override
    protected Fragment createFragment() {
        UUID tareaId = (UUID) getIntent().getSerializableExtra(EXTRA_TAREA_ID);
        Log.i("Mensaje", "TareaActivity");
        return TareaFragment.newInstance(tareaId);
    }

/*    public static Intent newIntent(Context packageContext, UUID tareaId){
        Intent intent = new Intent(packageContext, TareaActivity.class);
        intent.putExtra(EXTRA_TAREA_ID, tareaId);
        return intent;
    }*/
}
