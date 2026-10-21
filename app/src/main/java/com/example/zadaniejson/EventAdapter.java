package com.example.zadaniejson;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {
    Context context;
    List<Event> events = new ArrayList<>();

    public EventAdapter(Context context) {
        this.context = context;
    }
    public void setEvents(List<Event> events){
        this.events = events;
        notifyDataSetChanged();
    }

    public static class EventHolder extends RecyclerView.ViewHolder {
        ImageView image;
//        Calendar date;
        TextView title, date, description, category;
        EventHolder(LinearLayout layout, ImageView image, TextView title, TextView date, TextView description, TextView category){
            super(layout);
            this.image = image;
            this.title = title;
            this.date = date;
            this.description = description;
            this.category = category;

        }

    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LinearLayout layout = new LinearLayout(context);
        layout.setPadding(20, 20, 20, 20);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView image = new ImageView(context);
        image.setLayoutParams(new LinearLayout.LayoutParams(250, 250));

        LinearLayout texts = new LinearLayout(context);
        texts.setOrientation(LinearLayout.VERTICAL);
        texts.setPadding(20, 0, 0, 0);

        TextView title = new TextView(context);
        TextView date = new TextView(context);
        TextView description = new TextView(context);
        TextView category = new TextView(context);

        title.setTextSize(18);
        title.setTypeface(null, Typeface.BOLD);

        texts.addView(title);
        texts.addView(date);
        texts.addView(description);
        texts.addView(category);

        layout.addView(image);
        layout.addView(texts);

        return new EventHolder(layout, image, title, date, description, category);
    }
    @Override
    public void onBindViewHolder(EventHolder holder, final int position) {
        Event event = events.get(position);
        holder.title.setText(event.getTitle());
        Calendar date = event.getDate();
        if(date != null){
            holder.date.setText(
                    new java.text.SimpleDateFormat("dd.MM.yyyy",
                            java.util.Locale.getDefault())
                            .format(date.getTime()));
        }
        holder.description.setText(event.getDescription());
        holder.category.setText(event.getCategory());

        Glide.with(context)
                .load(event.getImageUrl())
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }
}