package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseItem implements Parcelable, Comparable<BaseItem> {
	public static final Parcelable.Creator<BaseItem> CREATOR = new Parcelable.Creator<BaseItem>() {
		public BaseItem createFromParcel(Parcel in) {
			return new BaseItem(in);
		}

		public BaseItem[] newArray(int size) {
			return new BaseItem[size];
		}
	};
	protected String mName;
	protected String mClassName;
	protected String mDescription;
	protected int mResId;

	public BaseItem(String name, String className, String description, int resId) {
		this(name);
		mClassName = className;
		mDescription = description;
		mResId = resId;
	}

	public BaseItem(String name) {
		mName = name;
	}

	protected BaseItem(Parcel in) {
		mName = in.readString();
		mClassName = in.readString();
		mDescription = in.readString();
		mResId = in.readInt();
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getClassName() {
		return mClassName;
	}

	public void setClassName(String className) {
		mClassName = className;
	}

	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String description) {
		mDescription = description;
	}

	public int getResId() {
		return mResId;
	}

	public void setResId(int resId) {
		mResId = resId;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(mName);
		parcel.writeString(mClassName);
		parcel.writeString(mDescription);
		parcel.writeInt(mResId);
	}

	@Override
	public int compareTo(BaseItem other) {
		return mClassName.compareTo(other.mClassName);
	}
}
