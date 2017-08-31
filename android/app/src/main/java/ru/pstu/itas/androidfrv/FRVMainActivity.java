package ru.pstu.itas.androidfrv;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class FRVMainActivity extends AppCompatActivity
{
	public static final String MAIN_SERVER_GETKEY = "http://tyomin.000webhostapp.com/getfrvkey.php";
	public static final String MAIN_SERVER_POSTFRV = "http://tyomin.000webhostapp.com/postfrv.php";
	public static final String TEST_SERVER_GETKEY = "http://tyomin.000webhostapp.com/test/getfrvkey.php";
	public static final String TEST_SERVER_POSTFRV = "http://tyomin.000webhostapp.com/test/postfrv.php";
	private final URL STRUCT_CSV_URL = new URL("http://tyomin.000webhostapp.com/file2.csv");
	private final URL OPERS_CSV_URL = new URL("http://tyomin.000webhostapp.com/file3.csv");
	private final URL FACTS_CSV_URL = new URL("http://tyomin.000webhostapp.com/file4.csv");
	private final URL TEST_STRUCT_CSV_URL = new URL("http://tyomin.000webhostapp.com/test/file2.csv");
	private final URL TEST_OPERS_CSV_URL = new URL("http://tyomin.000webhostapp.com/test/file3.csv");
	private final URL TEST_FACTS_CSV_URL = new URL("http://tyomin.000webhostapp.com/test/file4.csv");

	//	private final URL STRUCT_CSV_URL = new URL("http://192.168.43.223/file2.csv");
//	private final URL OPERS_CSV_URL = new URL("http://192.168.43.223/file3.csv");
//	private final URL FACTS_CSV_URL = new URL("http://192.168.43.223/file4.csv");
	public OrgsCSVdata orgsList = null;
	public OperCSVdata operList = null;
	public FactCSVdata factList = null;

	private FRVDatabase db;
	private int workerID;
	private static Timer t;
	private static String access_key = "";

	public FRVMainActivity() throws MalformedURLException
	{
		super();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		Logger.call("FRVMainActivity.onCreate", "");

		getAccessKey();

		setContentView(R.layout.activity_frvmain);
		db = new FRVDatabase(this);

		attachEvents();

		Logger.exit("FRVMainActivity.onCreate", db.toString());
	}

	private void getAccessKey()
	{
		Logger.call("FRVMainActivity.getAccessKey");
		AsyncTask<String, String, String> getk = new AsyncTask<String, String, String>()
		{
			@Override
			protected String doInBackground(String... strings)
			{
				Logger.call("Anonimous.doInBackground");
				try
				{
					TelephonyManager telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
					String devicIMEI = telephonyManager.getDeviceId();
					URLConnection c = (new URL(String.format("http://tyomin.000webhostapp.com/getaccesskey.php?key=%s", devicIMEI))).openConnection();
					c.connect();

					BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));

					String line = in.readLine();
					FRVMainActivity.access_key = line;
					Logger.exit("Anonimous.doInBackground", line);
				} catch (IOException e)
				{
					FRVMainActivity.access_key = "ERROR";
					Logger.exit("Anonimous.doInBackground", e.toString());
				}
				return null;
			}
		};
		getk.execute("");
		Logger.exit("FRVMainActivity.getAccessKey");
	}

	private void attachEvents()
	{
		Logger.call("FRVMainActivity.attachEvents", "");

		Button loadOrgStructureBtn = (Button) findViewById(R.id.loadOrgStructureBtn);
		Button loadOperListBtn = (Button) findViewById(R.id.loadOperListBtn);
		Button loadFactorListBtn = (Button) findViewById(R.id.loadFactorListBtn);
		AutoCompleteTextView workerIdEdit = (AutoCompleteTextView) findViewById(R.id.workerIdEdit);
		Button enterFRVHead = (Button) findViewById(R.id.enterFRVHead);
		Button exportFRVdata = (Button) findViewById(R.id.exportFRVdata);
		Button addWorkerBtn = (Button) findViewById(R.id.addWorkerBtn);

		setDownloadButtons(loadOrgStructureBtn, loadOperListBtn, loadFactorListBtn);
		setWorkerEditAutocomplete(workerIdEdit, addWorkerBtn);
		setFRVButtons(enterFRVHead, workerIdEdit); //enterFRVBody, workerIdEdit);
		setUploadDatabaseButtons(exportFRVdata); //, freeDatabase);

		Logger.exit("FRVMainActivity.attachEvents", "");
	}

	private void setFRVButtons(Button enterFRVHead, final EditText workerIDEdit)
	{
		Logger.call("FRVMainActivity.setFRVButtons", enterFRVHead.toString(), workerIDEdit.toString());
		enterFRVHead.setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						Logger.call("Anonimous.onClick", view.toString());
						String msg = getString(R.string.НЕОБХОДИМО_УКАЗАТЬ_ДАННЫЕ_ХРОНОМЕТРАЖИСТА);
						try
						{
							workerID = db.getXpohByName(workerIDEdit.getText().toString());
							FRVHeadActivity.workerId = workerID;
							Intent intent = new Intent(
									FRVMainActivity.this,
									FRVHeadActivity.class
							);

							FRVMainActivity.this.startActivity(intent);
							Logger.exit("Anonimous.onClick", intent.toString(), String.valueOf(workerID));
						} catch (Exception e)
						{
							showMessageBox(msg);
							Logger.exit("Anonimous.onClick", e.toString());
						}
					}
				}
		);
/*		enterFRVBody.setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						Intent intent = new Intent(
								FRVMainActivity.this,
								FRVBodyActivity.class
						);

						FRVMainActivity.this.startActivity(intent);
					}
				}
		);
		enterFRVBody.setVisibility(View.INVISIBLE);*/
		Logger.exit("FRVMainActivity.setFRVButtons", "");
	}

	private void setUploadDatabaseButtons(final Button exportFRVdata)
	{
		Logger.call("FRVMainActivity.setUploadDatabaseButtons", exportFRVdata.toString());
/*		freeDatabase.setOnClickListener(
				new View.OnClickListener()

				{
					@Override
					public void onClick(View v)
					{
						new AlertDialog.Builder(FRVMainActivity.this)
								.setMessage("Действительно удалить все?")
								.setPositiveButton(
										"Да, удалить все!",
										new DialogInterface.OnClickListener()
										{
											@Override
											public void onClick(DialogInterface dialogInterface, int i)
											{
												db.freeDatabase();
											}
										}
								)
								.setNegativeButton(
										"Пока не надо", null
								)
								.show();
					}
				}
		);*/
		exportFRVdata.setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						Logger.call("Anonimous.onClick", view.toString());

						String msg = getString(R.string.НЕОБХОДИМО_УКАЗАТЬ_ДАННЫЕ_ХРОНОМЕТРАЖИСТА);
						try
						{
							workerID = db.getXpohByName(((EditText) findViewById(R.id.workerIdEdit)).getText().toString());

							FRVMainActivity.t = new Timer();
							t.scheduleAtFixedRate(new UploadFeedbackTimer(db, exportFRVdata, getBaseContext()), 100, 1000);

							msg = getString(R.string.ОШИБКА_ЗАГРУЗКИ_НА_СЕРВЕР);
							db.exportCSV(workerID, new URL(MAIN_SERVER_GETKEY), new URL(MAIN_SERVER_POSTFRV));
							// db.exportCSV(workerID, new URL("http://192.168.1.12/getfrvkey.php"), new URL("http://192.168.1.12/postfrv.php"));
							Logger.exit("Anonimous.onClick", "");

						} catch (Exception e)
						{
							showMessageBox(msg);
							Logger.exit("Anonimous.onClick", e.toString());
						}
					}
				}
		);
		Logger.exit("FRVMainActivity.setUploadDatabaseButtons", "");
	}

	private void showMessageBox(String msg)
	{
		new AlertDialog.Builder(FRVMainActivity.this)
				.setMessage(msg)
				.setPositiveButton(
						getString(R.string.OK), null
				)
				.show();
	}

	private void setDownloadButtons(final Button loadOrgStructureBtn, final Button loadOperListBtn, final Button loadFactorListBtn)
	{
		Logger.call("FRVMainActivity.setDownloadButtons", loadFactorListBtn.toString(), loadOperListBtn.toString(), loadFactorListBtn.toString());
		loadOrgStructureBtn.setOnClickListener(
				new View.OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						orgsList = new OrgsCSVdata(STRUCT_CSV_URL, db);
						FRVMainActivity.t = new Timer();
						t.scheduleAtFixedRate(new DownloadFeedbackTimer(orgsList, loadOrgStructureBtn, getBaseContext()), 100, 1000);
						orgsList.execute();
					}
				}
		);
		loadOperListBtn.setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						operList = new OperCSVdata(OPERS_CSV_URL, db);
						FRVMainActivity.t = new Timer();
						t.scheduleAtFixedRate(new DownloadFeedbackTimer(operList, loadOperListBtn, getBaseContext()), 100, 1000);
						operList.execute();
					}
				}
		);
		loadFactorListBtn.setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						factList = new FactCSVdata(FACTS_CSV_URL, db);
						FRVMainActivity.t = new Timer();
						t.scheduleAtFixedRate(new DownloadFeedbackTimer(factList, loadFactorListBtn, getBaseContext()), 100, 1000);
						factList.execute();
					}
				}
		);
		Logger.exit("FRVMainActivity.setDownloadButtons", "");
	}

	private void setWorkerEditAutocomplete(final AutoCompleteTextView workerIdEdit, final Button addWorkerBtn)
	{
		Logger.call("FRVMainActivity.setWorkerEditAutocomplete", workerIdEdit.toString(), addWorkerBtn.toString());
		List<String> workerList = db.getWorkersForAC();
		if (workerList != null && workerList.size() == 1)
		{
			workerIdEdit.setText(workerList.get(0));
		} else
		{
			if (workerList == null) workerList = new ArrayList<>();
			workerIdEdit.setAdapter(
					new ArrayAdapter<>(
							this,
							android.R.layout.simple_dropdown_item_1line,
							workerList
					)
			);
		}
		workerIdEdit.setOnEditorActionListener(
				new TextView.OnEditorActionListener()
				{
					@Override
					public boolean onEditorAction(TextView textView, int actionId, KeyEvent event)
					{
						if (actionId == EditorInfo.IME_ACTION_SEARCH ||
								actionId == EditorInfo.IME_ACTION_DONE ||
								event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
						{
							workerID = db.checkOrAddWorker(workerIdEdit.getText().toString());
							FRVMainActivity.t = new Timer();
							t.scheduleAtFixedRate(new WorkerIdFeedbackTimer(addWorkerBtn, getBaseContext()), 10, 1000);
							return true;
						}
						return false;
					}
				}
		);
		addWorkerBtn.setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						workerID = db.checkOrAddWorker(workerIdEdit.getText().toString());
						FRVMainActivity.t = new Timer();
						t.scheduleAtFixedRate(new WorkerIdFeedbackTimer(addWorkerBtn, getBaseContext()), 100, 1000);
					}
				}
		);
		Logger.exit("FRVMainActivity.setWorkerEditAutocomplete", workerList.toString());
	}

}