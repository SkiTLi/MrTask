package com.sktl.mrtask;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;


/**
 * Created by USER-PC on 18.02.2018.
 */

public class MrTaskAdapter extends RecyclerView.Adapter<MrTaskAdapter.ProcessViewHolder> {

    List<Process> processes;

    MrTaskAdapter(List<Process> proc) {
        this.processes = proc;
    }


    public static class ProcessViewHolder extends RecyclerView.ViewHolder {


        CardView cv;
        TextView processName;
        TextView processButton;
        ImageView processPhoto;


        //        ProcessViewHolder(View  itemView) {
        ProcessViewHolder(final View itemView) {//потребовало
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            processName = (TextView) itemView.findViewById(R.id.process_name);
            processButton = (TextView) itemView.findViewById(R.id.process_button);
            processPhoto = (ImageView) itemView.findViewById(R.id.process_photo);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MrTaskAdapter.listener != null) {
                        MrTaskAdapter.listener.onItemClick(itemView, getLayoutPosition());
                    }
                }
            });


        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public ProcessViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_view, viewGroup, false);
        ProcessViewHolder pvh = new ProcessViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ProcessViewHolder processViewHolder, int i) {
        processViewHolder.processName.setText(
//                "ID=" + processes.get(i).getId() + ", \n " +
                        processes.get(i).getName());

        processViewHolder.processButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                processViewHolder.processButton.setText("killed");
            }
        });

    }


    @Override
    public int getItemCount() {
        return processes.size();
    }


    //все что ниже - для клика

    public static OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
