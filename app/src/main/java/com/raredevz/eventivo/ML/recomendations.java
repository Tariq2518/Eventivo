package com.raredevz.eventivo.ML;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.raredevz.eventivo.R;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class recomendations extends AppCompatActivity {

    Services s;
    TextView rec;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_recomendations);
        s = ServiceGenerator.createService(Services.class);
        rec = (TextView) findViewById(R.id.recomended);
        predict_Service(1,0,0);

    }

    public void predict_Service(final int attribute_1, final int attribute_2, final int attribute_3)
    {

       // Toast.makeText(this, "Service Called", Toast.LENGTH_SHORT).show();
        String json = "{\n" +
                "\n" +
                "        \"Inputs\": {\n" +
                "\n" +
                "                \"input1\":\n" +
                "                {\n" +
                "                    \"ColumnNames\": [\"Objective\", \"res_1\", \"res_2\"],\n" +
                "                    \"Values\": [ [ \"" + attribute_1 + "\" , \"" + attribute_2 + "\", \"" + attribute_3 + "\"], [ \"" + attribute_1 + "\", \"" + attribute_2 + "\", \"" + attribute_3 + "\"] ]\n" +
                "                }       },\n" +
                "            \"GlobalParameters\": {\n" +
                "}\n" +
                "    }";
//        Toast.makeText(this, "" + json, Toast.LENGTH_SHORT).show();
        JsonObject obj = new JsonObject();
        try
        {

            JsonParser jsonParser = new JsonParser();
            obj = (JsonObject)jsonParser.parse(json);


            Log.d("My App", obj.toString());

        }
        catch (Throwable t)
        {
            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
        }
        s.getNotification(obj,
                "Bearer 9KMnA02M97yKRl7JSIN7ihk/sXRw4RkPIi2DfAKjFnqT/SI3n7wZoYv1OTJRsY0ofmGIzqXXp7dZvMTlIoPGew==")
                .enqueue(new Callback<ResponseBody>()
                {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                    {
                        String b = "";
                        try
                        {
                            b = new String(response.body()
                                    .bytes());
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                        Toast.makeText(recomendations.this, b, Toast.LENGTH_SHORT)
                                .show();
                        b = b.toString();
                        String[] recomended_venue =  b.split("Values", 2);
                        String recomended = recomended_venue[1];
                        recomended = recomended.replace("\"]]}}}}","");
                        recomended = recomended.replace("\":[[\"","");

                        String[] vanues_according_to_hirarichy = recomended.split(",");

                        String mesaage_to_show = "";

                        for(String vanue : vanues_according_to_hirarichy)
                        {
                            mesaage_to_show = mesaage_to_show + vanue +"\n";
                        }
                        rec.setText(mesaage_to_show);


                        /*
                        ---------------------------Message for Nawaz ---------------------------------
                        Array  -- > vanues_according_to_hirarichy  contains all the venues according to Recommendation hierarchy ,
                         so just grab the elements in order and show them in the list


                         */
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t)
                    {

                    }
                });
    }
}