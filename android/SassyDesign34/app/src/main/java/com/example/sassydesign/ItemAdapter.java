package com.example.sassydesign;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    ArrayList<Item> items = new ArrayList<Item>();

    DailyScreen dailyScreen;
    Context itemAdapterContext;
    static ArrayList<String> objid = new ArrayList<String>();

    public ItemAdapter(DailyScreen dailyScreen){
        this.dailyScreen = dailyScreen;
    }

    public ItemAdapter(){
        this.dailyScreen = dailyScreen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int ViewType){
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_daily_list, viewGroup, false);
        itemAdapterContext = viewGroup.getContext();
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Item item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(Item item){
        items.add(item);
    }

    public void removeItem(){
        items.clear();
        this.notifyDataSetChanged();
    }

    public void setItems(ArrayList<Item> items){
        this.items = items;
    }

    public Item getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Item item){
        items.set(position, item);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, DatePickerDialog.OnDateSetListener{

        TextView itemTitle;
        TextView itemInOrOut;
        TextView itemCacheOrCard;
        TextView itemDetail;
        TextView itemCategory;
        TextView itemPrice;
        TextView itemQuantity;
        String newDate=""; //
        Button dailyDateButton;
        ItemDetailAdapter adapter;
        Button dateButton;

        public ViewHolder(View itemView){
            super(itemView);

            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemInOrOut = itemView.findViewById(R.id.itemInOrOut);
            itemCacheOrCard = itemView.findViewById(R.id.itemCacheOrCard);
            itemDetail = itemView.findViewById(R.id.itemDetail);
            itemCategory = itemView.findViewById(R.id.itemCategory);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);

            itemView.setOnCreateContextMenuListener(this);
        }

        //꾹 누르면 수정, 삭제 메뉴 생성
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "수정");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");

            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        TextView dailyOutcome;
        TextView dailyIncome;
        TextView dailyTotal;

        int totalIncome = 0;
        int totalOutcome = 0;
        int total = 0;

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1001: //수정
                        Intent ModifyIntent = new Intent(itemView.getContext(), ModifyActivity.class);
                        ModifyIntent.putExtra("items", items);
                        ModifyIntent.putExtra("position", getAdapterPosition());
                        itemView.getContext().startActivity(ModifyIntent);
                        break;

                    case 1002://삭제
                        Dialog dialog = new Dialog(itemView.getContext());
                        dialog.setContentView(R.layout.dialog_two_button);
                        TextView dialogTitle = (TextView)dialog.findViewById(R.id.dialog_title);
                        TextView dialogMessage = (TextView)dialog.findViewById(R.id.dialog_message);
                        dialogTitle.setText("삭제");
                        dialogMessage.setText("정말 삭제하시겠습니까?");

                        //취소 버튼
                        Button dialogButton = (Button)dialog.findViewById(R.id.dialog_button);
                        dialogButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        //확인 버튼
                        Button dialogButton2 = (Button)dialog.findViewById(R.id.dialog_button2);
                        dialogButton2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String url = "";
                                //년, 월, 일 처리 부분
                                String date = items.get(getAdapterPosition()).getDate();
                                String [] seperateDate = date.split("/");
                                String title = items.get(getAdapterPosition()).getTitle();

                                //여기서 년, 월, 일 나눠줌
                                String year = seperateDate[0];
                                String month = seperateDate[1];
                                String day = seperateDate[2];

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
                                                initTotal();
                                                setTotal();
                                                //99999999999999if=success면 삭제되었다는 다이어로그 띄워주는 코드 작성바람999999999999
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
                                        parameters.put("title", title);
                                        parameters.put("year", String.valueOf(year));
                                        parameters.put("month", String.valueOf(month));
                                        parameters.put("date", String.valueOf(day));

                                        return parameters;
                                    }

                                };
                                request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(

                                        20000 ,

                                        com.android.volley.DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                                        com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                request.setShouldCache(false);
                                Volley.newRequestQueue(itemView.getContext()).add(request);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();

                        break;
                }
                return true;
            }
        };

        public void initTotal(){
            total = 0;
            totalIncome = 0;
            totalOutcome = 0;
        }

        public void setTotal(){
            for(int i = 0 ; i<items.size() ; i++){

                //품목 하나 안의 세부 품목
                for(int j = 0; j<items.get(i).getPriceList().size(); j++){
                    //그 품목의 가격이 음수일 때(지출일 때)
                    if((items.get(i).getInOrOut()).equals("지출")){
                        totalOutcome += Integer.parseInt(items.get(i).getPriceList().get(j));
                    }
                    //그 품목의 가격이 양수일 때(수입일 때)
                    else if((items.get(i).getInOrOut()).equals("수입")){
                        totalIncome += Integer.parseInt(items.get(i).getPriceList().get(j));
                    }
                }
            }

            TextView dailyOutcome = dailyScreen.getDailyOutCome();
            TextView dailyIncome = dailyScreen.getDailyIncome();
            TextView dailyTotal = dailyScreen.getDailyTotal();

            dailyOutcome.setText("-"+totalOutcome);
            dailyIncome.setText("+"+totalIncome);

            total = totalIncome - totalOutcome;
            if (total>0)
            {
                dailyTotal.setText("+" + total);
            }
            else
            {
                //알아서 -로 나와서 -안 붙여도 됨
                dailyTotal.setText(""+total);
            }

        }


        public void showDatePicker(View view) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(itemView.getContext(), this,
                    calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }

        @Override
        public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
            newDate = year + "/" + (month + 1) + "/" + day;
            dateButton.setText(newDate);
            removeItem();

        }

        public void setItem(Item item){

            String items="";
            String categories = "";
            String quantities = "";
            String price = "";


            itemTitle.setText(item.getTitle());
            itemInOrOut.setText(item.getInOrOut());
            itemCacheOrCard.setText(item.getCacheOrCard());
            for(int i = 0; i < item.getItemList().size(); i++){
                if (i != item.getItemList().size()) {
                    items += item.getItemList().get(i)+"\n";
                    categories += item.getCategoryList().get(i)+"\n";
                    quantities += item.getQuantityList().get(i)+"\n";
                    price += item.getPriceList().get(i)+"\n";

                }
                else{
                    items += item.getItemList().get(i);
                    categories += item.getCategoryList().get(i);
                    quantities += item.getQuantityList().get(i);
                    price += item.getPriceList().get(i);
                }

                itemDetail.setText(items);
                itemCategory.setText(categories);
                itemPrice.setText(price);
                itemQuantity.setText(quantities);
            }
        }

    }

}