package com.example.automata;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getChildFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    SearchView searchView;
    RecycleAdapter recycleAdapter;


    String imageString = "";

    ArrayList<String> usernames;
    ArrayList<String> memos;
    ArrayList<String> createdAt;
    ArrayList<String> createdAt2;
    ArrayList<String> parseFiles;
    ArrayList<ParseUser> users;
    List<String> dateStringList;
    List<String> dateStringList2;
    DateFormat df, df2;



    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);


        df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); //2021-07-22T17:48:07.517Z
        df2 = new SimpleDateFormat("HH:mm");

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout = v.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.red,
                R.color.black,
                R.color.black,
                R.color.black);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                refreshActivity();
            }
        });

        if (ParseUser.getCurrentUser() != null) {
            Button shareButton = v.findViewById(R.id.newButton);
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Share Your Thoughts!");
                    EditText message = new EditText(getActivity());
                    message.setHint("What are you thinking...?");
                    builder.setView(message);
                    builder.setPositiveButton("Share", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (message.length() > 0) {
                                ParseObject memo = new ParseObject("Memos");
                                memo.put("memo", message.getText().toString());
                                memo.put("username", ParseUser.getCurrentUser().getUsername());
                                memo.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            Toast.makeText(getActivity(), "Memo Sent!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            e.printStackTrace();
                                            Toast.makeText(getActivity(), "Failed to Share", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    message.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if(s.length()>0){
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            } else {
                                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }




            });


            List<Object> list = ParseUser.getCurrentUser().getList("isFollowing");
            if (!list.contains(ParseUser.getCurrentUser().getUsername())) {
                ParseUser user2 = ParseUser.getCurrentUser();
                user2.add("isFollowing", ParseUser.getCurrentUser().getUsername());
                user2.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {

                    }
                });
            }
        }



        return v;
    }



    @Override
    public void onRefresh() {
        refreshActivity();
    }

    private void refreshActivity() {

        swipeRefreshLayout.setRefreshing(true);

        if (ParseUser.getCurrentUser() != null) {
            usernames = new ArrayList<>();
            memos = new ArrayList<>();
            createdAt = new ArrayList<>();
            createdAt2 = new ArrayList<>();
            parseFiles = new ArrayList<>();
            dateStringList = new ArrayList<>();
            dateStringList2 = new ArrayList<>();
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Memos");
            query.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));
            query.orderByDescending("createdAt");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (ParseObject object : objects) {
                            memos.add(object.getString("memo"));
                            usernames.add(object.getString("username"));
                            Date date = object.getCreatedAt();
                            String reportDate = df.format(date);
                            String ago = (String) DateUtils.getRelativeTimeSpanString(date.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.SECOND_IN_MILLIS);
                            createdAt.add(ago);
                        }
                        Log.i("info", String.valueOf(usernames.size()));
                        users = new ArrayList<ParseUser>();
                        ParseQuery<ParseUser> query = ParseUser.getQuery();
                        query.findInBackground(new FindCallback<ParseUser>() {
                            @Override
                            public void done(List<ParseUser> objects, ParseException e) {
                                if (e == null && objects.size() > 0) {
                                    for (ParseUser user : objects) {
                                        users.add(user);
                                    }
                                    ParseFile image;
                                    for(int q = 0; q<usernames.size(); q++){
                                        for(int w = 0; w<users.size(); w++){
                                            if(users.get(w).getUsername().equals(usernames.get(q))){
                                                //Log.i("users num", String.valueOf(users.size()));
                                                image = (ParseFile) users.get(w).get("profileImage");
                                                imageString = image.getUrl();
                                                parseFiles.add(imageString);
                                            }
                                        }
                                    }
                                    Log.i("size", String.valueOf(createdAt.size()));

                                    for (String date : createdAt) {
                                        String dateStr = String.valueOf(date);
                                        dateStringList.add(dateStr);
                                    }
                                    for (String date : createdAt2) {
                                        String dateStr = String.valueOf(date);
                                        dateStringList2.add(dateStr);
                                    }
                                    recycleAdapter= new RecycleAdapter(getActivity(), usernames, memos, parseFiles, dateStringList);
                                    recyclerView.setAdapter(recycleAdapter);
                                }
                            }
                        });
                }
                    swipeRefreshLayout.setRefreshing(false);
            }

        });
    }
    }
}

