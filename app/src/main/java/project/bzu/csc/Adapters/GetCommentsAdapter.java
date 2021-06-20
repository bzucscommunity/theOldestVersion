package project.bzu.csc.Adapters;
import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

        import com.squareup.picasso.Picasso;

        import java.util.List;

        import de.hdodenhof.circleimageview.CircleImageView;
        import project.bzu.csc.Models.Comment;
        import project.bzu.csc.Models.Post;
        import project.bzu.csc.Models.User;
        import project.bzu.csc.R;

public class GetCommentsAdapter  extends RecyclerView.Adapter<GetCommentsAdapter.ViewHolder>{
    LayoutInflater inflater;
    List<Comment> comments;
    Context context;


    public GetCommentsAdapter(Context context, List<Comment> comments){
        this.inflater=LayoutInflater.from(context);
        this.comments = comments;
        this.context=context;




    }
    @NonNull
    @Override
    public GetCommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.comment_layout,parent,false);
        return new GetCommentsAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull GetCommentsAdapter.ViewHolder holder, int position) {
        holder.usernameText.setText(comments.get(position).getUserName());
        holder.bodyComment.setText(comments.get(position).getBody());
        Picasso.get().load(comments.get(position).getUserImage()).into(holder.ImageViewComment);
    }

    @Override
    public int getItemCount() {

        return comments.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView usernameText;
        CircleImageView ImageViewComment;
        TextView bodyComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText=itemView.findViewById(R.id.UserName);
            ImageViewComment= itemView.findViewById(R.id.userImage);
            bodyComment= itemView.findViewById(R.id.body);

        }
    }
}