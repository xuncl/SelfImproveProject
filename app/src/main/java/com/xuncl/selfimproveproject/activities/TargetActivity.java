package com.xuncl.selfimproveproject.activities;

import com.xuncl.selfimproveproject.Constant;
import com.xuncl.selfimproveproject.R;
import com.xuncl.selfimproveproject.utils.LogUtils;
import com.xuncl.selfimproveproject.utils.Tools;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class TargetActivity extends BaseActivity
{
    private EditText nameEdit;
    private EditText desEdit;
    private EditText startEdit;
    private EditText endEdit;
    private EditText valueEdit;
    private CheckBox isDoneBox;
    private CheckBox isAgendaBox;
    private EditText intervalEdit;
    private EditText maxvalueEdit;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        LogUtils.d(Constant.SERVICE_TAG, "into onCreate()");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.new_target);
        save = (Button) findViewById(R.id.button_save);
        nameEdit = (EditText) findViewById(R.id.target_name);
        desEdit = (EditText) findViewById(R.id.target_description);
        startEdit = (EditText) findViewById(R.id.target_start_time);
        endEdit = (EditText) findViewById(R.id.target_end_time);
        valueEdit = (EditText) findViewById(R.id.target_value);
        isDoneBox = (CheckBox) findViewById(R.id.target_isdone);
        isAgendaBox = (CheckBox) findViewById(R.id.target_isagenda);
        intervalEdit = (EditText) findViewById(R.id.target_interval);
        maxvalueEdit = (EditText) findViewById(R.id.target_maxvalue);
        Intent intent = getIntent();
        String tname = intent.getStringExtra(Constant.NAME_PARA);
        boolean isagenda = intent.getBooleanExtra(Constant.ISAGENDA_PARA, false);
        if (!Tools.isEmpty(tname)) // modify
        {
            nameEdit.setText(tname);
            desEdit.setText(intent.getStringExtra(Constant.DESCRIPTION_PARA));
            startEdit.setText(intent.getStringExtra(Constant.START_PARA));
            endEdit.setText(intent.getStringExtra(Constant.END_PARA));
            valueEdit.setText("" + intent.getIntExtra(Constant.VALUE_PARA, Constant.BASED_VALUE));
            isDoneBox.setChecked(intent.getBooleanExtra(Constant.ISDONE_PARA, false));
            isAgendaBox.setChecked(isagenda);
            intervalEdit.setText("" + intent.getIntExtra(Constant.INTERVAL_PARA, Constant.BASED_INTERVAL));
            maxvalueEdit.setText("" + intent.getIntExtra(Constant.MAXVALUE_PARA, Constant.BASED_VALUE));
            if (isagenda)
            {
                isAgendaBox.setEnabled(false);
                intervalEdit.setEnabled(false);
                maxvalueEdit.setEnabled(false);
            }
            else
            {
                isAgendaBox.setVisibility(View.GONE);
                intervalEdit.setVisibility(View.GONE);
                maxvalueEdit.setVisibility(View.GONE);
            }
        }
        save.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                boolean valueParsable = true;

                boolean isagenda = isAgendaBox.isChecked();
                int value = Constant.BASED_VALUE;
                int interval = Constant.BASED_INTERVAL;
                int maxvalue = Constant.BASED_VALUE;
                try
                {
                    value = Integer.parseInt(valueEdit.getText().toString());
                    if (isagenda)
                    {
                        interval = Integer.parseInt(intervalEdit.getText().toString());
                        maxvalue = Integer.parseInt(maxvalueEdit.getText().toString());
                    }
                }
                catch (Exception e)
                {
                    valueParsable = false;
                }
                String name = nameEdit.getText().toString();
                String description = desEdit.getText().toString();
                String start = startEdit.getText().toString();
                String end = endEdit.getText().toString();
                boolean isdone = isDoneBox.isChecked();

                if (valueParsable && (!Tools.isEmpty(name)) && (Tools.checkTime(start)) && (Tools.checkTime(end)))
                {
                    Intent intent = new Intent();
                    intent.putExtra(Constant.NAME_PARA, name);
                    intent.putExtra(Constant.DESCRIPTION_PARA, description);
                    intent.putExtra(Constant.START_PARA, start);
                    intent.putExtra(Constant.END_PARA, end);
                    intent.putExtra(Constant.VALUE_PARA, value);
                    intent.putExtra(Constant.ISDONE_PARA, isdone);
                    intent.putExtra(Constant.ISAGENDA_PARA, isagenda);
                    intent.putExtra(Constant.INTERVAL_PARA, interval);
                    intent.putExtra(Constant.MAXVALUE_PARA, maxvalue);

                    setResult(RESULT_OK, intent);
                    finish();
                }
                else
                {
                    Toast.makeText(TargetActivity.this, "Input wrong, please try again.", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        super.onBackPressed();
    }

    /**
     * 传参启动添加计划的Activity
     * @param activity 启动者
     * @param name 计划名
     * @param description 描述
     * @param start 开始时间
     * @param end 结束时间
     * @param value 分值
     * @param isDone 是否完成
     * @param isAgenda 是否为长期任务
     * @param interval 分值增长间隔
     * @param maxvalue 分值最大值
     * @param resultTag 结果标签
     */
    public static void actionStart(BaseActivity activity, String name, String description, String start, String end,
                                   int value, boolean isDone, boolean isAgenda, int interval, int maxvalue, int resultTag)
    {
        Intent intent = new Intent(activity, TargetActivity.class);
        intent.putExtra(Constant.NAME_PARA, name);
        intent.putExtra(Constant.DESCRIPTION_PARA, description);
        intent.putExtra(Constant.START_PARA, start);
        intent.putExtra(Constant.END_PARA, end);
        intent.putExtra(Constant.VALUE_PARA, value);
        intent.putExtra(Constant.ISDONE_PARA, isDone);
        intent.putExtra(Constant.ISAGENDA_PARA, isAgenda);
        intent.putExtra(Constant.INTERVAL_PARA, interval);
        intent.putExtra(Constant.MAXVALUE_PARA, maxvalue);
        activity.startActivityForResult(intent, resultTag);
    }
}
