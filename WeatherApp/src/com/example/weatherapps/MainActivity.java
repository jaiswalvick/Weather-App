package com.example.weatherapps;

import java.io.InputStream;

import model.Weather;

import org.json.JSONException;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {


private TextView cityText;
private TextView state;

private TextView mintemp;
private TextView temp;
private TextView press;
private TextView windSpeed;
private TextView maxtemp;

private TextView hum;
private ImageView imgView;
String city = "";
ProgressBar progress;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);

ActionBar actionbar=getActionBar();
ColorDrawable colordraw=new ColorDrawable(Color.parseColor("#81BEF7"));
actionbar.setBackgroundDrawable(colordraw);
cityText = (TextView) findViewById(R.id.city);
state = (TextView) findViewById(R.id.state);
progress=(ProgressBar) findViewById(R.id.progressBar1);
progress.setVisibility(View.INVISIBLE);
mintemp = (TextView) findViewById(R.id.mintemp);
maxtemp = (TextView) findViewById(R.id.maxtemp);
//imgView=(ImageView)findViewById(R.id.imageView1);
temp = (TextView) findViewById(R.id.maintemp);
hum = (TextView) findViewById(R.id.humidity);
press = (TextView) findViewById(R.id.presure);
windSpeed = (TextView) findViewById(R.id.windspeed);

}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.main, menu);
	 
    
    View v = (View) menu.findItem(R.id.actionViewLayout).getActionView();
    final EditText txtSearch = ( EditText ) v.findViewById(R.id.searchbox);
    ImageButton Search = (ImageButton ) v.findViewById(R.id.searchbutton);
Search.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		   
		JSONWeatherTask task = new JSONWeatherTask();
		task.execute(new String[]{txtSearch.getText().toString()});
		progress.setVisibility(View.VISIBLE);
	}
});
   
    return super.onCreateOptionsMenu(menu);
    }
class asyn extends AsyncTask<Weather, Void, Void>
{	Bitmap bit;
	@Override
	protected Void doInBackground(Weather... params) {
	        try {
	            InputStream in = new java.net.URL("http://openweathermap.org/img/w/"+params[0].currentCondition.getIcon()+".png").openStream();
	            bit = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
		return null;
	}
	@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(bit!=null)
			{	imgView.setImageBitmap(bit);
						}
	}
}

private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

@Override
protected Weather doInBackground(String... params) {
Weather weather = new Weather();
String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

try {
weather = JSONWeatherParser.getWeather(data);
} catch (JSONException e) {	
e.printStackTrace();
}
return weather;

}

@Override
protected void onPostExecute(Weather weather) {	
super.onPostExecute(weather);
try
{
progress.setVisibility(View.INVISIBLE);
new asyn().execute(weather);

cityText.setText(weather.location.getCity());
state.setText(weather.location.getCountry());
mintemp.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescr() + ")");
temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)));
//mintemp.setText("" + Math.round((weather.temperature.getMinTemp() - 273.15)));
//maxtemp.setText("" + Math.round((weather.temperature.getMaxTemp() - 273.15)));

hum.setText("" + weather.currentCondition.getHumidity() + "%");
press.setText("" + weather.currentCondition.getPressure() + " hPa");
windSpeed.setText("" + weather.wind.getSpeed() + " mps");
maxtemp.setText("" + weather.location.getLongitude() + ", "+weather.location.getLatitude());
}
catch(Exception e)
{
	Toast.makeText(getApplicationContext(), "Enter valid addresss",Toast.LENGTH_LONG).show();
}
}

  }

	public void share(View v)
	{
		Intent intent=new Intent(Intent.ACTION_SEND);
		 intent.setType("text/plain");
		 String str= "City "+cityText.getText()+"  ,Country"+state.getText()+" ,Current Condition "+ mintemp.getText()+" ,Temp "+temp.getText();
		str+=" ,Humidity "+hum.getText()+" ,Pressure"+press.getText()+" ,Windspeed "+windSpeed.getText()+" ,Long,Lati"+maxtemp.getText();	
		 intent.putExtra(Intent.EXTRA_TEXT, str);
		startActivity(Intent.createChooser(intent, "Share"));
	}

}