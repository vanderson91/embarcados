/*	Este arquivo é parte do programa MonitorLog

    MonitorLog é um software livre; você pode redistribui-lo e/ou 
    modificá-lo dentro dos termos da Licença Pública Geral GNU como 
    publicada pela Fundação do Software Livre (FSF); na versão 2 da 
    Licença, ou qualquer versão.

    Este programa é distribuído na esperança que possa ser  útil, 
    mas SEM NENHUMA GARANTIA; sem uma garantia implícita de ADEQUAÇÂO a qualquer
    MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a

    Licença Pública Geral GNU para maiores detalhes.
    Você deve ter recebido uma cópia da Licença Pública Geral GNU
    junto com este programa, se não, escreva para a Fundação do Software
    Livre(FSF) Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
    
 */

package service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.monitorlog.R;

public class MonitorService extends Service implements LocationListener,
		SensorEventListener, Runnable {

	private static String TAG = "MonitorService";
	private SensorManager sensorManager;
	private float x, y, z;
	private Double coordenadasGPS[] = new Double[3];
	private boolean active;
	private long time = 1000;
	private String path = "/sdcard/MonitorLog";
	private String appName;
	private String datas;
	private String pathBatteryCapacity = "/sys/class/power_supply/battery/capacity";
	private String pathBatteryStatus = "/sys/class/power_supply/battery/status";
	private long inicialTaxaTotalTx = Long.parseLong(getTaxaTotalTx());
	private long inicialTaxaTotalRx = Long.parseLong(getTaxaTotalRx());
	private long inicialTaxaMobileTx = Long.parseLong(getTaxaMobileTx());
	private long inicialTaxaMobileRx = Long.parseLong(getTaxaMobileRx());

	@Override
	public void onCreate() {
		super.onCreate();
		appName = "monitor" + getString(R.string.app_name) + "-"
				+ getTimestamp() + ".txt";
		active = true;
		new Thread(this).start();
		Log.i(TAG, "onCreate()");

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		active = false;
		Log.i(TAG, "onDestroy()");
	}

	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	public void execute() {
		PrintWriter outPut = null;
		File file = new File(path);
		file.mkdirs();

		Log.i(TAG, "run()");
		try {
			outPut = new PrintWriter(new File(file.getPath(), appName));
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		outPut.println("timestamp;taxaTotalTx;taxaTotalRx;taxaMobileTx;taxaMobileRx;wiFiLinkSpeed;wiFiRSSI;TypeNameConnection;batteryCapacity;batteryStatus");

		while (active) {
			datas = getTimestamp() + ";" + getTaxaTotalTx() + ";"
					+ getTaxaTotalRx() + ";" + getTaxaMobileTx() + ";"
					+ getTaxaMobileRx() + ";" + getWiFiLinkSpeed() + ";"
					+ getWiFiRSSI() + ";" + getTypeNameConnection() + ";"
					+ getBatteryCapacity() + ";" + getBatteryStatus();
			outPut.println(datas);
			try {
				Thread.sleep(getTimeThread());
			} catch (InterruptedException e) {
				Log.e(TAG, e.getMessage(), e);
			}
		}

		outPut.close();
	}

	@Override
	public void run() {
		execute();

	}

	public long getTimeThread() {
		return time;
	}

	public String getBatteryCapacity() {
		String batteryCapacity = null;
		Scanner scan = null;
		try {
			scan = new Scanner(new File(pathBatteryCapacity));
			if (scan.hasNext()) {
				batteryCapacity = scan.nextLine();
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		scan.close();
		Log.i(TAG, "getBatteryCapacity() = " + batteryCapacity);

		return batteryCapacity;
	}

	public String getBatteryStatus() {
		String batteryStatus = null;
		Scanner scan = null;
		try {
			scan = new Scanner(new File(pathBatteryStatus));
			if (scan.hasNext()) {
				batteryStatus = scan.nextLine();
			}
		} catch (FileNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		scan.close();
		Log.i(TAG, "getBatteryStatus() = " + batteryStatus);

		return batteryStatus;
	}

	public String getDate() {
		Date date = new Date();
		String dateString = date.toString();
		Log.i(TAG, "getDate() = " + dateString);

		return dateString;
	}

	public String getTimestamp() {
		Date date = new Date();
		String dateTimestamp = String.valueOf(date.getTime() / 1000);
		Log.i(TAG, "getTimestamp() = " + dateTimestamp);

		return dateTimestamp;
	}

	public String getTaxaTotalTx() {
		String taxaTotalTx = String.valueOf(TrafficStats.getTotalTxBytes()
				- inicialTaxaTotalTx);
		Log.i(TAG, "getTaxaTotalTx() = " + taxaTotalTx);

		return taxaTotalTx;
	}

	public String getTaxaTotalRx() {
		String taxaTotalRx = String.valueOf(TrafficStats.getTotalRxBytes()
				- inicialTaxaTotalRx);
		Log.i(TAG, "getTaxaTotalRx() = " + taxaTotalRx);

		return taxaTotalRx;
	}

	public String getTaxaMobileTx() {
		String taxaMobileTx = String.valueOf(TrafficStats.getMobileTxBytes()
				- inicialTaxaMobileTx);
		Log.i(TAG, "getTaxaMobileTx() = " + taxaMobileTx);

		return taxaMobileTx;
	}

	public String getTaxaMobileRx() {
		String taxaMobileRx = String.valueOf(TrafficStats.getMobileRxBytes()
				- inicialTaxaMobileRx);
		Log.i(TAG, "getTaxaMobileRx() = " + taxaMobileRx);

		return taxaMobileRx;
	}

	public String getGPS() {
		String gps = String.valueOf(coordenadasGPS[0]) + ";"
				+ String.valueOf(coordenadasGPS[1]);
		Log.i(TAG, "getGPS() = " + gps);

		return gps;
	}

	public String getTypeNameConnection() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		String infoString = "Error";
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].isConnected()) {
						infoString = info[i].getTypeName();
						break;
					}
				}
			}
		}

		Log.i(TAG, "getTypeNameConnection() = " + infoString);
		return infoString;
	}

	public String getWiFiLinkSpeed() {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String wiFiLinkSpeed = String.valueOf(wifiInfo.getLinkSpeed());
		Log.i(TAG, "getWiFiLinkSpeed() = " + wiFiLinkSpeed);

		return wiFiLinkSpeed;
	}

	public String getWiFiRSSI() {
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String wiFiRSSI = String.valueOf(wifiInfo.getRssi());
		Log.i(TAG, "getWiFiRSSI() = " + wiFiRSSI);

		return wiFiRSSI;
	}

	public void accelerometer() {
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorManager.registerListener(this,
				sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		Log.i(TAG, "accelerometer()");
	}

	public String getAccelerometer() {
		String accelerometer = String.valueOf(x) + ";" + String.valueOf(y)
				+ ";" + String.valueOf(z);
		Log.i(TAG, "getAccelerometer() = " + accelerometer);

		return accelerometer;
	}

	private void gps() {
		Location loc = getLocationManager().getLastKnownLocation(
				LocationManager.GPS_PROVIDER);

		if (loc != null) {
			coordenadasGPS[0] = loc.getLatitude();
			coordenadasGPS[1] = loc.getLongitude();
			Log.i("getLocationManager", "ultima localizacao: " + getGPS());
		}
		getLocationManager().requestLocationUpdates(
				LocationManager.GPS_PROVIDER, 0, 0, this);

		Log.i(TAG, "gps()");
	}

	private LocationManager getLocationManager() {
		Log.i(TAG, "getLocationManager()");
		return (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			x = event.values[0];
			y = event.values[1];
			z = event.values[2];
		}
	}

	public void onLocationChanged(Location location) {
		coordenadasGPS[0] = location.getLatitude();
		coordenadasGPS[1] = location.getLongitude();
		Log.i("getLocationManager", "ultima localizacao: " + getGPS());
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}
