package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseItem implements Parcelable {
	public static final Parcelable.Creator<BaseItem> CREATOR = new Parcelable.Creator<BaseItem>() {
		public BaseItem createFromParcel(Parcel in) {
			return new BaseItem(in);
		}

		public BaseItem[] newArray(int size) {
			return new BaseItem[size];
		}
	};
	protected String mClassName;
	protected String mDescription;
	protected int mResId;

	public BaseItem(String className, String description, int resId) {
		mClassName = className;
		mDescription = description;
		mResId = resId;
	}

	protected BaseItem(Parcel in) {
		mClassName = in.readString();
		mDescription = in.readString();
		mResId = in.readInt();
	}

	public void setClassName(String className) {
		mClassName = className;
	}

	public String getClassName() {
		return mClassName;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setResId(int resId) {
		mResId = resId;
	}

	public int getResId() {
		return mResId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(mClassName);
		parcel.writeString(mDescription);
		parcel.writeInt(mResId);
	}
}
