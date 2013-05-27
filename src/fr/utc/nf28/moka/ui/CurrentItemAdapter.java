package fr.utc.nf28.moka.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import fr.utc.nf28.moka.R;
import fr.utc.nf28.moka.data.MokaItem;
import fr.utc.nf28.moka.ui.base.BaseMokaAdapter;
import fr.utc.nf28.moka.ui.base.ViewHolder;
import fr.utc.nf28.moka.util.DateUtils;

public class CurrentItemAdapter extends BaseMokaAdapter {
	private final Context mContext;
	private List<MokaItem> mCurrentItems = Collections.emptyList();

	public CurrentItemAdapter(Context context) {
		super(context);

		mContext = context;
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
		itemCreationDate.setText(DateUtils.getFormattedDate(item.getCreationDate()));
		Picasso.with(mContext)
				.load(item.getType().getResId())
				.resizeDimen(R.dimen.list_current_item_type_picture_width_height, R.dimen.list_current_item_type_picture_width_height)
				.into(itemImage);

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
