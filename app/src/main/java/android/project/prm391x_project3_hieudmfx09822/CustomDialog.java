package android.project.prm391x_project3_hieudmfx09822;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.util.List;

public class CustomDialog{
    ShardPreferencesData sharedPreferences = new ShardPreferencesData();
    public void showDialog(Context context, String alarmStatus,String alarmSMS, int position){
        List<AlarmEntity> list = sharedPreferences.readSharedPreferences(context);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alarm_detail_layout);
        TextView txDialogTitle = dialog.findViewById(R.id.tx_dialog_alarm_status);
        TextView txDialogContent = dialog.findViewById(R.id.edt_dialog_alarm_sms);
        txDialogTitle.setText(alarmStatus);
        txDialogContent.setText(alarmSMS);
        Button btnDialogFinish = dialog.findViewById(R.id.btn_dialog_done);
        btnDialogFinish.setOnClickListener(v->{
            for(int i=0;i<list.size();i++){
                if(i == position){
                    list.get(i).setStatus("Done");
                    sharedPreferences.updateSharedPreferences(context,list);
                }
            }
            Intent refreshIntent = new Intent(context,MainActivity.class);
            context.startActivity(refreshIntent);
            dialog.dismiss();
        });
        Button btnDialogRemove = dialog.findViewById(R.id.btn_dialog_remove);
        btnDialogRemove.setOnClickListener(v->{
            for(int i=0;i<list.size();i++){
                if(i == position){
                    list.remove(list.get(i));
                    sharedPreferences.updateSharedPreferences(context,list);
                }
            }
            Intent refreshIntent = new Intent(context,MainActivity.class);
            context.startActivity(refreshIntent);
            dialog.dismiss();
        });
        dialog.show();
    }
}
