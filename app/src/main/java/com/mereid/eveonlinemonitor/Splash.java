package com.mereid.eveonlinemonitor;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import com.mereid.eveonlinemonitor.dummy.DummyContent;
import com.mereid.eveonlinemonitor.dummy.dummy.KillContent;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by mereid on 5/27/2016.
 */
public class Splash extends Activity {
    public static boolean splashShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ProgressBar spinner;
        spinner = (ProgressBar)findViewById(R.id.progressBarSplash);
        DummyContent dummy = new DummyContent();
        new LongOperation(dummy).execute("");
    }

    private class LongOperation extends AsyncTask<String, Void, String> {
        DummyContent dummy;

        public LongOperation(DummyContent dummy) {
            this.dummy = dummy;
        }

        private Bitmap getBitmap(String characterId) {
            String url = "https://image.eveonline.com/Character/" + characterId + "_" + "1024.jpg";
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
            }
            return mIcon11;
        }
        private String getValue(String httpValue, String attrValue, int index) {
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
        protected String doInBackground(String... params) {
            for (int i = 0; i < Constants.userData.size(); i++) {
                String vCode = Constants.userData.get(i).first;
                String keyID = Constants.userData.get(i).second;
                String myurl = "https://api.eveonline.com//account/Characters.xml.aspx?keyID=" + keyID + "&vCode=" + vCode;
                int index = i;
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
                    if (response == 200) {
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
                                for (int j = 0; j < list.getLength(); j++) {
                                    Element character = (Element) list.item(j);
                                    String name = character.getAttribute("name");
                                    String characterId = character.getAttribute("characterID");
                                    String corpName = character.getAttribute("corporationName");
                                    Bitmap bitmap = getBitmap(characterId);
                                    String isk = getValue(characterId, "balance", i);
                                    dummy.addItem(dummy.createDummyItem(name, characterId, corpName, index, bitmap, DummyContent.ParseIsk(isk)));
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
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String s) {
            Splash.this.splashShown = true;
            dummy.initialized = 1;
            Intent mainClass = new Intent(Splash.this, CharacterListActivity.class);
            Splash.this.startActivity(mainClass);
            Splash.this.finish();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
