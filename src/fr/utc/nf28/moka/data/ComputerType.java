package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;
import fr.utc.nf28.moka.R;

import java.util.Map;

public abstract class ComputerType extends MokaType implements Parcelable {
	private static String TYPE_NAME = ComputerType.class.getSimpleName();

	public ComputerType(String name, String description, int resId) {
		super(name, description, resId, TYPE_NAME);
	}

	protected ComputerType(Parcel in) {
		super(in);
	}

	public static void setTypeName(String typeName) {
		TYPE_NAME = typeName;
	}

	public static class UmlType extends ComputerType implements Parcelable {
		public static final String KEY_TYPE = "UML";
		public static final Creator<UmlType> CREATOR = new Creator<UmlType>() {
			public UmlType createFromParcel(Parcel in) {
				return new UmlType(in);
			}

			public UmlType[] newArray(int size) {
				return new UmlType[size];
			}
		};

		public UmlType(String name, String description, int resId) {
			super(name, description, resId);
		}

		public UmlType(String name, String description) {
			this(name, description, R.drawable.logo_type_uml);
		}

		protected UmlType(Parcel in) {
			super(in);
		}

		@Override
		public Map<String, ItemData> fillItemData() {
			return null;
		}
	}
}
