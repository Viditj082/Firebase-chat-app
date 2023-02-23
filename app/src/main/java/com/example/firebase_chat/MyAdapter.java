package com.example.firebase_chat;



import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<String> list;
    private int activity_no;
    public MyAdapter(Context context, ArrayList<String> nlist,int no)
    {
        this.context = context;
        this.list=nlist;
        this.activity_no=no;
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).startsWith("1")&&activity_no==2||list.get(position).startsWith("2")&&activity_no==1)
            return  1;


        else if(list.get(position).contains("https://firebasestorage")&&list.get(position).contains("images")
            )
            return 3;
        else if(list.get(position).startsWith("1")&&activity_no==1||list.get(position).startsWith("2")&&activity_no==2) return  2;
        else return 4;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view;
        if (viewType==1) {
            view = layoutInflater.inflate(R.layout.messages_layout, parent, false);
            return  new ViewHolderone(view);
        }

        else if(viewType==2){
            view = layoutInflater.inflate(R.layout.sende_bg, parent, false);
            return  new ViewHoldertwo(view);
        }


else if(viewType==3)
        {
            view = layoutInflater.inflate(R.layout.image_sender, parent, false);
            return  new ViewHolderthree(view);
        }
        else
        {
            view = layoutInflater.inflate(R.layout.audio_layout, parent, false);
            return  new ViewHolderfour(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(list.get(position).startsWith("1")&&activity_no==2||list.get(position).startsWith("2")&&activity_no==1)
        {
            ViewHolderone viewHolderone=(ViewHolderone) holder;
            viewHolderone.message.setText(list.get(position).substring(3));
        }
        else if(list.get(position).contains("https://firebasestorage")&&list.get(position).contains("images"))
        {
            String imgurl=list.get(position);
            ViewHolderthree viewHolderthree=(ViewHolderthree) holder;
            Picasso.get().load(imgurl).into(viewHolderthree.image);

            viewHolderthree.image.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    new AlertDialog.Builder(context).setTitle("Download image").setMessage("Do you want to download the image?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    download_image("Your Image",list.get(position));

                                    Toast.makeText(context,"Downloading image",Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("No",null).setIcon(R.drawable.ic_baseline_check_circle_outline_24).show();


                    return true;
                }
            });


            viewHolderthree.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog=new Dialog(context);

                    dialog.setContentView(R.layout.maximize_image);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

                    dialog.show();

                    ImageView imageView=dialog.findViewById(R.id.image_opened);

                    AppCompatButton save=dialog.findViewById(R.id.save_image);

                    Picasso.get().load(imgurl).into(imageView);

                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new AlertDialog.Builder(context).setTitle("Download image").setMessage("Do you want to download the image?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            download_image("Your Image",list.get(position));

                                            Toast.makeText(context,"Downloading image",Toast.LENGTH_SHORT).show();
                                        }
                                    }).setNegativeButton("No",null).setIcon(R.drawable.ic_baseline_check_circle_outline_24).show();

                        }
                    });
                }
            });
        }

        else if(list.get(position).startsWith("1")&&activity_no==1||list.get(position).startsWith("2")&&activity_no==2)
        {

            ViewHoldertwo viewHoldertwo=(ViewHoldertwo) holder;
            viewHoldertwo.message_sent.setText(list.get(position).substring(3));
        }
        else
        {
            ViewHolderfour viewHolderfour=(ViewHolderfour) holder;

            viewHolderfour.play_pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC
                    );

                    if (!mediaPlayer.isPlaying()) {
                        String url = list.get(position);

                        Uri imguri = Uri.parse(url);

                    Toast.makeText(context,"Playing audio",Toast.LENGTH_SHORT).show();

                        try {

                            mediaPlayer.setDataSource(context, imguri);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }

            });
        }

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

     // Viewholders

    public   class ViewHolderone extends  RecyclerView.ViewHolder{

        private TextView message;
        public ViewHolderone(@NonNull View itemView) {
            super(itemView);
          message=itemView.findViewById(R.id.message);

        }
    }

    public   class ViewHoldertwo extends  RecyclerView.ViewHolder{

        private TextView message_sent;
        public ViewHoldertwo(@NonNull View itemView) {
            super(itemView);
            message_sent=itemView.findViewById(R.id.message_sent);

        }
    }
    public   class ViewHolderthree extends  RecyclerView.ViewHolder{

        private ImageView image;
        public ViewHolderthree(@NonNull View itemView) {
            super(itemView);
           image=itemView.findViewById(R.id.pic_sent);

        }
    }

    public   class ViewHolderfour extends  RecyclerView.ViewHolder{

        private ImageView play_pause;
        private TextView audio_status;
        public ViewHolderfour(@NonNull View itemView) {
            super(itemView);
            play_pause=itemView.findViewById(R.id.audio_sent);

        }
    }

//
    public void removeItem(int position)
    {
        list.remove(position);
        notifyItemRemoved(position);
    }
    public void restorItem(String item,int position)
    {
        list.add(position,item);
        notifyItemInserted(position);
    }


    public void download_image(String filename,String imageurl)
    {
        try{

            DownloadManager downloadManager=null;

            downloadManager= (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloaduri=Uri.parse(imageurl);

            DownloadManager.Request request=new DownloadManager.Request(downloaduri);

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI|
                    DownloadManager.Request.NETWORK_MOBILE).setAllowedOverRoaming(false).setTitle(filename).setMimeType("image/jpeg")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED).setDestinationInExternalFilesDir(context,Environment.DIRECTORY_PICTURES
            ,File.separator+filename+".jpg");

            downloadManager.enqueue(request);

        }
catch (Exception e)
{
    e.printStackTrace();
}

}

}
