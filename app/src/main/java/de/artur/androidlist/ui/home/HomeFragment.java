package de.artur.androidlist.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.artur.androidlist.R;

public class HomeFragment extends Fragment {
    private List<Item> items;
    private MyAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final RecyclerView list = root.findViewById(R.id.list);

        items = new ArrayList<>();
        adapter = new MyAdapter(items);
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getActivity()));

        Picasso.get().setIndicatorsEnabled(true);

        ImageButton btn = root.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });

        getData();

        return root;
    }

    void getData(){
        items.clear();
        adapter.notifyDataSetChanged();

        String url = "https://randomuser.me/api/?results=1000&nat=de&noinfo";

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res = new JSONObject(response);
                            JSONArray results = res.getJSONArray("results");
                            for(int i = 0; i < results.length(); i++){
                                JSONObject item = results.getJSONObject(i);
                                JSONObject name = item.getJSONObject("name");
                                items.add(new Item(
                                        name.getString("first") + " " + name.getString("last"),
                                        item.getJSONObject("login").getString("username"),
                                        item.getJSONObject("location").getString("city"),
                                        item.getJSONObject("picture").getString("large")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());
            }
        });
        queue.add(stringRequest);

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
        List<Item> items;


        MyAdapter(List<Item> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int i) {
            holder.name.setText(items.get(i).getName());
            holder.username.setText(items.get(i).getUsername());
            holder.address.setText(items.get(i).getAddress());
            Picasso.get().load(items.get(i).getImageSrc())
                    .placeholder(R.drawable.ic_face_black_24dp)
                    .error(R.drawable.ic_error_black_24dp)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.picture);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView name, username, address;
            ImageView picture;


            ViewHolder(@NonNull View itemView) {
                super(itemView);
                name = itemView.findViewById(R.id.name);
                username = itemView.findViewById(R.id.username);
                address = itemView.findViewById(R.id.address);
                picture = itemView.findViewById(R.id.picture);
            }
        }
    }

    private class Item{
        String name, username, address, imageSrc;

        public String getName() {
            return name;
        }

        public String getUsername() {
            return username;
        }

        public String getAddress() {
            return address;
        }

        public String getImageSrc() {
            return imageSrc;
        }

        public Item(String name, String username, String address, String imageSrc) {
            this.name = name;
            this.username = username;
            this.address = address;
            this.imageSrc = imageSrc;
        }
    }
}