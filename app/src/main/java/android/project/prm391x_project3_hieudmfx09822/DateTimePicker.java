package android.project.prm391x_project3_hieudmfx09822;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.widget.EditText;

import java.util.Locale;

public class DateTimePicker {
    //Show Calendar Date
    public void initDate(Context mContext,Calendar myCalendar,EditText editText){
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR,year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
            initTime(mContext,myCalendar,editText);
        };
        new DatePickerDialog(mContext,dateSetListener,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    //Show Calender Time
    public void initTime(Context mContext,Calendar myCalendar,EditText edtDateTime){
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            myCalendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
            myCalendar.set(Calendar.MINUTE,minute);
            updateLabel(edtDateTime,myCalendar);
        };
        new TimePickerDialog(mContext,timeSetListener,myCalendar.get(Calendar.HOUR_OF_DAY),myCalendar.get(Calendar.MINUTE),true).show();
    }
    //method below to set up time to Edit Text
    public void updateLabel(EditText edtDateTime,Calendar myCalendar){
        String myFormat = "dd/MM/yyyy HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtDateTime.setText(sdf.format(myCalendar.getTime()));
        edtDateTime.setTag(myCalendar.getTimeInMillis());
    }
}
