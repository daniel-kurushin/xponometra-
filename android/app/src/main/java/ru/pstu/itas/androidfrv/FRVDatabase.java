package ru.pstu.itas.androidfrv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class FRVDatabase extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "contactDb";
	private static final File FRVDATA = new File(
			Environment.getExternalStoragePublicDirectory(
					Environment.DIRECTORY_DOCUMENTS),
			"FRVDATA"
	);
	static final int EXPORT_CSV = 1;
	static final int UPLOAD_CSV = 2;
	static final int EXPORT_DONE = 3;
	static final int EXPORT_ERROR = -1;
	static boolean isNeedToReloadPodr = true;
	static boolean isNeedToReloadDolg = true;
	private static boolean needToReloadOper;
	private static boolean needToReloadFact;
	private String ДОБАВЛЕНО_ВРУЧНУЮ;
	private String НЕ_ЗАПОЛНЕНО;
	int uploadState = 0;

	static boolean isNeedToReloadOper()
	{
		return needToReloadOper;
	}

	static boolean isNeedToReloadFact()
	{
		return needToReloadFact;
	}

	FRVDatabase(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Logger.call("FRVDatabase", context.toString());
		this.ДОБАВЛЕНО_ВРУЧНУЮ = context.getString(R.string.добавлено_вручную);
		this.НЕ_ЗАПОЛНЕНО = context.getString(R.string.не_заполнено);
		Integer checked = 0;
		Integer created = 0;
		try	{ FRVDATA.mkdirs();	} catch (Exception e) { }
		try	{ checkPODR(); checked++; } catch (Exception e) { createPODR(); created++; }
		try	{ checkOPER(); checked++; } catch (Exception e) { createOPER(); created++; }
		try { checkDOLG(); checked++; } catch (Exception e) { createDOLG(); created++; }
		try	{ checkFACT(); checked++; } catch (Exception e) { createFACT(); created++; }
		try	{ checkXPOH(); checked++; } catch (Exception e) { createXPOH(); created++; }
		try { checkFRVH(); checked++; } catch (Exception e) { createFRVH(); created++; }
		try { checkFRVB(); checked++; } catch (Exception e) { createFRVB(); created++; }
		Logger.exit("FRVDatabase", checked.toString(), created.toString());
	}

	public void setNeedToReloadOper(boolean needToReloadOper)
	{
		FRVDatabase.needToReloadOper = needToReloadOper;
	}

	public void setNeedToReloadFact(boolean needToReloadFact)
	{
		FRVDatabase.needToReloadFact = needToReloadFact;
	}

	private void checkFRVH()
	{
		SQLiteDatabase db = getReadableDatabase();
		db.query("FRVH", new String[]{"ID"}, null, null, null, null, null);
	}

	private void checkFRVB()
	{
		SQLiteDatabase db = getReadableDatabase();
		db.query("FRVB", new String[]{"ID"}, null, null, null, null, null);
	}

	private void checkXPOH()
	{
		SQLiteDatabase db = getReadableDatabase();
		db.query("XPOH", new String[]{"ID"}, null, null, null, null, null);
	}

	private void checkFACT()
	{
		SQLiteDatabase db = getReadableDatabase();
		db.query("FACT", new String[]{"ID"}, null, null, null, null, null);
	}

	private void checkDOLG()
	{
		SQLiteDatabase db = getReadableDatabase();
		db.query("DOLG", new String[]{"ID"}, null, null, null, null, null);
	}

	private void checkOPER()
	{
		SQLiteDatabase db = getReadableDatabase();
		db.query("OPER", new String[]{"ID"}, null, null, null, null, null);
	}

	private void checkPODR()
	{
		SQLiteDatabase db = getReadableDatabase();
		db.query("PODR", new String[]{"ID"}, null, null, null, null, null);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Logger.call("FRVDatabase.onCreate", "");
		//
		Logger.exit("FRVDatabase.onCreate", "");
	}

	private void createFRVH()
	{
		Logger.call("FRVDatabase.createFRVH", "");
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(
				"create table FRVH (" +
						"id integer primary key autoincrement, " +
						"date_time text, " +
						"n_head integer, " +
						"id_podr integer, " +
						"id_dolg integer, " +
						"id_xpoh integer, " +
						"sm_nom integer, " +
						"sm_dlit integer, " +
						"rab_gr_a integer, " +
						"rab_gr_b integer, " +
						"num_men integer, " +
						"worker_nik text, " +
						"constraint date_time_n_head_unique unique (date_time, n_head)" +
						");"
		);
		db.close();
		Logger.exit("FRVDatabase.createFRVH", "");
	}

	private void createFRVB()
	{
		Logger.call("FRVDatabase.createFRVB", "");
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(
				"create table FRVB (" +
						"id integer primary key autoincrement, " +
						"frvh_id integer, " +
						"time text, " +
						"P3B_OTL_OP integer, " +
						"KO integer, " +
						"KP integer, " +
						"oper_id integer, " +
						"fact1_id integer, " +
						"fact2_id integer, " +
						"fact3_id integer, " +
						"comments text, " +
						"constraint name_unique unique (time, frvh_id)" +
						");"
		);
		db.close();
		Logger.exit("FRVDatabase.createFRVB", "");
	}

	private void createFACT()
	{
		Logger.call("FRVDatabase.createFACT", "");
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(
				"create table FACT (" +
						"id integer primary key autoincrement, " +
						"name text, " +
						"vruc text, " +
						"date text, " +
						"xpoh text, " +
						"constraint name_unique unique (name)" +
						");"
		);
		db.close();
		Logger.exit("FRVDatabase.createFACT", "");
	}

	private void createXPOH()
	{
		Logger.call("FRVDatabase.createXPOH", "");
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(
				"create table XPOH (" +
						"id integer primary key autoincrement, " +
						"fio text, " +
						"comments text, " +
						"constraint name_unique unique (fio)" +
						");"
		);
		db.close();
		Logger.exit("FRVDatabase.createXPOH", "");
	}

	private void createOPER()
	{
		Logger.call("FRVDatabase.createOPER", "");

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(
				"create table OPER (" +
						"id integer primary key autoincrement, " +
						"name text, " +
						"vruc text, " +
						"date text, " +
						"xpoh text, " +
						"constraint name_unique unique (name)" +
						");"
		);
		db.close();
		Logger.exit("FRVDatabase.createOPER", "");
	}

	private void createPODR()
	{
		Logger.call("FRVDatabase.createPODR", "");

		SQLiteDatabase db = this.getWritableDatabase();
		try
		{
			db.execSQL(
					"create table PODR (" +
							"id integer primary key autoincrement, " +
							"name text, " +
							"constraint name_unique unique (name)" +
							");"
			);
		} catch (Exception e)
		{
			Log.e("Error", e.toString());
		}
		db.close();
		Logger.exit("FRVDatabase.createPODR", "");
	}

	private void createDOLG()
	{
		Logger.call("FRVDatabase.createDOLG", "");

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(
				"create table DOLG (" +
						"id integer primary key autoincrement, " +
						"podr_id integer, " +
						"name text, " +
						"constraint name_unique unique (name)" +
						");"
		);
		db.close();
		Logger.exit("FRVDatabase.createDOLG", "");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int i, int i1)
	{
		Logger.call("FRVDatabase.onUpgrade");
		db.execSQL("drop table if exists DOLG");
		db.execSQL("drop table if exists PODR");
		db.execSQL("drop table if exists OPER");
		db.execSQL("drop table if exists FACT");
		db.execSQL("drop table if exists XPOH");
		Logger.exit("FRVDatabase.onUpgrade");
	}

	int addNewStructure(String name) throws Exception
	{
		Logger.call("FRVDatabase.addNewStructure", name);
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();

		cv.put("name", name);
		db.insert("PODR", null, cv);

		db.close();

		Cursor c = getReadableDatabase().query("PODR",
				new String[]{"ID"},
				"NAME = ?",
				new String[]{name},
				null, null, null);

		if (c.moveToFirst())
		{
			Logger.exit("FRVDatabase.addNewStructure", c.toString());
			return getIDandCloseCursor(c);
		} else
		{
			throw new Exception("Error creating op = " + name);
		}
	}

	int addNewPosition(int org_id, String name) throws Exception
	{
		Logger.call("FRVDatabase.addNewPosition", name);

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("podr_id", org_id);
		cv.put("name", name);
		db.insert("DOLG", null, cv);

		db.close();

		Cursor c = getReadableDatabase().query("DOLG",
				new String[]{"ID"},
				"PODR_ID = ? and NAME = ?",
				new String[]{Integer.toString(org_id), name},
				null, null, null);

		if (c.moveToFirst())
		{
			Logger.exit("FRVDatabase.addNewPosition", c.toString());
			return getIDandCloseCursor(c);
		} else
		{
			throw new Exception("Error creating op = " + name);
		}
	}

	int addNewOperation(String name) throws Exception
	{
		Logger.call("FRVDatabase.addNewOperation", name);

		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("name", name);
		db.insert("OPER", null, cv);

		db.close();

		Cursor c = getReadableDatabase().query("OPER",
				new String[]{"ID"},
				"NAME = ?",
				new String[]{name},
				null, null, null);

		if (c.moveToFirst())
		{
			Logger.exit("FRVDatabase.addNewOperation", c.toString());
			return getIDandCloseCursor(c);
		} else
		{
			throw new Exception("Error creating op = " + name);
		}
	}

	int addNewFactor(String name) throws Exception
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("name", name);
		db.insert("FACT", null, cv);

		db.close();

		Cursor c = getReadableDatabase().query("FACT",
				new String[]{"ID"},
				"NAME = ?",
				new String[]{name},
				null, null, null);

		if (c.moveToFirst())
		{
			return getIDandCloseCursor(c);
		} else
		{
			throw new Exception("Error creating op = " + name);
		}
	}

	void freeDatabase()
	{
		try (SQLiteDatabase db = this.getWritableDatabase())
		{
			deleteDOLG(db);
			deleteOPER(db);
			deletePODR(db);
			deleteFACT(db);
			deleteXPOH(db);
		} catch (Exception e)
		{
			Log.e("Error:", e.toString());
		}
	}

	private void deleteXPOH(SQLiteDatabase db)
	{
		db.execSQL("delete from XPOH");
	}

	private void deleteFACT(SQLiteDatabase db)
	{
		db.execSQL("delete from FACT");
	}

	private void deletePODR(SQLiteDatabase db)
	{
		db.execSQL("delete from PODR");
	}

	private void deleteOPER(SQLiteDatabase db)
	{
		db.execSQL("delete from OPER");
	}

	private void deleteDOLG(SQLiteDatabase db)
	{
		db.execSQL("delete from DOLG");
	}

	int checkOrAddWorker(String fio)
	{
		if (!fio.trim().isEmpty())
		{
			Cursor c = this.getReadableDatabase().query("XPOH",
					new String[]{"ID"},
					"FIO = ?",
					new String[]{fio},
					null, null, null);

			if (c.moveToFirst())
			{
				return getIDandCloseCursor(c);
			} else
			{
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues cv = new ContentValues();
				cv.put("fio", fio);
				db.insert("XPOH", null, cv);
				db.close();
				return checkOrAddWorker(fio);
			}
		} else {
			return -1;
		}
	}

	int checkOrAddFact(String name, int workerId)
	{
		if (!name.trim().isEmpty())
		{
			Cursor c = this.getReadableDatabase().query("FACT",
					new String[]{"ID"},
					"NAME = ?",
					new String[]{name},
					null, null, null);

			if (c.moveToFirst())
			{
				return getIDandCloseCursor(c);
			} else
			{
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues cv = new ContentValues();
				cv.put("name", name);
				cv.put("vruc", ДОБАВЛЕНО_ВРУЧНУЮ);
				cv.put("date", String.format("%tY.%tm.%td", new Date(), new Date(), new Date()));
				cv.put("xpoh", getXpohById(String.valueOf(workerId)));
				db.insert("FACT", null, cv);
				db.close();
				needToReloadFact = true;
				return checkOrAddFact(name, workerId);
			}
		} else {
			return -1;
		}
	}

	int checkOrAddOper(String name, int workerId)
	{
		if (!name.trim().isEmpty())
		{
			Cursor c = this.getReadableDatabase().query("OPER",
					new String[]{"ID"},
					"NAME = ?",
					new String[]{name},
					null, null, null);

			if (c.moveToFirst())
			{
				return getIDandCloseCursor(c);
			} else
			{
				SQLiteDatabase db = this.getWritableDatabase();
				ContentValues cv = new ContentValues();
				cv.put("name", name);
				cv.put("vruc", ДОБАВЛЕНО_ВРУЧНУЮ);
				cv.put("date", String.format("%tY.%tm.%td", new Date(), new Date(), new Date()));
				cv.put("xpoh", getXpohById(String.valueOf(workerId)));
				db.insert("OPER", null, cv);
				db.close();
				needToReloadOper = true;
				return checkOrAddOper(name, workerId);
			}
		} else {
			return -1;
		}
	}

	List<String> getWorkersForAC()
	{
		return getStringListFromTable("XPOH", new String[]{"ID", "FIO"});
	}

	List<String> getPositionsForAC()
	{
		return getStringListFromTable("DOLG", new String[]{"ID", "NAME"});
	}

	List<String> getStructureForAC()
	{
		return getStringListFromTable("PODR", new String[]{"ID", "NAME"});
	}

	List<String> getOperationsForAC()
	{
		return getStringListFromTable("OPER", new String[]{"ID", "NAME"});
	}

	@Nullable
	private List<String> getStringListFromTable(String table, String[] fields)
	{
		Logger.call("FRVDatabase.getStringListFromTable", table, fields.toString());
		List<String> res = new ArrayList<>();
		Cursor c = this.getReadableDatabase().query(table,
				fields,
				null, null, null, null, null);

		if (c.moveToFirst())
		{
			do
			{
				res.add(c.getString(1));
			} while (c.moveToNext());
			c.close();
			Logger.exit("FRVDatabase.getStringListFromTable", res.toString());
			return res;
		} else
		{
			c.close();
			Logger.exit("FRVDatabase.getStringListFromTable", "''");
			res.add("");
			return res;
		}
	}

	List<String> getFactorsForAC()
	{
		List<String> res = new ArrayList<>();
		Cursor c = this.getReadableDatabase().query("FACT",
				new String[]{"ID", "NAME"},
				null, null, null, null, null);

		if (c.moveToFirst())
		{
			do
			{
				res.add(c.getString(1));
			} while (c.moveToNext());
			c.close();
			return res;
		} else
		{
			c.close();
			return null;
		}
	}

	private String getXpohById(String ID)
	{
		return getFieldsFromTableByID(new String[]{"FIO"}, "XPOH", ID);
	}

	private String getDolgById(String ID)
	{
		return getFieldsFromTableByID(new String[]{"NAME"}, "DOLG", ID);
	}

	private String getPodrById(String ID)
	{
		return getFieldsFromTableByID(new String[]{"NAME"}, "PODR", ID);
	}

	private String getOperById(String ID)
	{
		return getFieldsFromTableByID(new String[]{"NAME","VRUC"}, "OPER", ID);
	}

	private String getFactById(String ID)
	{
		return getFieldsFromTableByID(new String[]{"NAME","VRUC"}, "FACT", ID);
	}

	private String getFieldsFromTableByID(String[] fieldNames, String tableName, String ID)
	{
		Logger.call("FRVDatabase.getFieldsFromTableByID", fieldNames.toString(), tableName, ID);
		Cursor c = this.getReadableDatabase().query(tableName,
				fieldNames,
				"ID = ?",
				new String[]{ID},
				null, null, null);

		if (c.moveToFirst())
		{
			String a = "";
			int n = c.getColumnCount();
			for (int i = 0; i < n; i++)
			{
				String x = c.getString(i);
				if (x == null)
				{
					a += " ";
				} else
				{
					a += x;
				}
			}
			a = a.trim();
			c.close();
			Logger.exit("FRVDatabase.getFieldsFromTableByID", a);
			return a;
		} else
		{
			Logger.exit("FRVDatabase.getFieldsFromTableByID", НЕ_ЗАПОЛНЕНО);
			return НЕ_ЗАПОЛНЕНО;
		}
	}

	int getXpohByName(String fio) throws Exception
	{
		return getObjectFromTableByName("XPOH", "FIO = ?", fio);
	}

	private int getObjectFromTableByName(String tableName, String where, String value) throws Exception
	{
		Logger.call("FRVDatabase.getObjectFromTableByName", tableName, where, value);
		Cursor c = this.getReadableDatabase().query(tableName,
				new String[]{"ID"},
				where,
				new String[]{value},
				null, null, null);

		if (c.moveToFirst())
		{
			Logger.call("FRVDatabase.getObjectFromTableByName", c.toString());
			return getIDandCloseCursor(c);
		} else
		{
			Logger.call("FRVDatabase.getObjectFromTableByName", "EXCEPTION(No " + tableName + " found, q = " + value + ")");
			throw new Exception("No " + tableName + " found, q = " + value);
		}
	}

	int getPodrByName(String name) throws Exception
	{
		return getObjectFromTableByName("PODR", "NAME = ?", name);
	}

	int getDolgByName(String name) throws Exception
	{
		return getObjectFromTableByName("DOLG", "NAME = ?", name);
	}

/*	int getOperByName(String name)
	{
		Cursor c = this.getReadableDatabase().query("OPER",
				new String[]{"ID"},
				"NAME = ?",
				new String[]{name},
				null, null, null);

		if (c.moveToFirst())
		{
			return getIDandCloseCursor(c);
		} else
		{
			return -1;
		}
	}

	int getFactByName(String name)
	{
		Cursor c = this.getReadableDatabase().query("FACT",
				new String[]{"ID"},
				"NAME = ?",
				new String[]{name},
				null, null, null);

		if (c.moveToFirst())
		{
			return getIDandCloseCursor(c);
		} else
		{
			return -1;
		}
	}
*/
	private int getIDandCloseCursor(Cursor c)
	{
		int a = c.getInt(0);
		c.close();
		return a;
	}

	int addNewFRVHead(String frvdate,
	                  int n_head,
	                  int id_xpoh,
	                  int id_podr,
	                  int id_dolg,
	                  String sm_nom,
	                  String sm_dlit,
	                  String rab_gr_a,
	                  String rab_gr_b,
	                  String num_men,
	                  String worker_nik) throws Exception
	{
		Logger.call("FRVDatabase.addNewFRVHead", frvdate, sm_nom, sm_dlit, rab_gr_a, rab_gr_b, num_men, worker_nik);
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("date_time", frvdate);
		cv.put("n_head", n_head);
		cv.put("id_xpoh", id_xpoh);
		cv.put("id_podr", id_podr);
		cv.put("id_dolg", id_dolg);
		cv.put("sm_nom", sm_nom);
		cv.put("sm_dlit", sm_dlit);
		cv.put("rab_gr_a", rab_gr_a);
		cv.put("rab_gr_b", rab_gr_b);
		cv.put("num_men", num_men);
		cv.put("worker_nik", worker_nik);
		db.insert("FRVH", null, cv);
		db.close();

		Cursor c = this.getReadableDatabase().query("FRVH",
				new String[]{"ID"},
				"DATE_TIME = ? AND N_HEAD = ?",
				new String[]{frvdate, String.valueOf(n_head)},
				null, null, null);

		if (c.moveToFirst())
		{
			Logger.exit("FRVDatabase.addNewFRVHead", c.toString());
			return getIDandCloseCursor(c);
		} else
		{
			Logger.exit("FRVDatabase.addNewFRVHead", "Exception(Error creating frvhead = " + frvdate);
			throw new Exception("Error creating frvhead = " + frvdate);
		}
	}

	int addFrvBodyLine(
			String time,
			int frvh_id,
			String P3B_OTL_OP,
			boolean KO,
			boolean KP,
			int oper_id,
			int fact1_id,
			int fact2_id,
			int fact3_id,
			String comments)
	{
		Logger.call("FRVDatabase.addFrvBodyLine", time, P3B_OTL_OP, comments);
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("time", time);
		cv.put("frvh_id", frvh_id);
		cv.put("P3B_OTL_OP", P3B_OTL_OP);
		cv.put("KO", KO);
		cv.put("KP", KP);
		cv.put("oper_id", oper_id);
		cv.put("fact1_id", fact1_id);
		cv.put("fact2_id", fact2_id);
		cv.put("fact3_id", fact3_id);
		cv.put("comments", comments);
		db.insert("FRVB", null, cv);
		db.close();

		Cursor c = this.getReadableDatabase().query("FRVB",
				new String[]{"ID"},
				"TIME = ? AND FRVH_ID = ?",
				new String[]{time, String.valueOf(frvh_id)},
				null, null, null);

		if (c.moveToFirst())
		{
			Logger.exit("FRVDatabase.addFrvBodyLine", c.toString());
			return getIDandCloseCursor(c);
		} else
		{
			Logger.exit("FRVDatabase.addFrvBodyLine", "-1");
			return -1;
		}
	}


	void exportCSV(int workerID, URL geturl, URL posturl) throws Exception
	{
		Logger.call("FRVDatabase.exportCSV", String.format("%s, %s, %s", workerID, geturl, posturl));
		uploadState = EXPORT_CSV;
		List<String> frvdata =  writeFRVBodyToStringList();
		String toExport = String.format("%s.csv", getXpohById(String.valueOf(workerID)));
		uploadState = UPLOAD_CSV;
		HTTPUpload upload = new HTTPUpload(FRVDatabase.this, String.format("(%tFT%tT) %s", new Date(), new Date(), toTranslit(toExport)), frvdata, geturl, posturl);
		upload.execute();
		Logger.exit("FRVDatabase.exportCSV");
	}

	void exportCSV(int workerID) throws Exception
	{
		Logger.call("FRVDatabase.exportCSV", String.format("%s", workerID));
		uploadState = EXPORT_CSV;
		String FIO = getXpohById(String.valueOf(workerID));
		final File toExport = new File(FRVDATA, String.format("%s.csv", FIO));
		FileOutputStream f = new FileOutputStream(toExport);
//)		writeTableToFile(f, "XPOH", new String[]{"ID", "FIO", "COMMENTS"});
//		writeTableToFile(f, "PODR", new String[]{"ID", "NAME"});
//		writeTableToFile(f, "DOLG", new String[]{"ID", "NAME", "PODR_ID"});
//		writeTableToFile(f, "OPER", new String[]{"ID", "NAME"});
//		writeTableToFile(f, "FACT", new String[]{"ID", "NAME"});
		writeFRVBodyToFile(f);
		f.close();
		uploadState = UPLOAD_CSV;
		AsyncTask uploadTask = new AsyncTask()
		{

			@Override
			protected Object doInBackground(Object[] objects)
			{
				Logger.call("AsyncTask.doInBackground");
				try
				{
					new FTPUpload(FRVDatabase.this, String.format("(%tFT%tT) %s", new Date(), new Date(), toTranslit(toExport.getName())), new FileInputStream(toExport));
					Logger.exit("AsyncTask.doInBackground", "FTP");
				} catch (Exception e)
				{
					Logger.exit("AsyncTask.doInBackground", e.toString());
				}
				return null;
			}
		};
		uploadTask.execute();
		Logger.exit("FRVDatabase.exportCSV", String.format("%s", workerID));
	}

	private String toTranslit(String name)
	{
		return Translit.translit(name);
	}

	/*
		private void writeTableToFile(FileOutputStream f, String table, String[] fields) throws IOException
		{
			byte[] str;
			Cursor c;
			f.write(String.format("%s\n", table).getBytes());
			c = this.getReadableDatabase().query(table,
					fields,
					null, null, null, null, null);

			if (c.moveToFirst())
			{
				do
				{
					for (int i = 0; i < c.getColumnCount(); i++)
					{
						try
						{
							str = c.getString(i).getBytes();
						} catch (NullPointerException e)
						{
							str = НЕ_ЗАПОЛНЕНО.getBytes();
						}
						f.write(str);
						f.write("|".getBytes());
					}
					f.write("\n".getBytes());
				} while (c.moveToNext());
			}
			c.close();
		}
	*/
	private List<String> writeFRVBodyToStringList()
	{
		Logger.call("FRVDatabase.writeFRVBodyToStringList");
		List<String> res = new ArrayList<>();
		Cursor c;
		Integer nrec = 0;

		String str = "";
		String val = "";
		String[] fields = new String[]{"frvh_id", "time", "P3B_OTL_OP", "KO", "KP", "oper_id", "fact1_id", "fact2_id", "fact3_id", "comments"};
		c = this.getReadableDatabase().query("FRVB",
				fields,
				null, null, null, null, null);

		if (c.moveToFirst())
		{
			do
			{
				for (String field : fields)
				{
					val = c.getString(c.getColumnIndex(field));
					if (field.equals("frvh_id"))
						val = writeFRVToString(val);
					if (field.equals("oper_id")) val = getOperById(val);
					if (field.equals("fact1_id")) val = getFactById(val);
					if (field.equals("fact2_id")) val = getFactById(val);
					if (field.equals("fact3_id")) val = getFactById(val);
					if (val.equals("-1")) val = НЕ_ЗАПОЛНЕНО;
					try
					{
						str += val;
					} catch (NullPointerException e)
					{
						str += НЕ_ЗАПОЛНЕНО;
					}
					str += "|";
				}
				res.add(str);
				str = "";
				nrec++;
			} while (c.moveToNext());
		}
		c.close();
		Logger.exit("FRVDatabase.writeFRVBodyToStringList", nrec.toString());
		return res;
	}


	private void writeFRVBodyToFile(FileOutputStream f) throws IOException
	{
		Logger.call("FRVDatabase.writeFRVBodyToFile", f.toString());
		Cursor c;
		byte[] str;
		Integer nrec = 0;

		String[] fields = new String[]{"frvh_id", "time", "P3B_OTL_OP", "KO", "KP", "oper_id", "fact1_id", "fact2_id", "fact3_id", "comments"};
		c = this.getReadableDatabase().query("FRVB",
				fields,
				null, null, null, null, null);

		if (c.moveToFirst())
		{
			do
			{
				for (String field : fields)
				{
					String val = c.getString(c.getColumnIndex(field));
					if (field.equals("frvh_id")) writeFRVToFile(f, val);
					if (field.equals("oper_id")) val = getOperById(val);
					if (field.equals("fact1_id")) val = getFactById(val);
					if (field.equals("fact2_id")) val = getFactById(val);
					if (field.equals("fact3_id")) val = getFactById(val);
					if (val.equals("-1")) val = НЕ_ЗАПОЛНЕНО;
					if (!field.equals("frvh_id"))
					{
						try
						{
							str = val.getBytes();
						} catch (NullPointerException e)
						{
							str = НЕ_ЗАПОЛНЕНО.getBytes();
						}
						f.write(str);
						f.write("|".getBytes());
					}
				}
				f.write("\n".getBytes());
				nrec++;
			} while (c.moveToNext());
		}
		c.close();
		Logger.call("FRVDatabase.writeFRVBodyToFile", nrec.toString());
	}

	private String writeFRVToString(String ID)
	{
		Cursor c;
		String res = "";

		String[] fields = new String[]{"id", "date_time", "n_head", "id_podr", "id_dolg", "id_xpoh", "sm_nom", "sm_dlit", "rab_gr_a", "rab_gr_b", "num_men", "worker_nik"};
		c = this.getReadableDatabase().query("FRVH",
				fields,
				"ID = ?",
				new String[]{ID},
				null, null, null);

		if (c.moveToFirst())
		{
			do
			{
				for (String field : fields)
				{
					String val = c.getString(c.getColumnIndex(field));
					if (field.equals("id_podr")) val = getPodrById(val);
					if (field.equals("id_dolg")) val = getDolgById(val);
					if (field.equals("id_xpoh")) val = getXpohById(val);
					if (val.equals("-1")) val = НЕ_ЗАПОЛНЕНО;
					try
					{
						res += val;
					} catch (NullPointerException e)
					{
						res += НЕ_ЗАПОЛНЕНО;
					}
					res += "|";
				}
			} while (c.moveToNext());
		}
		c.close();
		return res;
	}

	private void writeFRVToFile(FileOutputStream f, String ID) throws IOException
	{
		Cursor c;
		byte[] str;

		String[] fields = new String[]{"id", "date_time", "n_head", "id_podr", "id_dolg", "id_xpoh", "sm_nom", "sm_dlit", "rab_gr_a", "rab_gr_b", "num_men", "worker_nik"};
		c = this.getReadableDatabase().query("FRVH",
				fields,
				"ID = ?",
				new String[]{ID},
				null, null, null);

		if (c.moveToFirst())
		{
			do
			{
				for (String field : fields)
				{
					String val = c.getString(c.getColumnIndex(field));
					if (field.equals("id_podr")) val = getPodrById(val);
					if (field.equals("id_dolg")) val = getDolgById(val);
					if (field.equals("id_xpoh")) val = getXpohById(val);
					if (val.equals("-1")) val = НЕ_ЗАПОЛНЕНО;
					try
					{
						str = val.getBytes();
					} catch (NullPointerException e)
					{
						str = НЕ_ЗАПОЛНЕНО.getBytes();
					}
					f.write(str);
					f.write("|".getBytes());
				}
			} while (c.moveToNext());
		}
		c.close();
	}
}
/*

		public FRVDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
		{
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db)
		{
//			db.execSQL(
//					"create table XRON (" +
//							"id text primary key autoincrement," +
//							"fio text," +
//							"pwdhash text," +
//							"comments text)");
//			db.execSQL(
//					"create table FRV (" +
//							"id integer primary key autoincrement," +
//							"date text," +
//							"smena integer," +
//							"frvNo integer," +
//							"working_day text," +
//							"working_scedulle text");
//			db.execSQL(
//					"create table FRV (" +
//							"id integer primary key autoincrement, " +
//							"operationName text, " +
//							"operationBTime text, " +
//							"Factor1 text, " +
//							"Factor2 text, " +
//							"Factor3 text, " +
//							"Comments text" +
//							")"
//			);
//			db.execSQL(
//					"create table RABMESTO (" +
//							"id integer primary key autoincrement," +
//							"id_podr integer," +
//							"id_dolg integer," +
//							"id_fakt integer M2M," +
//							"id_oper integer M2M," +
//							")"
//			);
//			db.execSQL(
//					"create table OPER (" +
//							"id integer primary key autoincrement, " +
//							"name text, " +
//							")"
//			);
//			db.execSQL(
//					"create table FAKT (" +
//							"id integer primary key autoincrement, " +
//							"name text, " +
//							")"
//			);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int i, int i1)
		{
			db.execSQL("drop table if exists FRV");
		}
	}

	private static final String DATABASE_NAME = "frv_database";
	private static final int DATABASE_VERSION = 1;

	public FRVDatabase(Context context)
	{
		this.sqlite = new FRVSQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public void addNewFRV(String operationName, String operationBTime, String Factor1, String Factor2, String Factor3, String Comments)
	{
//		SQLiteDatabase db = this.sqlite.getWritableDatabase();
//		ContentValues cv = new ContentValues();
//
//		cv.put("operationName", operationName);
//		cv.put("operationBTime", operationBTime);
//		db.insert("FRV", null, cv);
//		db.execSQL(
//				"insert into FRV " +
//						"(operationName, operationBTime, Factor1, Factor2, Factor3, Comments) " +
//						"values" +
//						String.format("('%s', '%s', '%s', '%s', '%s', '%s')", operationName, operationBTime, Factor1, Factor2, Factor3, Comments)
//		);
	}


	public void addNewOperation()
	{
		//
	}

	public String getFrvList()
	{
//		SQLiteDatabase db = this.sqlite.getWritableDatabase();
//		Cursor c = db.query("FRV", null, null, null, null, null, null);
//
//		if (c.moveToFirst())
//		{
//			String html = "<html><body>\n<p>Список операций</p>\n";
//			do
//			{
//				html.concat(String.format("<p>Строка %s, %s, %s</p>\n", c.getString(1), c.getString(2), c.getString(3)));
//			} while (c.moveToNext());
//			html.concat("</body></html>\n");
//			return html;
//		} else
//		{
		return "<html><p>Нечего показать</p></html>";
//		}
	}
}

ПЗВ
Оперативная работа 	ОП
Обслуживание рабочего места 	ОБС
Отдых и личные надобности 	ОТЛ
Потери рабочего времени в связи с нарушением трудовой дисциплины 	НТД
Потери рабочего времени по организационно-техническим причинам 	НТП
*/