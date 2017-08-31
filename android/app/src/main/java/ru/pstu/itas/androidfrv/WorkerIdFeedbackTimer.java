package ru.pstu.itas.androidfrv;

import android.content.Context;
import android.util.Log;
import android.widget.Button;

import java.util.TimerTask;

class WorkerIdFeedbackTimer extends TimerTask
{
	private static final int STATE_INIT = 0;
	private static final int STATE_SHOW_MESSAGE = 1;
	private static final int STATE_RESTORE_TEXT = 2;
	private static String ЗАПИСАНО = "";
	private String textToRemember;
	private final Button buttonToOperateOn;

	private int state;

	WorkerIdFeedbackTimer(Button buttonToOperateOn, Context context)
	{
		this.buttonToOperateOn = buttonToOperateOn;
		this.ЗАПИСАНО = context.getString(R.string.Записано);
	}

	@Override
	public void run()
	{
		try
		{
			switch (state)
			{
				case STATE_INIT:
				{
					state = STATE_SHOW_MESSAGE;
					textToRemember = this.buttonToOperateOn.getText().toString();
					break;
				}
				case STATE_SHOW_MESSAGE:
				{
					state = STATE_RESTORE_TEXT;
					buttonToOperateOn.setText(ЗАПИСАНО);
					break;
				}
				case STATE_RESTORE_TEXT:
				{
					this.cancel();
					state = STATE_INIT;
					buttonToOperateOn.setText(textToRemember);
					break;
				}
			}
		} catch (Exception e)
		{
			Log.e("Error:", e.toString());
		}
	}
}

