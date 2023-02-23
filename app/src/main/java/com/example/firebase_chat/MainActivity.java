package com.example.firebase_chat;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
  private ImageView send;
  private TextView  sendTv,textView;
   private ArrayList<String> list;
   private RecyclerView recyclerView;
   private FirebaseDatabase firebaseDatabase;
   private ProgressDialog progressDialog;
   private ImageView insert_image,send_audio;
   private ActivityResultLauncher<String> launcher;
   private  ActivityResultLauncher<String> launcher_audio;
   private  ActivityResultLauncher<String> launcher_profile;
   private FirebaseStorage firebaseStorage;
   private Random random;
   private ScrollView scrollView;
   private   MyAdapter myAdapter;
   private CircleImageView profile;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollView=findViewById(R.id.scrollView);
        insert_image=findViewById(R.id.insert_image);
        send_audio=findViewById(R.id.send_audio);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Loading..");
        progressDialog.show();
         send=findViewById(R.id.send);
         sendTv=findViewById(R.id.sendtView);
         textView=findViewById(R.id.textView);
         profile=findViewById(R.id.profile_image);



        recyclerView=findViewById(R.id.recyclerView);

        list=new ArrayList<>();
        random=new Random();
        firebaseDatabase=FirebaseDatabase.getInstance("https://fir-chat-a189a-default-rtdb.firebaseio.com/");
        firebaseStorage=FirebaseStorage.getInstance("gs://fir-chat-a189a.appspot.com");

        getChats();
        get_profile();

        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);



    profile.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            launcher_profile.launch("image/*");
        }
    });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(!sendTv.getText().toString().isEmpty())
               {
                   list.clear();
                 firebaseDatabase.getReference().child("Chats").child("12").push().setValue("1. "+sendTv.getText()
                 .toString());
                   firebaseDatabase.getReference().child("Chats").child("21").push().setValue("1. "+sendTv.getText()
                           .toString());


                   sendTv.setText("");
               }

            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.clear();
                startActivity(new Intent(MainActivity.this,MainActivity2.class));

            }
        });

        launcher_profile=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {

            @Override
            public void onActivityResult(Uri result) {
             profile.setImageURI(result);

                StorageReference storageReference=  firebaseStorage.getReference().child("Profile");
                storageReference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            firebaseDatabase.getReference("Profile").setValue(uri.toString());

                        }
                    }) ;
                 }
             });

            }
        });


        launcher=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {

            @Override
            public void onActivityResult(Uri result) {

                int x=random.nextInt(20);
                int y=random.nextInt(20);
                int z=random.nextInt(20);


                StorageReference storageReference=  firebaseStorage.getReference().child("images").child("12").child(Integer.toString(x)+Integer.toString(y)+Integer.toString(z));

                storageReference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                Toast.makeText(MainActivity.this,"Image uploaded",Toast.LENGTH_SHORT).show();
                                firebaseDatabase.getReference().child("Chats").child("12").push().setValue(uri.toString());
                                firebaseDatabase.getReference().child("Chats").child("21").push().setValue(uri.toString());

                                firebaseDatabase.getReference().child("Chats").child("12").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        list.clear();
                                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                                        {
                                            String message=dataSnapshot.getValue(String.class);

                                                list.add(message);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
  Toast.makeText(MainActivity.this,"Cannot load image, Try again later",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });



            }
        });

        launcher_audio=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {

            @Override
            public void onActivityResult(Uri result) {

                int x=random.nextInt(20);
                int y=random.nextInt(20);
                int z=random.nextInt(20);


                StorageReference storageReference=  firebaseStorage.getReference().child("audio").child("12").child(Integer.toString(x)+Integer.toString(y)+Integer.toString(z));

                storageReference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                Toast.makeText(MainActivity.this,"Audio uploaded",Toast.LENGTH_SHORT).show();
                                firebaseDatabase.getReference().child("Chats").child("12").push().setValue(uri.toString());
                                firebaseDatabase.getReference().child("Chats").child("21").push().setValue(uri.toString());

                                firebaseDatabase.getReference().child("Chats").child("12").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        list.clear();
                                        for (DataSnapshot dataSnapshot:snapshot.getChildren())
                                        {
                                            String message=dataSnapshot.getValue(String.class);

                                            list.add(message);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(MainActivity.this,"Cannot load audio, Try again later",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });



            }
        });





        insert_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launcher.launch("image/*");


            }
        });

         send_audio.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 launcher_audio.launch("audio/*");
             }
         });

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    private void getChats()
    {

       firebaseDatabase.getReference().child("Chats").child("12").addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               for (DataSnapshot datasnapshot:snapshot.getChildren()
                    ) {
                   String message=datasnapshot.getValue(String.class);
                   list.add(message);
               }

               progressDialog.dismiss();
                myAdapter=new MyAdapter(MainActivity.this,list,1);
             recyclerView.setAdapter(myAdapter);
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(MainActivity.this,"Cancelled",Toast.LENGTH_SHORT).show();
           }
       });


    }
    // ItemTouchHelper

    ItemTouchHelper.SimpleCallback callback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position=viewHolder.getAdapterPosition();
            String message=list.get(position);
            myAdapter.removeItem(position);
            Snackbar snackbar=Snackbar.make(scrollView,"Message Deleted",Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myAdapter.restorItem(message,position);
                    recyclerView.scrollToPosition(position);
                }
            }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if(!(event==DISMISS_EVENT_ACTION))
                    {
                        list.clear();
                     firebaseDatabase.getReference().child("Chats").child("12").addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {

                             for (DataSnapshot datasnapshot:snapshot.getChildren()
                                  ) {

                                 String todelete=datasnapshot.getValue(String.class);
                                 if(message.equals(todelete))
                                     datasnapshot.getRef().setValue(null);
                             }

                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });
                    }
                }
            });
            snackbar.setActionTextColor(Color.RED);
            snackbar.show();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId()==R.id.Clear_chat)
        {

            new AlertDialog.Builder(this).setTitle("Clear chats").setMessage("Are you sure youwant to clear all chats?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            list.clear();
                            myAdapter.notifyDataSetChanged();
                            firebaseDatabase.getReference().child("Chats").child("12").setValue(null);

                            Toast.makeText(MainActivity.this,"All chats cleared",Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("No",null).setIcon(R.drawable.ic_baseline_delete_forever_24).show();


        }

        return true;
    }

    private void get_profile()
    {
        firebaseDatabase.getReference().child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String imgurl=snapshot.getValue(String.class);
                Picasso.get().load(imgurl).into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}