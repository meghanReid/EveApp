package com.mereid.eveonlinemonitor.dummy.dummy;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.mereid.eveonlinemonitor.Constants;
import com.mereid.eveonlinemonitor.R;

import org.json.JSONException;
import org.json.JSONObject;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 */
/**
 * Created by mereid on 5/26/2016.
 */
public class KillContent {

    /**
     * An array of sample solar system items.
     */
    public static final List<SolarSystem> SYSTEM_ITEMS = new ArrayList<SolarSystem>();

    /**
     * A map of sample solar system items, by ID.
     */
    public static final Map<String, SolarSystem> SYSTEM_ITEM_MAP = new HashMap<String, SolarSystem>();

    public static int initializedSystem = 0;

    public void Init(Context context)
    {
        if (initializedSystem == 0) {
            new LongOperation(context).execute("");
        }
    };

//
//    private static final int COUNT = 2;
//
//
//    // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createSolarSystem(i));
//        }

    private void addItem(SolarSystem item) {
        SYSTEM_ITEMS.add(item);
        SYSTEM_ITEM_MAP.put(item.systemID, item);
    }

    private SolarSystem createSolarSystem(String systemID, int shipKills, int podKills, String name, String securityStatus) {
        return new SolarSystem(systemID, shipKills, podKills, name, securityStatus);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public class SolarSystem {
        public final String systemID;
        public final int shipKills;
        public final int podKills;
        public final String name;
        public final String securityStatus;

        public SolarSystem(String systemID, int shipKills, int podKills, String name, String securityStatus) {
            this.systemID = systemID;
            this.shipKills = shipKills;
            this.podKills = podKills;
            this.name = name;
            this.securityStatus = securityStatus;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        Context context;

        public LongOperation(Context context) {
            this.context = context;
        }

        public class SystemData {
            public final String name;
            public final double securityStatus;

            public SystemData(String name, double securityStatus) {
                this.name = name;
                this.securityStatus = securityStatus;
            }

            @Override
            public String toString() {
                return name;
            }
        }

        private String parseSecStatus(double secStatus)
        {
            return String.format( "%.2f", secStatus );
        }

        private SystemData getSystemData(String systemID)
        {
            SystemData data;
            String name = "";
            double status = -999;

            SharedPreferences sharedPref = this.context.getSharedPreferences(
                    this.context.getString(R.string.keySysId_valueName_file), Context.MODE_PRIVATE);

            if (sharedPref.contains(systemID))
            {
                SharedPreferences sharedPreferences = this.context.getSharedPreferences(
                        this.context.getString(R.string.keySysId_valuesecStatus_file), Context.MODE_PRIVATE);
                name = sharedPref.getString(systemID, "");
                status = sharedPreferences.getFloat(systemID, -99.99f);
            }
            else {

                String myurl = "https://crest-tq.eveonline.com/solarsystems/" + systemID + "/";
                URL url = null;
                int response = -1;
                try {
                    url = new URL(myurl);
                    String result;
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
                    if (response == 200) {
                        InputStream stream = conn.getInputStream();
                        result = Constants.convertStreamToString(stream);
                        JSONObject systemObject = new JSONObject(result);
                        name = systemObject.getString("name");
                        status = systemObject.getDouble("securityStatus");
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(systemID, name);
                        editor.commit();
                        SharedPreferences sharedPreferences = this.context.getSharedPreferences(
                                this.context.getString(R.string.keySysId_valuesecStatus_file), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorSecStatus = sharedPreferences.edit();
                        editorSecStatus.putFloat(systemID, (float) status);
                        editorSecStatus.commit();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            data = new SystemData(name, status);
            return data;
        }

        private Bitmap getBitmap(String characterId)
        {
            String url = "https://image.eveonline.com/Character/"+characterId+"_"+"1024.jpg";
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
            }
            return mIcon11;
        }

        @Override
        protected String doInBackground(String... params)  {
            String myurl = "https://api.eveonline.com/map/kills.xml.aspx";
            URL url = null;
            int response = -1;
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
                            Map<Integer, Integer> idKills = new HashMap<Integer, Integer>();
                            Map<Integer, Integer> idPodKills = new HashMap<Integer, Integer>();

                            for (int i = 0; i<list.getLength(); i++) {

                                Element system = (Element) list.item(i);
                                String shipKill = system.getAttribute("shipKills");
                                int shipKills = Integer.parseInt(shipKill);
                                int systemId = Integer.parseInt(system.getAttribute("solarSystemID"));
                                if (shipKills < 5) continue;
                                idKills.put(systemId, shipKills);
                                idPodKills.put(systemId, Integer.parseInt(system.getAttribute("podKills")));
//                                String characterId = character.getAttribute("characterID");
//                                String corpName = character.getAttribute("corporationName");
//                                Bitmap bitmap = getBitmap(characterId);
////                                addItem(createSolarSystem(systemID, shipKills, podKills, name, securityStatus));

                            }
                            Map<Integer, Integer> sortedMap = new HashMap<Integer, Integer>();
                            sortedMap = Constants.sortByValue(idKills);

                            Iterator it = sortedMap.entrySet().iterator();
                            int j = 0;
                            while (it.hasNext()) {
                                Map.Entry pair = (Map.Entry)it.next();
                                String systemId = pair.getKey().toString();
                                SystemData data = getSystemData(systemId);
                                if (data.securityStatus > 0.45) continue;
                                addItem(createSolarSystem(systemId, Integer.parseInt(pair.getValue().toString()), idPodKills.get(Integer.parseInt(systemId)), data.name, parseSecStatus(data.securityStatus)));
                                it.remove();
                                if (j == 24) break;
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

            return "Executed";
        }

        @Override
        protected void onPostExecute(String s)
        {
            initializedSystem = 1;
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

}

