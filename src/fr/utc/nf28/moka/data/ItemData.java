package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemData implements Parcelable {
	public static final Parcelable.Creator<ItemData> CREATOR = new Parcelable.Creator<ItemData>() {
		public ItemData createFromParcel(Parcel in) {
			return new ItemData(in);
		}

		public ItemData[] newArray(int size) {
			return new ItemData[size];
		}
	};
	private final String mField;

	public ItemData(String field) {
		mField = field;
	}

	protected ItemData(Parcel in) {
		mField = in.readString();
	}

	public String getField() {
		return mField;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(mField);
	}
}
