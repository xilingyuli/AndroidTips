package com.xilingyuli.androidtips.blog.list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xilingyuli.androidtips.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by xilingyuli on 2017/3/13.
 */

class BlogListRecyclerViewAdapter extends RecyclerView.Adapter<BlogListRecyclerViewAdapter.ToolsViewHolder>{

    private LayoutInflater inflater;
    private List<Map<String, String>> data;
    private SimpleDateFormat sdf;

    BlogListRecyclerViewAdapter(LayoutInflater inflater){
        this.inflater = inflater;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    }

    public void setData(List<Map<String, String>> data){
        this.data = data;
        this.notifyDataSetChanged();
    }

    @Override
    public ToolsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ToolsViewHolder(inflater.inflate(R.layout.listitem_blog, parent, false));
    }

    @Override
    public void onBindViewHolder(ToolsViewHolder holder, int position) {
        Date date = new Date(Long.parseLong(data.get(position).get("ctime"))*1000);
        holder.setData(
                (position+1)+"",
                (data.get(position).get("name")).replace(".md",""),
                sdf.format(date)
        );
    }

    @Override
    public int getItemCount() {
        return data==null?0:data.size();
    }

    static class ToolsViewHolder extends RecyclerView.ViewHolder
    {
        View view;
        TextView id,title,date;
        ToolsViewHolder(View view)
        {
            super(view);
            this.view = view;
            id = (TextView) view.findViewById(R.id.id);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
        }
        void setData(String id, String title, String date){
            this.id.setText(id);
            this.title.setText(title);
            this.date.setText(date);
        }
    }
}