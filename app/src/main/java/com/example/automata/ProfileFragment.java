package com.example.automata;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.INPUT_METHOD_SERVICE;



public class ProfileFragment extends Fragment{

    int followersNum;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                ParseFile file = new ParseFile("profileImage.jpeg", byteArray);

                ParseUser user = ParseUser.getCurrentUser();
                user.put("profileImage", file);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Toast.makeText(getActivity(), "Image Changed Successfully", Toast.LENGTH_SHORT).show();
                            ImageView profilePicture = getActivity().findViewById(R.id.imageView2);
                            ParseFile imageFile = (ParseFile) ParseUser.getCurrentUser().get("profileImage");
                            if (imageFile != null) {

                                imageFile.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        profilePicture.setImageBitmap(bitmap);
                                    }
                                });}
                        } else {
                            Toast.makeText(getActivity(), "An Error Has Occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        } else if(requestCode == 2 && resultCode == RESULT_OK){
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                ParseFile file = new ParseFile("profileImage.jpeg", byteArray);

                ParseUser user = ParseUser.getCurrentUser();
                user.put("profileImage", file);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            Toast.makeText(getActivity(), "Image Changed Successfully", Toast.LENGTH_SHORT).show();
                            ImageView profilePicture = getActivity().findViewById(R.id.imageView2);
                            ParseFile imageFile = (ParseFile) ParseUser.getCurrentUser().get("profileImage");
                            if (imageFile != null) {

                                imageFile.getDataInBackground(new GetDataCallback() {
                                    @Override
                                    public void done(byte[] data, ParseException e) {
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        profilePicture.setImageBitmap(bitmap);
                                    }
                                });}
                        } else {
                            Toast.makeText(getActivity(), "An Error Has Occurred", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        } else if(requestCode == 2){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 2);
            }
        }

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        TextView profileUsername = view.findViewById(R.id.profileUsername);
        profileUsername.setText(ParseUser.getCurrentUser().getUsername());

        ImageView profilePicture = view.findViewById(R.id.imageView2);
        ParseFile imageFile = (ParseFile) ParseUser.getCurrentUser().get("profileImage");
        if (imageFile != null) {
        imageFile.getDataInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                profilePicture.setImageBitmap(bitmap);
            }
        });}

        TextView followingNumTextView = view.findViewById(R.id.followingNumberTextView);
        List<Object> followingList = ParseUser.getCurrentUser().getList("isFollowing");
        String followingString;
        followingString = String.valueOf(followingList.size()-1);


        TextView followersNumTextView = view.findViewById(R.id.followersNumberTextView);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null && objects.size() > 0){
                    for(ParseUser user : objects) {
                        if (user.getList("isFollowing").contains(ParseUser.getCurrentUser().getUsername())) {
                            followersNum++;
                        }
                        followersNumTextView.setText(String.valueOf(followersNum));
                        followingNumTextView.setText(followingString);
                    }
                }
            }
        });


        Button changePictureButton = view.findViewById(R.id.changePictureButton);
        changePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Pick A Source");

                    builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                            } else{
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, 1);}

                        }
                    });
                    builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.i("Info", "Operation Cancelled");
                            dialog.cancel();

                        }
                    });
                    builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                requestPermissions(new String[]{Manifest.permission.CAMERA}, 2);
                            } else{
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, 2);}
                        }
                    });
                    builder.show();
                }
            });
        TextView profileUsernameTextView = view.findViewById(R.id.profileUsername);
        profileUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager2 = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getActivity().getCurrentFocus();

                if (focusedView != null) {
                    inputMethodManager2.hideSoftInputFromWindow(focusedView.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        ConstraintLayout profileBackgroundLayout = view.findViewById(R.id.profileBackgroundLayout);
        profileBackgroundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager2 = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getActivity().getCurrentFocus();

                if (focusedView != null) {
                    inputMethodManager2.hideSoftInputFromWindow(focusedView.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

        RelativeLayout shape_layout = view.findViewById(R.id.shape_layout);
        shape_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager2 = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                View focusedView = getActivity().getCurrentFocus();

                if (focusedView != null) {
                    inputMethodManager2.hideSoftInputFromWindow(focusedView.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });


        TextView bioEditText = view.findViewById(R.id.bioEditText2);

        if(ParseUser.getCurrentUser().get("bio") != null){
        bioEditText.setText(ParseUser.getCurrentUser().get("bio").toString());}

        Button changeBioButton = view.findViewById(R.id.changeBioButton);
        changeBioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Change your Bio");
                final EditText input = new EditText(getActivity());

                input.setSingleLine(false);
                input.setLines(4);
                InputFilter[] FilterArray = new InputFilter[1];
                FilterArray[0] = new InputFilter.LengthFilter(200);
                input.setFilters(FilterArray);
                //input.setMaxLines(5);
                input.setGravity(Gravity.LEFT | Gravity.TOP);
                input.setHorizontalScrollBarEnabled(false);

                builder.setView(input);

                builder.setPositiveButton("Change Bio", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bioEditText.setText(input.getText().toString());
                        ParseUser user = ParseUser.getCurrentUser();
                        user.put("bio", bioEditText.getText().toString());
                        user.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null){

                                } else {

                                }
                            }
                        });
                        Toast.makeText(getActivity(), "Bio Changed Successfully", Toast.LENGTH_SHORT).show();

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


        return view;
    }
}
