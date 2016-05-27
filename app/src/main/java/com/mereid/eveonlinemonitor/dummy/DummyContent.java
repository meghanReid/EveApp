package com.mereid.eveonlinemonitor.dummy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.mereid.eveonlinemonitor.CharacterListActivity;
import com.mereid.eveonlinemonitor.Constants;
import com.mereid.eveonlinemonitor.Splash;

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



//
//    private static final int COUNT = 2;
//
//
//    // Add some sample items.
//        for (int i = 1; i <= COUNT; i++) {
//            addItem(createDummyItem(i));
//        }

    public void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public DummyItem createDummyItem(String name, String id, String corporation, int index, Bitmap bitmap) {
        return new DummyItem(name, id, corporation, index, bitmap);
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
        public Bitmap charBitmap;
        public int charInitialized = 0;

        public DummyItem(String name, String characterId, String corp, int index, Bitmap bitmap) {
            this.id = /*"Character: " +*/ name;
            this.details = /*"Corporation: " +*/ characterId;
            this.content = /*"Character id: " +*/ corp;
            this.userDataIndex = index;
            this.isk = "";
            this.charBitmap = bitmap;
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

        public void SetBitmap(Bitmap bitmap) {
            this.charBitmap = bitmap;
        }

        @Override
        public String toString() {
            return content;
        }
    }




}
