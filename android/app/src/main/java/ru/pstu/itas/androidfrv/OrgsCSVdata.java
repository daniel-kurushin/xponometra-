package ru.pstu.itas.androidfrv;

import java.net.URL;


class OrgsCSVdata extends CSVFromURL
{
	OrgsCSVdata(URL url, FRVDatabase db)
	{
		super(url, db);
	}

	@Override
	protected void onPostExecute(String s)
	{
		super.onPostExecute(s);
		isLoaded = false;
		String orgs;
		String dolg;

		for (int i = 1; i < responseData.size(); i++)
		{
			String str = responseData.get(i).trim();
			if (!str.isEmpty())
			{
				String[] arr = str.split("%");
				orgs = arr[0];
				dolg = arr[1];
				try
				{
					int org_id = this.db.addNewStructure(orgs);
					int pos_id = this.db.addNewPosition(org_id, dolg);
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
