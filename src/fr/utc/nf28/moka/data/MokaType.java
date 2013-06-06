package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public abstract class MokaType implements Parcelable, Comparable<MokaType> {
	public static final String KEY_TITLE = "title";
	protected String mDescription;
	protected int mResId;
	protected String mCategoryName;
	private List<ItemData> mItemData;
	private String mName;

	public MokaType(String name, String description, int resId, String categoryName) {
		mName = name;
		mDescription = description;
		mResId = resId;
		mCategoryName = categoryName;
	}

	protected MokaType(Parcel in) {
		mName = in.readString();
		mDescription = in.readString();
		mResId = in.readInt();
		mCategoryName = in.readString();
		if (in.readLong() != -1) {
			mItemData = new ArrayList<ItemData>();
			in.readList(mItemData, ItemData.class.getClassLoader());
		}
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

	public String getCategoryName() {
		return mCategoryName;
	}

	public void setCategoryName(String categoryName) {
		mCategoryName = categoryName;
	}

	public List<ItemData> getItemData() {
		if (mItemData == null) {
			mItemData = fillItemData();
		}
		return mItemData;
	}

	protected abstract List<ItemData> fillItemData();

	@Override
	public int compareTo(MokaType other) {
		return mCategoryName.compareTo(other.mCategoryName);
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
		parcel.writeString(mCategoryName);
		if (mItemData != null) {
			parcel.writeLong(0);
			parcel.writeList(mItemData);
		} else {
			parcel.writeLong(-1);
		}
	}
}
