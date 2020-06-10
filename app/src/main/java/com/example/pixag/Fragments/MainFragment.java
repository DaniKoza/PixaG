package com.example.pixag.Fragments;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pixag.Activities.MainActivity;
import com.example.pixag.Adapters.ImageAdapterRecycleView;
import com.example.pixag.CustomListeners.RecyclerItemClickListener;
import com.example.pixag.Interfaces.PixabayService;
import com.example.pixag.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainFragment extends Fragment {

    private PixabayService pixabayService;
    private HashMap<Integer, HashMap<String, String>> imagesData;


    private RecyclerView recyclerView;
    private ImageAdapterRecycleView imageAdapterRecycleView;
    private Context mContext;
    private Button BTN_search;
    private Button BTN_upload;
    private final int INITIAL_CAPACITY = 200;
    private EditText searchBar;
    private boolean dirty = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View v = inflater.inflate(R.layout.main_fragment, container, false);
        mContext = container.getContext();

        searchBar = v.findViewById(R.id.searchBar);
        recyclerView = v.findViewById(R.id.recycleView);
        BTN_search = v.findViewById(R.id.BTN_search);
        BTN_upload = v.findViewById(R.id.BTN_upload);

        imagesData = new HashMap<>(INITIAL_CAPACITY);

        imageAdapterRecycleView = new ImageAdapterRecycleView(mContext, imagesData);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));

        RequestQueue queue = Volley.newRequestQueue(mContext);
        sendRequestFromApi(queue, pixabayService.BASE_URL);


        BTN_search.setOnClickListener(v1 -> {
            sendRequestFromApi(queue, pixabayService.BASE_URL + searchBar.getText().toString());
            searchBar.setText("");
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            View view = getActivity().getCurrentFocus();
            if (imm != null && view != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        BTN_upload.setOnClickListener(v12 -> {
            MainActivity ma = (MainActivity) getActivity();
            ma.goToUserUploadActivity();
        });


        return v;
    }

    private void sendRequestFromApi(RequestQueue queue, String baseUrl) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, baseUrl, null,
                response -> {
                    try {
                        getJsonResponseToHashMap(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show());

        queue.add(jsonObjectRequest);
    }

    private void getJsonResponseToHashMap(JSONObject response) throws JSONException {
        JSONArray jsonArray = response.getJSONArray("hits");
        for (int i = 0; i < jsonArray.length(); i++) {
            imagesData.put(i, new HashMap<>());
            imagesData.get(i).put("ImageUrl", jsonArray.getJSONObject(i).getString("webformatURL"));
            imagesData.get(i).put("Likes", jsonArray.getJSONObject(i).getString("likes"));
            imagesData.get(i).put("Downloads", jsonArray.getJSONObject(i).getString("downloads"));
            imagesData.get(i).put("Views", jsonArray.getJSONObject(i).getString("views"));
            imagesData.get(i).put("Tags", jsonArray.getJSONObject(i).getString("tags"));
        }
        recyclerView.setAdapter(imageAdapterRecycleView);
        imageAdapterRecycleView.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MainActivity ma = (MainActivity) getActivity();
                        ma.goToImageDetailsFragment(imagesData.get(position), searchBar.getText().toString());
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
    }


    public interface onMainFragmentInteractionListener {
        // TODO: Update argument type and name
        void onGetMobileNumberFragmentInteraction(Uri uri);
    }




}
