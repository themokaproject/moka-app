package fr.utc.nf28.moka.data;

import android.os.Parcel;
import android.os.Parcelable;
import fr.utc.nf28.moka.MokaApplication;

public abstract class ComputerItem extends MokaItem implements Parcelable {
	public ComputerItem(String title, MokaType type) {
		super(title, type);
	}

	protected ComputerItem(Parcel in) {
		super(in);
	}

	public static class UmlItem extends ComputerItem implements Parcelable {
		public static final Creator<UmlItem> CREATOR = new Creator<UmlItem>() {
			public UmlItem createFromParcel(Parcel in) {
				return new UmlItem(in);
			}

			public UmlItem[] newArray(int size) {
				return new UmlItem[size];
			}
		};

		public UmlItem(String title) {
			super(title, MokaApplication.MOKA_TYPES.get(ComputerType.UmlType.KEY_TYPE));
		}

		protected UmlItem(Parcel in) {
			super(in);
		}
	}
}
