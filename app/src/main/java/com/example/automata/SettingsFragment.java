package com.example.automata;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SettingsFragment extends Fragment {


    EditText newUsername;
    ParseUser parseUser;


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_settings, container, false);




        String[] accountItems = {"Change Username", "Change Email", "Change Password"};

        ListView accountListView = view.findViewById(R.id.accountListView);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, accountItems);
        accountListView.setAdapter(arrayAdapter1);

        accountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //CHANGE USERNAME
                if(position == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Change Your Username");
                    builder.setMessage("Feature Currently Unavailable.");
                    //newUsername = new EditText(getActivity());
                    String temp2 = ParseUser.getCurrentUser().getUsername();
                    //builder.setView(newUsername);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                           /*parseUser = ParseUser.getCurrentUser();
                            parseUser.setUsername(newUsername.getText().toString());
                            parseUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (null == e) {
                                        if(ParseUser.getCurrentUser().getList("isFollowing").contains(temp2)) {
                                            Toast.makeText(getActivity(), "its in" + temp2, Toast.LENGTH_SHORT).show();
                                        }
                                        //ParseUser.getCurrentUser().getList("isFollowing").remove(temp2);
                                        String temp = ParseUser.getCurrentUser().getUsername();
                                        //Toast.makeText(getActivity(), "Username Changed Successfully", Toast.LENGTH_SHORT).show();
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Memos");
                                        query.whereEqualTo("username", temp);
                                        query.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> objects, ParseException e) {
                                                if (e == null) {
                                                    for (ParseObject user : objects) {
                                                        if (user.get("username").equals(temp)) {
                                                            user.put("username", newUsername.getText().toString());
                                                            user.saveInBackground(new SaveCallback() {
                                                                @Override
                                                                public void done(ParseException e) {

                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });*/

                        }
                    });
                    /*builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("Info", "Operation Cancelled");
                            dialog.cancel();
                        }
                    });*/
                    builder.show();
                }








                //CHANGE EMAIL
                if(position == 1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Change Your Email");
                    builder.setMessage("Please enter your new Email.");
                    EditText newEmail = new EditText(getActivity());
                    newEmail.setMaxLines(1);
                    newEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    builder.setView(newEmail);
                    builder.setIcon(R.drawable.ic_email_2);

                    builder.setPositiveButton("Change Email", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ParseUser parseUser = ParseUser.getCurrentUser();
                            parseUser.setEmail(newEmail.getText().toString());
                            parseUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(Patterns.EMAIL_ADDRESS.matcher(newEmail.getText().toString()).matches()){
                                        if (null == e) {
                                            Toast.makeText(getActivity(), "Email Changed Successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else{
                                        Toast.makeText(getActivity(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
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








                //CHANGE PASSWORD
                if(position == 2){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Change Your Password");
                    builder.setMessage("Please enter your new Password.");
                    EditText newPassword = new EditText(getActivity());
                    newPassword.setMaxLines(1);
                    newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    builder.setView(newPassword);
                    builder.setIcon(R.drawable.ic_password2);

                    builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ParseUser parseUser = ParseUser.getCurrentUser();
                            parseUser.setPassword(newPassword.getText().toString());
                            parseUser.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(newPassword.getText().toString().length() >=8) {
                                        if (null == e) {
                                            Toast.makeText(getActivity(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "The password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
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


            }
        });






        String[] otherItems = {"Contact Us", "Log Out"};

        ListView otherListView =  view.findViewById(R.id.otherListView);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, otherItems);
        otherListView.setAdapter(arrayAdapter2);

        otherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Contact Info");
                    builder.setMessage("For Any Enquiries or Reports, Contact Us at The Following Email:\n\nautomataapp@outlook.com");
                    builder.setIcon(R.drawable.ic_info);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }

                if(position==1){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Are you sure you want to log out?");
                    builder.setIcon(R.drawable.ic_logout);
                    builder.setPositiveButton("Log out", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ParseUser.logOut();
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
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
            }
        });






        String[] deleteAccount = {"Delete Account"};

        ListView deleteAccountListView = view.findViewById(R.id.deleteAccountListView);
        ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(getActivity(), R.layout.textcolor, deleteAccount);
        deleteAccountListView.setAdapter(arrayAdapter3);

        deleteAccountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    EditText email = new EditText(getActivity());
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                            .setTitle("ARE YOU SURE YOU WANT TO DELETE YOUR ACCOUNT?")
                            .setView(email)
                            .setMessage("Please enter your Email to confirm.")
                            .setIcon(R.drawable.ic_warning)
                            .setCancelable(true)
                            .setPositiveButton("DELETE PERMANENTLY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (email.getText().toString().equals(ParseUser.getCurrentUser().getEmail())) {
                                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Memo");
                                        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                                        query.findInBackground(new FindCallback<ParseObject>() {
                                            @Override
                                            public void done(List<ParseObject> objects, ParseException e) {
                                                for(ParseObject object : objects){
                                                    if(object.get("username").equals(ParseUser.getCurrentUser().getUsername())){
                                                        try {
                                                            object.delete();
                                                        } catch (ParseException parseException) {
                                                            parseException.printStackTrace();
                                                        }
                                                    }
                                                }
                                            }
                                        });



                                        ParseUser parseUser = ParseUser.getCurrentUser();
                                        parseUser.deleteInBackground(new DeleteCallback() {
                                            @Override
                                            public void done(ParseException e) {
                                                if (e == null) {
                                                    Toast.makeText(getActivity(), "Account Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getActivity(), LoginOrSignupActivity.class);
                                                    startActivity(intent);
                                                } else {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                        ParseUser.logOut();
                                    } else {
                                        Toast.makeText(getActivity(), "Wrong Email, Process Aborted", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
                }
            }
        });



        return view;

    }
}
