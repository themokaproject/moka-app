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
	private String mTextValue;

	public ItemData() {
		this("");
	}

	public ItemData(String textValue) {
		mTextValue = textValue;
	}

	protected ItemData(Parcel in) {
		mTextValue = in.readString();
	}

	public String getTextValue() {
		return mTextValue;
	}

	public void setTextValue(String textValue) {
		mTextValue = textValue;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(mTextValue);
	}
}
