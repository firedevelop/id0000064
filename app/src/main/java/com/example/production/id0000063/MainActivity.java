package com.example.production.id0000063;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView= (TextView)findViewById(R.id.text);

        try {
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            InputStream is = getApplicationContext().getAssets().open("sample.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false); //Indicates whether or not the factory is configured to produce parsers which are namespace aware
            parser.setInput(is, null); //Sets the input stream the parser is going to process. This call resets the parser state and sets the event type to the initial value START_DOCUMENT.

            ArrayList<Country> countries = parseXML(parser);

            String text="";

            for(Country e:countries)
            {

                text+= "id : "+e.getId()+" name : "+e.getName()+" capital : "+e.getCapital()+"\n";
            }

            textView.setText(text);



        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    private ArrayList<Country> parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        ArrayList<Country> countries = null;
        int eventType = parser.getEventType();
        Country country = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    countries = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("country")){
                        country = new Country();
                        country.id=parser.getAttributeValue(null,"id");
                    } else if (country != null){
                        if (name.equals("name")){
                            country.name = parser.nextText();
                        } else if (name.equals("capital")){
                            country.capital = parser.nextText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("country") && country != null){
                        countries.add(country);
                    }
            }
            eventType = parser.next();
        }

        return countries;

    }
}
