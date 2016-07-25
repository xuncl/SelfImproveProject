package com.xuncl.selfimproveproject.activities;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuncl.selfimproveproject.R;
import com.xuncl.selfimproveproject.service.Target;

public class TargetAdapter extends ArrayAdapter<Target>
{

    private int resourceId;

    public TargetAdapter(Context context, int textViewResourceId, List<Target> objects)
    {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Target target = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (null == convertView)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.targetIcon = (ImageView) view.findViewById(R.id.target_icon);
            viewHolder.targetText = (TextView) view.findViewById(R.id.target_name);
            viewHolder.targetDone = (ImageView) view.findViewById(R.id.target_done);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.targetIcon.setImageResource(target.getIconId());
        viewHolder.targetText.setText(target.toString());
        if (target.isDone())
        {
            viewHolder.targetDone.setImageResource(R.drawable.target_setdone);
        }
        else
        {
            viewHolder.targetDone.setImageResource(R.drawable.target_done);
        }
        viewHolder.targetDone.setTag(target);
        viewHolder.targetDone.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ImageView ib = (ImageView) v;
                Target tgt = (Target) v.getTag();
                tgt.check();
                // Toast.makeText(TargetAdapter.this.getContext(), tgt.getName()
                // + "is done? " + tgt.isDone(),
                // Toast.LENGTH_SHORT).show();
                if (tgt.isDone())
                {
                    ib.setImageResource(R.drawable.target_setdone);
                }
                else
                {
                    ib.setImageResource(R.drawable.target_done);
                }

            }
        });
        return view;
    }

    class ViewHolder
    {
        ImageView targetIcon;
        TextView targetText;
        ImageView targetDone;
    }

}
