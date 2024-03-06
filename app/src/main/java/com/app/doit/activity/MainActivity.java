package com.app.doit.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.doit.dao.TodoItemDao;
import com.app.doit.model.TodoItemDto;
import com.app.doit.R;
import com.app.doit.recyclerview.TodoItemAdapter;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TodoItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton addTodoItemButton = findViewById(R.id.addTodoItemButton);
        addTodoItemButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ToDoItemActivity.class);
            startActivity(intent);
        });

        createNotificationChannel();
        List<TodoItemDto> todoItemDtoList = TodoItemDao.getTodoItemList(this);

        RecyclerView recyclerView = findViewById(R.id.todoList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TodoItemAdapter(todoItemDtoList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<TodoItemDto> todoItemDtoList = TodoItemDao.getTodoItemList(this);
        if (adapter != null) {
            adapter.updateData(todoItemDtoList);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}