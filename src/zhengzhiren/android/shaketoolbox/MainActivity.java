package zhengzhiren.android.shaketoolbox;

import java.util.List;

import zhengzhiren.android.shaketoolbox.actions.Action;
import zhengzhiren.android.shaketoolbox.actions.ActionCaptureScreen;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * 程序的主界面
 * 
 * @author ZZR
 * 
 */
public class MainActivity extends Activity {

	/**
	 * 保存上次运行本程序的版本号
	 */
	private static final String PREF_LAST_RUN_VERSION = "last_run_version";

	ListView mActionsListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.main);

		mActionsListView = (ListView) findViewById(R.id.ActionsListView);

		// 添加操作
		ActionsListAdapter adapter = new ActionsListAdapter(this);
		List<Action> actions = Action.getAllActions(this);
		adapter.setActionList(actions);

		mActionsListView.setAdapter(adapter);

		// 点击列表项
		mActionsListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				ToggleButton toggle = (ToggleButton) view
						.findViewById(R.id.toggle_action);
				Action action = (Action) mActionsListView
						.getItemAtPosition(position);
				boolean enabled = !toggle.isChecked();
				action.setEnabled(enabled);
				toggle.setChecked(enabled);
			}
		});

		// 检查是否首次运行
		if (checkFirstRun()) {
			// 显示关于界面
			showAboutDialog();
			// 释放截图所需的bin文件
			ActionCaptureScreen.releaseBin(this);
		}
	}

	/**
	 * 创建菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mi = this.getMenuInflater();
		mi.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 选择菜单
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// 首选项
		case R.id.menu_prefs:
			Intent intent = new Intent(this, Preferences.class);
			this.startActivity(intent);
			break;
		// 关于
		case R.id.menu_about:
			showAboutDialog();
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * 进入程序后，停止服务
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Intent intent = new Intent();
		intent.setClass(this, ToolboxService.class);
		stopService(intent);

	}

	/**
	 * 退出程序时，启动服务
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			Intent intent = new Intent();
			intent.setClass(this, ToolboxService.class);
			startService(intent);
		}
	}

	/**
	 * 显示关于对话框
	 */
	private void showAboutDialog() {
		LayoutInflater inflater = this.getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_about, null);

		// 显示版本
		try {
			String version = getPackageManager().getPackageInfo(
					getPackageName(), 0).versionName;
			TextView versionTextView = (TextView) view
					.findViewById(R.id.TextViewVersion);
			versionTextView.setText(version);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String titleFmt = getResources().getString(
				R.string.dialog_about_title_fmt);
		String appName = getResources().getString(R.string.app_name);
		String title = String.format(titleFmt, appName);
		builder.setTitle(title);
		builder.setIcon(R.drawable.app_icon);
		builder.setView(view);

		builder.create().show();
	}

	/**
	 * 检查本程序此版本是否首次运行
	 * 
	 * @return
	 */
	private boolean checkFirstRun() {
		PackageManager pm = getPackageManager();
		try {
			PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
			int versionCode = info.versionCode;
			SharedPreferences sp = PreferenceManager
					.getDefaultSharedPreferences(this);
			int lastRunVersion = sp.getInt(PREF_LAST_RUN_VERSION, 0);
			if (versionCode == lastRunVersion) { // 非首次运行
				return false;
			} else { // 是首次运行
				// 将本程序的版本号保存到配置文件
				Editor editor = sp.edit();
				editor.putInt(PREF_LAST_RUN_VERSION, versionCode);
				editor.commit();
				return true;
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return true;
		}
	}
}
