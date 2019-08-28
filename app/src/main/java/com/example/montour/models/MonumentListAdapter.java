package com.example.montour.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.montour.R;
import com.example.montour.callbacks.IOnToggleClicked;

import java.util.ArrayList;



public class MonumentListAdapter extends RecyclerView.Adapter<MonumentListAdapter.ViewHolder> {
    private final Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<MonumentItem> monuments = new ArrayList<>();
    private IOnToggleClicked listener;
    private MonumentSelection selection = MonumentSelection.getInstance();


    public MonumentListAdapter(Context context, ArrayList monuments, IOnToggleClicked listener) {
        this.context = context;
        this.monuments = monuments;
        this.layoutInflater = LayoutInflater.from(context);
        this.listener = listener;

    }

    public MonumentListAdapter(Context context, ArrayList monuments) {
        this.context = context;
        this.monuments = monuments;
        this.layoutInflater = LayoutInflater.from(context);
        this.listener = null;

    }
    @NonNull
    @Override
    public MonumentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = this.layoutInflater.inflate(R.layout.mon_list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MonumentListAdapter.ViewHolder holder, int position) {
        MonumentItem item = this.monuments.get(position);
        holder.viewTitle.setText(item.getMyName());
        holder.viewAddres.setText(item.getAdress());
        holder.viewToggle.setSelected(selection.isMonumentInList(item));
        holder.viewToggle.setEnabled(this.shouldToggleBeEnabled(item));

        holder.viewToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.handleClickedToggle(item);
                    holder.viewToggle.setSelected(selection.isMonumentInList(item));

                } else {
                    holder.viewToggle.setEnabled(false);
                }



            }
        });


    }

    @Override
    public int getItemCount() {
        return this.monuments.size();
    }

    public boolean shouldToggleBeEnabled(MonumentItem item){
        if(this.listener == null)
            return false;
        else
            return true;


    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView viewTitle;
        TextView viewAddres;
        ToggleButton viewToggle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewTitle = (TextView)itemView.findViewById(R.id.mon_title);
            viewAddres = (TextView)itemView.findViewById(R.id.mon_add);
            viewToggle = (ToggleButton)itemView.findViewById(R.id.mon_toggle_add);
        }
    }
}
