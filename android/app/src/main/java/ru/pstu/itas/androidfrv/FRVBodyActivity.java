package ru.pstu.itas.androidfrv;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class FRVBodyActivity extends AppCompatActivity
{
	public static int[] frvh_ids;

	private Button operTime;
	private CheckBox check1;
	private CheckBox check2;
	private CheckBox check3;
	//private RadioGroup P3B_OTL_OP;
	private RadioButton radioP3B;
	private RadioButton radioOP;
	private RadioButton radioO6C;
	private RadioButton radioOTL;
	private RadioButton radioHTD;
	private RadioButton radioHTP;
	private CheckBox checkKO;
	private CheckBox checkKP;
	private AutoCompleteTextView OPER;
	private AutoCompleteTextView FACT1;
	private AutoCompleteTextView FACT2;
	private AutoCompleteTextView FACT3;
	private EditText comments;
	private Button addNewFRVLine;
	private TextView viewFRVLines;
	private Button endFRV;

	private FRVDatabase db;
	private String textToRemember = "";

	static String html;
	static int workerId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Logger.call("FRVBodyActivity.onCreate", "");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_frvbody);

		db = new FRVDatabase(this);

		findControls();
		attachEvents();
		fillAutoCompleteValues();
		viewFRVLines.setText(Html.fromHtml(html));
		Logger.exit("FRVBodyActivity.onCreate", db.toString(), html);
	}

	private void fillAutoCompleteValues()
	{
		Logger.call("FRVBodyActivity.fillAutoCompleteValues", "");
		List<String> operList = db.getOperationsForAC();
		List<String> factList = db.getFactorsForAC();
		OPER.setAdapter(
				new ArrayAdapter<>(
						this,
						android.R.layout.simple_dropdown_item_1line,
						operList
				)
		);
		for (AutoCompleteTextView FACT : new AutoCompleteTextView[]{FACT1, FACT2, FACT3})
		{
			FACT.setAdapter(
					new ArrayAdapter<>(
							this,
							android.R.layout.simple_dropdown_item_1line,
							factList
					)
			);
		}
		Logger.exit("FRVBodyActivity.fillAutoCompleteValues", operList.toString(), factList.toString());
	}

	private void attachEvents()
	{
		Logger.call("FRVBodyActivity.attachEvents", "");
		operTime.setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						fillTime();
					}
				}
		);
		addNewFRVLine.setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						addNewFrvRecord();
					}

					private void addNewFrvRecord()
					{
						Logger.call("Anonimous.addNewFrvRecord", "");
						int oper_id, fact1_id, fact2_id, fact3_id;
						db.setNeedToReloadFact(false); db.setNeedToReloadOper(false);
						fact1_id = db.checkOrAddFact(FACT1.getText().toString(), workerId); //db.getFactByName(FACT1.getText().toString());
						fact2_id = db.checkOrAddFact(FACT2.getText().toString(), workerId); //db.getFactByName(FACT2.getText().toString());
						fact3_id = db.checkOrAddFact(FACT3.getText().toString(), workerId); //db.getFactByName(FACT3.getText().toString());
						oper_id = db.checkOrAddOper(OPER.getText().toString(), workerId); //db.getOperByName(OPER.getText().toString());
						if(FRVDatabase.isNeedToReloadOper() || FRVDatabase.isNeedToReloadFact()) fillAutoCompleteValues();
						String Index = getIndexState();
						boolean Nabl = (check1.isChecked() || check2.isChecked() || check3.isChecked());
						if (Nabl && oper_id > 0 && !textToRemember.isEmpty() && !Index.isEmpty())
						{
							createRecord(oper_id, fact1_id, fact2_id, fact3_id, Index);
						} else
						{
							showErrorMessage(Index, Nabl);
						}
						Logger.exit("Anonimous.addNewFrvRecord", String.format("%s, %s, %s, %s, %s", oper_id, fact1_id, fact2_id, fact3_id, Index));
					}

					private void createRecord(int oper_id, int fact1_id, int fact2_id, int fact3_id, String index)
					{
						Logger.call("Anonimous.createRecord", String.format("%s, %s, %s, %s, %s", oper_id, fact1_id, fact2_id, fact3_id, index));
						int n = 0;
						for (CheckBox check : new CheckBox[]{check1, check2, check3})
						{
							int frvh_id = frvh_ids[n++];
							if (frvh_id > 0 && check.isChecked())
							{
								addRecordToView(index, n);
								db.addFrvBodyLine(
										operTime.getText().toString(),
										frvh_id,
										index,
										checkKO.isChecked(),
										checkKP.isChecked(),
										oper_id,
										fact1_id,
										fact2_id,
										fact3_id,
										comments.getText().toString()
								);
							}
						}
						clearEditorFields();
						Logger.exit("Anonimous.createRecord", "");
					}

					private void clearEditorFields()
					{
						Logger.call("Anonimous.clearEditorFields", "");
						for (EditText c : new EditText[]{OPER, FACT1, FACT2, FACT3, comments})
							c.setText("");
						for (CheckBox c : new CheckBox[]{check1, check2, check3, checkKO, checkKP})
							c.setChecked(false);
						for (RadioButton c : new RadioButton[]{radioP3B, radioOP, radioO6C, radioOTL, radioHTD, radioHTP})
							c.setChecked(false);
						operTime.setText(textToRemember);
						textToRemember = "";
						operTime.setEnabled(true);
						Logger.exit("Anonimous.clearEditorFields", "");
					}

					private void addRecordToView(String index, int n)
					{
						Logger.call("Anonimous.addRecordToView", String.format("%s, %s", index, n));
						html = String.format(
								"<p>%s,%s,%s,%s,%s,%s,%s,%s,%s,%s</p>\n",
								operTime.getText().toString(),
								n,
								index,
								checkKO.isChecked() ? getString(R.string.ПРОДОЛЖ_ОПЕР) : "",
								checkKP.isChecked() ? getString(R.string.ОКОНЧ_РАБОТ) : "",
								OPER.getText().toString(),
								FACT1.getText().toString(),
								FACT2.getText().toString(),
								FACT3.getText().toString(),
								comments.getText().toString()
						) + html;
						viewFRVLines.setText(Html.fromHtml(html));
						Logger.exit("Anonimous.addRecordToView", html);
					}

					private void showErrorMessage(String index, boolean nabl)
					{
						Logger.call("Anonimous.showErrorMessage", index);
						String aMessage = "";
						if (!nabl) aMessage += getString(R.string.Не_выбран_ни_один_наблюдаемый);
						if (textToRemember.isEmpty())
							aMessage += getString(R.string.Вы_забыли_установить_время_начала_операции);
						if (index.isEmpty()) aMessage += getString(R.string.Не_выбран_ни_один_индекс);
						new AlertDialog.Builder(FRVBodyActivity.this)
								.setMessage(aMessage)
								.setPositiveButton(
										getString(R.string.Повторить_ввод), null
								)
								.show();
						Logger.exit("Anonimous.showErrorMessage", "");
					}

					private String getIndexState()
					{
						Logger.call("Anonimous.getIndexState", "");
						String index = "";
						int n_idx = 0;
						String[] Index_name = {
								getString(R.string.ПЗВ),
								getString(R.string.ОП),
								getString(R.string.ОБС),
								getString(R.string.ОТЛ),
								getString(R.string.НТД),
								getString(R.string.НТП)};
						for (RadioButton c : new RadioButton[]{radioP3B, radioOP, radioO6C, radioOTL, radioHTD, radioHTP})
						{
							if (c.isChecked())
							{
								index = Index_name[n_idx];
								break;
							}
							n_idx++;
						}
						Logger.exit("Anonimous.getIndexState", index);
						return index;
					}
				}
		);

		endFRV.setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						askWorkIsDone();
					}

					private void askWorkIsDone()
					{
						Logger.call("Anonimous.askWorkIsDone", "");
						new AlertDialog.Builder(FRVBodyActivity.this)
								.setMessage(getString(R.string.Действительно_закончить_запись))
								.setPositiveButton(
										getString(R.string.Да_работа_окончена),
										new DialogInterface.OnClickListener()
										{
											@Override
											public void onClick(DialogInterface dialogInterface, int i)
											{
												Logger.call("Anonimous.onClick", "");
												Intent intent = new Intent(
														FRVBodyActivity.this,
														FRVMainActivity.class
												);

												FRVBodyActivity.this.startActivity(intent);
												Logger.exit("Anonimous.onClick", intent.toString());
											}
										}
								)
								.setNegativeButton(
										getString(R.string.Пока_не_надо), null
								)
								.show();
						Logger.exit("Anonimous.askWorkIsDone", "");
					}
				}
		);
		Logger.exit("FRVBodyActivity.attachEvents", "");
	}

	private void fillTime()
	{
		textToRemember = operTime.getText().toString();
		operTime.setText(String.format("%tT", new Date()));
		operTime.setEnabled(false);
	}

	private void findControls()
	{
		operTime = (Button) findViewById(R.id.operTime);
		check1 = (CheckBox) findViewById(R.id.check1);
		check2 = (CheckBox) findViewById(R.id.check2);
		check3 = (CheckBox) findViewById(R.id.check3);
//		P3B_OTL_OP = (RadioGroup) findViewById(R.id.P3B_OTL_OP);
		radioP3B = (RadioButton) findViewById(R.id.radioP3B);
		radioOP = (RadioButton) findViewById(R.id.radioOP);
		radioO6C = (RadioButton) findViewById(R.id.radioO6C);
		radioOTL = (RadioButton) findViewById(R.id.radioOTL);
		radioHTD = (RadioButton) findViewById(R.id.radioHTD);
		radioHTP = (RadioButton) findViewById(R.id.radioHTP);
		checkKO = (CheckBox) findViewById(R.id.checkKO);
		checkKP = (CheckBox) findViewById(R.id.checKP);
		OPER = (AutoCompleteTextView) findViewById(R.id.OPER);
		FACT1 = (AutoCompleteTextView) findViewById(R.id.FACT1);
		FACT2 = (AutoCompleteTextView) findViewById(R.id.FACT2);
		FACT3 = (AutoCompleteTextView) findViewById(R.id.FACT3);
		comments = (EditText) findViewById(R.id.comments);
		addNewFRVLine = (Button) findViewById(R.id.addNewFRVLine);
		endFRV = (Button) findViewById(R.id.endFRV);
		viewFRVLines = (TextView) findViewById(R.id.viewFRVLines);
	}
}
