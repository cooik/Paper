package com.example.sony.client;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sony.client.models.Paper;

import java.util.Collections;
import java.util.List;

/**
 * Created by sony on 2017/4/16.
 */

public class PaperAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    List<Paper> data= Collections.emptyList();
    Paper current;
    int currentPos=0;
    OnItemClickListener mItemClickListener;

    public PaperAdapter(Context context, List<Paper> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.container_paper, parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder myholder  = (ViewHolder) holder;
        Paper current  = data.get(position);
        myholder.tv_time.setText("发布时间: "+current.getCreated_at());
        myholder.tv_author.setText("作者: "+current.getAuthor());
        myholder.tv_title.setText("标题: "+current.getTitle());



    }



    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_title;
        TextView tv_author;
        TextView tv_time;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_papertitle);
            tv_author = (TextView) itemView.findViewById(R.id.tv_paperauthor);
            tv_time = (TextView) itemView.findViewById(R.id.tv_papertime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    public void SetOnItemClickListener(final  OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
}
