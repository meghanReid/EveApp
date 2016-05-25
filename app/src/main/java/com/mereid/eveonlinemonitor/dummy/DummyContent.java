package com.mereid.eveonlinemonitor.dummy;

import android.os.AsyncTask;

import com.mereid.eveonlinemonitor.Constants;

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
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    public static int initialized = 0;

    public void Init()
    {
        if (initialized == 0) {
            for (int i = 0; i < Constants.userData.size(); i++) {
                String vCode = Constants.userData.get(i).first;
                String keyID = Constants.userData.get(i).second;
                try {
                    new LongOperation().execute(keyID, vCode, Integer.toString(i)).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            initialized = 1;
        }
    };

//
//    private static final int COUNT = 2;
//
//
//    // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createDummyItem(i));
//        }

    private void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private DummyItem createDummyItem(String name, String id, String corporation, int index) {
        return new DummyItem(name, id, corporation, index);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public class DummyItem {
        public final String id;
        public final String content;
        public final String details;
        public String isk;
        public int userDataIndex;

        public DummyItem(String name, String characterId, String corp, int index) {
            this.id = /*"Character: " +*/ name;
            this.details = /*"Corporation: " +*/ characterId;
            this.content = /*"Character id: " +*/ corp;
            this.userDataIndex = index;
            this.isk = "";
        }

        public void SetIsk(String iskRetrieved) {
            int third = 0;
            for(int i = iskRetrieved.length()-4; i>0; i--)
            {

                if (third == 2)
                {
                    iskRetrieved = iskRetrieved.subSequence(0,i) + "," + iskRetrieved.subSequence(i, iskRetrieved.length());
                    third = 0;
                }
                else
                {
                    third++;
                }
            }
            this.isk = iskRetrieved + " isk";
        }

        @Override
        public String toString() {
            return content;
        }
    }

    private class LongOperation extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... params)  {
            String myurl = "https://api.eveonline.com//account/Characters.xml.aspx?keyID="+ params[0] +"&vCode="+params[1];
            int index = Integer.parseInt(params[2]);
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
                            for (int i = 0; i<list.getLength(); i++) {
                                Element character = (Element) list.item(i);
                                String name = character.getAttribute("name");
                                String characterId = character.getAttribute("characterID");
                                String corpName = character.getAttribute("corporationName");
                                addItem(createDummyItem(name, characterId, corpName, index));
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

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}
