package com.mereid.eveonlinemonitor;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mereid.eveonlinemonitor.dummy.DummyContent;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * A fragment representing a single Character detail screen.
 * This fragment is either contained in a {@link CharacterListActivity}
 * in two-pane mode (on tablets) or a {@link CharacterDetailActivity}
 * on handsets.
 */
public class CharacterDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CharacterDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String key = getArguments().getString(ARG_ITEM_ID);
            mItem = DummyContent.ITEM_MAP.get(key);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            ImageView charImage;
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.id);
                charImage = (ImageView) appBarLayout.findViewById(R.id.character_image);
                if (mItem.charBitmap == null) {
                    try {
                        new ImageData(charImage, key).execute(mItem.details, "1024").get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    charImage.setImageBitmap(mItem.charBitmap);
                }
            }

            String isk = "0.00";
            try {
                isk = new CharacterData().execute(mItem.details, "balance", Integer.toString(mItem.userDataIndex)).get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            mItem.SetIsk(isk);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.character_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            TextView rootViewTextView = (TextView) rootView.findViewById(R.id.character_detail);
            rootViewTextView.setText(mItem.content);
            rootViewTextView.setTextColor(Color.WHITE);

            TextView iskView = (TextView) rootView.findViewById(R.id.character_isk);
            iskView.setText(mItem.isk);
            iskView.setTextColor(Color.WHITE);
        }

        return rootView;
    }



    private class CharacterData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params)  {
            String httpValue = params[0];
            String attrValue = params[1];
            int index = Integer.parseInt(params[2]);
            String myurl = "https://api.eveonline.com//char/AccountBalance.xml.aspx?keyID="+ Constants.userData.get(index).second +"&vCode="+ Constants.userData.get(index).first + "&characterID="+httpValue;
            URL url = null;
            int response = -1;
            String value = "";
            try {
                url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                try {
                    conn.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                response = conn.getResponseCode();
                if(response == 200){
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

                    // use the factory to create a documentbuilder
                    DocumentBuilder builder = null;
                    InputStream content = conn.getInputStream();
                    try {
                        builder = factory.newDocumentBuilder();
                        try {
                            Document doc = builder.parse(content);
                            // get the first element
                            Element element = doc.getDocumentElement();
                            NodeList list = element.getElementsByTagName("row");
                            if (list.getLength() > 0) {
                                Element character = (Element) list.item(0);
                                value = character.getAttribute(attrValue);
                            }

                        } catch (SAXException e) {
                            e.printStackTrace();
                        }
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                    }
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
//                    String line;
//                    while((line = reader.readLine()) != null){
//                        builder.append(line);
//                    }
                } else {
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return value;
        }

        @Override
        protected void onPostExecute(String s)
        {

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class ImageData extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        String key;

        public ImageData(ImageView bmImage, String key) {
            this.bmImage = bmImage;
            this.key = key;
        }

        protected Bitmap doInBackground(String... params) {
            String characterId = params[0];
            String size = params[1];
            String url = "https://image.eveonline.com/Character/"+characterId+"_"+size+".jpg";
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            DummyContent.DummyItem newItem = DummyContent.ITEM_MAP.get(key);
            newItem.charBitmap = result;
            DummyContent.ITEM_MAP.put(key, mItem);
            bmImage.setImageBitmap(result);
        }
    }
}
