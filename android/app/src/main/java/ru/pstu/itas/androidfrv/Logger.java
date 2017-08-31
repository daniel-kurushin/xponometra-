package ru.pstu.itas.androidfrv;

import android.os.Environment;
import android.os.Build.VERSION;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

class Logger
{

	private static final boolean DO_LOG = false;
	private static File DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
	private static final int IN = 1;
	private static final int OUT = 2;

	static void call(String call, String... msgs)
	{
		writeLog(call, msgs, IN);
	}

	static void exit(String call, String... msgs)
	{
		writeLog(call, msgs, OUT);
	}

	private static void writeLog(String call, String[] msgs, Integer in_out)
	{
		if (DO_LOG)
		{
			String msg = "";
			for (String i : msgs)
				msg += i + ", ";
			try
			{
				FileOutputStream f = new FileOutputStream(new File(DIRECTORY, "call.txt"), true);
				String dir = in_out.equals(IN) ? ">>>" : "<<<";
				f.write(
						String.format("%tc: %s%s %s(%s)\n", new Date(), VERSION.CODENAME, dir, call, msg).getBytes()
				);
				f.close();
			} catch (IOException e)
			{
				//
			}
		}
	}
}
