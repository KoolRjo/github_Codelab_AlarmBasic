package android.project.prm391x_project3_hieudmfx09822;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class UsersPermission extends AppCompatActivity {
    public static final int request_Permission_SEND_SMS = 1;
    public static final int request_Permission_CALL_PHONE = 2;
    public static final int request_Permission_CAMERA = 3;
    public static final int request_Permission_READ_EXTERNAL_STORAGE = 4;
    //Ask to user to get SEND_SMS permission
    public void checkSendSMSPermission(Context context, Activity activity){
        if(ContextCompat.checkSelfPermission(context,Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.SEND_SMS)){
                Toast.makeText(getApplicationContext(),"Dịch vụ nhắn tin sẽ không hoạt động",Toast.LENGTH_LONG).show();
            }else{
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.SEND_SMS},request_Permission_SEND_SMS);
            }
        }
    }
    //Ask to user to get CALL_PHONE permission
    public void checkCallPhonePermission(Context context, Activity activity){
        if(ContextCompat.checkSelfPermission(context,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.CALL_PHONE)){
                Toast.makeText(getApplicationContext(),"Dịch vụ nhắn tin sẽ không hoạt động",Toast.LENGTH_LONG).show();
            }else{
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CALL_PHONE},request_Permission_CALL_PHONE);
            }
        }
    }
    //Ask to user to get CAMERA permission
    public void checkCameraPermission(Context context, Activity activity){
        if(ContextCompat.checkSelfPermission(context,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.CAMERA)){
                Toast.makeText(getApplicationContext(),"chức năng camera sẽ không hoạt động",Toast.LENGTH_LONG).show();
            }else{
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CAMERA},request_Permission_CAMERA);
            }
        }
    }
    //Ask to user to get READ STORAGE permission
    public void checkReadStoragePermission(Context context, Activity activity){
        if(ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(activity,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(getApplicationContext(),"ứng dụng sẽ không được phép vào Gallery",Toast.LENGTH_LONG).show();
            }else{
                ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},request_Permission_READ_EXTERNAL_STORAGE);
            }
        }
    }
    //Handle the user's choice

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case request_Permission_SEND_SMS:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"Bạn đã đồng ý ưng dụng có quyền gửi tin nhắn",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Bạn đã từ chối ưng dụng có quyền gửi tin nhắn",Toast.LENGTH_LONG).show();
                }
                break;
            case request_Permission_CALL_PHONE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"Bạn đã đồng ý ưng dụng có quyền gọi điện thoại bằng thiết bị này",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Bạn đã từ chối ưng dụng có quyền gọi điện thoại bằng thiết bị này",Toast.LENGTH_LONG).show();
                }
                break;
            case request_Permission_CAMERA:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"Bạn đã đồng ý ưng dụng có quyền sử dụng CAMERA bằng thiết bị này",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Bạn đã từ chối ưng dụng có quyền sử dụng CAMERA bằng thiết bị này",Toast.LENGTH_LONG).show();
                }
                break;
            case request_Permission_READ_EXTERNAL_STORAGE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"Bạn đã đồng ý ưng dụng có quyền truy xuất Gallery của thiết bị này",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Bạn đã từ chối ưng dụng có quyền truy xuất Gallery của thiết bị này",Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
