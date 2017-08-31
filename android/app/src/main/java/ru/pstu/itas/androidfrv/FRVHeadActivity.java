package ru.pstu.itas.androidfrv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import java.util.Date;
import java.util.List;

import static ru.pstu.itas.androidfrv.R.id.returnBtn;
import static ru.pstu.itas.androidfrv.R.id.makeFRVBtn;

public class FRVHeadActivity extends AppCompatActivity
{

	public static int workerId;
	public FRVDatabase db;
	public EditText date_time_1;
	public AutoCompleteTextView podr_1;
	public AutoCompleteTextView dolg_1;
	public EditText sm_nom_1;
	public EditText sm_dlit_1;
	public EditText rab_gr_1_a;
	public EditText rab_gr_1_b;
	public EditText num_men_1;
	public EditText worker_nik_1;
	public EditText date_time_2;
	public AutoCompleteTextView podr_2;
	public AutoCompleteTextView dolg_2;
	public EditText sm_nom_2;
	public EditText sm_dlit_2;
	public EditText rab_gr_2_a;
	public EditText rab_gr_2_b;
	public EditText num_men_2;
	public EditText worker_nik_2;
	public EditText date_time_3;
	public AutoCompleteTextView podr_3;
	public AutoCompleteTextView dolg_3;
	public EditText sm_nom_3;
	public EditText sm_dlit_3;
	public EditText rab_gr_3_a;
	public EditText rab_gr_3_b;
	public EditText num_men_3;
	public EditText worker_nik_3;

	int[] frvh_ids = new int[] {-1,-1,-1};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Logger.call("FRVHeadActivity.onCreate", "");
		setContentView(R.layout.activity_frvhead);

		db = new FRVDatabase(this);

		date_time_1 = (EditText) findViewById(R.id.date_time_1);
		podr_1 = (AutoCompleteTextView) findViewById(R.id.podr_1);
		dolg_1 = (AutoCompleteTextView) findViewById(R.id.dolg_1);
		sm_nom_1 = (EditText) findViewById(R.id.sm_nom_1);
		sm_dlit_1 = (EditText) findViewById(R.id.sm_dlit_1);
		rab_gr_1_a = (EditText) findViewById(R.id.rab_gr_1_a);
		rab_gr_1_b = (EditText) findViewById(R.id.rab_gr_1_b);
		num_men_1 = (EditText) findViewById(R.id.num_men_1);
		worker_nik_1 = (EditText) findViewById(R.id.worker_nik_1);
		date_time_2 = (EditText) findViewById(R.id.date_time_2);
		date_time_2 = (EditText) findViewById(R.id.date_time_2);
		podr_2 = (AutoCompleteTextView) findViewById(R.id.podr_2);
		dolg_2 = (AutoCompleteTextView) findViewById(R.id.dolg_2);
		sm_nom_2 = (EditText) findViewById(R.id.sm_nom_2);
		sm_dlit_2 = (EditText) findViewById(R.id.sm_dlit_2);
		rab_gr_2_a = (EditText) findViewById(R.id.rab_gr_2_a);
		rab_gr_2_b = (EditText) findViewById(R.id.rab_gr_2_b);
		num_men_2 = (EditText) findViewById(R.id.num_men_2);
		worker_nik_2 = (EditText) findViewById(R.id.worker_nik_2);
		date_time_3 = (EditText) findViewById(R.id.date_time_3);
		podr_3 = (AutoCompleteTextView) findViewById(R.id.podr_3);
		dolg_3 = (AutoCompleteTextView) findViewById(R.id.dolg_3);
		sm_nom_3 = (EditText) findViewById(R.id.sm_nom_3);
		sm_dlit_3 = (EditText) findViewById(R.id.sm_dlit_3);
		rab_gr_3_a = (EditText) findViewById(R.id.rab_gr_3_a);
		rab_gr_3_b = (EditText) findViewById(R.id.rab_gr_3_b);
		num_men_3 = (EditText) findViewById(R.id.num_men_3);
		worker_nik_3 = (EditText) findViewById(R.id.worker_nik_3);

		makeReturnBtn();
		makeFillFRVBtn();
		attachEvents();
		fillAutocompleteValues(podr_1, dolg_1, podr_2, dolg_2, podr_3, dolg_3);
		fillCurrentDateTime(date_time_1, date_time_2, date_time_3);
		setInputTypes(sm_nom_1, sm_dlit_1, rab_gr_1_a, rab_gr_1_b, num_men_1, sm_nom_2, sm_dlit_2, rab_gr_2_a, rab_gr_2_b, num_men_2, sm_nom_3, sm_dlit_3, rab_gr_3_a, rab_gr_3_b, num_men_3);

		Logger.exit("FRVHeadActivity.onCreate", db.toString());
	}

	private void attachEvents()
	{
		Logger.call("FRVHeadActivity.attachEvents","");
		Logger.exit("FRVHeadActivity.attachEvents","");
	}

	private void setInputTypes(EditText sm_nom_1, EditText sm_dlit_1, EditText rab_gr_1_a, EditText rab_gr_1_b, EditText num_men_1, EditText sm_nom_2, EditText sm_dlit_2, EditText rab_gr_2_a, EditText rab_gr_2_b, EditText num_men_2, EditText sm_nom_3, EditText sm_dlit_3, EditText rab_gr_3_a, EditText rab_gr_3_b, EditText num_men_3)
	{
		Logger.call("FRVHeadActivity.setInputTypes", "");
		EditText edits[] = {sm_dlit_1, sm_nom_1, num_men_1, sm_nom_2, sm_dlit_2, num_men_2, sm_nom_3, sm_dlit_3, num_men_3, rab_gr_1_a, rab_gr_1_b, rab_gr_2_a, rab_gr_2_b, rab_gr_3_a, rab_gr_3_b};

		for (EditText edit : edits)
		{
			edit.setInputType(InputType.TYPE_CLASS_PHONE);
		}
		Logger.exit("FRVHeadActivity.setInputTypes", "");
	}

	private void fillCurrentDateTime(EditText date_time_1, EditText date_time_2, EditText date_time_3)
	{
		Logger.call("FRVHeadActivity.fillCurrentDateTime", date_time_1.toString());
		EditText datetime[] = {date_time_1, date_time_2, date_time_3};

		String ret = "";
		for (EditText aDatetime : datetime)
		{
			Date x = new Date();
			ret = String.format("%tY.%tm.%tdT%tT", x, x, x, x);
			aDatetime.setText(ret);
		}
		Logger.call("FRVHeadActivity.fillCurrentDateTime", ret);
	}

	private void fillAutocompleteValues(AutoCompleteTextView podr_1, AutoCompleteTextView dolg_1, AutoCompleteTextView podr_2, AutoCompleteTextView dolg_2, AutoCompleteTextView podr_3, AutoCompleteTextView dolg_3)
	{
		Logger.call("FRVHeadActivity.fillAutocompleteValues", "");
		AutoCompleteTextView podr[] = {podr_1, podr_2, podr_3};
		AutoCompleteTextView dolg[] = {dolg_1, dolg_2, dolg_3};

		List<String> dolgList = db.getPositionsForAC();
		List<String> podrList = db.getStructureForAC();


		for (int i = 0; i < podr.length; i++)
		{
			podr[i].setAdapter(
					new ArrayAdapter<>(
							this,
							android.R.layout.simple_dropdown_item_1line,
							podrList
					)
			);
			dolg[i].setAdapter(
					new ArrayAdapter<>(
							this,
							android.R.layout.simple_dropdown_item_1line,
							dolgList
					)
			);
		}
		Logger.exit("FRVHeadActivity.fillAutocompleteValues", podrList.toString(), dolgList.toString());
	}

	private void makeFillFRVBtn()
	{
		Logger.call("FRVHeadActivity.makeFillFRVBtn", "");
		findViewById(makeFRVBtn).setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						createFRVHeadRecords();
						switchToFRVBody();
					}
				}
		);
		Logger.exit("FRVHeadActivity.makeFillFRVBtn", "");
	}

	private void createFRVHeadRecords()
	{
		Logger.call("FRVHeadActivity.createFRVHeadRecords", "");
		try
		{
			AutoCompleteTextView podrs[] = {podr_1, podr_2, podr_3};
			AutoCompleteTextView dolgs[] = {dolg_1, dolg_2, dolg_3};
			EditText dates[] = {date_time_1, date_time_2, date_time_3};
			EditText sm_noms[] = {sm_nom_1, sm_nom_2, sm_nom_3};
			EditText sm_dlits[] = {sm_dlit_1, sm_dlit_2, sm_dlit_3};
			EditText rab_grs_a[] = {rab_gr_1_a, rab_gr_2_a, rab_gr_3_a};
			EditText rab_grs_b[] = {rab_gr_1_b, rab_gr_2_b, rab_gr_3_b};
			EditText num_mens[] = {num_men_1, num_men_2, num_men_3};
			EditText worker_niks[] = {worker_nik_1, worker_nik_2, worker_nik_3};

			for (int n_head = 0; n_head < podrs.length; n_head++)
			{
				String podr_name = podrs[n_head].getText().toString();
				String dolg_name = dolgs[n_head].getText().toString();
				if (!podr_name.isEmpty() && !dolg_name.isEmpty())
				{
					int id_podr = db.getPodrByName(podr_name);
					int id_dolg = db.getDolgByName(dolg_name);

					int id_head = db.addNewFRVHead(
							dates[n_head].getText().toString(),
							n_head,
							workerId,
							id_podr,
							id_dolg,
							sm_noms[n_head].getText().toString(),
							sm_dlits[n_head].getText().toString(),
							rab_grs_a[n_head].getText().toString(),
							rab_grs_b[n_head].getText().toString(),
							num_mens[n_head].getText().toString(),
							worker_niks[n_head].getText().toString()
					);
					frvh_ids[n_head] = id_head;
				}
			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}

		Logger.exit("FRVHeadActivity.createFRVHeadRecords", frvh_ids.toString());
	}

	private void switchToFRVBody()
	{
		Logger.call("FRVHeadActivity.switchToFRVBody", "");
		FRVBodyActivity.frvh_ids = frvh_ids;
		FRVBodyActivity.html = "";
		FRVBodyActivity.workerId = workerId;
		Intent intent = new Intent(
				FRVHeadActivity.this,
				FRVBodyActivity.class
		);

		FRVHeadActivity.this.startActivity(intent);
		Logger.exit("FRVHeadActivity.switchToFRVBody", frvh_ids.toString(), intent.toString());
	}

	private void makeReturnBtn()
	{
		Logger.call("FRVHeadActivity.makeReturnBtn", "");
		findViewById(returnBtn).setOnClickListener(
				new View.OnClickListener()
				{
					@Override
					public void onClick(View view)
					{
						Intent intent = new Intent(
								FRVHeadActivity.this,
								FRVMainActivity.class
						);

						FRVHeadActivity.this.startActivity(intent);
					}
				}
		);
		Logger.exit("FRVHeadActivity.makeReturnBtn", "");
	}
}

/*
*
Дата/Время
Подразделение
Должность
Номер смены
Длительность смены
Рабочий график (1 через 3)
Количество смен в сутки
Никнэйм
Подразделение
*/

