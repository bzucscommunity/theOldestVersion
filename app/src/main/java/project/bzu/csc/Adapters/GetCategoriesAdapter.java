package project.bzu.csc.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import project.bzu.csc.Activities.QuestionCardView;
import project.bzu.csc.Activities.SearchCardView;
import project.bzu.csc.Models.Subject;
import project.bzu.csc.R;

public class GetCategoriesAdapter extends RecyclerView.Adapter<GetCategoriesAdapter.ViewHolder> {
    Context context;
    LayoutInflater inflater;
    List<Subject> subjects;


    public GetCategoriesAdapter(Context context, List<Subject> subjects){
        this.inflater=LayoutInflater.from(context);
        this.subjects = subjects;
        this.context=context;


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.categories_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {




        holder.subjectName.setText(subjects.get(position).getName());
      //  holder.Icon.setVisibility(View.VISIBLE);
        Picasso.get().load(subjects.get(position).getImageURL()).into(holder.subjectImage);

     //   holder.Icon.getDrawable(ic_baseline_keyboard_arrow_right_24);
//        Drawable drawable=holder.Icon.getDrawable();
      //  Picasso.get().load().into(holder.Icon);
       // holder.Icon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_keyboard_arrow_right_24));
        holder.cardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(context, SearchCardView.class);
                intent.putExtra("subjectNameFromSearch",subjects.get(position).getName());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView subjectName;
        ImageView subjectImage,Icon;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName=itemView.findViewById(R.id.categories_title);
            subjectImage=itemView.findViewById(R.id.categories_image);
            Icon=itemView.findViewById(R.id.Icon);
            cardView=itemView.findViewById(R.id.categories_card);

        }
    }
}




