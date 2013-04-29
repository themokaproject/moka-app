package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentItem implements Parcelable {
	public static final Parcelable.Creator<CurrentItem> CREATOR = new Parcelable.Creator<CurrentItem>() {
		public CurrentItem createFromParcel(Parcel in) {
			return new CurrentItem(in);
		}

		public CurrentItem[] newArray(int size) {
			return new CurrentItem[size];
		}
	};
	private String mName;

	public CurrentItem(String name) {
		mName = name;
	}
	protected CurrentItem(Parcel in) {
		mName = in.readString();
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(mName);
	}
}
