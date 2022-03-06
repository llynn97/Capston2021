package com.example.sassydesign;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupUserAdapter extends RecyclerView.Adapter<GroupUserAdapter.ViewHolder> {
    ArrayList<GroupUser> items = new ArrayList<GroupUser>();

    @NonNull
    @Override
    public GroupUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int ViewType){
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.bet_group_detail_user, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupUserAdapter.ViewHolder viewHolder, int position) {
        GroupUser item = items.get(position);
        viewHolder.setItem(item);
    }
    Context context;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(GroupUser item){
        items.add(item);
    }

    public void setItems(ArrayList<GroupUser> items){
        this.items = items;
    }

    public GroupUser getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, GroupUser item){
        items.set(position, item);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        ImageView detailProfile;
        TextView detailUserName;
        TextView detailSum;
        TextView detailPercent;

        public ViewHolder(View itemView){
            super(itemView);

            detailProfile = itemView.findViewById(R.id.betDetailProfile);
            detailUserName = itemView.findViewById(R.id.betDetailUserName);
            detailSum = itemView.findViewById(R.id.betDetailSum);
            detailPercent = itemView.findViewById(R.id.betDetailPercent);
        }

        public void setItem(GroupUser item){
            if (item.getDetailProfile().isEmpty() || item.getDetailProfile()==null) {
                detailProfile.setImageResource(R.drawable.default_profile_image);
            } else{
                Picasso.with(context)
                        .load(item.getDetailProfile())
                        .into(detailProfile);
            }

            detailUserName.setText(item.getDetailUserName());
            detailSum.setText(item.getDetailSum());
            detailPercent.setText(item.getDetailPercent() + "%");
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Delete = menu.add(Menu.NONE, 1001, 1, "삭제");

            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case 1001://삭제
                        AlertDialog deleteDialog;
                        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(itemView.getContext());
                        deleteDialog = deleteBuilder.setMessage("정말 삭제하시겠습니까?")
                                .setPositiveButton("확인", yesButtonClickListener)
                                .setNegativeButton("취소", noButtonClickListener)
                                .create();
                        deleteDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                deleteDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#FF719AFF"));
                                deleteDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#FF719AFF"));
                            }
                        });
                        deleteDialog.show();
                        break;
                }
                return true;
            }
        };

        // 전체삭제
        private DialogInterface.OnClickListener yesButtonClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };

        private DialogInterface.OnClickListener noButtonClickListener = new DialogInterface.OnClickListener()     {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        };
    }
}