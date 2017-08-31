package ru.pstu.itas.androidfrv;

import android.content.Context;
import android.widget.Button;

import java.util.TimerTask;

public class UploadFeedbackTimer extends TimerTask
{
	private static final int STATE_INIT = 0;
	private static final int STATE_DISABLE = 1;
	private static final int STATE_SHOW_MESSAGE_1 = 2;
	private static final int STATE_SHOW_MESSAGE_2 = 3;
	private static final int STATE_RESTORE_TEXT = 4;
	private static final int STATE_ENABLE = 5;
	private static final int STATE_SHOW_ERROR = 6;
	private final Context context;
	private int uploadTime = 0;
	private final FRVDatabase dataset;
	private String textToRemember;
	private final Button buttonToOperateOn;

	private int state;

	UploadFeedbackTimer(FRVDatabase db, Button buttonToOperateOn, Context context)
	{
		this.buttonToOperateOn = buttonToOperateOn;
		this.dataset = db;
		this.uploadTime = 0;
		this.context = context;
	}

	@Override
	public void run()
	{
		Logger.call("UploadFeedbackTimer.run", "");
		try
		{
			switch (state)
			{
				case STATE_INIT:
				{
					state = STATE_DISABLE;
					this.buttonToOperateOn.setEnabled(false);
					break;
				}
				case STATE_DISABLE:
				{
					state = STATE_SHOW_MESSAGE_1;
					textToRemember = this.buttonToOperateOn.getText().toString();
					break;
				}
				case STATE_SHOW_MESSAGE_1:
				{
					if (dataset.uploadState == FRVDatabase.EXPORT_CSV) state = STATE_SHOW_MESSAGE_1;
					if (dataset.uploadState == FRVDatabase.UPLOAD_CSV) state = STATE_SHOW_MESSAGE_2;
					if (dataset.uploadState == FRVDatabase.EXPORT_DONE) state = STATE_RESTORE_TEXT;
					buttonToOperateOn.setText(context.getString(R.string.Готовим_данные));
					break;
				}
				case STATE_SHOW_MESSAGE_2:
				{
					if (dataset.uploadState == FRVDatabase.UPLOAD_CSV) state = STATE_SHOW_MESSAGE_2;
					if (dataset.uploadState == FRVDatabase.EXPORT_DONE) state = STATE_RESTORE_TEXT;
					if (dataset.uploadState == FRVDatabase.EXPORT_ERROR) state = STATE_SHOW_ERROR;
					buttonToOperateOn.setText(String.format(context.getString(R.string.Отправляем_на_сервер), ++uploadTime));
					break;
				}
				case STATE_SHOW_ERROR:
				{
					state = STATE_RESTORE_TEXT;
					buttonToOperateOn.setText(context.getString(R.string.ОШИБКА_СОЕДИНЕНИЯ_ПОВТОРИТЕ_ПОЗЖЕ));
				}
				case STATE_RESTORE_TEXT:
				{
					state = STATE_ENABLE;
					buttonToOperateOn.setText(textToRemember);
					break;
				}
				case STATE_ENABLE:
				{
					this.cancel();
					state = STATE_INIT;
					buttonToOperateOn.setEnabled(true);
					break;
				}
			}
			Logger.exit("UploadFeedbackTimer.run", String.valueOf(state), String.valueOf(uploadTime));
		} catch (Exception e)
		{
			Logger.exit("UploadFeedbackTimer.run", e.toString());
		}
	}
}

