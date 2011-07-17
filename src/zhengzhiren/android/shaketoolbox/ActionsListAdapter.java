/**
 * 
 */
package zhengzhiren.android.shaketoolbox;

import java.util.ArrayList;
import java.util.List;

import zhengzhiren.android.shaketoolbox.actions.Action;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 显示操作列表的Adapter
 * 
 * @author ZZR
 * 
 */
public class ActionsListAdapter extends BaseAdapter {

	List<Action> mActionList = new ArrayList<Action>();
	LayoutInflater inflater;

	public ActionsListAdapter(Context context) {
		inflater = LayoutInflater.from(context);
	}

	/**
	 * 设置Action列表
	 * 
	 * @param action
	 */
	public void setActionList(List<Action> actions) {
		mActionList = actions;
	}

	@Override
	public int getCount() {
		return mActionList.size();
	}

	@Override
	public Object getItem(int position) {
		return mActionList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Action action = (Action) getItem(position);
		if (convertView == null) { // 构造新的View
			convertView = inflater.inflate(R.layout.list_item_action, null);
		}

		ImageView img = (ImageView) convertView.findViewById(R.id.action_icon);
		TextView name = (TextView) convertView.findViewById(R.id.action_name);
		TextView description = (TextView) convertView
				.findViewById(R.id.action_description);
		ToggleButton toggle = (ToggleButton) convertView
				.findViewById(R.id.toggle_action);

		// 显示操作的图标、名称、描述和状态
		img.setImageResource(action.getIcon());
		name.setText(action.getName());
		description.setText(action.getDescription());
		toggle.setChecked(action.isEnabled());

		return convertView;
	}

}
