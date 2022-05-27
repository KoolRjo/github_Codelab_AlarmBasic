package android.project.prm391x_project3_hieudmfx09822;

import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class PendingServices extends JobService {
    private static final String TAG = PendingServices.class.getName();
    private final Map<Integer,JobAsyncTask> jobAsyncTasks = new HashMap<>();
    private final static String KEY_PREF = "KEY_DATA";
    private JobAsyncTask jobAsyncTask;
    private String jobName;
    //Start your job
    @Override
    public boolean onStartJob(JobParameters params) {
        jobName = params.getExtras().getString("TYPE");
        jobAsyncTask = new JobAsyncTask();
        jobAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
        jobAsyncTasks.put(params.getJobId(),jobAsyncTask);
        Log.i(TAG,jobName + " is starting");
        Log.i(TAG,"Job scheduled counted:" + jobAsyncTasks.size());
        return false;
    }
    //Stop your job before its finish
    @Override
    public boolean onStopJob(JobParameters params) {
        jobName = params.getExtras().getString("TYPE");
        Log.d(TAG,jobName + " was stopped before its complete");
        jobAsyncTask = jobAsyncTasks.get(params.getJobId());
        if(jobAsyncTask != null){
            jobAsyncTask.cancel(true);
            //No reschedule this job after stop
            return false;
        }
        //No reschedule this job after stop
        return false;
    }
    //Do any job we got from JobScheduler by their job types
    private void doPendingTask(JobParameters jobParameters){
        String type = jobParameters.getExtras().getString("TYPE");
        switch (type){
            case "TYPE_SEND_SMS":
                sendSMS(jobParameters);
                break;
            case "TYPE_CALL_PHONE":
                callPhone(jobParameters);
                break;
            case "TYPE_ALARM":
                setAlarm(jobParameters);
                break;
        }
    }
    //Handle the job to sendSMS job
    private void sendSMS(JobParameters jobParameters){
        Long nowMillisTime = Calendar.getInstance().getTimeInMillis();
        Long targetMillisTime = jobParameters.getExtras().getLong("KEY_SMS_TIME");
        Log.i(TAG,"Schedule time of "+jobParameters.getExtras().getString("TYPE")+": "+targetMillisTime);
        Log.i(TAG,jobParameters.getExtras().getString("TYPE")+": in "+targetMillisTime);
        Log.i(TAG,jobParameters.getExtras().getString("TYPE")+" delay time in:  "+(targetMillisTime-nowMillisTime)/1000+" seconds");
        String phoneNumber = jobParameters.getExtras().getString("KEY_SMS_PHONE","No target phone number was received");
        String contentSMS = jobParameters.getExtras().getString("KEY_SMS_CONTENT","No content of SMS was received");
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(()->{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber,null,contentSMS,null,null);
        },(targetMillisTime - nowMillisTime));
    }
    //Handle the job to call a phone job
    private void callPhone(JobParameters jobParameters){
        Long nowMillisTime = Calendar.getInstance().getTimeInMillis();
        Long targetMillisTime = jobParameters.getExtras().getLong("KEY_CALL_TIME");
        Log.i(TAG,"Schedule time of "+jobParameters.getExtras().getString("TYPE")+": "+targetMillisTime);
        Log.i(TAG,jobParameters.getExtras().getString("TYPE")+": in "+targetMillisTime);
        Log.i(TAG,jobParameters.getExtras().getString("TYPE")+" delay time in:  "+(targetMillisTime-nowMillisTime)/1000+" seconds");
        String phoneNumber = jobParameters.getExtras().getString("KEY_CALL_PHONE","No target phone number was received");
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(()->{
            Uri uri = Uri.parse("tel: "+phoneNumber);
            Intent intentCall = new Intent(Intent.ACTION_CALL);
            intentCall.setData(uri);
            startActivity(intentCall);
        },(targetMillisTime - nowMillisTime));
    }
    //Handle the job to set up a alarm
    private void setAlarm(JobParameters jobParameters){
        Long nowMillisTime = Calendar.getInstance().getTimeInMillis();
        Long targetMillisTime = jobParameters.getExtras().getLong("KEY_ALARM_TIME");
        Log.i(TAG,"Schedule time of "+jobParameters.getExtras().getString("TYPE")+": "+targetMillisTime);
        Log.i(TAG,jobParameters.getExtras().getString("TYPE")+": in "+targetMillisTime);
        Log.i(TAG,jobParameters.getExtras().getString("TYPE")+" delay time in:  "+(targetMillisTime-nowMillisTime)/1000+" seconds");
        int setID = jobParameters.getExtras().getInt("KEY_ID");
        String setJob = jobParameters.getExtras().getString("KEY_ALARM","Not any job was set up yet");
        String setDesTime = jobParameters.getExtras().getString("KEY_TARGET_TIME");
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(()->{
            Intent intentStart = new Intent(PendingServices.this,MainActivity.class);
            intentStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentStart.putExtra("ALARM_ID",setID);
            intentStart.putExtra("ALARM_SMS",setJob);
            intentStart.putExtra("ALARM_TIME",setDesTime);
            startActivity(intentStart);
        },(targetMillisTime - nowMillisTime));
    }

    //Handle AsyncTask
    @SuppressLint("StaticFieldLeak")
    private class JobAsyncTask extends AsyncTask<JobParameters,Void,JobParameters>{
        //Invoke method below before we do any tasks in doInBackground
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i(TAG,"Task is setting up in onPreExecute!");
        }
        //Job will run in worker thread by method below
        @Override
        protected JobParameters doInBackground(JobParameters... jobParameters) {
            if(!isCancelled())
            {
                doPendingTask(jobParameters[0]);
                return jobParameters[0];
            }
            Toast.makeText(PendingServices.this,"Job was canceled absolutely in doInBackground!!!",Toast.LENGTH_LONG).show();
            return null;
        }

        @Override
        protected void onCancelled(JobParameters jobParameters) {
            super.onCancelled(jobParameters);
        }

        //Run in UIThread and get result of method doInBackground
        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            super.onPostExecute(jobParameters);
            Log.i(TAG, "Task Finished ");
            jobFinished(jobParameters,false);
        }
    }
}
