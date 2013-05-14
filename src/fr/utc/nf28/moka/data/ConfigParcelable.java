package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ConfigParcelable implements Parcelable {

	private String mSSID;
	private String mPWD;
	private String mIpAgent;
	private String mPortAgent;


	@Override
	public int describeContents() {
		return 0;  //To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void writeToParcel(Parcel out, int i) {
		out.writeString(mSSID);
		out.writeString(mPWD);
		out.writeString(mIpAgent);
		out.writeString(mPortAgent);
	}

	public ConfigParcelable(String ssid, String pwd, String ipAgent, String portAgent) {
		mSSID = ssid;
		mPWD = pwd;
		mIpAgent = ipAgent;
		mPortAgent = portAgent;
	}

	public String getSSID() {
		return mSSID;
	}

	public String getPWD() {
		return mPWD;
	}

	public String getIpAgent() {
		return mIpAgent;
	}

	public String getPortAgent() {
		return mPortAgent;
	}
}
