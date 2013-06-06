package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;
import fr.utc.nf28.moka.R;

import java.util.ArrayList;
import java.util.List;

public abstract class TextType extends MokaType implements Parcelable {
	private static String TYPE_NAME = TextType.class.getSimpleName();

	public TextType(String name, String description, int resId) {
		super(name, description, resId, TYPE_NAME);
	}

	protected TextType(Parcel in) {
		super(in);
	}

	public static void setTypeName(String typeName) {
		TYPE_NAME = typeName;
	}

	public static class PlainTextType extends TextType implements Parcelable {
		public static final String KEY_TYPE = "plain_text";
		public static final Parcelable.Creator<PlainTextType> CREATOR = new Parcelable.Creator<PlainTextType>() {
			public PlainTextType createFromParcel(Parcel in) {
				return new PlainTextType(in);
			}

			public PlainTextType[] newArray(int size) {
				return new PlainTextType[size];
			}
		};

		public PlainTextType(String name, String description, int resId) {
			super(name, description, resId);
		}

		public PlainTextType(String name, String description) {
			this(name, description, R.drawable.logo_type_text);
		}

		protected PlainTextType(Parcel in) {
			super(in);
		}

		@Override
		public List<ItemData> getItemsData() {
			return null;
		}
	}

	public static class ListType extends TextType implements Parcelable {
		public static final String KEY_TYPE = "list";
		public static final Parcelable.Creator<ListType> CREATOR = new Parcelable.Creator<ListType>() {
			public ListType createFromParcel(Parcel in) {
				return new ListType(in);
			}

			public ListType[] newArray(int size) {
				return new ListType[size];
			}
		};

		public ListType(String name, String description, int resId) {
			super(name, description, resId);
		}

		public ListType(String name, String description) {
			this(name, description, R.drawable.logo_type_list);
		}

		protected ListType(Parcel in) {
			super(in);
		}

		@Override
		public List<ItemData> getItemsData() {
			return null;
		}
	}

	public static class PostItType extends TextType implements Parcelable {
		public static final String KEY_TYPE = "post-it";
		public static final Parcelable.Creator<PostItType> CREATOR = new Parcelable.Creator<PostItType>() {
			public PostItType createFromParcel(Parcel in) {
				return new PostItType(in);
			}

			public PostItType[] newArray(int size) {
				return new PostItType[size];
			}
		};

		public PostItType(String name, String description, int resId) {
			super(name, description, resId);
		}

		public PostItType(String name, String description) {
			this(name, description, R.drawable.logo_type_postit);
		}

		protected PostItType(Parcel in) {
			super(in);
		}

		@Override
		public List<ItemData> getItemsData() {
			final List<ItemData> datas = new ArrayList<ItemData>();
			datas.add(new ItemData("title"));
			datas.add(new ItemData("content"));
			return datas;
		}
	}
}
