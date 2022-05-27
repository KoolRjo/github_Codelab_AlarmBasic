package android.project.prm391x_project3_hieudmfx09822;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    CustomDialog customDialog = new CustomDialog();
    private final Context context;
    private final List<AlarmEntity> alarmList;

    public DataAdapter(Context context, List<AlarmEntity> alarmList) {
        this.context = context;
        this.alarmList = alarmList;
    }
    //Show list size
    @Override
    public int getItemCount() {
        return alarmList == null? 0 : alarmList.size();
    }

    public static class DataViewHolder extends RecyclerView.ViewHolder{
        private final TextView tx_alarm_time;
        public DataViewHolder(@NonNull View itemView) {
            super(itemView);
            tx_alarm_time = itemView.findViewById(R.id.tx_time_alarm);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        String desTime = alarmList.get(position).getTargetTime();
        holder.tx_alarm_time.setText(desTime);
    }

    public void onSetItemClick(RecyclerView rv,View v){
        int position = rv.getChildLayoutPosition(v);
        String status = alarmList.get(position).getStatus();
        String sms = alarmList.get(position).getAlarmSMS();
        if(status.equalsIgnoreCase("Stopped")){
            customDialog.showDialog(context,status,sms,position);
            Toast.makeText(context,"Status: "+status,Toast.LENGTH_LONG).show();
        }else if(status.equalsIgnoreCase("Done")){
            customDialog.showDialog(context,status,sms,position);
            Toast.makeText(context,"Status: "+status,Toast.LENGTH_LONG).show();
        }else{
            customDialog.showDialog(context,status,sms,position);
            Toast.makeText(context,"Status: "+status,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemViewType(int position) {
        String status = alarmList.get(position).getStatus();
        switch (status){
            case "Stopped":
                return 1;
            case "Done":
                return 2;
            default:
                return 3;
        }
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType){
            case 1:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_stopped_layout,parent,false);
                itemView.setOnClickListener(v-> onSetItemClick((RecyclerView)parent,itemView));
                break;
            case 2:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_done_layout,parent,false);
                itemView.setOnClickListener(v-> onSetItemClick((RecyclerView)parent,itemView));
                break;
            case 3:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_waiting_layout,parent,false);
                itemView.setOnClickListener(v-> onSetItemClick((RecyclerView)parent,itemView));
                break;
            default:
                throw new IllegalStateException("Unexpected viewType's value: " + viewType);
        }
        return new DataViewHolder(itemView);
    }
}
