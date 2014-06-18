package com.monitorlog;

import service.MonitorServiceConexao;
import service.MonitorServiceConexao.MonitorBinder;
import service.widget.MonitorServiceInterface;
import com.monitorlog.R;
import android.app.Activity;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements ServiceConnection {
	/** Called when the activity is first created. */

	private MonitorServiceInterface monitorServiceInterface;
	final ServiceConnection serviceConnection = this;
	private static String TAG = "MainActivity";
	private Button buttonStart, buttonStop;
	private boolean isServiceRunning = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		buttonStart = (Button) findViewById(R.id.button_start);
		buttonStop = (Button) findViewById(R.id.button_stop);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isServiceRunning) {
			unbindService(serviceConnection);
			Log.i(TAG, "Serviço finalizado");
			Toast.makeText(this, "Seviço finalizado", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public void onClickStartMyService(View v) {
		bindService(new Intent(this, MonitorServiceConexao.class),
				serviceConnection, Context.BIND_AUTO_CREATE);
		buttonStart.setEnabled(false);
		buttonStop.setEnabled(true);
		isServiceRunning = true;
		Log.i(TAG, "Serviço iniciado");
		Toast.makeText(this, "Seviço iniciado", Toast.LENGTH_SHORT).show();
	}

	public void onClickStopMyService(View v) {
		unbindService(serviceConnection);
		buttonStart.setEnabled(true);
		buttonStop.setEnabled(false);
		isServiceRunning = false;
		Log.i(TAG, "Serviço finalizado");
		Toast.makeText(this, "Seviço finalizado", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		MonitorBinder binder = (MonitorBinder) service;
		monitorServiceInterface = binder.getService();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		monitorServiceInterface = null;

	}
}