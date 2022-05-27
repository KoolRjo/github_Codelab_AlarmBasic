package android.project.prm391x_project3_hieudmfx09822;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
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

public class FrmSMS extends Fragment {
    android.project.prm391x_project3_hieudmfx09822.UsersPermission usersPermission = new android.project.prm391x_project3_hieudmfx09822.UsersPermission();
    DateTimePicker dateTimePicker = new DateTimePicker();
    private EditText edtPhoneNumber;
    private EditText edtContentSMS;
    private EditText edtDateTime;
    private Button btnSendMess;
    private Button btnCancelMess;
    private Context mContext;
    private static final int SEND_SMS_ID = 1;
    private final Calendar myCalendar = Calendar.getInstance();
    //Method below to get Component on SMS Fragment
    public void getComponentList(View view){
        edtPhoneNumber = view.findViewById(R.id.edt_inputPhone_sms);
        edtContentSMS = view.findViewById(R.id.edt_content_sms);
        edtDateTime = view.findViewById(R.id.edt_time_sms);
        btnSendMess = view.findViewById(R.id.btn_setup_sms);
        btnCancelMess = view.findViewById(R.id.btn_cancel_sms);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
    //Set up date time to schedule our Call
    private void setupDateTimeSMS(){
        edtDateTime.setOnClickListener(v->dateTimePicker.initDate(mContext,myCalendar,edtDateTime));
        btnSendMess.setOnClickListener(v->{
            usersPermission.checkSendSMSPermission(mContext,getActivity());
            sendSMS();
        });
        btnCancelMess.setOnClickListener(v->cancelSMS());
    }
    //Create a job to send SMS
    public void sendSMS(){
        if(edtPhoneNumber.getText().toString().trim().isEmpty() || edtContentSMS.getText().toString().trim().isEmpty()){
            Toast.makeText(mContext,"Fill up phone number and sms content",Toast.LENGTH_SHORT).show();
            edtPhoneNumber.requestFocus();
            return;
        }
        if(edtDateTime.getText().toString().isEmpty()){
            Toast.makeText(mContext,"You have to set up time to send SMS first",Toast.LENGTH_SHORT).show();
            edtDateTime.requestFocus();
            return;
        }
        //Initiate a PendingService to start a task
        ComponentName serviceComponent = new ComponentName(mContext,PendingServices.class);
        JobInfo.Builder builder = new JobInfo.Builder(SEND_SMS_ID,serviceComponent);
        builder.setMinimumLatency(1000);
        builder.setOverrideDeadline(2*1000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("TYPE","TYPE_SEND_SMS");
        bundle.putString("KEY_SMS_PHONE",edtPhoneNumber.getText().toString().trim());
        bundle.putString("KEY_SMS_CONTENT",edtContentSMS.getText().toString().trim());
        bundle.putLong("KEY_SMS_TIME",(Long) edtDateTime.getTag());
        builder.setExtras(bundle);

        JobScheduler jobScheduler = (JobScheduler)mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int result = jobScheduler.schedule(builder.build());
        if(result == JobScheduler.RESULT_SUCCESS){
            Toast.makeText(mContext,"Message will be sent sometime",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(mContext, "Job schedule fail", Toast.LENGTH_LONG).show();
        }
    }
    //Cancel this job
    public void cancelSMS(){
        JobScheduler jobScheduler = (JobScheduler)mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        //jobScheduler.cancel(SEND_SMS_ID);
        jobScheduler.cancelAll();
        Toast.makeText(mContext,"SMS was canceled to sending",Toast.LENGTH_SHORT).show();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_sms_layout,container,false);
        getComponentList(view);
        setupDateTimeSMS();
        return view;
    }
}
