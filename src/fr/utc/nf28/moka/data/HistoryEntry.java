package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

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
	@SerializedName("date")
	private String mDate;

	public HistoryEntry(String action, String date) {
		mAction = action;
		mDate = date;
	}

	public HistoryEntry(String action, Date date) {
		this(action, date.toString());
	}

	protected HistoryEntry(Parcel in) {
		mAction = in.readString();
		mDate = in.readString();
	}

	public String getAction() {
		return mAction;
	}

	public void setAction(String action) {
		mAction = action;
	}

	public String getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date.toString();
	}

	public void setDate(String date) {
		mDate = date;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(mAction);
		parcel.writeString(mDate);
	}
}
