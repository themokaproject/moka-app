package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public abstract class MokaItem implements Parcelable {
	private static final String DEFAULT_CREATOR_NAME = MokaItem.class.getSimpleName();
	private static int sIdIndex = 0;
	private int mId;
	private String mTitle;
	private MokaType mType;
	private String mCreatorName;
	private DateTime mCreationDate;
	private List<HistoryEntry> mHistoryEntries;

	public MokaItem() {
		mId = sIdIndex++;
	}

	public MokaItem(String title, MokaType type) {
		this(title, type, DEFAULT_CREATOR_NAME, new DateTime());
	}

	public MokaItem(String title, MokaType type, String creatorName, DateTime creationDate) {
		this();

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
			mCreationDate = new DateTime(in.readLong());
		}
		if (mHistoryEntries != null) {
			in.readTypedList(mHistoryEntries, HistoryEntry.CREATOR);
		}
	}

	public int getId() {
		return mId;
	}

	public void setId(int mId) {
		this.mId = mId;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
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

	public void addHistoryEntry(HistoryEntry historyEntry) {
		if (mHistoryEntries == null) {
			mHistoryEntries = new ArrayList<HistoryEntry>();
		}
		mHistoryEntries.add(historyEntry);
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
		if (mHistoryEntries != null) {
			parcel.writeTypedList(mHistoryEntries);
		}
	}
}
