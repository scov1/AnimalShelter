package org.seda.animalshelter.Fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.seda.animalshelter.Activity.ServiceDetailsActivity;
import org.seda.animalshelter.Activity.UpdateUserInfoActivity;
import org.seda.animalshelter.DB.DBQuery;
import org.seda.animalshelter.R;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;


public class UpdateUserFragment extends Fragment {

    public UpdateUserFragment() {
        // Required empty public constructor
    }


    private CircleImageView circleImageView;
    private Button changePhotoBtn,removePhotoBtn,updateBtn,doneBtn;
    private EditText nameField,emailField,password;
    private Dialog loadingDialog,passwordDialog;
    private String name,email,photo;
    private Uri imageUri;
    private boolean updatePhoto = false;



//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_update_user, container, false);




        circleImageView = view.findViewById(R.id.circleImageView_fragment_update_user);
        changePhotoBtn = view.findViewById(R.id.button_fragment_update_user_change_photo);
        removePhotoBtn = view.findViewById(R.id.button_fragment_update_user_remove_photo);
        updateBtn = view.findViewById(R.id.button_fragment_update_user_updateBtn);
        nameField = view.findViewById(R.id.editTextTextPersonName_fragment_update_user_name);
        emailField = view.findViewById(R.id.editTextTextEmailAddress_fragment_update_user_email);

        //malenkoe okoshko
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //malenkoe okoshko

        //password okoshko
        passwordDialog = new Dialog(getContext());
        passwordDialog.setContentView(R.layout.password_confirmation_dialog);
        passwordDialog.setCancelable(true);
        passwordDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        password = passwordDialog.findViewById(R.id.editTextTextPassword_password_confirmation_dialog);
        doneBtn = passwordDialog.findViewById(R.id.button_password_confirmation_dialog_ok);

        //password okoshko


         name = getArguments().getString("Name");
         email = getArguments().getString("Email");
         photo = getArguments().getString("Photo");

        Glide.with(getContext()).load(photo).into(circleImageView);
        nameField.setText(name);
        emailField.setText(email);

        changePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
                    if(getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent,1);
                    }else{
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                    }
                }else{
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent,1);
                }
            }
        });

        removePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri=null;
                updatePhoto=true;
                Glide.with(getContext()).load(R.mipmap.ic_img_profile ).into(circleImageView);
            }
        });

        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkEmailAndPassword();
            }
        });

        return  view;
    }

    private void checkInputs(){
        if(!TextUtils.isEmpty(emailField.getText())){
            if(!TextUtils.isEmpty(nameField.getText())){
                updateBtn.setEnabled(true);
                updateBtn.setTextColor(Color.rgb(255,255,255));
            }else{
                updateBtn.setEnabled(false);
                updateBtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
            updateBtn.setEnabled(false);
            updateBtn.setTextColor(Color.argb(50,255,255,255));
        }
    }

    private void checkEmailAndPassword(){

        String pattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";


        if(Patterns.EMAIL_ADDRESS.matcher(emailField.getText().toString()).matches()) {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            if (emailField.getText().toString().matches(pattern)){
                if (emailField.getText().toString().toLowerCase().trim().equals(email.toLowerCase().trim())) {
                    loadingDialog.show();
                    updatePhoto(user);
                } else {//update email
                    passwordDialog.show();
                    doneBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            loadingDialog.show();
                            String userPassword = password.getText().toString();
                            passwordDialog.dismiss();

                            AuthCredential credential = EmailAuthProvider
                                    .getCredential(email, userPassword);

                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                user.updateEmail(emailField.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            updatePhoto(user);

                                                        } else {
                                                            loadingDialog.dismiss();
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                loadingDialog.dismiss();
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    });
                }
        }

        }else{
            emailField.setText("Email не корректный!");
        }
    }

    private void updatePhoto(final FirebaseUser user){
        //update photo

        if(updatePhoto){
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("profile/" + user.getUid() + ".jpg");
            if(imageUri != null){


                Glide.with(getContext()).asBitmap().load(imageUri).circleCrop().into(new ImageViewTarget<Bitmap>(circleImageView) {

                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if(task.isSuccessful()){

                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if(task.isSuccessful()){
                                                imageUri = task.getResult();
                                                DBQuery.profile = task.getResult().toString();
                                                Glide.with(getContext()).load(DBQuery.profile).into(circleImageView);

                                                Map<String,Object> updateData = new HashMap<>();
                                                updateData.put("email",emailField.getText().toString());
                                                updateData.put("fullname",nameField.getText().toString());
                                                updateData.put("profile",DBQuery.profile);

                                                updateField(user,updateData);


                                            }else{
                                                loadingDialog.dismiss();
                                                DBQuery.profile = "";
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }else{
                                    loadingDialog.dismiss();
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        return;
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {

                        circleImageView.setImageResource(R.mipmap.ic_img_profile);
                    }
                });
            }else{
                //remove photo
                storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            DBQuery.profile = "";

                            Map<String,Object> updateData = new HashMap<>();
                            updateData.put("email",emailField.getText().toString());
                            updateData.put("fullname",nameField.getText().toString());
                            updateData.put("profile","");

                            updateField(user,updateData);

                        }else{
                            loadingDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }else{
            Map<String,Object> updateData = new HashMap<>();
            updateData.put("fullname",nameField.getText().toString());
            updateField(user,updateData);
        }

        //update photo
    }

    private void updateField(FirebaseUser user, final Map<String,Object>updateData){
        FirebaseFirestore.getInstance().collection("USERS").document(user.getUid()).update(updateData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            if(updateData.size()>1){
                                DBQuery.email = emailField.getText().toString().trim();
                                DBQuery.fullname = nameField.getText().toString().trim();
                            }else{
                                DBQuery.fullname = nameField.getText().toString().trim();
                            }
                            getActivity().finish();
                            Toast.makeText(getContext(), "Данные обновлены успешно!", Toast.LENGTH_SHORT).show();
                        }else{
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == getActivity().RESULT_OK){
                if(data != null){
                    imageUri = data.getData();
                    updatePhoto=true;
                    Glide.with(getContext()).load(imageUri).into(circleImageView);
                }else{
                    Toast.makeText(getContext(), "Картинка не найдена!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 2){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,1);
            }else{
                Toast.makeText(getContext(), "В доступе отказано!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}