package ru.pstu.itas.androidfrv;

import android.content.Context;
import android.widget.Button;
import java.util.TimerTask;

class DownloadFeedbackTimer extends TimerTask
{
	private String ЗАГРУЗКА_S_S = "";
	private String ЗАГРУЖЕНО_S_ЗАП_ЗА_S_СЕК = "";
	private String ОШИБКА_СОЕДИНЕНИЯ_ПОВТОРИТЕ_ПОЗЖЕ = "";
	private int downloadTime = 0;
	private CSVFromURL dataset;
	private Button buttonToOperateOn;
	private String textToRemember;
	private static final int STATE_INIT = 0;

	private static final int STATE_SHOW_CNT = 1;
	private static final int STATE_SUCCESS = 2;
	private static final int STATE_FAIL = 3;
	private static final int STATE_SHOW_BTN = 4;
	private static final int STATE_ENABLE_BTN = 5;

	private static final int MAX_DOWNLOAD_TIME = 40;

	private int state = STATE_INIT;

	DownloadFeedbackTimer(CSVFromURL dataset, Button buttonToOperateOn, Context context)
	{
		this.dataset = dataset;
		this.buttonToOperateOn = buttonToOperateOn;
		ЗАГРУЗКА_S_S = context.getString(R.string.ЗАГРУЗКА_S_S);
		ЗАГРУЖЕНО_S_ЗАП_ЗА_S_СЕК = context.getString(R.string.ЗАГРУЖЕНО_S_ЗАП_ЗА_S_СЕК);
		ОШИБКА_СОЕДИНЕНИЯ_ПОВТОРИТЕ_ПОЗЖЕ = context.getString(R.string.ОШИБКА_СОЕДИНЕНИЯ_ПОВТОРИТЕ_ПОЗЖЕ);
	}

	@Override
	public void run()
	{
		Logger.call("DownloadFeedbackTimer.run", "");
		try
		{
			switch (state)
			{
				case STATE_INIT:
				{
					if(!dataset.isLoaded) state = STATE_SHOW_CNT;
					textToRemember = buttonToOperateOn.getText().toString();
					downloadTime = 0;
					buttonToOperateOn.setEnabled(false);
					break;
				}
				case STATE_SHOW_CNT:
				{
					if(downloadTime > MAX_DOWNLOAD_TIME)
					{
						state = STATE_FAIL;
					} else
					{
						if(dataset.isLoaded) state = STATE_SUCCESS;
						else state = STATE_SHOW_CNT;
					}
					buttonToOperateOn.setText(String.format(ЗАГРУЗКА_S_S, dataset.count, ++downloadTime));
					break;
				}
				case STATE_SUCCESS:
				{
					state = STATE_SHOW_BTN;
					buttonToOperateOn.setText(String.format(ЗАГРУЖЕНО_S_ЗАП_ЗА_S_СЕК,dataset.count, downloadTime));
					break;
				}
				case STATE_FAIL:
				{
					state = STATE_SHOW_BTN;
					dataset.cancel(true);
					buttonToOperateOn.setText(ОШИБКА_СОЕДИНЕНИЯ_ПОВТОРИТЕ_ПОЗЖЕ);
					break;
				}
				case STATE_SHOW_BTN:
				{
					state = STATE_ENABLE_BTN;
					buttonToOperateOn.setText(textToRemember);
					break;
				}
				case STATE_ENABLE_BTN:
				{
					state = STATE_INIT;
					this.cancel();
					notifyAll();
					buttonToOperateOn.setEnabled(true);
					break;
				}
			}
			Logger.exit("DownloadFeedbackTimer.run", String.valueOf(state), String.valueOf(downloadTime));
		} catch (Exception e)
		{
			Logger.exit("DownloadFeedbackTimer.run", e.toString());
			// TODO : Переделать на сообщениях...
		}
	}


}

/*
			if (dataset.isLoaded)
					{
					if(flag)
					{
					this.cancel();
					buttonToOperateOn.setEnabled(true);
					buttonToOperateOn.setText(textToRemember);
					}
					else
					{
					flag = true;
					}
					} else
					{
					if(flag)
					{
					flag = false;
					textToRemember = buttonToOperateOn.getText().toString();
					}
					if(downloadTime++ > 29)
					{
					dataset.cancel(true);
					dataset.isLoaded = true;
					buttonToOperateOn.setText("Ошибка соединения, повторите позже");
					}
					else
					{
					buttonToOperateOn.setEnabled(false);
					buttonToOperateOn.setText(String.format("Загрузка... %s/%s", dataset.count, downloadTime));
					}
					}
*/