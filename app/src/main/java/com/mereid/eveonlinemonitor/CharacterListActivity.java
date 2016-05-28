package com.mereid.eveonlinemonitor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import javax.xml.parsers.*;


import com.mereid.eveonlinemonitor.dummy.DummyContent;
import com.mereid.eveonlinemonitor.dummy.SolarSystemDetailFragment;
import com.mereid.eveonlinemonitor.dummy.SolarSystemListActivity;
import com.mereid.eveonlinemonitor.dummy.dummy.KillContent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * An activity representing a list of Characters. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CharacterDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class CharacterListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);
        KillContent killContent = new KillContent();
        killContent.Init(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }});
        Button button = (Button) findViewById(R.id.pvp_locator);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, SolarSystemListActivity.class);
                context.startActivity(intent);
            }
        });

        View recyclerView = findViewById(R.id.character_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.character_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        int secondsWaited = 60;
        // Add error message or timer here
        while (DummyContent.initialized == 0)
        {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (secondsWaited == 0)
                break;
            secondsWaited--;
        }
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.character_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mIdView.setTextColor(Color.WHITE);
            holder.mContentView.setText(mValues.get(position).content);
            holder.mContentView.setTextColor(Color.WHITE);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(CharacterDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        CharacterDetailFragment fragment = new CharacterDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.character_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, CharacterDetailActivity.class);
                        intent.putExtra(CharacterDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }

//    private class LongOperation extends AsyncTask<String, Void, String> {
//
//
//
//        @Override
//        protected String doInBackground(String... params)  {
//            String myurl = "https://api.eveonline.com//account/Characters.xml.aspx?keyID=5332468&vCode=ukapJVh2jLrWgKkuHnHO7Db9Lz4rbZx0AjzI7G1IOQlcx8hQiAD15rhHezrfj4xR";
//            URL url = null;
//            int response = -1;
//            try {
//                url = new URL(myurl);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setReadTimeout(10000 /* milliseconds */);
//                conn.setConnectTimeout(15000 /* milliseconds */);
//                try {
//                    conn.setRequestMethod("GET");
//                } catch (ProtocolException e) {
//                    e.printStackTrace();
//                }
//                conn.setDoInput(true);
//                // Starts the query
//                conn.connect();
//                response = conn.getResponseCode();
//                if(response == 200){
//                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//
//                    // use the factory to create a documentbuilder
//                    DocumentBuilder builder = null;
//                    InputStream content = conn.getInputStream();
//                    try {
//                        builder = factory.newDocumentBuilder();
//                        try {
//                            Document doc = builder.parse(content);
//                            // get the first element
//                            Element element = doc.getDocumentElement();
//                            NodeList list = element.getElementsByTagName("row");
//                            for (int i = 0; i<list.getLength(); i++) {
//                                Element character = (Element) list.item(i);
//                                String name = character.getAttribute("name");
//                                String characterId = character.getAttribute("characterID");
//                                String corpName = character.getAttribute("corporationName");
//                            }
//
//                        } catch (SAXException e) {
//                            e.printStackTrace();
//                        }
//                    } catch (ParserConfigurationException e) {
//                        e.printStackTrace();
//                    }
////                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
////                    String line;
////                    while((line = reader.readLine()) != null){
////                        builder.append(line);
////                    }
//                } else {
//                }
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return "Executed";
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//
//        }
//
//        @Override
//        protected void onPreExecute() {}
//
//        @Override
//        protected void onProgressUpdate(Void... values) {}
//    }

}
