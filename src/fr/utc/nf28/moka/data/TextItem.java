package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;
import fr.utc.nf28.moka.MokaApplication;

public abstract class TextItem extends MokaItem implements Parcelable {
	public TextItem(String title, MokaType type) {
		super(title, type);
	}

	protected TextItem(Parcel in) {
		super(in);
	}

	public static class PlainTextItem extends TextItem implements Parcelable {
		public static final Creator<PlainTextItem> CREATOR = new Creator<PlainTextItem>() {
			public PlainTextItem createFromParcel(Parcel in) {
				return new PlainTextItem(in);
			}

			public PlainTextItem[] newArray(int size) {
				return new PlainTextItem[size];
			}
		};

		public PlainTextItem(String title) {
			super(title, MokaApplication.MOKA_TYPES.get(TextType.PlainTextType.KEY_TYPE));
		}

		protected PlainTextItem(Parcel in) {
			super(in);
		}
	}

	public static class ListItem extends TextItem implements Parcelable {
		public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
			public ListItem createFromParcel(Parcel in) {
				return new ListItem(in);
			}

			public ListItem[] newArray(int size) {
				return new ListItem[size];
			}
		};

		public ListItem(String title) {
			super(title, MokaApplication.MOKA_TYPES.get(TextType.ListType.KEY_TYPE));
		}

		protected ListItem(Parcel in) {
			super(in);
		}
	}

	public static class PostItItem extends TextItem implements Parcelable {
		public static final Creator<PostItItem> CREATOR = new Creator<PostItItem>() {
			public PostItItem createFromParcel(Parcel in) {
				return new PostItItem(in);
			}

			public PostItItem[] newArray(int size) {
				return new PostItItem[size];
			}
		};

		public PostItItem(String title) {
			super(title, MokaApplication.MOKA_TYPES.get(TextType.PostItType.KEY_TYPE));
		}

		protected PostItItem(Parcel in) {
			super(in);
		}
	}
}
