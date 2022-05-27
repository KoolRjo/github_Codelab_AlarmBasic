package android.project.prm391x_project3_hieudmfx09822;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AlarmEntity implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("alarmSMS")
    private String alarmSMS;
    @SerializedName("targetTime")
    private String targetTime;
    @SerializedName("status")
    //status includes "waiting", "done", "stopped"
    private String status;

    //Constructor without args
    public AlarmEntity() {
    }
    //Constructor within args
    public AlarmEntity(int id, String alarmSMS, String targetTime, String status) {
        this.id = id;
        this.alarmSMS = alarmSMS;
        this.targetTime = targetTime;
        this.status = status;
    }
    //Getter and Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlarmSMS() {
        return alarmSMS;
    }

    public void setAlarmSMS(String alarmSMS) {
        this.alarmSMS = alarmSMS;
    }

    public String getTargetTime() {
        return targetTime;
    }

    public void setTargetTime(String targetTime) {
        this.targetTime = targetTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
