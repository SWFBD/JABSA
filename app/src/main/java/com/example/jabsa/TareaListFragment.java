package com.example.jabsa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jabsa.model.Tarea;
import com.example.jabsa.model.TareaLab;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

// Este fragment representa una lista de tareas que se mostrará en una RecyclerView
public class TareaListFragment extends Fragment {
    private RecyclerView mTareaRecyclerView;
    private TareaAdapter mAdapter;
    private boolean mSubtitleVisible;
    private Callbacks mCallbacks;

    // Clave utilizada para guardar el estado de la visibilidad del subtítulo al guardar el estado del fragment
    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    // Esta interfaz se utilizará para comunicar los eventos de selección de tarea a la actividad contenedora
    public interface Callbacks{
        void onTareaSelected(Tarea tarea);
    }
    // Este método se llama al crear el fragment
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Mensaje", "TareaListFragment onCreate");
        setHasOptionsMenu(true);
    }
    // Este método se llama cuando se selecciona una opción del menú en este fragment
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            // Si se selecciona "Nueva tarea", se crea una nueva tarea y se inicia una actividad de detalle para ella
            case R.id.new_task:{
                Tarea tarea = new Tarea();
                TareaLab.get(getActivity()).addTarea(tarea);
                Intent intent = TareaPagerActivity.newIntent(getActivity(), tarea.getmId());
                startActivity(intent);
/*                updateUI();ss
                mCallbacks.onTareaSelected(tarea);*/
                return true;
            }
            // Si se selecciona "Mostrar subtítulo", se cambia el estado de visibilidad del subtítulo y se actualiza el menú
            case R.id.show_subtitle:
            {
                mSubtitleVisible = !mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // Este método se llama al crear la vista para el fragment
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("Mensaje", "TareaListFragment onCreateView");
        View view = inflater.inflate(R.layout.tarea_crime_list, container, false);
        mTareaRecyclerView = (RecyclerView) view.findViewById(R.id.tarea_recycler_view);
        mTareaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState != null){
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        // Actualiza la interfaz de usuario con los datos más recientes
        updateUI();
        return view;
    }

    public void updateUI() {
        TareaLab tareaLab = TareaLab.get(getActivity());
        List<Tarea> tareas = tareaLab.getTareas();

        if(mAdapter == null){
            mAdapter = new TareaAdapter(tareas);
            mTareaRecyclerView.setAdapter(mAdapter);
        }
        else{
            mAdapter.setmTareas(tareas);
            mAdapter.notifyDataSetChanged();
        }

        updateSubtitle();
    }
    private class TareaHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mTaskStateIconView;

        private Tarea mTarea;

        public TareaHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_tarea, parent, false));
            itemView.setOnClickListener(this);
            mTitleTextView = (TextView) itemView.findViewById(R.id.task_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.date_task);
            mTaskStateIconView = (ImageView) itemView.findViewById(R.id.task_solved);
        }

        public void bind(Tarea tarea){
            // Asignar la tarea a la variable mTarea
            mTarea = tarea;
            // Establecer el título de la tarea en el TextView correspondiente
            mTitleTextView.setText(mTarea.getmTitulo());
            // Crear un objeto SimpleDateFormat con el formato de fecha deseado
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            // Establecer la fecha de la tarea en el TextView correspondiente
            mDateTextView.setText(dateFormat.format(mTarea.getmFecha()));

            // Obtener la fecha de la tarea
            Date mFecha = mTarea.getmFecha();
            // Obtener la hora de la tarea
            String mHora = mTarea.getmHora();

            // Crear un objeto Date con la fecha y hora actual
            Date mFechaActual = new Date();

            // Crear un objeto Calendar con la fecha y hora actual
            Calendar cal = Calendar.getInstance();
            cal.setTime(mFechaActual);

            // Sumar una hora al objeto Calendar para ajustar la hora
            // del telefono a la hora actual

            cal.add(Calendar.HOUR_OF_DAY, 1);

            // Actualizar la fecha y hora actual
            mFechaActual = cal.getTime();
            Date fechaTarea;

            // Crear un objeto SimpleDateFormat con el formato de fecha y hora deseado
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");


            String fechaHoraString;

            if(mHora != null){
                // Concatenar la fecha y la hora en un String
                fechaHoraString = sdf.format(mFecha).substring(0, 10) + " " + mHora;
            }
           else{
                fechaHoraString = sdf.format(mFecha).substring(0, 10) + " 00:00";
            }

            // Crear un objeto Date a partir del String concatenado
            try {
                fechaTarea = sdf.parse(fechaHoraString);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            // Comparar la fecha actual con la fecha de la tarea
            if (mFechaActual.before(fechaTarea)) {
                if(mTarea.getmCompletado()){
                    // Si la tarea se ha completado
                    mTaskStateIconView.setVisibility(View.VISIBLE);
                    mTaskStateIconView.setImageResource(R.drawable.icono_completado);
                }
                else{
                    mTaskStateIconView.setVisibility(View.VISIBLE);
                    mTaskStateIconView.setImageResource(R.drawable.icono_pendiente);
                }
            } else if (mFechaActual.after(fechaTarea)) {
                if(mTarea.getmCompletado()){
                    // Si la tarea se ha completado
                    mTaskStateIconView.setVisibility(View.VISIBLE);
                    mTaskStateIconView.setImageResource(R.drawable.icono_completado);
                }
                else{
                    mTaskStateIconView.setVisibility(View.VISIBLE);
                    mTaskStateIconView.setImageResource(R.drawable.icono_fuera_tiempo);
                }

            } else if (mFecha.equals(mFechaActual)) {
                if(mTarea.getmCompletado()){
                    // Si la tarea se ha completado
                    mTaskStateIconView.setVisibility(View.VISIBLE);
                    mTaskStateIconView.setImageResource(R.drawable.icono_completado);
                }
                else{
                    mTaskStateIconView.setVisibility(View.VISIBLE);
                    mTaskStateIconView.setImageResource(R.drawable.icono_pendiente);
                    mDateTextView.setText("ÚLTIMO DÍA");
                }

            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = TareaPagerActivity.newIntent(getActivity(), mTarea.getmId());
            startActivity(intent);
        }
    }
    private class TareaAdapter extends RecyclerView.Adapter<TareaHolder>{

        private List<Tarea> mTareas;
        public TareaAdapter(List<Tarea> tareas){
            mTareas = tareas;
        }

        @NonNull
        @Override
        public TareaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new TareaHolder(layoutInflater, parent);
        }

        // Define el comparador personalizado
        Comparator<Tarea> comparaFechas = new Comparator<Tarea>() {
            @Override
            public int compare(Tarea o1, Tarea o2) {
                return o1.getmFecha().compareTo(o2.getmFecha());
            }
        };

        @Override
        public void onBindViewHolder(@NonNull TareaHolder holder, int position) {
            // Ordenar la lista de objetos por fecha ascendente
            Collections.sort(mTareas, comparaFechas);
            Tarea tarea = mTareas.get(position);
            holder.bind(tarea);
        }


        @Override
        public int getItemCount() {
            return mTareas.size();
        }
        public void setmTareas(List<Tarea> tareas){
            mTareas = tareas;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.tarea_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);

        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        }
        else{
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    private void updateSubtitle() {
        TareaLab tareaLab = TareaLab.get(getActivity());
        int tareaCount = tareaLab.getTareas().size();
        String subtitle = ""+tareaCount+" tasks";

        if(!mSubtitleVisible){
            subtitle = null;
        }
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
