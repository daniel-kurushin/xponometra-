package ru.pstu.itas.androidfrv;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

class CSVFromURL extends AsyncTask<String, String, String>
{
	private final URL url;
	final FRVDatabase db;

	List<String> responseData = new ArrayList<String>();
	boolean isLoaded = false;
	int count;
	static final String SPLIT = ",";

	public interface AsynResponse
	{
		void processFinish(Boolean output);
	}

	AsynResponse asynResponse = null;

	public CSVFromURL(URL url, FRVDatabase db, AsynResponse ar)
	{
		this.url = url;
		this.db = db;
		this.asynResponse = ar;
	}

	public CSVFromURL(URL url, FRVDatabase db)
	{
		this.db = db;
		this.url = url;
	}

	@Override
	protected String doInBackground(String... f_url)
	{
		isLoaded = false;
		try
		{
			URLConnection c = this.url.openConnection();
			c.connect();

			BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			String line = null;

			count = 0;
			while ((line = in.readLine()) != null)
			{
				responseData.add(line.trim());
				count++;
			}

			//in.close();

		} catch (Exception e)
		{
			Log.e("Error: ", e.getMessage());
		}

		return null;
	}

	@Override
	protected void onPostExecute(String s)
	{
		super.onPostExecute(s);
		isLoaded = true;
	}
	public void execute()
	{
		super.execute("");
		isLoaded = false;
	}
}