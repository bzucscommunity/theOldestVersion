package project.bzu.csc.Adapters;


import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import project.bzu.csc.Activities.ViewPostInHome;
import project.bzu.csc.Models.Post;
import project.bzu.csc.Models.User;
import project.bzu.csc.R;


public class GetAllPostsAdapter extends RecyclerView.Adapter<GetAllPostsAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<Post> posts;
    List<User> users;
    Context context;
    String[] imagesArray;

    public GetAllPostsAdapter(Context context, List<Post> posts){
        this.inflater=LayoutInflater.from(context);
        this.posts = posts;
        this.context=context;



    }
    private String calculateTimeAgo(String times) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        try {
            long time = sdf.parse(times).getTime();
            Log.d("TAG", "calculateTime: "+time);
            long now = System.currentTimeMillis();
            Log.d("TAG", "calculateNow: "+now);
            CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);

            return ago+ "";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.post_card_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.userName.setText(posts.get(position).getUser().getUserName());

        if(posts.get(position).getPostType().equals("Question")){
            holder.postType.setText("Q");}
        else if(posts.get(position).getPostType().equals("Topic")){

            holder.postType.setText("T");}

        holder.postTitle.setText(posts.get(position).getPostTitle());
        holder.postContent.setText(posts.get(position).getPostBody());
        String tagsString=posts.get(position).getPostTags();
        String[] tagsArray=tagsString.split(",");
        Log.d("TAG", "onBindViewHolder: "+Arrays.toString(tagsArray));
        if(tagsArray.length==1){
            holder.tag1.setText(tagsArray[0]);
            holder.tag1.setVisibility(View.VISIBLE);
            holder.tags.setVisibility(View.VISIBLE);
        }else if(tagsArray.length==2){
            holder.tag1.setText(tagsArray[0]);
            holder.tag2.setText(tagsArray[1]);
            holder.tag1.setVisibility(View.VISIBLE);
            holder.tag2.setVisibility(View.VISIBLE);
            holder.tags.setVisibility(View.VISIBLE);
        }else if(tagsArray.length==3){
            holder.tag1.setText(tagsArray[0]);
            holder.tag2.setText(tagsArray[1]);
            holder.tag3.setText(tagsArray[2]);
            holder.tag1.setVisibility(View.VISIBLE);
            holder.tag2.setVisibility(View.VISIBLE);
            holder.tag3.setVisibility(View.VISIBLE);
            holder.tags.setVisibility(View.VISIBLE);
        }else if(tagsArray.length==4){
            holder.tag1.setText(tagsArray[0]);
            holder.tag2.setText(tagsArray[1]);
            holder.tag3.setText(tagsArray[2]);
            holder.tag4.setText(tagsArray[3]);
            holder.tag1.setVisibility(View.VISIBLE);
            holder.tag2.setVisibility(View.VISIBLE);
            holder.tag3.setVisibility(View.VISIBLE);
            holder.tag4.setVisibility(View.VISIBLE);
            holder.tags.setVisibility(View.VISIBLE);

        }else if(tagsArray.length==5) {
            holder.tag1.setText(tagsArray[0]);
            holder.tag2.setText(tagsArray[1]);
            holder.tag3.setText(tagsArray[2]);
            holder.tag4.setText(tagsArray[3]);
            holder.tag5.setText(tagsArray[4]);
            holder.tag1.setVisibility(View.VISIBLE);
            holder.tag2.setVisibility(View.VISIBLE);
            holder.tag3.setVisibility(View.VISIBLE);
            holder.tag4.setVisibility(View.VISIBLE);
            holder.tag5.setVisibility(View.VISIBLE);
            holder.tags.setVisibility(View.VISIBLE);

        }else {
            holder.tags.setVisibility(View.GONE);
        }
        String imagesString=posts.get(position).getPostAttachment();
        //Log.d("TAG", "onBindViewHolder: YES"+ imagesString);


        Log.d("TAG", "onBindViewHolder: "+imagesString.length());
        if(imagesString.length() >5){
            Log.d("TAG", "onBindViewHolder: hi");
             imagesArray=imagesString.split(",");
            //Log.d("TAG", "onBindViewHolder:4 "+ imagesString);

            //Log.d("TAG", "onBindViewHolder2: "+ Arrays.toString(imagesArray));
            //Log.d("TAG", "onBindViewHolder3: "+imagesArray.length);
            if(imagesArray.length==1){
                Picasso.get().load(imagesArray[0]).into(holder.image1);
                holder.imagesPreviews.setVisibility(View.VISIBLE);
                holder.image1.setVisibility(View.VISIBLE);
             }else if(imagesArray.length==2){
                Picasso.get().load(imagesArray[0]).into(holder.image1);
                Picasso.get().load(imagesArray[1]).into(holder.image2);
                holder.imagesPreviews.setVisibility(View.VISIBLE);
                holder.image1.setVisibility(View.VISIBLE);
                holder.image2.setVisibility(View.VISIBLE);
            }else if(imagesArray.length==3){
                Picasso.get().load(imagesArray[0]).into(holder.image1);
                Picasso.get().load(imagesArray[1]).into(holder.image2);
                Picasso.get().load(imagesArray[2]).into(holder.image3);
                holder.imagesPreviews.setVisibility(View.VISIBLE);
                holder.image1.setVisibility(View.VISIBLE);
                holder.image2.setVisibility(View.VISIBLE);
                holder.image3.setVisibility(View.VISIBLE);
            }else if(imagesArray.length==4){
                Picasso.get().load(imagesArray[0]).into(holder.image1);
                Picasso.get().load(imagesArray[1]).into(holder.image2);
                Picasso.get().load(imagesArray[2]).into(holder.image3);
                Picasso.get().load(imagesArray[3]).into(holder.image4);
                holder.imagesPreviews.setVisibility(View.VISIBLE);
                holder.image1.setVisibility(View.VISIBLE);
                holder.image2.setVisibility(View.VISIBLE);
                holder.image3.setVisibility(View.VISIBLE);
                holder.image4.setVisibility(View.VISIBLE);

            }else if(imagesArray.length==5) {
                Picasso.get().load(imagesArray[0]).into(holder.image1);
                Picasso.get().load(imagesArray[1]).into(holder.image2);
                Picasso.get().load(imagesArray[2]).into(holder.image3);
                Picasso.get().load(imagesArray[3]).into(holder.image4);
                Picasso.get().load(imagesArray[4]).into(holder.image5);
                holder.imagesPreviews.setVisibility(View.VISIBLE);
                holder.image1.setVisibility(View.VISIBLE);
                holder.image2.setVisibility(View.VISIBLE);
                holder.image3.setVisibility(View.VISIBLE);
                holder.image4.setVisibility(View.VISIBLE);
                holder.image5.setVisibility(View.VISIBLE);
            }
        }else {
            Log.d("TAG", "onBindViewHolder: hi2");
            holder.imagesPreviews.setVisibility(View.GONE);
            holder.image1.setVisibility(View.GONE);
            holder.image2.setVisibility(View.GONE);
            holder.image3.setVisibility(View.GONE);
            holder.image4.setVisibility(View.GONE);
            holder.image5.setVisibility(View.GONE);
        }
        String flag = posts.get(position).getPostTitle();
        if(flag .equalsIgnoreCase("Dijkstra’s Shortest Path Algorithm")||flag.equalsIgnoreCase("Data structures")){
            Picasso.get().load("https://cdn.icon-icons.com/icons2/792/PNG/512/YOUTUBE_icon-icons.com_65537.png").into(holder.image2);
            holder.imagesPreviews.setVisibility(View.VISIBLE);
            holder.image2.setVisibility(View.VISIBLE);}
//        String videosString=posts.get(position).getPostTags();
//        String[] videosArray=videosString.split(",");
//        if(videosArray.length==1){
//            Picasso.get().load(videosArray[0]).into((Target) holder.video1);
//            Picasso.get().load(imagesArray[1]).into(holder.image2);
//            holder.tag1.setVisibility(View.VISIBLE);
//            holder.tags.setVisibility(View.VISIBLE);
//        }else if(videosArray.length==2){
//            Picasso.get().load(videosArray[0]).into( holder.video1);
//            Picasso.get().load(videosArray[1]).into( holder.video2);
//            holder.video1.setVisibility(View.VISIBLE);
//            holder.video2.setVisibility(View.VISIBLE);
//          //  holder.tags.setVisibility(View.VISIBLE);
//        }
        holder.postTime.setText(calculateTimeAgo(posts.get(position).getPostTime()));
        //holder.postTime.setText("hello");

        Picasso.get().load(posts.get(position).getUser().getUserImage()).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context, ViewPostInHome.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("postID",posts.get(position).getPostID());
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView userName,postTime,postType,postTitle,postContent,tag1,tag2,tag3,tag4,tag5,postViews,postComments,postShares;
        ImageView postMoreMenu,image1,image2,image3,image4,image5,image;
        //  CircleImageView image;


        VideoView video1,video2;
        ConstraintLayout tags,imagesPreviews,videosPreviews;
        CardView cardView;
        ImageButton favorite;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName=itemView.findViewById(R.id.userName);
            postTime=itemView.findViewById(R.id.post_time);
            postType=itemView.findViewById(R.id.postType);
            postTitle=itemView.findViewById(R.id.post_Title);
            postContent=itemView.findViewById(R.id.post_content);
            favorite = itemView.findViewById(R.id.fav);
            tag1=itemView.findViewById(R.id.tag1);
            tag2=itemView.findViewById(R.id.tag2);
            tag3=itemView.findViewById(R.id.tag3);
            tag4=itemView.findViewById(R.id.tag4);
            tag5=itemView.findViewById(R.id.tag5);


            image =  itemView.findViewById(R.id.userImage);
            postMoreMenu=itemView.findViewById(R.id.post_more_menu);
            image1=itemView.findViewById(R.id.image_preview1);
            image2=itemView.findViewById(R.id.image_preview2);
            image3=itemView.findViewById(R.id.image_preview3);
            image4=itemView.findViewById(R.id.image_preview4);
            image5=itemView.findViewById(R.id.image_preview5);
            video1=itemView.findViewById(R.id.video_preview1);
            video2=itemView.findViewById(R.id.video_preview2);
            tags=itemView.findViewById(R.id.tags);
            imagesPreviews=itemView.findViewById(R.id.images_previews);
            videosPreviews=itemView.findViewById(R.id.videos_previews);
            cardView = itemView.findViewById(R.id.card);

        }
    }
}
