package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;

import fr.utc.nf28.moka.R;

import java.util.List;

public abstract class MediaType extends MokaType implements Parcelable {
	private static String TYPE_NAME = MediaType.class.getSimpleName();

	public MediaType(String name, String description, int resId) {
		super(name, description, resId, TYPE_NAME);
	}

	protected MediaType(Parcel in) {
		super(in);
	}

	public static void setTypeName(String typeName) {
		TYPE_NAME = typeName;
	}

	public static class ImageType extends MediaType implements Parcelable {
		public static final String KEY_TYPE = "image";
		public static final Parcelable.Creator<ImageType> CREATOR = new Parcelable.Creator<ImageType>() {
			public ImageType createFromParcel(Parcel in) {
				return new ImageType(in);
			}

			public ImageType[] newArray(int size) {
				return new ImageType[size];
			}
		};

		public ImageType(String name, String description, int resId) {
			super(name, description, resId);
		}

		public ImageType(String name, String description) {
			this(name, description, R.drawable.logo_type_picture);
		}

		protected ImageType(Parcel in) {
			super(in);
		}

        @Override
        public List<ItemData> getItemsData() {
            return null;
        }
    }

	public static class VideoType extends MediaType implements Parcelable {
		public static final String KEY_TYPE = "video";
		public static final Parcelable.Creator<VideoType> CREATOR = new Parcelable.Creator<VideoType>() {
			public VideoType createFromParcel(Parcel in) {
				return new VideoType(in);
			}

			public VideoType[] newArray(int size) {
				return new VideoType[size];
			}
		};

		public VideoType(String name, String description, int resId) {
			super(name, description, resId);
		}

		public VideoType(String name, String description) {
			this(name, description, R.drawable.logo_type_video);
		}

		protected VideoType(Parcel in) {
			super(in);
		}

        @Override
        public List<ItemData> getItemsData() {
            return null;
        }
    }

	public static class WebType extends MediaType implements Parcelable {
		public static final String KEY_TYPE = "web";
		public static final Parcelable.Creator<WebType> CREATOR = new Parcelable.Creator<WebType>() {
			public WebType createFromParcel(Parcel in) {
				return new WebType(in);
			}

			public WebType[] newArray(int size) {
				return new WebType[size];
			}
		};

		public WebType(String name, String description, int resId) {
			super(name, description, resId);
		}

		public WebType(String name, String description) {
			this(name, description, R.drawable.logo_type_web);
		}

		protected WebType(Parcel in) {
			super(in);
		}

        @Override
        public List<ItemData> getItemsData() {
            return null;
        }
    }
}
