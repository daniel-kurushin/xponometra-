package ru.pstu.itas.androidfrv;

import java.net.URL;

/**
 * Created by dan on 17.07.17.
 */

class FactCSVdata extends CSVFromURL
{
	public FactCSVdata(URL url, FRVDatabase db)
	{
		super(url, db);
	}

	@Override
	protected void onPostExecute(String s)
	{
		super.onPostExecute(s);
		isLoaded = false;
		String fact;

		for (int i = 0; i < responseData.size(); i++)
		{
			fact = responseData.get(i);
			try
			{
				int fact_id = this.db.addNewFactor(fact);
				count++;
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		isLoaded = true;
	}
}
