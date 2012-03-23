/**
 * 
 */
package zhengzhiren.android.shaketoolbox;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * @author wjx
 * 
 */
public class StartAppPreference extends PreferenceActivity {
	
	public static final String APP_LAUNCH_INTENT_NAME="app_launch_intent_name";
	private ListView mListView;
	private List<Programe> mListPrograme;
	private AppListAdapter mAppListAdapter;
	private Programe mCurrentPrograme=null;
	/**
	 * 
	 */
	public StartAppPreference() {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.start_app_preferences);
		PackagesInfo pi = new PackagesInfo(this);
		mListPrograme = pi.getProgrameList();
		mListView = (ListView) findViewById(android.R.id.list);
		mAppListAdapter = new AppListAdapter(mListPrograme, this);
		mAppListAdapter.setCurSelectPosition(pi.getPos(getApp()));
		mListView.setAdapter(mAppListAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {
			/**update list UI when item on click*/  
	        class UpdateListStaus implements Runnable{  
	            private View mView;   
	              
	            public UpdateListStaus(View view){  
	                this.mView  = view;  
	            }  
	              
	            public void run(){  
	            	if(mAppListAdapter.getSelectHolder() != null){  
	            		mAppListAdapter.getSelectHolder().radiobutton.setChecked(false);
	                } 
	            	ViewHolder tmp = new ViewHolder();  
	                tmp.radiobutton= (RadioButton)mView.findViewById(R.id.app_rbtn); 
	                tmp.imgage = (ImageView) mView
							.findViewById(R.id.app_image);
	                tmp.text = (TextView) mView
							.findViewById(R.id.app_text);
	                tmp.radiobutton.setChecked(true) ;
	                mAppListAdapter.setSelectHolder(tmp); 
	            }  
	        }  
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int pos, long id) {
				mAppListAdapter.setCurSelectPosition(pos);
				new Handler().post(new UpdateListStaus(view)); 
				mCurrentPrograme=mListPrograme.get(pos);
				
			}
		});
		findViewById(R.id.start_app_cancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				finish();
			}
		});
		findViewById(R.id.start_app_save).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				saveApp();
				finish();
			}
		});
		
	}
	private String getApp()
	{
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		return sp.getString(APP_LAUNCH_INTENT_NAME, null);
	}
	private void saveApp()
	{
		if(mCurrentPrograme==null)
		{
			return;
		}
		if(mCurrentPrograme.packname.isEmpty()||mCurrentPrograme.packname==null)
		{
			return;
		}
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(APP_LAUNCH_INTENT_NAME,mCurrentPrograme.packname);
		editor.apply();
	}
	public class AppListAdapter extends BaseAdapter {
		List<Programe> list = new ArrayList<Programe>();
		LayoutInflater la;
		Context context;
		/**on selected item*/  
        private ViewHolder mSelectHolder;  
        /**on selected item posion*/  
		private int mCurSelectPosition = -1;

		public AppListAdapter(List<Programe> list, Context context) {
			this.list = list;
			this.context = context;
			la = LayoutInflater.from(context);
		}
		public void setCurSelectPosition(int curSelectPosition) {  
            mCurSelectPosition = curSelectPosition;  
        }  
        public ViewHolder getSelectHolder() {  
            return mSelectHolder;  
        }  
        public void setSelectHolder(ViewHolder selectHolder) {  
            this.mSelectHolder = selectHolder;  
        }  
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			final Programe pr = (Programe) list.get(position);
			if (convertView == null) {
				convertView = la.inflate(R.layout.list_item_apps, null);
				holder = new ViewHolder();
				holder.imgage = (ImageView) convertView
						.findViewById(R.id.app_image);
				holder.text = (TextView) convertView
						.findViewById(R.id.app_text);
				holder.radiobutton = (RadioButton) convertView
						.findViewById(R.id.app_rbtn);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 设置图标
			holder.imgage.setImageDrawable(pr.getIcon());
			// 设置程序名
			holder.text.setText(pr.getName());
			if(position==mCurSelectPosition)
			{
				setSelectHolder(holder);
				holder.radiobutton.setChecked(true);
			}else
			{
				holder.radiobutton.setChecked(false);
			}
			return convertView;
		}
	}

	class ViewHolder {
		RadioButton radiobutton;
		ImageView imgage;
		TextView text;
	}

	public class PackagesInfo {
		private List<ApplicationInfo> appList;
		private PackageManager pm;

		public PackagesInfo(Context context) {
			// 通包管理器，检索所有的应用程序（甚至卸载的）与数据目录
			pm = context.getApplicationContext().getPackageManager();
			appList = pm
					.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		}
		public List<Programe> getProgrameList() {
			List<Programe> list = new ArrayList<Programe>();

			for (ApplicationInfo app : appList) {
				// 这里主要是过滤系统的应用和电话应用，当然你也可以把它注释掉。
				if (app.processName.equals("system")
						|| (getInfo(app.processName) == null)) {
					continue;
				}
				Programe pr = new Programe();
				pr.setIcon(getInfo(app.processName).loadIcon(pm));
				pr.setName(getInfo(app.processName).loadLabel(pm).toString());
				pr.setPackgeName(app.packageName.toString());
				list.add(pr);
			}
			return list;
		}
		/**
		 * 通过一个程序名返回该程序的一个Application对象。
		 * 
		 * @param name
		 *            程序名
		 * @return ApplicationInfo
		 */
		public ApplicationInfo getInfo(String name) {
			if (name == null) {
				return null;
			}
			for (ApplicationInfo appinfo : appList) {
				if (name.equals(appinfo.processName)) {
					return appinfo;
				}
			}
			return null;
		}
		public int getPos(String packname) {
			if (packname == null||packname.isEmpty()) {
				return -1;
			}
			int i=0;
			for (ApplicationInfo appinfo : appList) {
				if (appinfo.processName.equals("system")
						|| (getInfo(appinfo.processName) == null)) {
					continue;
				}
				if (packname.equals(appinfo.packageName)) {
					return i;
				}
				i++;
			}
			return -1;
		}

	}

	public class Programe {
		// 图标
		private Drawable icon;
		// 程序名
		private String name;
		private String packname;
		public Drawable getIcon() {
			return icon;
		}

		public void setIcon(Drawable icon) {
			this.icon = icon;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		public String getPackgeName() {
			return packname;
		}

		public void setPackgeName(String packname) {
			this.packname = packname;
		}

	}

}
