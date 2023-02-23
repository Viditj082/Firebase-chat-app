package com.example.firebase_chat;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity {


    private ImageView send2,insert_image2,send_audio2;
    private TextView sendTv2;
    private ArrayList<String> list;
    private RecyclerView recyclerView2;
    private FirebaseDatabase firebaseDatabase;
    private ActivityResultLauncher<String> launcher;
    private FirebaseStorage firebaseStorage;
    private Random random;
    private    MyAdapter myAdapter;
    private ScrollView scrollView2;
    private  ActivityResultLauncher<String> launcher_audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        send_audio2=findViewById(R.id.send_audio2);
        insert_image2=findViewById(R.id.insert_image2);
        send2=findViewById(R.id.send2);
        sendTv2=findViewById(R.id.sendtView2);
        recyclerView2=findViewById(R.id.recyclerView2);
scrollView2=findViewById(R.id.scrollView2);
        list=new ArrayList<>();

        random=new Random();
        firebaseStorage=FirebaseStorage.getInstance("gs://fir-chat-a189a.appspot.com");
        firebaseDatabase=FirebaseDatabase.getInstance("https://fir-chat-a189a-default-rtdb.firebaseio.com/");

        getChats();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView2.setLayoutManager(linearLayoutManager);





        send2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!sendTv2.getText().toString().isEmpty())
                {
                    list.clear();
                    firebaseDatabase.getReference().child("Chats").child("21").push().setValue("2. "+sendTv2.getText()
                            .toString());
                    firebaseDatabase.getReference().child("Chats").child("12").push().setValue("2. "+sendTv2.getText()
                            .toString());
                    sendTv2.setText("");
                }

            }
        });

        launcher=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                int x=random.nextInt(20);
                int y=random.nextInt(20);
                int z=random.nextInt(20);


                StorageReference storageReference=  firebaseStorage.getReference().child("images").child("21").child(Integer.toString(x)+Integer.toString(y)+Integer.toString(z));

                storageReference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                Toast.makeText(MainActivity2.this,"Image uploaded",Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(MainActivity2.this,"Cannot load image, Try again later",Toast.LENGTH_SHORT).show();
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


                StorageReference storageReference=  firebaseStorage.getReference().child("audio").child("21").child(Integer.toString(x)+Integer.toString(y)+Integer.toString(z));

                storageReference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {


                                Toast.makeText(MainActivity2.this,"Audio uploaded",Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(MainActivity2.this,"Cannot load audio, Try again later",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                });



            }
        });



        send_audio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher_audio.launch("audio/*");
            }
        });

        insert_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launcher.launch("image/*");


            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView2);

    }

    private void getChats()
    {
        ArrayList<String>chats=new ArrayList<>();

        firebaseDatabase.getReference().child("Chats").child("21").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot datasnapshot:snapshot.getChildren()
                ) {
                    String message=datasnapshot.getValue(String.class);
                    list.add(message);

                }
                if(list.isEmpty())
                    Toast.makeText(MainActivity2.this,"No chats to show",Toast.LENGTH_SHORT).show();
                 myAdapter=new MyAdapter(MainActivity2.this,list,2);
                recyclerView2.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity2.this,"Cancelled",Toast.LENGTH_SHORT).show();
            }
        });


    }
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
            Snackbar snackbar=Snackbar.make(scrollView2,"Message Deleted",Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myAdapter.restorItem(message,position);
                    recyclerView2.scrollToPosition(position);
                }
            }).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    if(!(event==DISMISS_EVENT_ACTION))
                    {
                        list.clear();
                        firebaseDatabase.getReference().child("Chats").child("21").addValueEventListener(new ValueEventListener() {
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
                            firebaseDatabase.getReference().child("Chats").child("21").setValue(null);

                            Toast.makeText(MainActivity2.this,"All chats cleared",Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("No",null).setIcon(R.drawable.ic_baseline_delete_forever_24).show();
        }
        return true;
    }
}