package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import fr.utc.nf28.moka.util.DateUtils;

import java.util.Calendar;

public abstract class MokaItem implements Parcelable {
	private static final int INVALID_ID = -1;
	protected MokaType mType;
	private int mId = INVALID_ID;
	private String mTitle;
	@SerializedName("creationDate")
	private String mCreationDate;

	public MokaItem(String title, MokaType type) {
		this(title, type, DateUtils.getFormattedDate(Calendar.getInstance().getTime()));
	}

	public MokaItem(String title, MokaType type, String creationDate) {
		mTitle = title;
		mType = type;
		mCreationDate = creationDate;
	}

	protected MokaItem(Parcel in) {
		mId = in.readInt();
		mTitle = in.readString();
		mType = in.readParcelable(MokaType.class.getClassLoader());
		mCreationDate = in.readString();
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

	public String getCreationDate() {
		return mCreationDate;
	}

	public void setCreationDate(String creationDate) {
		mCreationDate = creationDate;
	}

	public abstract void update(String field, String newValue);

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeInt(mId);
		parcel.writeString(mTitle);
		parcel.writeParcelable(mType, flags);
		parcel.writeString(mCreationDate);
	}
}
