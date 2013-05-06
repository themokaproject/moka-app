package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public abstract class MokaItem implements Parcelable {
	private static int sIdIndex = 0;
	private int mId;
	private String mTitle;
	private MokaType mType;
	private List<HistoryEntry> mHistoryEntries;

	public MokaItem() {
		mId = sIdIndex++;
	}

	public MokaItem(String title, MokaType type) {
		this();

		mTitle = title;
		mType = type;
	}

	protected MokaItem(Parcel in) {
		mId = in.readInt();
		mTitle = in.readString();
		mType = in.readParcelable(MokaType.class.getClassLoader());
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
		parcel.writeTypedList(mHistoryEntries);
	}
}
