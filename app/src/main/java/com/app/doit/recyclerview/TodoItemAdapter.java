package com.app.doit.recyclerview;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.app.doit.alarm.AlarmReceiver;
import com.app.doit.dao.TodoItemDao;
import com.app.doit.model.TodoItemDto;
import com.app.doit.R;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.ItemViewHolder> {
    private List<TodoItemDto> itemList;
    private final Context context;

    public TodoItemAdapter(List<TodoItemDto> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ItemViewHolder(view, context, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        TodoItemDto todoItem = itemList.get(position);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, todoItem.getYear());
        calendar.set(Calendar.MONTH, todoItem.getMonth());
        calendar.set(Calendar.DAY_OF_MONTH, todoItem.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, todoItem.getHour());
        calendar.set(Calendar.MINUTE, todoItem.getMinute());
        calendar.set(Calendar.SECOND, 0);

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy hh:mm", Locale.getDefault());
        String formattedDate = sdf.format(calendar.getTime());

        holder.todoItemTitle.setText(todoItem.getTitle());
        holder.todoItemDate.setText(formattedDate);
        holder.removeTodoItemButton.setTag(todoItem);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateData(List<TodoItemDto> todoItemDtoList) {
        this.itemList = todoItemDtoList;
        notifyDataSetChanged();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView todoItemTitle;
        public TextView todoItemDate;
        public ImageButton removeTodoItemButton;
        public Context context;
        public TodoItemAdapter todoItemAdapter;

        public ItemViewHolder(View itemView, Context context, TodoItemAdapter todoItemAdapter) {
            super(itemView);
            this.context = context;
            this.todoItemAdapter = todoItemAdapter;
            todoItemTitle = itemView.findViewById(R.id.todoItemTitle);
            todoItemDate = itemView.findViewById(R.id.todoItemDate);
            removeTodoItemButton = itemView.findViewById(R.id.removeTodoItem);
            removeTodoItemButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.removeTodoItem){
                TodoItemDto todoItem = (TodoItemDto) v.getTag();
                Log.i(context.getString(R.string.doitapptag), "Removed todo item: " + todoItem);
                AlarmManager alarmManager = (AlarmManager) getSystemService(context, AlarmManager.class);
                Intent intent = new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, todoItem.getAlarmRequestId(), intent, PendingIntent.FLAG_IMMUTABLE);
                if (alarmManager != null){
                    alarmManager.cancel(pendingIntent);
                    Log.i(context.getString(R.string.doitapptag), "Cancelled alarm: " + todoItem.getAlarmRequestId());
                }
                todoItemAdapter.updateData(TodoItemDao.removeTodoItem(context, todoItem));
            }
        }
    }
}
