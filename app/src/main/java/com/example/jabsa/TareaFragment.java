package com.example.jabsa;

import static android.Manifest.permission.READ_CONTACTS;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.jabsa.alarmNotification.Utils;
import com.example.jabsa.model.Categoria;
import com.example.jabsa.model.CategoriaLab;
import com.example.jabsa.model.Tarea;
import com.example.jabsa.model.TareaLab;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

public class TareaFragment extends Fragment {
    private static final String ARG_TAREA_ID = "tarea_id";
    private static final String DIALOG_DATE = "DialogDate";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;

    private static final int REQUEST_PHOTO = 2;
    private static final String[] CONTACTS_PERMISSIONS = {
            READ_CONTACTS
    };
    private static final int REQUEST_CONTACTS_PERMISSIONS = 1;
    private Tarea mTarea;
    private EditText mTitulo;
    private EditText mDescripcion;
    private Button mFecha;
    private Button mHora;
    private CheckBox mCompletado;
    private CheckBox mLlamada_activada;

    private CheckBox mAlarma_activada;
    private Button mContacto;
    private Button mNumero;
    private File mPhotoFile;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private Callbacks mCallbacks;
    private int hour;
    private int minute;
    private String mPersonId;
    private Spinner mListaDesplegable;
    private Button mBotonAñadir;
    private Button mBotonEliminar;

    private CategoriaLab mCategoriaLab;
    private List<Categoria> mCategorias;
    private CategoryMap mCategoryMap;
    private List<String> mCategoryList;

    public static final String MY_ACTION = "com.example.myapp.MY_ACTION";

    private static final int ALARM_ID = 1;



    public interface Callbacks{
        void onTareaUpdated(Tarea tarea);
    }

    public static TareaFragment newInstance(UUID tareaId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_TAREA_ID, tareaId);
        TareaFragment fragment = new TareaFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Mensaje", "TareaFragment onCreate");
        setHasOptionsMenu(true);
        UUID tareaId = (UUID)getArguments().getSerializable(ARG_TAREA_ID);
        mTarea = TareaLab.get(getActivity()).getTarea(tareaId);
        mPhotoFile = TareaLab.get(getActivity()).getPhotoFile(mTarea);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tarea, container, false);
        Log.i("Mensaje", "TareaFragment onCreateView");
        mTitulo = (EditText) view.findViewById(R.id.tarea_title);
        mTitulo.setText(mTarea.getmTitulo());
        mTitulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTarea.setmTitulo(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateTarea();
            }
        });
        mDescripcion = (EditText) view.findViewById(R.id.tarea_description);
        mDescripcion.setText(mTarea.getmDescripcion());
        mDescripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTarea.setmDescripcion(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateTarea();
            }
        });

        mFecha = (Button) view.findViewById(R.id.tarea_date);

        if(mTarea.getmFecha() != null)
            updateDate();

        mFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DataPickerFragment dialog = DataPickerFragment.newInstance(mTarea.getmFecha());
                dialog.setTargetFragment(TareaFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
                updateDate();
                updateTarea();
            }
        });


        mHora = (Button) view.findViewById(R.id.tarea_time);
        if(mTarea.getmHora() != null)
            mHora.setText(mTarea.getmHora());
        mHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        //Hora seleccionada
                        hour = selectedHour;
                        //Minutos seleccionados
                        minute = selectedMinute;

                        //Instancia de un objeto de la clase Date
                        Date fechaActual = new Date();
                        //Instancia del objeto de la clase Calendar
                        Calendar calendarioActual = Calendar.getInstance();
                        //se establece el objeto Date al objeto Calendar
                        calendarioActual.setTime(fechaActual);
                        //se suma una hora a la hora del teléfono
                        calendarioActual.add(Calendar.HOUR_OF_DAY, 1);

                        //La fecha seleccionada esta formada por la establecida más la hora y los minutos

                        //Fecha seleccionada
                        Date fechaSeleccionada = new Date();
                        //objeto de la clase calendar
                        Calendar calendarioSeleccionado = Calendar.getInstance();

                        //establecimiento de la fecha al objeto de la clase Calendar.
                        calendarioSeleccionado.setTime(fechaSeleccionada);

                        //Se establece la hora seleccionada y los minutos a la fecha comparativa
                        calendarioSeleccionado.set(Calendar.MINUTE, minute);
                        calendarioSeleccionado.set(Calendar.HOUR_OF_DAY, hour);


                        //Si se ha establecido una fecha
                        if(mTarea.getmFecha() != null){

                            Date fechaEstablecida = mTarea.getmFecha();
                            Calendar calendarioEstablecido = Calendar.getInstance();
                            calendarioEstablecido.setTime(fechaEstablecida);

                            calendarioSeleccionado = calendarioEstablecido;
                            //Se establece la hora seleccionada y los minutos a la fecha comparativa
                            calendarioSeleccionado.set(Calendar.MINUTE, minute);
                            calendarioSeleccionado.set(Calendar.HOUR_OF_DAY, hour);


                            if(calendarioActual.get(Calendar.DAY_OF_MONTH) == calendarioEstablecido.get(Calendar.DAY_OF_MONTH)){
                                if(calendarioActual.get(Calendar.MONTH) == calendarioEstablecido.get(Calendar.MONTH)){
                                    if(calendarioActual.get(Calendar.YEAR) == calendarioEstablecido.get(Calendar.YEAR)){
                                        if(calendarioActual.get(Calendar.HOUR_OF_DAY) <= calendarioSeleccionado.get(Calendar.HOUR_OF_DAY)){

                                            if(calendarioActual.get(Calendar.HOUR_OF_DAY) == calendarioSeleccionado.get(Calendar.HOUR_OF_DAY)){
                                                if(calendarioActual.get(Calendar.MINUTE) <= calendarioSeleccionado.get(Calendar.MINUTE)){
                                                    mHora.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                                                    mTarea.setmHora(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                                                    if(!mTarea.getmAlarma_activada()){
                                                        setAlarm(calendarioSeleccionado);
                                                    }
                                                    else{
                                                        unsetAlarm();
                                                    }
                                                    updateTarea();
                                                }
                                                else{
                                                    Toast.makeText(getActivity(), "Selecciona una hora y minutos posterior al actual", Toast.LENGTH_SHORT).show();
                                                }
                                                return;
                                            }
                                            else{
                                                mHora.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                                                mTarea.setmHora(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                                                if(!mTarea.getmAlarma_activada()){
                                                    setAlarm(calendarioSeleccionado);
                                                }
                                                else{
                                                    unsetAlarm();
                                                }
                                                updateTarea();
                                                return;
                                            }


                                        }
                                        else{
                                            Toast.makeText(getActivity(), "Selecciona una hora y minutos posterior al actual", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                }
                            }
                           if(calendarioActual.after(calendarioEstablecido)){
                               Toast.makeText(getActivity(), "Selecciona una hora y minutos posterior al actual", Toast.LENGTH_SHORT).show();
                           }
                           else{
                               // El calendario seleccionado siempre es la fecha actual, cosa que no es verdad
                               if(calendarioActual.after(calendarioSeleccionado)){
                                   Toast.makeText(getActivity(), "Selecciona una hora y minutos posterior al actual", Toast.LENGTH_SHORT).show();
                               }
                               else{
                                   mHora.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                                   mTarea.setmHora(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                                   if(!mTarea.getmAlarma_activada()){
                                       setAlarm(calendarioSeleccionado);
                                   }
                                   else{
                                       unsetAlarm();
                                   }
                                   updateTarea();
                               }
                           }

                        }
                        //si no hay una fecha establecida
                        else{
                            if(calendarioSeleccionado.before(calendarioActual)){
                                Toast.makeText(getActivity(), "Selecciona una hora y minutos posterior al actual", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                mHora.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                                mTarea.setmHora(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
                                if(!mTarea.getmAlarma_activada()){
                                    setAlarm(calendarioSeleccionado);
                                }
                                else{
                                    unsetAlarm();
                                }
                                updateTarea();
                            }
                        }

                }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, true);
                timePickerDialog.setTitle("Select time");
                timePickerDialog.show();
            }
        });
        mCompletado = (CheckBox) view.findViewById(R.id.tarea_completed);
        mCompletado.setChecked(mTarea.getmCompletado());
        mCompletado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("Checkeado", new Boolean(isChecked).toString());
                mTarea.setmCompletado(isChecked);
                updateTarea();
            }
        });

        mLlamada_activada = (CheckBox) view.findViewById(R.id.tarea_activated);
        mLlamada_activada.setChecked(mTarea.getmLlamada_activada());
        mLlamada_activada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTarea.setmLlamada_activada(isChecked);
                updateTarea();
            }
        });

        mAlarma_activada = (CheckBox) view.findViewById(R.id.set_alarm);
        mAlarma_activada.setChecked(mTarea.getmAlarma_activada());
        mAlarma_activada.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mTarea.setmAlarma_activada(isChecked);
                updateTarea();
            }
        });

        mListaDesplegable = view.findViewById(R.id.category_spinner);

        mCategoryList = updateCategoryName();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mCategoryList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mListaDesplegable.setAdapter(adapter);

        String categoriaSeleccionada = mCategoryMap.getCategoryName(mTarea.getmIdCategoria());
        //Obtener el indice de la categoria dentro del spinnerAdapter
        int categoriaIndex = ((ArrayAdapter<String>) mListaDesplegable.getAdapter()).getPosition(categoriaSeleccionada);
        //Establecer la seleccion del spinner al indice de la categoria
        mListaDesplegable.setSelection(categoriaIndex);

        mListaDesplegable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    // Establecer la categoria seleccionada como la propia de la tarea
                    String categoriaSeleccionada = (String) parent.getItemAtPosition(position);
                    String codigoCategoria = mCategoryMap.getCategoryCode(categoriaSeleccionada);
                    mTarea.setmIdCategoria(codigoCategoria);
                    updateTarea();
                    Log.i("Guarda", ""+categoriaSeleccionada+" "+codigoCategoria);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //  No se ha seleccionado nada
            }
        });

        mBotonAñadir = view.findViewById(R.id.add_category_button);
        mBotonAñadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("New Category");

                final EditText input = new EditText(getActivity());
                builder.setView(input);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newCategory = input.getText().toString();
                        Categoria categoria = new Categoria();
                        categoria.setmNombre(newCategory);
                        mCategoriaLab.addCategoria(categoria);
                        mCategoryList = updateCategoryName();

                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) mListaDesplegable.getAdapter();
                        adapter.clear();
                        adapter.addAll(mCategoryList);
                        adapter.notifyDataSetChanged();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        mBotonEliminar = view.findViewById(R.id.delete_category_button);
        mBotonEliminar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete category");
                //mCategoryList = updateCategoryName();

                builder.setItems(mCategoryList.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int position = which;
                        String categoryToDelete = mCategoryList.get(position);
                        String categoryToDeleteId = mCategoryMap.getCategoryCode(categoryToDelete);
                        for(Categoria categoria : mCategorias){
                            if(categoria.getmId().toString().equals(categoryToDeleteId)){
                                mCategoriaLab.deleteCategoria(categoria);
                            }
                        }
                        mCategoryList = updateCategoryName();
                        ArrayAdapter<String> adapter = (ArrayAdapter<String>) mListaDesplegable.getAdapter();
                        adapter.clear();
                        adapter.addAll(mCategoryList);
                        adapter.notifyDataSetChanged();

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mContacto = (Button) view.findViewById(R.id.tarea_person);
        if(mTarea.getmContacto() != null)
            mContacto.setText(mTarea.getmContacto());

        mContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        mNumero = (Button) view.findViewById(R.id.tarea_call);
        if(mTarea.getmNumero() != null)
            mNumero.setText(mTarea.getmNumero());
        if(mTarea.getmLlamada_activada()){
            mNumero.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String number = mTarea.getmNumero();
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel: "+number));
                    startActivity(callIntent);
                }
            });
        }
        else{
            mContacto.setEnabled(false);
        }
        PackageManager packageManager = getActivity().getPackageManager();
        mPhotoButton = (ImageButton) view.findViewById(R.id.tarea_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.example.jabsa.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
                for(ResolveInfo activity : cameraActivities){
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) view.findViewById(R.id.tarea_photo);
        updatePhotoView();

        return view;

    }

    private List<String> updateCategoryName(){
        mCategoriaLab = CategoriaLab.get(getActivity());
        mCategorias = mCategoriaLab.getCategorias();

        List<String> categoryList = new ArrayList<>();
        List<String> categoryListId = new ArrayList<>();

        for(Categoria categoria : mCategorias){
            categoryList.add(categoria.getmNombre());
            categoryListId.add(categoria.getmId().toString());
        }

        mCategoryMap = new CategoryMap(categoryListId, categoryList);
        return categoryList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == REQUEST_DATE){
            Date date = (Date)data
                    .getSerializableExtra(DataPickerFragment.EXTRA_DATE);
            mTarea.setmFecha(date);
            updateTarea();
            updateDate();
        }
        else if(requestCode == REQUEST_CONTACT && data != null){

            if(hasContactPermission()){
                Uri contactUri = data.getData();
                String[] queryFields = new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME
                };
                Cursor cursor = getActivity().getContentResolver()
                        .query(contactUri, queryFields, null, null, null);
                try{
                    if(cursor.getCount() == 0){
                        return;
                    }
                    cursor.moveToFirst();
                    String contacto = cursor.getString(1);
                    mPersonId = cursor.getString(0);

                    mTarea.setmContacto(contacto);
                    updateTarea();
                    mContacto.setText(contacto);
                    String personNumber = getPersonPhoneNumber(mPersonId);
                    mTarea.setmNumero(personNumber);
                }finally{
                    cursor.close();
                }
            }
            else{
                requestPermissions(CONTACTS_PERMISSIONS, REQUEST_CONTACTS_PERMISSIONS);
            }
        }else if(requestCode == REQUEST_PHOTO){
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "com.example.jabsa.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            updateTarea();
            updatePhotoView();
        }

    }

    private void updatePhotoView() {
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }
        else{
            Bitmap bitmap = PictureUtils.getScaledBitMap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
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

    private String getPerson(Intent data){
        Uri contactUri = data.getData();

        String[] queryFields = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        Cursor c = getActivity().getContentResolver()
                .query(contactUri, queryFields, null, null, null);

        try{
            if(c.getCount() == 0){
                return null;
            }
            c.moveToFirst();
            mPersonId = c.getString(0);
            String personName = c.getString(1);
            return personName;
        }finally{
            c.close();
        }
    }
    @SuppressLint("Range")
    private String getPersonPhoneNumber(String personId){
        String personNumber = null;
        Uri phoneContactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] queryFields = new String[]{
                ContactsContract.Data.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE,
        };
        String mSelectionClause = ContactsContract.Data.CONTACT_ID + "= ?";

        String[] mSelectionArgs = {""};
        mSelectionArgs[0] = personId;

        Cursor c = getActivity().getContentResolver().query(phoneContactUri, queryFields, mSelectionClause, mSelectionArgs, null);

        try{
            if(c.getCount() == 0){
                return null;
            }

            while(c.moveToNext()){
                @SuppressLint("Range") int phoneType = c.getInt(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                if(phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE){
                    personNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA));
                    break;
                }
            }
        }finally {
            c.close();
        }
        return personNumber;
    }
    private void updatePersonNumber(){
        String personNumber = getPersonPhoneNumber(mPersonId);
        if(personNumber != null)
            mTarea.setmNumero(personNumber);
    }
    private boolean hasContactPermission(){
        int result = ContextCompat.checkSelfPermission(getActivity(), CONTACTS_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CONTACTS_PERMISSIONS:
                if(hasContactPermission()){
                    updatePersonNumber();
                }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.delete_task:{
                TareaLab.get(getActivity()).deleteTask(mTarea);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .remove(this).commit();
                getActivity().finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.tarea_fragment_menu, menu);
        MenuItem subtitleItem = menu.findItem(R.id.delete_task);
        subtitleItem.setTitle(R.string.delete_task);
    }

    @Override
    public void onPause() {
        super.onPause();
        TareaLab.get(getActivity()).updateTarea(mTarea);
    }

    private void updateTarea() {
        TareaLab.get(getActivity()).updateTarea(mTarea);
        mCallbacks.onTareaUpdated(mTarea);
    }

    private void setAlarm(Calendar calendar){
            Intent intent = new Intent();
            intent.setAction(MY_ACTION);
            intent.putExtra("tarea", mTarea.getmTitulo());
            getContext().sendBroadcast(intent);
        Utils.setAlarm(ALARM_ID, calendar.getTimeInMillis(), getContext());

    }

    private void unsetAlarm(){
        Utils.cancelAlarm(ALARM_ID, getContext());
    }
    private void updateDate() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        String fechaFormateada = formatoFecha.format(mTarea.getmFecha());
        mFecha.setText(fechaFormateada);
    }


}
