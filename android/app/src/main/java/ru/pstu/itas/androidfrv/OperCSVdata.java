package ru.pstu.itas.androidfrv;

import java.net.URL;

class OperCSVdata extends CSVFromURL
{
	OperCSVdata(URL url, FRVDatabase db)
	{
		super(url, db);
	}

	@Override
	protected void onPostExecute(String s)
	{
		super.onPostExecute(s);
		isLoaded = false;
		String oper;

		for (int i = 0; i < responseData.size(); i++)
		{
			oper = responseData.get(i).trim();
			if (!oper.isEmpty())
			{
				try
				{
					int oper_id = this.db.addNewOperation(oper);
					count++;
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		isLoaded = true;
	}
}
