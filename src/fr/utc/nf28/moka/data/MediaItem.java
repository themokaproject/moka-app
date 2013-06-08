package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;
import fr.utc.nf28.moka.MokaApplication;

public abstract class MediaItem extends MokaItem implements Parcelable {
	private String mUrl;

	public MediaItem(String title, MokaType type) {
		this(title, type, "");
	}

	public MediaItem(String title, MokaType type, String url) {
		super(title, type);
		setUrl(url);
	}

	protected MediaItem(Parcel in) {
		super(in);
		mUrl = in.readString();
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
		updateTypeValueUrl(url);
	}

	@Override
	public void update(String field, String newValue) {
		if (MediaType.KEY_URL.equals(field)) {
			setUrl(newValue);
		}
	}

	protected abstract void updateTypeValueUrl(String url);

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		super.writeToParcel(parcel, flags);
		parcel.writeString(mUrl);
	}

	public static final class ImageItem extends MediaItem implements Parcelable {
		public static final Parcelable.Creator<ImageItem> CREATOR = new Parcelable.Creator<ImageItem>() {
			public ImageItem createFromParcel(Parcel in) {
				return new ImageItem(in);
			}

			public ImageItem[] newArray(int size) {
				return new ImageItem[size];
			}
		};

		public ImageItem(String title) {
			super(title, MokaApplication.MOKA_TYPES.get(MediaType.ImageType.KEY_TYPE));
		}

		protected ImageItem(Parcel in) {
			super(in);
		}

		@Override
		protected void updateTypeValueUrl(String url) {
			mType.getFieldValue(MediaType.ImageType.KEY_URL_UPLOAD).setTextValue(url);
		}
	}

	public static final class VideoItem extends MediaItem implements Parcelable {
		public static final Parcelable.Creator<VideoItem> CREATOR = new Parcelable.Creator<VideoItem>() {
			public VideoItem createFromParcel(Parcel in) {
				return new VideoItem(in);
			}

			public VideoItem[] newArray(int size) {
				return new VideoItem[size];
			}
		};

		public VideoItem(String title) {
			super(title, MokaApplication.MOKA_TYPES.get(MediaType.VideoType.KEY_TYPE));
		}

		protected VideoItem(Parcel in) {
			super(in);
		}

		@Override
		protected void updateTypeValueUrl(String url) {
			mType.getFieldValue(MediaType.KEY_URL).setTextValue(url);
		}
	}

	public static final class WebItem extends MediaItem implements Parcelable {
		public static final Parcelable.Creator<WebItem> CREATOR = new Parcelable.Creator<WebItem>() {
			public WebItem createFromParcel(Parcel in) {
				return new WebItem(in);
			}

			public WebItem[] newArray(int size) {
				return new WebItem[size];
			}
		};

		public WebItem(String title) {
			super(title, MokaApplication.MOKA_TYPES.get(MediaType.WebType.KEY_TYPE));
		}

		protected WebItem(Parcel in) {
			super(in);
		}

		@Override
		protected void updateTypeValueUrl(String url) {
			mType.getFieldValue(MediaType.KEY_URL).setTextValue(url);
		}
	}
}
