package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

public abstract class MokaItem implements Parcelable {
	public static final int INVALID_ID = -1;
	private static final String DEFAULT_CREATOR_NAME = MokaItem.class.getSimpleName();
	private int mId = INVALID_ID;
	private String mTitle;
	private MokaType mType;
	private String mCreatorName;
	private DateTime mCreationDate;

	public MokaItem(String title, MokaType type) {
		this(title, type, DEFAULT_CREATOR_NAME, new DateTime());
	}

	public MokaItem(String title, MokaType type, String creatorName, DateTime creationDate) {
		mTitle = title;
		mType = type;
		mCreatorName = creatorName;
		mCreationDate = creationDate;
	}

	protected MokaItem(Parcel in) {
		mId = in.readInt();
		mTitle = in.readString();
		mType = in.readParcelable(MokaType.class.getClassLoader());
		mCreatorName = in.readString();
		final long millis = in.readLong();
		if (millis != -1) {
			mCreationDate = new DateTime(millis);
		}
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public MokaType getType() {
		return mType;
	}

	public void setType(MokaType type) {
		mType = type;
	}

	public String getCreatorName() {
		return mCreatorName;
	}

	public void setCreatorName(String creatorName) {
		mCreatorName = creatorName;
	}

	public DateTime getCreationDate() {
		return mCreationDate;
	}

	public void setCreationDate(DateTime date) {
		mCreationDate = date;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(mId);
		parcel.writeString(mTitle);
		parcel.writeParcelable(mType, flags);
		parcel.writeString(mCreatorName);
		if (mCreationDate != null) {
			parcel.writeLong(mCreationDate.getMillis());
		} else {
			parcel.writeLong(-1);
		}
	}
}
