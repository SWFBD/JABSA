package com.example.jabsa;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jabsa.model.Categoria;

public class CategoriaHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView mNameTextView;
    private Categoria mCategoria;

    public CategoriaHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.list_encabezado_tarea, parent, false));
        itemView.setOnClickListener(this);
        mNameTextView = (TextView) itemView.findViewById(R.id.nombre_categoria);

    }

    public void bind(Categoria categoria) {

        mCategoria = categoria;

        mNameTextView.setText(mCategoria.getmNombre());

    }

    @Override
    public void onClick(View v) {

    }
}
