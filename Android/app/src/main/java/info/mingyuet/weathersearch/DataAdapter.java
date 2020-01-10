package info.mingyuet.weathersearch;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
    private List<String> paths;
    private Context context;
    int width;

    public DataAdapter(Context context, List<String> paths, int width) {
        this.context = context;
        this.paths = paths;
        this.width = width;

    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        // Show progress bar
//        ProgressBar progressBar = (ProgressBar) viewHolder.
//        progressBar.setVisibility(View.VISIBLE);
// Hide progress bar on successful load
//        final int index = i;
//        Picasso.get().load(paths.get(i)).resize(width, 0)
//                .into(viewHolder.img_android, new com.squareup.picasso.Callback() {
//                    @Override
//                    public void onSuccess() {
////                        if (progressBar != null) {
////                            progressBar.setVisibility(View.GONE);
////                        }
//                        Log.d("res", "download " + paths.get(index));
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//
//                    }
//                });
        Glide.with(viewHolder.img_android.getContext())
                .load(paths.get(i))
                .into(viewHolder.img_android);

//        Picasso.get().load(paths.get(i)).fit().into(viewHolder.img_android);
    }

    @Override
    public int getItemCount() {
        return paths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_android;
        CardView cardView;
        public ViewHolder(View view) {
            super(view);
            img_android = (ImageView)view.findViewById(R.id.img_android);
            cardView = (CardView) view.findViewById(R.id.img_card);
        }
    }
}