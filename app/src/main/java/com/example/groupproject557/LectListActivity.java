package com.example.groupproject557;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.groupproject557.adapter.LecturerAdapter;
import com.example.groupproject557.model.SharedPrefManager;
import com.example.groupproject557.model.User;
import com.example.groupproject557.remote.ApiUtils;
import com.example.groupproject557.remote.UserService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LectListActivity extends AppCompatActivity {

    UserService userService;
    Context context;
    RecyclerView lectList;
    LecturerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lect_list);
        context = this;

        lectList = findViewById(R.id.LectList);
        registerForContextMenu(lectList);

        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        userService = ApiUtils.getUserService();
        
        userService.getAllLect(user.getToken()).enqueue((new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                // Get list of user object from response
                List<User> users = response.body();

                // initialize adapter
                adapter = new LecturerAdapter(context, users);

                // set adapter to the RecyclerView
                lectList.setAdapter(adapter);

                // set layout to recycler view
                lectList.setLayoutManager(new LinearLayoutManager(context));

                // add separator between item in the list
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(lectList.getContext(),
                        DividerItemDecoration.VERTICAL);
                lectList.addItemDecoration(dividerItemDecoration);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(context, "Error connecting to the server", Toast.LENGTH_LONG).show();
                Log.e("MyApp:", t.getMessage());
            }
        }));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lect_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        User selectedUser = adapter.getSelectedItem();
        System.out.println(selectedUser.getId());
        Log.d("MyApp", "selected "+ selectedUser.toString());
        switch (item.getItemId()) {
            case R.id.details://should match the id in the context menu file
                doDetails(selectedUser);
                break;
        }
        return super.onContextItemSelected(item);
    }
    private void doDetails(User selectedUser){
        Log.d("MyApp:", "viewing details "+ selectedUser.toString());
        Intent intent = new Intent(context, LecturerDetailActivity.class);
        intent.putExtra("id", selectedUser.getId());
        startActivity(intent);
    }

}