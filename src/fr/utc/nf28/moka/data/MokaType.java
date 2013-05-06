package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class MokaType implements Parcelable, Comparable<MokaType> {
	protected String mDescription;
	protected int mResId;
	protected String mTypeName;
	private String mName;

	public MokaType(String name, String description, int resId, String typeName) {
		mName = name;
		mDescription = description;
		mResId = resId;
		mTypeName = typeName;
	}

	protected MokaType(Parcel in) {
		mName = in.readString();
		mDescription = in.readString();
		mResId = in.readInt();
		mTypeName = in.readString();
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
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

	public String getTypeName() {
		return mTypeName;
	}

	public void setTypeName(String typeName) {
		mTypeName = typeName;
	}

	@Override
	public int compareTo(MokaType other) {
		return mTypeName.compareTo(other.mTypeName);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(mName);
		parcel.writeString(mDescription);
		parcel.writeInt(mResId);
		parcel.writeString(mTypeName);
	}
}
