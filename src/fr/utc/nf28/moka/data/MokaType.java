package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public abstract class MokaType implements Parcelable, Comparable<MokaType> {
	public static final String KEY_TITLE = "title";
	protected String mDescription;
	protected int mResId;
	protected String mCategoryName;
	private Map<String, ItemData> mItemData;
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
		final int size = in.readInt();
		if (size != -1) {
			mItemData = new HashMap<String, ItemData>(size);
			in.readMap(mItemData, ItemData.class.getClassLoader());
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

	public Map<String, ItemData> getItemData() {
		if (mItemData == null) {
			mItemData = fillItemData();
		}
		return mItemData;
	}

	public ItemData getFieldValue(String key) {
		return getItemData().get(key);
	}

	protected abstract Map<String, ItemData> fillItemData();

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
			parcel.writeInt(mItemData.size());
			parcel.writeMap(mItemData);
		} else {
			parcel.writeInt(-1);
		}
	}
}
