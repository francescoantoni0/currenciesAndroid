package com.example.myapplication;

import android.os.Bundle;
import com.google.android.material.appbar.MaterialToolbar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.*;


public class ExchangeActivity extends AppCompatActivity implements View.OnClickListener{
    private MaterialToolbar materialToolbar;
    private Button convertButton;
    private EditText editTextNumberDecimal;
    private Spinner spinner;
    private TextView resultString;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        convertButton = findViewById(R.id.convert_button);
        editTextNumberDecimal = findViewById(R.id.edit_text_number_decimal);
        spinner = findViewById(R.id.spinner);
        resultString = findViewById(R.id.result_string);
        //TODO: implement XML file download in the internal storage

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.convert_button){

        }
    }

    private final String url = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    private HashMap<String, Double> cambi;

    public boolean getChangeRate(String XMLFile) throws IOException {
        int status;
        URL url = new URL(this.url);
        HttpsURLConnection service = (HttpsURLConnection) url.openConnection();
        service.setRequestProperty("Host", "www.ecb.europa.eu");
        service.setRequestProperty("Accept", "application/xml");
        service.setRequestProperty("Accept-Charset", "UTF-8");
        service.setRequestMethod("GET");
        service.setDoOutput(true);

        status = service.getResponseCode();
        if (status != 200) {
            return false;
        }
        Files.copy(service.getInputStream(), new File(XMLFile).toPath(), StandardCopyOption.REPLACE_EXISTING);
        return true;
    }

    public void parseXML(String XMLDocument) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document document;
        Element root, element;
        NodeList nodeList;
        String key, value;

        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
        document = builder.parse(XMLDocument);
        cambi = new HashMap<>();
        root = document.getDocumentElement();

        nodeList = root.getElementsByTagName("Cube");
        nodeList = nodeList.item(0).getChildNodes();
        nodeList = nodeList.item(0).getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++){
            element = (Element) nodeList.item(i);
            key = element.getAttribute("currency");
            value = element.getAttribute("rate");
            cambi.put(key, Double.parseDouble(value));
        }
    }
}