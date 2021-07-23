package com.example.automata;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;
    ArrayAdapter arrayAdapter;
    ListView usersListView;
    ArrayList<String> users;
    ParseUser currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);


        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh2);
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


        usersListView = view.findViewById(R.id.usersListView);
        users = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.newlistview, users);
        usersListView.setAdapter(arrayAdapter);

        currentUser = ParseUser.getCurrentUser();


        if(ParseUser.getCurrentUser() != null) {
            Button viewProfileButton = view.findViewById(R.id.viewButton);
            viewProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Enter The Username");
                    EditText viewUser = new EditText(getActivity());
                    builder.setView(viewUser);

                    builder.setPositiveButton("View", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(getActivity(), OtherUserProfile.class);
                            intent.putExtra("username", viewUser.getText().toString());
                            startActivity(intent);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("Info", "Operation Cancelled");
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });


            Button addButton = view.findViewById(R.id.addButton);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Enter The Username");
                    EditText addUser = new EditText(getActivity());
                    builder.setView(addUser);

                    builder.setPositiveButton("Follow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<String> temp;
                            temp = ParseUser.getCurrentUser().getList("isFollowing");
                            String user = addUser.getText().toString();
                            if (user.equals(ParseUser.getCurrentUser().getUsername())) {
                                Toast.makeText(getActivity(), "You Can't Add Yourself", Toast.LENGTH_SHORT).show();
                            } else if (temp.contains(user)) {
                                Toast.makeText(getActivity(), "User Already Followed", Toast.LENGTH_SHORT).show();
                            } else {
                                ParseQuery<ParseUser> query = ParseUser.getQuery();
                                query.whereEqualTo("username", user);
                                query.findInBackground(new FindCallback<ParseUser>() {
                                    @Override
                                    public void done(List<ParseUser> objects, ParseException e) {
                                        if (e == null) {
                                            if (users.size() > 0) {
                                                if (!users.contains(user)) {
                                                    Toast.makeText(getActivity(), "User Doesn't Exist", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "User Followed", Toast.LENGTH_SHORT).show();
                                                    currentUser.add("isFollowing", user);
                                                    currentUser.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e == null) {
                                                                Log.i("info", "data saved");
                                                            } else {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        } else {
                                            e.printStackTrace();
                                        }
                                    }
                                });

                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("Info", "Operation Cancelled");
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });


            Button unfollowButton = view.findViewById(R.id.unfollowButton);
            unfollowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Enter The Username");
                    EditText removeUser = new EditText(getActivity());
                    builder.setView(removeUser);

                    builder.setPositiveButton("Unfollow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            List<String> temp;
                            temp = ParseUser.getCurrentUser().getList("isFollowing");

                            String unfollow = removeUser.getText().toString();
                            ParseQuery<ParseUser> query = ParseUser.getQuery();
                            query.whereEqualTo("username", unfollow);
                            query.findInBackground(new FindCallback<ParseUser>() {
                                @Override
                                public void done(List<ParseUser> objects, ParseException e) {
                                    if (e == null) {
                                        Log.i("info", "entered");
                                        if (users.size() > 0) {
                                            if (!users.contains(unfollow)) {
                                                Toast.makeText(getActivity(), "User Doesn't Exist", Toast.LENGTH_SHORT).show();
                                            } else if (!temp.contains(unfollow)) {
                                                Toast.makeText(getActivity(), "You Don't Follow That User", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getActivity(), "User Unfollowed", Toast.LENGTH_SHORT).show();
                                                currentUser.removeAll("isFollowing", Collections.singleton(unfollow));
                                                currentUser.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e == null) {
                                                            Log.i("info", "data saved");
                                                        } else {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    } else {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("Info", "Operation Cancelled");
                            dialog.cancel();
                        }
                    });
                    builder.show();

                }
            });
        }



        return view;
    }

    @Override
    public void onRefresh() {
        refreshActivity();
    }

    private void refreshActivity() {

        swipeRefreshLayout.setRefreshing(true);
        arrayAdapter.clear();
        arrayAdapter.notifyDataSetChanged();
        if (ParseUser.getCurrentUser() != null) {
            usersListView.setAdapter(arrayAdapter);
            usersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(), OtherUserProfile.class);
                    intent.putExtra("username", users.get(position));
                    startActivity(intent);
                    return false;
                }
            });


            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null && objects.size() > 0) {
                        for (ParseUser user : objects) {
                            users.add(user.getUsername());
                        }
                        arrayAdapter.notifyDataSetChanged();
                    } else {
                        try {
                            e.printStackTrace();
                        } catch (Exception e1) {

                        }
                    }
                }
            });


            swipeRefreshLayout.setRefreshing(false);
        }
    }


}
