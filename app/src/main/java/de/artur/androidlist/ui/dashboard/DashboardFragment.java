package de.artur.androidlist.ui.dashboard;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.artur.androidlist.R;
import de.artur.androidlist.ui.home.HomeFragment;

public class DashboardFragment extends Fragment {
    private ArrayList<Integer> images;
    private MyAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        RecyclerView list = root.findViewById(R.id.list2);
        images = new ArrayList<>();
        adapter = new MyAdapter(images);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        Picasso.get().setIndicatorsEnabled(true);

        ImageButton btn = root.findViewById(R.id.button2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadCats();
            }
        });

        loadCats();
        return root;
    }

    private void loadCats() {
        images.clear();
        adapter.notifyDataSetChanged();

        images.add(R.drawable.cat0);
        images.add(R.drawable.cat1);
        images.add(R.drawable.cat2);
        images.add(R.drawable.cat3);
        images.add(R.drawable.cat4);
        images.add(R.drawable.cat5);
        images.add(R.drawable.cat6);
        images.add(R.drawable.cat7);
        images.add(R.drawable.cat8);
        images.add(R.drawable.cat9);

        Collections.shuffle(images);

        adapter.notifyDataSetChanged();
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        List<Integer> items;

        MyAdapter(List<Integer> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_image_item, parent, false);
            return new MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int i) {

            Picasso.get()
                    .load(items.get(i))
                    .placeholder(R.drawable.ic_face_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .resize(200, 200)
                    .centerCrop()
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.picture);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView picture;


            ViewHolder(@NonNull View itemView) {
                super(itemView);

                picture = itemView.findViewById(R.id.imageView);
            }
        }
    }
}