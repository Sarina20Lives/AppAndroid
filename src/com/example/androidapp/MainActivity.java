package com.example.androidapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{

	Button buscar;
	EditText parametro1;
	EditText parametro2;
	EditText parametro3;
	TextView resultado;

	ObtenerNumeros on;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		buscar = (Button)findViewById(R.id.buscar);
		parametro1 = (EditText)findViewById(R.id.parametro1);
		parametro2 = (EditText)findViewById(R.id.parametro2);
		parametro3 = (EditText)findViewById(R.id.parametro3);
		resultado = (TextView)findViewById(R.id.resultado);
		
		
		buscar.setOnClickListener(this);
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.buscar:
			String param = parametro1.getText().toString();
			if(param == null || param.isEmpty()){
				resultado.setText("El parametro de marco es obligatorio");
				break;
			}
			param = parametro2.getText().toString();
			if(param == null || param.isEmpty()){
				resultado.setText("El parametro de polo es obligatorio");
				break;
			}
			param = parametro3.getText().toString();
			if(param == null ){
				param = "";
			}
			resultado.setText("");
			String parametros = parametro1.getText().toString() + "&polo=" + parametro2.getText().toString() + "&md5sum=" + param;
			String urlstring = "http://192.168.1.6:8080/hello/check-polo?marco="+parametros;
			on = new ObtenerNumeros();
			on.execute(urlstring);
			break;

		default:
			break;
		}

		
	}

	
	public class ObtenerNumeros extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... params) {			
			try {
				//Creando la conexión
				URL url = new URL(params[0]);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 1.5; es-ES) Ejemplo AndroidApp ");;
				
				int respuesta = connection.getResponseCode();
				//Comprobando que la conexión sea correcta
				if(respuesta != HttpURLConnection.HTTP_OK)
					return "Error en la conexión";

				InputStreamReader input = new InputStreamReader(connection.getInputStream());
   			    BufferedReader lector = new BufferedReader(input);
				String salida = "";
   			    String linea = lector.readLine();
				while(linea != null){
					salida += linea;
					linea = lector.readLine();
				}   			    
				lector.close();
				input.close();
				connection.disconnect();
   			    return salida;
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return "Error de ejecución";
		}
		
		
		@Override
		protected void onPostExecute(String salida) {
			resultado.setText(salida);
			
		}
		
		
	}


}
