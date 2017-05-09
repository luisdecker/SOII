/**
 * 
 */
package br.com.mobgui4so.view;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import br.com.mobgui4so.R;
import br.com.mobgui4so.utils.AndroidUtils;

/**
 * @author Ercilio Nascimento
 */
public class BaseActivity extends AppCompatActivity  {

	private TransactionTask task;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setBehindContentView(R.layout.slidingmenu);
		// getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
	}

	protected void alert(int message) {
		AndroidUtils.alertDialog(this, message);
	}

	public void startTransaction(ITransaction transaction) {
		boolean networkOK = AndroidUtils.isNetworkAvailable(this);
		if (networkOK) {
			task = new TransactionTask(this, transaction);
			task.execute();
		} else {
			AndroidUtils.alertDialog(this, R.string.error_connection_unavaiable);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		this.finishTransaction();
	}

	public void finishTransaction() {
		if (task != null) {
			boolean executing = task.getStatus().equals(AsyncTask.Status.RUNNING);
			if (executing) {
				task.cancel(true);
			}
		}
	}

}
