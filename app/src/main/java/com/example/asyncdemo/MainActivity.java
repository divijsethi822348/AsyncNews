package com.example.asyncdemo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    ArrayList<Model> list=new ArrayList<Model>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


       new Async().execute();

    }


    public class Async extends AsyncTask<String,String,String>{

        String title,name,Author,urlImage;
        HttpURLConnection  httpURLConnection;
        String json="";
        JSONObject jsonObject;
        StringBuilder stringBuilder=new StringBuilder();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                URL url=new URL("https://newsapi.org/v2/top-headlines?country=in&apiKey=b4f0614a42bc491498fa9fc73943a173");
                httpURLConnection=(HttpURLConnection)url.openConnection();

                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String data;
                while((data=bufferedReader.readLine())!=null){
                    stringBuilder.append(data);

                }
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            json=stringBuilder.toString();

            Log.d("do in background",json);
            return json;
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try{

                jsonObject=new JSONObject(json);

                JSONArray jsonArray=jsonObject.getJSONArray("articles");

                for(int i=0;i<jsonArray.length();i++){
                    Model model=new Model();
                   Author=jsonArray.getJSONObject(i).getString("author");

                   title=jsonArray.getJSONObject(i).getString("title");

                   urlImage=jsonArray.getJSONObject(i).getString("urlToImage");

                   JSONObject jsonObject1=jsonArray.getJSONObject(i).getJSONObject("source");

                   name=jsonObject1.getString("name");


                   Log.d("Author",Author);
                   Log.d("title",title);
                   Log.d("urlImage",urlImage);
                   Log.d("name",name);

                   model.setAuthor(Author);
                   model.setName(name);
                   model.setTitle(title);
                   model.setUrlImage(urlImage);

                   list.add(model);




                }
            }catch (JSONException e){
                e.printStackTrace();;
            }

            adapter=new RecyclerAdapter(list,getApplicationContext());
            recyclerView.setAdapter(adapter);

        }
    }
}
