package edu.upf.database;

        import org.apache.http.client.HttpClient;
        import org.apache.http.client.ResponseHandler;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.impl.client.BasicResponseHandler;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.w3c.dom.Document;
        import org.w3c.dom.NodeList;

        import android.app.Service;
        import android.content.Intent;
        import android.os.IBinder;
        import android.util.Log;

        import java.io.ByteArrayInputStream;
        import java.io.InputStream;
        import java.net.URL;
        import java.nio.charset.StandardCharsets;

        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;

public class MyService extends Service{

    private static String deviceID;
    private static String url;
    private static String TAG = MyService.class.getSimpleName();
    private MyThread mythread;
    public boolean isRunning = false;
    final static String MY_ACTION = "MY_ACTION";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mythread = new MyThread();
    }

    @Override
    public synchronized void onDestroy() {
        super.onDestroy();
        if(!isRunning){
            mythread.interrupt();
            mythread.stop();
        }
    }

    @Override
    public synchronized void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        /*
        url = intent.getStringExtra("URL");
        InputStream devices;
        try {
            devices = new URL(url + "/devices").openStream();
        }
        catch(Exception e) {
            e.printStackTrace();
            devices = null;
        }

        Document xml = parseXML(devices);
        deviceID = xml.getElementsByTagName("id").item(0).getTextContent();
        */

        if(!isRunning){
            mythread.start();
            isRunning = true;
        }
    }

    public String[] readWebPage(){
        HttpClient client = new DefaultHttpClient();

        //HttpGet request = new HttpGet(url + "/devices/" + deviceID + "/inventory");
        HttpGet request = new HttpGet("http://barcelonaapi.marcpous.com/bicing/stations.xml");

        // Get the response
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String response_str = null;
        try {
            response_str = client.execute(request, responseHandler);
            if(!response_str.equalsIgnoreCase("")){
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Document temp = parseXML(new ByteArrayInputStream(response_str.getBytes(StandardCharsets.UTF_8)));

        //NodeList tags = temp.getElementsByTagName("epc");
        NodeList tags = temp.getElementsByTagName("id");

        /*String[] tagsEPC = new String[tags.getLength()];
        for(int i=0;i<tagsEPC.length;i++) {
            tagsEPC[i] = tags.item(i).getTextContent();
        } */

        String[] tagsEPC = new String[4];
        for(int i=0;i<4;i++) {
            tagsEPC[i] = tags.item(i).getTextContent();
        }
        return tagsEPC;
    }

    public static Document parseXML(InputStream xml_input) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document xml;
        try {
            builder = factory.newDocumentBuilder();
            xml = builder.parse(xml_input);
            return xml;
        }
        catch(Exception e) {
            e.printStackTrace();
            return xml = null;
        }

    }

    class MyThread extends Thread{
        static final long DELAY = 3000;
        @Override
        public void run(){
            while(isRunning){
                try {
                    String[] response = readWebPage();
                    Thread.sleep(DELAY);
                    Intent intent = new Intent();
                    intent.setAction(MY_ACTION);
                    intent.putExtra("RFIDs", response);
                    sendBroadcast(intent);
                } catch (InterruptedException e) {
                    isRunning = false;
                    e.printStackTrace();
                }
            }
        }

    }

}
