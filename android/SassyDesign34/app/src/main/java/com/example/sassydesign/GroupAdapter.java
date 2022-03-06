package com.example.sassydesign;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.Serializable;
import java.nio.channels.AlreadyBoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
// group 객체를 리싸이클러뷰 하나씩에다가 붙여주는 역할이라고 현정이가 말했다.
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> implements Serializable {
    ArrayList<Group> items = new ArrayList<Group>();

    @NonNull
    @Override
    public GroupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int ViewType){
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.bet_group_list_detail, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.ViewHolder viewHolder, int position) {
        Group item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Group item){
        items.add(item);
    }

    public void removeItem(){
        items.clear();
        this.notifyDataSetChanged();
    }

    public void setItems(ArrayList<Group> items){
        this.items = items;
    }

    public Group getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Group item){
        items.set(position, item);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener { // 그냥 가져옴 초기화
        TextView betGroupListDetailName;
        TextView betGroupListDetailGoal;
        TextView betGroupListDetailNumberPeople;

        public ViewHolder(View itemView){
            super(itemView);

            betGroupListDetailName = itemView.findViewById(R.id.betGroupListDetailName);
            betGroupListDetailGoal = itemView.findViewById(R.id.betGroupListDetailGoal);
            betGroupListDetailNumberPeople = itemView.findViewById(R.id.betGroupListDetailNumberPeople);


            itemView.setOnCreateContextMenuListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { // 누르면 상세페이지 groupdetailactivity로 이동
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        //click event
                        Intent GroupDatailIntent = new Intent(itemView.getContext(), GroupDetailActivity.class);
                        GroupDatailIntent.putExtra("items", items);
                        GroupDatailIntent.putExtra("position", getAdapterPosition());
                        itemView.getContext().startActivity(GroupDatailIntent);
                    }
                }
            });
        }

        public void setItem(Group item){ // group에 저장되서 여기에 목록조회
            betGroupListDetailName.setText(item.getGroupName());
            betGroupListDetailGoal.setText(item.getGoal());
            betGroupListDetailNumberPeople.setText(item.getPeopleNum() + "명");
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) { // 꾹 누르면 방나가기
            MenuItem Delete = menu.add(Menu.NONE, 1001, 1, "방 나가기");
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case 1001://삭제
                        AlertDialog deleteDialog;
                        AlertDialog.Builder deleteBuilder = new AlertDialog.Builder(itemView.getContext());
                        deleteDialog = deleteBuilder.setMessage("정말 방을 나가시겠습니까?")
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

        private DialogInterface.OnClickListener yesButtonClickListener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String url = "";
                String inviteCode = items.get(getAdapterPosition()).getRandomCode();
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            Boolean success=jsonResponse.getBoolean("success");

                            if(success){
                                items.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                                notifyItemRangeChanged(getAdapterPosition(), items.size());
                            }
                            else{
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> parameters = new HashMap<>();
                        parameters = new HashMap<>();
                        parameters.put("id", MainActivity.ID);
                        parameters.put("inviteCode", inviteCode);

                        return parameters;
                    }

                };
                request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                        20000 ,
                        com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                request.setShouldCache(false);
                Volley.newRequestQueue(itemView.getContext()).add(request);

            }
            };

        private DialogInterface.OnClickListener noButtonClickListener = new DialogInterface.OnClickListener()     {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        };

    }

}
