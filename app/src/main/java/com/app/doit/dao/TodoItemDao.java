package com.app.doit.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.doit.model.TodoItemDto;
import com.app.doit.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodoItemDao {
    private static final Gson gson = new Gson();

    public static ArrayList<TodoItemDto> getTodoItemList(Context context){
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(context.getString(R.string.todo_list), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Type type = new TypeToken<ArrayList<TodoItemDto>>() {}.getType();
        String json = preferences.getString(context.getString(R.string.todo_list), null);
        ArrayList<TodoItemDto> todoList = new ArrayList<>();
        if (json != null){
            todoList = gson.fromJson(json, type);
        }
        deleteStaleTodoItem(context, todoList, editor);
        Log.i(context.getString(R.string.doitapptag), "TodoList: " + todoList);
        return todoList;
    }

    public static void saveTodoItem(Context context, TodoItemDto todoItem) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.todo_list), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Type type = new TypeToken<ArrayList<TodoItemDto>>() {}.getType();
        String json = preferences.getString(context.getString(R.string.todo_list), null);
        ArrayList<TodoItemDto> todoList = new ArrayList<>();
        if (json != null){
            todoList = gson.fromJson(json, type);
        }
        todoList.add(todoItem);

        saveTodoItemList(context, todoList);
        Log.i(context.getString(R.string.doitapptag), "Updated todo list");
    }

    public static void saveTodoItemList(Context context, ArrayList<TodoItemDto> todoList) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.todo_list), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(context.getString(R.string.todo_list), gson.toJson(todoList));
        editor.commit();
    }

    public static ArrayList<TodoItemDto> removeTodoItem(Context context, TodoItemDto todoItem){
        Log.i(context.getString(R.string.doitapptag), "Removed todo item: " + todoItem);
        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences(context.getString(R.string.todo_list), Context.MODE_PRIVATE);
        Type type = new TypeToken<ArrayList<TodoItemDto>>() {}.getType();
        String json = preferences.getString(context.getString(R.string.todo_list), null);
        ArrayList<TodoItemDto> todoList = new ArrayList<>();
        if (json != null){
            todoList = gson.fromJson(json, type);
        }
        Log.i(context.getString(R.string.doitapptag), "TodoList: " + todoList.contains(todoItem));
        todoList.remove(todoItem);
        saveTodoItemList(context, todoList);
        Log.i(context.getString(R.string.doitapptag), "TodoList: " + todoList);
        return todoList;
    }

    private static void deleteStaleTodoItem(Context context, ArrayList<TodoItemDto> todoList, SharedPreferences.Editor editor) {
        List<TodoItemDto> staleItems = new ArrayList<>();
        for (TodoItemDto todoItem: todoList){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, todoItem.getYear());
            calendar.set(Calendar.MONTH, todoItem.getMonth());
            calendar.set(Calendar.DAY_OF_MONTH, todoItem.getDay());
            calendar.set(Calendar.HOUR_OF_DAY, todoItem.getHour());
            calendar.set(Calendar.MINUTE, todoItem.getMinute());
            calendar.set(Calendar.SECOND, 0);

            if (calendar.getTimeInMillis() <= System.currentTimeMillis()){
                Log.i(context.getString(R.string.doitapptag), "Removed todo item: " + todoItem);
                staleItems.add(todoItem);
            }
        }

        for (TodoItemDto staleItem: staleItems){
            todoList.remove(staleItem);
        }

        if (!staleItems.isEmpty()){
            saveTodoItemList(context, todoList);
        }
    }
}
