package project.bzu.csc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import project.bzu.csc.Activities.ViewSearchPost;
import project.bzu.csc.Models.Post;
import project.bzu.csc.R;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>  {
    LayoutInflater inflater;
    List<Post> posts;
    Context context;
    public SearchResultsAdapter(Context context, List<Post> posts)  {
        this.inflater= LayoutInflater.from(context);
        this.posts = posts;
        this.context=context;

    }
//    private String calculateTimeAgo(String times) {
//        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
//
//        try {
//            long time = sdf.parse(times).getTime();
//            Log.d("TAG", "calculateTime: "+time);
//            long now = System.currentTimeMillis();
//            Log.d("TAG", "calculateNow: "+now);
//            CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
//
//            return ago+ "";
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

    @NonNull
    @Override
    public SearchResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.search_results_card_layout,parent,false);
        return new SearchResultsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(posts.get(position).getPostType().equals("Question")){
            holder.postType.setText("Q");}
        else if(posts.get(position).getPostType().equals("Topic")){
            holder.postType.setText("T");}

        holder.postTitle.setText(posts.get(position).getPostTitle());
        holder.postBody.setText(posts.get(position).getPostBody());
        String tagsString=posts.get(position).getPostTags();
        String[] tagsArray=tagsString.split(",");
        Log.d("TAG", "onBindViewHolder: "+ Arrays.toString(tagsArray));
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

        }

        holder.postTime.setText((posts.get(position).getPostTime()));
        //holder.postTime.setText("hello");


        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context, ViewSearchPost.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("postIDFromSearch",posts.get(position).getPostID());
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView postTime,postType,postTitle,postBody,tag1,tag2,tag3,tag4,tag5;
        ConstraintLayout tags;
        CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postTime=itemView.findViewById(R.id.post_time);
            postType=itemView.findViewById(R.id.postType);
            postTitle=itemView.findViewById(R.id.post_Title);
            postBody=itemView.findViewById(R.id.post_content);

            tag1=itemView.findViewById(R.id.tag1);
            tag2=itemView.findViewById(R.id.tag2);
            tag3=itemView.findViewById(R.id.tag3);
            tag4=itemView.findViewById(R.id.tag4);
            tag5=itemView.findViewById(R.id.tag5);
            tags=itemView.findViewById(R.id.tags);

            cardView = itemView.findViewById(R.id.card);

        }
    }
}
