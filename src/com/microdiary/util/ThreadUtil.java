package com.microdiary.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;

import com.microdiary.R;

public class ThreadUtil {

	private static SelectDialog dialog = null;
	
	public static Thread thread = null;
	
	//出现转菊花
	public static void showProgress(Context context) {
		dialog = new SelectDialog(context, R.style.dialog, R.layout.progress_dialog);
		dialog.setCancelable(true);
        dialog.show();
        dialog.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if(keyCode == 4) {
					if(thread != null)
						thread.interrupt();
					dialog.dismiss();
				}
				return false;
			}
		});
        if(!NetWorkUtil.isConnect(context)) {
        	Toast.makeText(context, "好像连接不到网络哦", Toast.LENGTH_SHORT).show();
        }
	}
	
	//关闭转菊花
	public static void closeProgress() {
		if(dialog != null) {
			dialog.dismiss();
		}
	}
	
	
}
 
class SelectDialog extends Dialog{

	public int layoutId;
	
	public SelectDialog(Context context, int theme, int layoutId) {
	    super(context, theme);
	    this.layoutId = layoutId;
	}

	public SelectDialog(Context context, int layoutId) {
	    super(context);
	    this.layoutId = layoutId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(layoutId);
	}
}