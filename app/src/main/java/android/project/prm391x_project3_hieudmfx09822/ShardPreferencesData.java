package android.project.prm391x_project3_hieudmfx09822;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShardPreferencesData {
    public static final String TAG = ShardPreferencesData.class.getName();
    public static final String  key_storage = "KEY_DATA";
    //save data into SharedPreferences
    public void saveSharedPreferences(Context context, int id, String alarmSMS, String targetTime, String status){
        List<AlarmEntity> list = new ArrayList<>();
        AlarmEntity alarmEntity = new AlarmEntity(id,alarmSMS,targetTime,status);
        //Get String key of SharedPreferences
        String txtData = context.getSharedPreferences(context.getString(R.string.sharedPreferences_alarm_file),Context.MODE_PRIVATE).getString(key_storage,null);
        if (txtData != null) {
            Gson gson = new Gson();
            //get current array of AlarmEntity class
            AlarmEntity[] alarmsArray = gson.fromJson(txtData, AlarmEntity[].class);
            //put list of current Array into List<AlarmEntity>
            list.addAll(Arrays.asList(alarmsArray));
        }
        list.add(alarmEntity);
        //Now put data into SharedPreferences
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.sharedPreferences_alarm_file),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key_storage,new Gson().toJson(list));
        if(editor.commit()){
            Log.i(TAG,"Alarm's information saved success!");
        }else{
            Log.i(TAG,"Alarm's information saved failed!");
        }
    }
    //update list then put into SharedPreferences
    public void updateSharedPreferences(Context context, List<AlarmEntity> list){
        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.sharedPreferences_alarm_file),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key_storage,new Gson().toJson(list));
        if(editor.commit()){
            Log.i(TAG,"SharedPreferences was updated success!");
        }else{
            Log.i(TAG,"SharedPreferences was updated failed!");
        }
    }
    //read data from SharedPreferences
    public List<AlarmEntity> readSharedPreferences(Context context){
        List<AlarmEntity> list = null;
        String txtData = context.getSharedPreferences(context.getString(R.string.sharedPreferences_alarm_file),Context.MODE_PRIVATE).getString(key_storage,null);
        if(txtData == null){
            Log.i(TAG,"No alarm set up in ShardPreferences");
        }else{
            Gson gson = new Gson();
            AlarmEntity[] alarmArray = gson.fromJson(txtData,AlarmEntity[].class);
            list = new ArrayList<>(Arrays.asList(alarmArray));
        }
        return list;
    }
}
