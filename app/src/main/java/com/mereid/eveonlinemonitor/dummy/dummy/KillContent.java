package com.mereid.eveonlinemonitor.dummy.dummy;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public void Init()
    {
        if (initializedSystem == 0) {
            try {
                new LongOperation().execute("").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }


            initializedSystem = 1;
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
//            String myurl = "https://api.eveonline.com//account/Characters.xml.aspx?keyID="+ params[0] +"&vCode="+params[1];
//            int index = Integer.parseInt(params[2]);
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
//                                Bitmap bitmap = getBitmap(characterId);
////                                addItem(createSolarSystem(systemID, shipKills, podKills, name, securityStatus));
                              addItem(createSolarSystem("testID", 5, 12, "testname", "-2.998"));

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

            return "Executed";
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

}

