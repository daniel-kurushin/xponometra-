package ru.pstu.itas.androidfrv;

import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;

public class FTPUpload
{
	private static final String HOST = "files.000webhost.com";
	private static final String USER = "tyomin";
	private static final String PASS = "gfhffvbylb'nbkfybkbycekmafn";
	private static final String PATHNAME = "/public_html/frv";
	private final FTPClient ftpClient;
	private final FRVDatabase dataset;

	FTPUpload(FRVDatabase db, String remote, InputStream local)
	{
		Logger.call("FTPUpload", remote, local.toString());
		Integer state = 0;
		dataset = db;
		ftpClient = new FTPClient();
		try
		{
			beforeConnect();                                state++;
			ftpClient.connect(HOST);                        state++;
			ftpClient.user(USER);                           state++;
			ftpClient.pass(PASS);                           state++;
			ftpClient.changeWorkingDirectory(PATHNAME);     state++;
			ftpClient.storeFile(remote, local);             state++;
			ftpClient.disconnect();                         state++;
			onSuccess();
		} catch (IOException e)
		{
			onUploadError(e);
		}
		Logger.call("FTPUpload", state.toString());
	}

	private void onSuccess()
	{
		Logger.call("FTPUpload.onSuccess");
		dataset.uploadState = FRVDatabase.EXPORT_DONE;
		Logger.exit("FTPUpload.onSuccess");
	}

	private void beforeConnect()
	{
		Logger.call("FTPUpload.beforeConnect");
		Logger.exit("FTPUpload.beforeConnect");
	}

	private void onUploadError(IOException e)
	{
		Logger.call("FTPUpload.onUploadError", e.toString());
		dataset.uploadState = FRVDatabase.EXPORT_ERROR;
		Logger.exit("FTPUpload.onUploadError");
	}
}
