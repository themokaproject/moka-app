package fr.utc.nf28.moka;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import fr.utc.nf28.moka.data.MokaItem;

import java.util.Collections;
import java.util.List;

public class CurrentItemAdapter extends BaseMokaAdapter {
	private List<MokaItem> mCurrentItems = Collections.emptyList();

	public CurrentItemAdapter(Context context) {
		super(context);
	}

	public void updateCurrentItems(List<MokaItem> currentItems) {
		mCurrentItems = currentItems;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mCurrentItems.size();
	}

	@Override
	public MokaItem getItem(int position) {
		return mCurrentItems.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.current_item, null);
		}

		final TextView itemName = ViewHolder.get(convertView, R.id.item_name);
		final TextView itemCreationDate = ViewHolder.get(convertView, R.id.item_creation_date);
		final ImageView itemImage = ViewHolder.get(convertView, R.id.item_image);

		final MokaItem item = getItem(position);
		itemName.setText(item.getTitle());
		itemCreationDate.setText("ajouté le 28/04/2013 à 11h29");
		itemImage.setImageResource(R.drawable.logo_type_text);

		return convertView;
	}

	public void remove(MokaItem item) {
		mCurrentItems.remove(item);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		mCurrentItems.remove(position);
		notifyDataSetChanged();
	}
}
