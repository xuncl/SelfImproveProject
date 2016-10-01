package com.xuncl.selfimproveproject.utils;

import android.content.Context;
import android.os.Environment;

import com.xuncl.selfimproveproject.service.Scheme;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CLEVO on 2016/8/26.
 */
public class FileUtils {

    public static String read(Context context, String fileName) {
        try {
            FileInputStream inStream = context.openFileInput(fileName);
            byte[] buffer = new byte[1024];
            int hasRead = 0;
            StringBuilder sb = new StringBuilder();
            while ((hasRead = inStream.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, hasRead));
            }

            inStream.close();
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void write(Context context, String msg, String fileName){
        // 步骤1：获取输入值
        if(msg == null) return;
        try {
            // 步骤2:创建一个FileOutputStream对象,MODE_PRIVATE覆盖模式。(MODE_APPEND追加模式)
            FileOutputStream fos = context.openFileOutput(fileName,
                    Context.MODE_PRIVATE);
            // 步骤3：将获取过来的值放入文件
            fos.write(msg.getBytes());
            // 步骤4：关闭数据流
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将所有数据保存成文件
     *
     * @param object 传进来的数据
     */
    public static void saveFile(Serializable object) {
        String path = Environment.getExternalStorageDirectory().getPath() + "/scheme";
        FileOutputStream out = null;
        ObjectOutputStream oos = null;
        ArrayList<Scheme> arrayList = (ArrayList<Scheme>) object;
        LogUtils.e("savefile", "" + arrayList.size());
        try {
            // sdcard时会报分隔符错误
//            out = openFileOutput(path, Context.MODE_PRIVATE);
            out = new FileOutputStream(path);
            oos = new ObjectOutputStream(out);
            oos.writeObject(object);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将所有数据保存成文件
     *
     * @param fileDir 文件路径
     */
    public static Object loadFile(String fileDir) {
        FileInputStream in = null;
        ObjectInputStream ois = null;
        Object obj = null;
        try {
            // sdcard时会报分隔符错误
//            in = openFileInput(fileDir);
            in = new FileInputStream(fileDir);
            ois = new ObjectInputStream(in);
            obj = ois.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }
}
