/**
 * 
 */
package zhengzhiren.android.shaketoolbox;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * @author wjx
 * 
 */
public class StartAppPreference extends PreferenceActivity {

	public static final String APP_LAUNCH_INTENT_NAME = "app_launch_intent_name";
	private static final int MSG_UPDATE_LIST = 0;
	private ListView mListView;
	private List<Programe> mListPrograme;
	private AppListAdapter mAppListAdapter;
	private Programe mCurrentPrograme = null;
	private ProgressDialog dialog;

	/**
	 * 
	 */
	public StartAppPreference() {

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.start_app_preferences);
		
		mListView = (ListView) findViewById(android.R.id.list);
		mListPrograme=new ArrayList<Programe>();
		mAppListAdapter = new AppListAdapter(mListPrograme, this);
		
		
		
        dialog = new ProgressDialog(this);   
        dialog.setTitle("应用程序列表");  
        dialog.setMessage("正在加载数据中...");  
        dialog.setCancelable(false);  
        dialog.show();  
          
        mThreadLoadApps.start(); 

		mListView.setOnItemClickListener(new OnItemClickListener() {
			class UpdateListStaus implements Runnable {
				private View mView;

				public UpdateListStaus(View view) {
					this.mView = view;
				}

				public void run() {
					if (mAppListAdapter.getSelectHolder() != null) {
						mAppListAdapter.getSelectHolder().radiobutton
								.setChecked(false);
					}
					ViewHolder tmp = new ViewHolder();
					tmp.radiobutton = (RadioButton) mView
							.findViewById(R.id.app_rbtn);
					tmp.imgage = (ImageView) mView.findViewById(R.id.app_image);
					tmp.text = (TextView) mView.findViewById(R.id.app_text);
					tmp.radiobutton.setChecked(true);
					mAppListAdapter.setSelectHolder(tmp);
				}
			}

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int pos, long id) {
				mAppListAdapter.setCurSelectPosition(pos);
				new Handler().post(new UpdateListStaus(view));
				mCurrentPrograme = mListPrograme.get(pos);

			}
		});
		findViewById(R.id.start_app_cancel).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						finish();
					}
				});
		findViewById(R.id.start_app_save).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						saveApp();
						finish();
					}
				});
		
		

	}
	private Handler handler = new Handler(){  
        @Override  
        public void handleMessage(Message msg) {  
            // TODO Auto-generated method stub  
            switch (msg.what) {  
            case MSG_UPDATE_LIST:  
                // 更新应用程序列表  
            	mListView.setAdapter(mAppListAdapter);
                break;  
            } 
            super.handleMessage(msg);  
        }  
    };  
	private Thread mThreadLoadApps = new Thread(){  
        @Override  
        public void run() {  
        	getAppList();
            dialog.dismiss();  
            mAppListAdapter.setCurSelectPosition(getPos(getApp()));
        }  
          
    };
	private String getApp() {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		return sp.getString(APP_LAUNCH_INTENT_NAME, null);
	}

	private void saveApp() {
		if (mCurrentPrograme == null) {
			return;
		}
		if (mCurrentPrograme.getPackgeName().isEmpty()
				|| mCurrentPrograme.getPackgeName() == null) {
			return;
		}
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(APP_LAUNCH_INTENT_NAME, mCurrentPrograme.getPackgeName());
		editor.apply();
	}
	
	private void getAppList() {
		PackageManager pm;
		pm = getPackageManager();
		List<ApplicationInfo> appList = pm
				.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		mListPrograme = new ArrayList<Programe>();
		for (ApplicationInfo app : appList) {
			if (app.processName.equals("system") || (app == null)) {
				continue;
			}
			Programe pr = new Programe();
			pr.setIcon(app.loadIcon(pm));
			pr.setName(app.loadLabel(pm).toString());
			pr.setPackgeName(app.packageName.toString());
			mListPrograme.add(pr);
			handler.sendEmptyMessage(MSG_UPDATE_LIST);
		} 
	}

	public int getPos(String packname) {
		if (packname == null || packname.isEmpty()) {
			return -1;
		}
		int i = 0;
		for (Programe appinfo : mListPrograme) {
			if (packname.equals(appinfo.packname)) {
				return i;
			}
			i++;
		}
		return -1;
	}
	
	public class AppListAdapter extends BaseAdapter {
		LayoutInflater la;
		Context context;
		private ViewHolder mSelectHolder;
		private int mCurSelectPosition = -1;

		public AppListAdapter(List<Programe> list, Context context) {
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
			return mListPrograme.size();
		}

		@Override
		public Object getItem(int position) {
			return mListPrograme.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			final Programe pr = (Programe) mListPrograme.get(position);
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
			holder.imgage.setImageDrawable(pr.getIcon());
			holder.text.setText(pr.getName());
			if (position == mCurSelectPosition) {
				setSelectHolder(holder);
				holder.radiobutton.setChecked(true);
			} else {
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
	class Programe {
		
		public Programe()
		{
			this.icon=null;
			this.name=null;
			this.packname=null;
			
		}
		
		private Drawable icon;
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
