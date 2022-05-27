package android.project.prm391x_project3_hieudmfx09822;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class FrmAlarm extends Fragment {
    android.project.prm391x_project3_hieudmfx09822.DateTimePicker setDateTime = new android.project.prm391x_project3_hieudmfx09822.DateTimePicker();
    ShardPreferencesData sharedPreferences = new ShardPreferencesData();
    private ControlFragment listener;
    private EditText edtContentAlarm;
    private EditText edtDateTime;
    private Button btnAlarm;
    private Button btnFinish;
    private Button btnCancel;
    private Context mContext;
    private static int ALARM_ID;
    private final Calendar myCalendar = Calendar.getInstance();
    //Method below to get Component on SMS Fragment
    public void getComponentList(View view) {
        edtContentAlarm = view.findViewById(R.id.edt_content_alarm);
        edtDateTime = view.findViewById(R.id.edt_time_alarm);
        btnAlarm = view.findViewById(R.id.btn_setup_alarm);
        btnFinish = view.findViewById(R.id.btn_finish_alarm);
        btnCancel = view.findViewById(R.id.btn_cancel_alarm);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        //Check whether if MainActivity implemented ControlFragment Interface
        if(context instanceof  MainActivity){
            listener = (ControlFragment) context;
        }else{
            throw new RuntimeException(context.toString()+" must Implement onViewSelected");
        }
    }
    //Set up date time to schedule our Alarm
    private void setupDateTimeAlarm(){
        edtDateTime.setOnClickListener(v->setDateTime.initDate(mContext,myCalendar,edtDateTime));
        btnAlarm.setOnClickListener(v->setAlarm());
    }
    //Handle the jobScheduler to setting up an Alarm
    private void setAlarm(){
        Random rd = new Random();
        ALARM_ID = rd.nextInt();
        int alarmID = ALARM_ID;
        String alarmSMS = edtContentAlarm.getText().toString().trim();
        String alarmTime = edtDateTime.getText().toString();
        String alarmStatus = "Waiting";
        if( edtContentAlarm.getText().toString().trim().isEmpty()){
            Toast.makeText(mContext,"You have to add a notice about job",Toast.LENGTH_SHORT).show();
            edtContentAlarm.requestFocus();
            return;
        }
        if(edtDateTime.getText().toString().isEmpty()){
            Toast.makeText(mContext,"You have to set up time to set Alarm first",Toast.LENGTH_SHORT).show();
            edtDateTime.requestFocus();
            return;
        }
        //Schedule to set up an alarm
        ComponentName serviceComponent = new ComponentName(mContext,PendingServices.class);
        JobInfo.Builder builder = new JobInfo.Builder(ALARM_ID,serviceComponent);
        builder.setMinimumLatency(1000);
        builder.setOverrideDeadline(2*1000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresDeviceIdle(false);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("TYPE","TYPE_ALARM");
        bundle.putInt("KEY_ID",alarmID);
        bundle.putString("KEY_ALARM",alarmSMS);
        bundle.putString("KEY_TARGET_TIME",alarmTime);
        bundle.putLong("KEY_ALARM_TIME",(long)edtDateTime.getTag());
        builder.setExtras(bundle);
        JobScheduler jobScheduler = (JobScheduler)mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int result = jobScheduler.schedule(builder.build());
        if(result == JobScheduler.RESULT_SUCCESS){
            sharedPreferences.saveSharedPreferences(mContext,alarmID,alarmSMS,alarmTime,alarmStatus);
            Toast.makeText(mContext,"Alarm will woke up sometime",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(mContext,"Job scheduled fail",Toast.LENGTH_SHORT).show();
        }
    }
    //Show alarm 
    private void showAlarm(){
        List<AlarmEntity> list = sharedPreferences.readSharedPreferences(mContext);
        if(getArguments() != null){
            int alarmID = getArguments().getInt("showID");
            String alarm = getArguments().getString("showAlarm");
            String alarmDesTime = getArguments().getString("showDesTime");
            edtContentAlarm.setText(alarm);
            edtDateTime.setText(alarmDesTime);
            showHiddenButton(alarmDesTime);
            btnFinish.setOnClickListener(v->{
                for(AlarmEntity item:list){
                    if(item.getId() == alarmID){
                        item.setStatus("Done");
                        sharedPreferences.updateSharedPreferences(mContext,list);
                        Toast.makeText(mContext,"SMS: "+item.getAlarmSMS() +", Status: "+item.getStatus(),Toast.LENGTH_LONG).show();
                    }
                }
                cancelAlarm(alarmID);
            });
            btnCancel.setOnClickListener(v->{
                for(AlarmEntity item:list){
                    if(item.getId() == alarmID){
                        item.setStatus("Stopped");
                        sharedPreferences.updateSharedPreferences(mContext,list);
                        Toast.makeText(mContext,"ID: "+item.getId() +", Status: "+item.getStatus(),Toast.LENGTH_LONG).show();
                    }
                }
                cancelAlarm(alarmID);
            });
        }
    }
    //Cancel alarm and back to FrmSMS
    private void cancelAlarm(int alarmID){
        JobScheduler jobScheduler = (JobScheduler)mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        //jobScheduler.cancelAll();
        jobScheduler.cancel(alarmID);
        btnCancel.setVisibility(View.INVISIBLE);
        btnFinish.setVisibility(View.INVISIBLE);
        edtContentAlarm.setText("");
        edtDateTime.setText("");
        listener.backFrmSMS();
    }
    //Show Cancel and Stop Button
    private void showHiddenButton(String dateTime){
        String myFormat = "dd/MM/yyyy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String nowTime = sdf.format(Calendar.getInstance().getTime());
        if((edtDateTime.getText().toString()).equalsIgnoreCase(nowTime)){
            btnCancel.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.VISIBLE);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_alarm_layout,container,false);
        getComponentList(view);
        setupDateTimeAlarm();
        showAlarm();
        return view;
    }
}
