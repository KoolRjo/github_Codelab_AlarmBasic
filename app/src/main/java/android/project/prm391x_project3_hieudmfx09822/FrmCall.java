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

public class FrmCall extends Fragment {
    UsersPermission usersPermission = new UsersPermission();
    DateTimePicker setDateTime = new DateTimePicker();
    private EditText edtPhoneNumber;
    private EditText edtDateTime;
    private Button btnCall;
    private Button btnCancel;
    private Context mContext;
    private static final int CALL_PHONE_ID = 2;
    private final Calendar myCalendar = Calendar.getInstance();
    //Get all Component on SMS Fragment
    public void getComponentList(View view) {
        edtPhoneNumber = view.findViewById(R.id.edt_inputPhone_call);
        edtDateTime = view.findViewById(R.id.edt_time_call);
        btnCall = view.findViewById(R.id.btn_setup_call);
        btnCancel = view.findViewById(R.id.btn_cancel_call);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
    //Set up date time to schedule our Call
    private void setupDateTimeCall(){
        edtDateTime.setOnClickListener(v->setDateTime.initDate(mContext,myCalendar,edtDateTime));
        btnCall.setOnClickListener(v->{
            usersPermission.checkCallPhonePermission(mContext,getActivity());
            takePhone();
        });
        btnCancel.setOnClickListener(v->cancelCall());
    }
    //Handle the jobScheduler to take a call
    private void takePhone(){
        if(edtPhoneNumber.getText().toString().trim().isEmpty()){
            Toast.makeText(mContext,"Fill up target phone number",Toast.LENGTH_SHORT).show();
            edtPhoneNumber.requestFocus();
            return;
        }
        if(edtDateTime.getText().toString().isEmpty()){
            Toast.makeText(mContext,"You have to set up time to send SMS first",Toast.LENGTH_SHORT).show();
            edtDateTime.requestFocus();
            return;
        }
        //Schedule to take a Call
        ComponentName serviceComponent = new ComponentName(mContext,PendingServices.class);
        JobInfo.Builder builder = new JobInfo.Builder(CALL_PHONE_ID,serviceComponent);
        builder.setMinimumLatency(1000);
        builder.setOverrideDeadline(2*1000);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        builder.setRequiresDeviceIdle(false);
        builder.setRequiresCharging(false);
        PersistableBundle bundle = new PersistableBundle();
        bundle.putString("TYPE","TYPE_CALL_PHONE");
        bundle.putString("KEY_CALL_PHONE",edtPhoneNumber.getText().toString().trim());
        bundle.putLong("KEY_CALL_TIME",(Long) edtDateTime.getTag());
        builder.setExtras(bundle);
        JobScheduler jobScheduler = (JobScheduler)mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int result = jobScheduler.schedule(builder.build());
        if(result == JobScheduler.RESULT_SUCCESS){
            Toast.makeText(mContext,"A call will done sometime",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(mContext,"Job schedule failed",Toast.LENGTH_LONG).show();
        }
    }
    //Cancel our Call
    private void cancelCall(){
        JobScheduler jobScheduler = (JobScheduler)mContext.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancelAll();
        Toast.makeText(mContext,"Alarm was canceled",Toast.LENGTH_LONG).show();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_call_layout,container,false);
        getComponentList(view);

        setupDateTimeCall();
        return view;
    }
}
