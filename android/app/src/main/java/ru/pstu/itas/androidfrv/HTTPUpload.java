package ru.pstu.itas.androidfrv;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class HTTPUpload extends AsyncTask<String, String, String>
{
	private final List<String> data;
	private final String remoteFileName;
	private final URL geturl;
	private final URL posturl;
	private final FRVDatabase dataset;

	private String key = "super";

	public HTTPUpload(FRVDatabase frvDatabase, String remoteFileName, List<String> data, URL geturl, URL posturl)
	{
		Logger.call("HTTPUpload", data.toString());
		this.dataset = frvDatabase;
		this.remoteFileName = remoteFileName;
		this.data = data;
		this.geturl = geturl;
		this.posturl = posturl;
		Logger.exit("HTTPUpload");
	}

	@Override
	protected String doInBackground(String... strings)
	{
		Logger.call("HTTPUpload.doInBackground");
		int nrec = 0;
		try
		{
			this.key = getSecureKey();
			//this.key = "super";
//			for (int i = 0; i < 200; i++)
//			{
				for (String str: data)
				{
					postStr(str);
					nrec++;
				}
//			}

			//in.close();
			Logger.exit("HTTPUpload.doInBackground", String.format("%s", nrec));

		} //catch (Exception e)
		catch (IOException e)
		{
			dataset.uploadState = FRVDatabase.EXPORT_ERROR;
			Logger.exit("HTTPUpload.doInBackground", e.toString());
		}
		//{
		//}
		return null;
	}

	private void postStr(String str) throws IOException
	{
		Logger.call("HTTPUpload.postStr", str, this.remoteFileName);

		URLConnection c = this.posturl.openConnection();
		HttpURLConnection h = (HttpURLConnection) c;
		h.setRequestMethod("POST");
		h.setDoOutput(true);

		String toSend = "";
		toSend += String.format("key=%s&", this.key);
		toSend += String.format("trg=%s&", this.remoteFileName);
		toSend += String.format("str=%s", URLEncoder.encode(str, "UTF-8"));

		byte[] out = toSend.getBytes(StandardCharsets.UTF_8);
		int l = out.length;

		h.setFixedLengthStreamingMode(l);
		h.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		h.connect();
		OutputStream o = new BufferedOutputStream(h.getOutputStream());
		o.write(out);
		o.close();
		InputStream i = new BufferedInputStream(h.getInputStream());
		byte[] b = new byte[100];
		i.read(b);
		String res = new String(b);
		h.disconnect();
		Logger.exit("HTTPUpload.postStr", res);
	}

	private String getSecureKey() throws IOException
	{
		Logger.call("HTTPUpload.getSecureKey");
		URLConnection c = this.geturl.openConnection();
		c.connect();

		BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));

		String line;
		while ((line = in.readLine()) != null)
		{
			Logger.exit("HTTPUpload.getSecureKey", line.trim());
			return line.trim();
		}
		Logger.exit("HTTPUpload.getSecureKey", "ERROR");
		return "";
	}

	@Override
	protected void onPostExecute(String s)
	{
		super.onPostExecute(s);
		Logger.call("HTTPUpload.onPostExecute", s);
		if (dataset.uploadState != FRVDatabase.EXPORT_ERROR)
		{
			dataset.uploadState = FRVDatabase.EXPORT_DONE;
		}
		Logger.exit("HTTPUpload.onPostExecute", String.format("%s", dataset.uploadState));
	}

	public void execute()
	{
		super.execute("");
	}
}
