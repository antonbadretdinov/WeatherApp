package com.example.weatherapp

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import android.content.Context



const val TOKEN="cf5ce9b04c663ea943cbe23110355483"


class Test: AppCompatActivity() {
    //val userListFromJson: List<User> = mapper.readValue(jsonArray)
    /*println(userListFromJson)*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_layout)
        getData(59.937500,30.308611)

    }




    private fun getData(lat:Double,lon:Double){
        var URL="https://api.openweathermap.org/data/2.5/weather?" +
                "lat=$lat" +
                "&lon=$lon" +
                "&appid=$TOKEN"
        val queue= Volley.newRequestQueue(applicationContext)
        val request= StringRequest(
            Request.Method.GET,URL,
            {result-> Log.d("mylog","Result: $result")
            },
            {error->Log.d("mylog","Result: $error")}
        )
        queue.add(request)
    }
}
/*private class GetURLData : AsyncTask<String?, String?, String?>() {
    //будет работать асинхронно
    override fun onPreExecute() {
        super.onPreExecute()
        descr_tsk.setText("...")
        temp_tsk.setText("...")
        wind_tsk.setText("...")
        fl_tsk.setText("...")
    }*/

/*
    protected override fun doInBackground(vararg strings: String): String? {
        var connection: HttpURLConnection? = null
        var reader: BufferedReader? = null
        try {
            val url =
                URL(strings[0]) // url-соединение, 0 - потому что первый элемент массива strings это url оказывается
            connection = url.openConnection() as HttpURLConnection //http-соединение
            connection.connect()
            val stream: InputStream = connection.getInputStream() //считываем полученный поток
            reader = BufferedReader(InputStreamReader(stream)) //для считывания в формате строки
            val buffer = StringBuilder()
            var line: String? = ""
            while (reader.readLine().also { line = it } != null) {
                buffer.append(line).append("\n")
            }
            return buffer.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (connection != null) {
                connection.disconnect()
            }
            try {
                if (reader != null) {
                    reader.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }

    @SuppressLint("SetTextI18n")
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        try {
            time_tsk.setText(df.format(Calendar.getInstance().getTime()))
            val jsonObject = JSONObject(result)
            temp_tsk.setText(
                jsonObject.getJSONObject("main").getDouble("temp").toInt().toString() + "°C"
            )
            fl_tsk.setText(
                jsonObject.getJSONObject("main").getDouble("feels_like").toInt().toString() + "°C"
            )
            wind_tsk.setText(
                jsonObject.getJSONObject("wind").getDouble("speed").toInt().toString() + " m/s"
            )
            descr_tsk.setText(
                jsonObject.getJSONArray("weather").getJSONObject(0)
                    .getString("description") as CharSequence
            )
            if (descr_tsk.getText().equals("clear sky")) {
                sun_tsk.setVisibility(View.VISIBLE)
            } else if (descr_tsk.getText().equals("few clouds")
                || descr_tsk.getText().equals("scattered clouds")
                || descr_tsk.getText().equals("broken clouds")
            ) {
                cloud_tsk.setVisibility(View.VISIBLE)
            } else {
                rain_tsk.setVisibility(View.VISIBLE)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun doInBackground(vararg p0: String?): String? {
        TODO("Not yet implemented")
    }
}*/
