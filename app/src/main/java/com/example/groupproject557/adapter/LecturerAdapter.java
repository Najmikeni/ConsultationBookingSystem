package com.example.groupproject557.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.groupproject557.R;
import com.example.groupproject557.model.User;

import java.util.List;


public class LecturerAdapter extends RecyclerView.Adapter<LecturerAdapter.ViewHolder>  {


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener
{

        public TextView tvName;
        public TextView tvId;

        public ViewHolder(View itemView){

            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvId = (TextView) itemView.findViewById(R.id.tvId);
            itemView.setOnLongClickListener(this);

        }
    @Override
    public boolean onLongClick(View view) {
        currentPos = getAdapterPosition(); //key point, record the position here

        if( mListData!=null){System.out.println("bla bla bla" + currentPos);}
        return false;
    }

    }

    private List<User> mListData;
    private Context mContext;
    private int currentPos;

    public LecturerAdapter(Context context,List<User> listData){
        mListData = listData;
        mContext = context;

    }

    private Context getmContext(){return mContext;}

   @Override
   public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view =inflater.inflate(R.layout.lect_list_items,parent,false);

        ViewHolder viewHolder= new ViewHolder(view);
        return viewHolder;
   }

   public void onBindViewHolder(ViewHolder holder,int position) {
       User user = mListData.get(position);
       holder.tvName.setText("Name : "+ user.getName());
        String temp = String.valueOf(user.getId());
        holder.tvId.setText("ID   : "+temp);
   }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public User getSelectedItem() {
        if(currentPos>=0 && mListData!=null && currentPos<mListData.size()) {
            return mListData.get(currentPos);
        }
        return null;
    }

}
