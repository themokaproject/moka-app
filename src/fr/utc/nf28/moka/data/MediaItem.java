package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;
import fr.utc.nf28.moka.MokaApplication;

public abstract class MediaItem extends MokaItem implements Parcelable {
	public MediaItem(String title) {
		super(title, MokaApplication.MOKA_TYPES.get(MediaType.ImageType.KEY_TYPE));
	}

	protected MediaItem(Parcel in) {
		super(in);
	}

	public static class ImageItem extends MediaItem implements Parcelable {
		public static final Parcelable.Creator<ImageItem> CREATOR = new Parcelable.Creator<ImageItem>() {
			public ImageItem createFromParcel(Parcel in) {
				return new ImageItem(in);
			}

			public ImageItem[] newArray(int size) {
				return new ImageItem[size];
			}
		};

		public ImageItem(String title) {
			super(title);
		}

		protected ImageItem(Parcel in) {
			super(in);
		}
	}

	public static class VideoItem extends MediaItem implements Parcelable {
		public static final Parcelable.Creator<VideoItem> CREATOR = new Parcelable.Creator<VideoItem>() {
			public VideoItem createFromParcel(Parcel in) {
				return new VideoItem(in);
			}

			public VideoItem[] newArray(int size) {
				return new VideoItem[size];
			}
		};

		public VideoItem(String title) {
			super(title);
		}

		protected VideoItem(Parcel in) {
			super(in);
		}
	}

	public static class WebItem extends MediaItem implements Parcelable {
		public static final Parcelable.Creator<WebItem> CREATOR = new Parcelable.Creator<WebItem>() {
			public WebItem createFromParcel(Parcel in) {
				return new WebItem(in);
			}

			public WebItem[] newArray(int size) {
				return new WebItem[size];
			}
		};

		public WebItem(String title) {
			super(title);
		}

		protected WebItem(Parcel in) {
			super(in);
		}
	}
}
