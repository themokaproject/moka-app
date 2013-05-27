package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class HistoryEntry implements Parcelable {
	public static final Parcelable.Creator<HistoryEntry> CREATOR = new Parcelable.Creator<HistoryEntry>() {
		public HistoryEntry createFromParcel(Parcel in) {
			return new HistoryEntry(in);
		}

		public HistoryEntry[] newArray(int size) {
			return new HistoryEntry[size];
		}
	};
	@SerializedName("action")
	private String mAction;

	public HistoryEntry(String action) {
		mAction = action;
	}

	protected HistoryEntry(Parcel in) {
		mAction = in.readString();
	}

	public String getAction() {
		return mAction;
	}

	public void setAction(String action) {
		mAction = action;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(mAction);
	}
}
