package com.example.jabsa;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DataPickerFragment extends DialogFragment {

    // Etiqueta para el argumento de fecha en el fragmento
    private static final String ARG_DATE = "date";

    // Instancia de DatePicker
    private DatePicker mDatePicker;
    // Etiqueta para el extra de fecha en el intent

    public static final String EXTRA_DATE = "extra_date";
    // Crea una nueva instancia del fragmento DatePickerFragment
    public static DataPickerFragment newInstance(Date date){
        // Crea un nuevo objeto Bundle para almacenar los argumentos

        Bundle args = new Bundle();
        // Agrega la fecha como argumento serializable

        args.putSerializable(ARG_DATE, date);
        // Crea una instancia del fragmento y establece los argumentos

        DataPickerFragment dataPickerFragment = new DataPickerFragment();
        dataPickerFragment.setArguments(args);
        return  dataPickerFragment;
    }
    // Crea el cuadro de diálogo de selección de fecha

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Obtiene la fecha del argumento
        // Date date = (Date)getArguments().getSerializable(ARG_DATE);
            Date dateInit = (Date)getArguments().getSerializable(ARG_DATE);
        // Crea un objeto Calendar y establece la fecha

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateInit);
        // Obtiene el año, mes y día de la fecha

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // Infla el diseño de la vista de selección de fecha

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);
        // Obtiene una referencia al DatePicker de la vista y establece la fecha

        mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(year, month, day, null);
        // Crea un cuadro de diálogo de alerta con el DatePicker como vista

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Date of task:")
                .setPositiveButton(
                        android.R.string.ok,
                        // Crea un escuchador de clics para el botón Aceptar
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Obtiene el año, mes y día seleccionados en el DatePicker
                               int year = mDatePicker.getYear();
                                int month = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();

                                //Crea una nueva fecha a partir de los valores seleccionados
                                Calendar selectedCalendar = new GregorianCalendar(year, month, day);
                                Date selectedDate = selectedCalendar.getTime();

                                //Crea una instancia de la clase calendar
                                Calendar currentCalendar = Calendar.getInstance();

                                // Obtiene el año, mes y dia actuales
                                int current_year = currentCalendar.get(Calendar.YEAR);
                                int current_month = currentCalendar.get(Calendar.MONTH);
                                int current_day = currentCalendar.get(Calendar.DAY_OF_MONTH);

                                Date currentDate = new GregorianCalendar(current_year, current_month, current_day).getTime();

                                // Si la fecha seleccionada no es posterior o igual a la fecha actual
                                if(!(selectedDate.after(currentDate) || selectedDate.equals(currentDate))){
                                    //Muestra un mensaje de error
                                   Toast.makeText(getActivity(), "Selecciona una fecha igual o posterior a la actual", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    sendResult(Activity.RESULT_OK, selectedDate);
                                }

                                // Crea una nueva fecha a partir de los valores seleccionados
                                //Date date = new GregorianCalendar(year, month, day).getTime();

                                // Envía la fecha de vuelta al fragmento de destino
                                //sendResult(Activity.RESULT_OK, date);
                            }
                        }
                )
                .create();
    }
    // Envía el resultado al fragmento de destino
    private void sendResult(int resultCode, Date date){
        // Si el fragmento de destino no está configurado, sale del método

        if(getTargetFragment() == null){
            return;
        }
        // Crea un nuevo intent y agrega la fecha extra
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
