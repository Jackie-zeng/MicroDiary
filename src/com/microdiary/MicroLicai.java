package com.microdiary;

/**
 * 微理财页面
 */

import java.util.ArrayList;
import java.util.Calendar;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.microdiary.dao.MoneyDB;
import com.microdiary.entity.Money;
import com.microdiary.util.MicroDiaryUtil;

public class MicroLicai {
	
	
	public MainMicroDiary context;
	public View view;               //微日记view
	
	TextView title;
    public ImageButton write;    //右上角按扭
	public Button income;      //收入按扭
	public Button extend;   //支出按扭
	public ListView moneyList;  //收入(支出)列表
	public TextView result;   //总计
	
	
	EditText dateEdit;    //收入(支出)对话框中的日期编辑框
	RadioButton incomeRadio;    //收入按扭
	RadioButton extendRadio;   //支出按扭
	EditText moneyEdit;    //金额编辑框
	EditText remarkEdit;    //备注
	Button opButton;   //右下角的操作按扭
	
	
	SelectDialog moneyDialog = null;   //收入(支出)对话框
	
	/*
	 * 弹窗类型
	 */
	public final int WRITE_DIALOG = 1;    //新建 
	public final int EDIT_DIALOG = 2;     //编辑
	public final int REMOVE_DIALOG = 3;   //删除
	public final int READ_DIALOG = 4;     //查看
	
	MoneyDB db;
	public ArrayList<Money> incomeMoneys;   //收入列表
	public ArrayList<Money> extendMoneys;  //支出列表
	
	public int currList = 1; //1收入列表 2支出列表
	public Money currentMoney;  //当前数据
	public int currentIndex;   //当前数据在列表中的id
	
	/**
	 * 构造函数
	 * @param context
	 * @param view
	 */
	public MicroLicai(MainMicroDiary context, View view) {
		this.context = context;
		this.view = view;
		title = (TextView) view.findViewById(R.id.title);
		title.setText("微理财");
		
		db = new MoneyDB(context);
		
		incomeMoneys = db.selectIncome();
		extendMoneys = db.selectExtend();
		
		write = (ImageButton) view.findViewById(R.id.write);
		income = (Button) view.findViewById(R.id.income);
		extend = (Button) view.findViewById(R.id.extend);
		moneyList = (ListView) view.findViewById(R.id.moneyList);
		result = (TextView) view.findViewById(R.id.result);
		
		
		write.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showMoneyDialog(WRITE_DIALOG);
			}
		});
		income.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				refresh1();
			}
		});
		extend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				refresh2();
			}
		});
		
		
		moneyList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				System.out.println("神马情况");
				if(currList == 1) {
					currentIndex = arg2;
					currentMoney = incomeMoneys.get(arg2);
				}
				else {
					currentIndex = arg2;
					currentMoney = extendMoneys.get(arg2);
				}
				showMoneyDialog(READ_DIALOG);
			}
		});
		

		moneyList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(currList == 1) {
					currentIndex = arg2;
					currentMoney = incomeMoneys.get(arg2);
				}
				else {
					currentIndex = arg2;
					currentMoney = extendMoneys.get(arg2);
				}
				showEditOrRemoveDialog();
				
				return false;
			}
		});

		
		refresh1();
		
	}
	
	/**
	 * 刷新收入列表
	 */
	public void refresh1() {
		/*
		incomeMoneys = new ArrayList<Money>();
		for(int i = 0; i < 10; i++) {
			Money money = new Money();
			money.setDate("2012-12-12 12:12:12");
			money.setValue("收入：1000元");
			money.setRemark("备注：通过做外包项目的收入");
			incomeMoneys.add(money);
		}*/
		income.setTextSize(20);
		extend.setTextSize(16);
		currList = 1;
		
		int sum = 0;
		for(int i = 0; i < incomeMoneys.size(); i++) {
			sum += incomeMoneys.get(i).getMoney();
		}
		result.setText("列表共有"  + incomeMoneys.size() + "项\n收入总计" + sum + "元");
		
		MoneyAdapter adapter = new MoneyAdapter(context, incomeMoneys);
		moneyList.setAdapter(adapter);
		moneyList.setSelection(currentIndex);
	}
	
	
	/**
	 * 刷新支出列表
	 */
	public void refresh2() {
		/*
		extendMoneys = new ArrayList<Money>();
		for(int i = 0; i < 10; i++) {
			Money money = new Money();
			money.setDate("2012-12-12 12:12:12");
			money.setValue("支出：500元");
			money.setRemark("备注：充饭卡");
			extendMoneys.add(money);
		}*/
		income.setTextSize(16);
		extend.setTextSize(20);
		currList = 2;
		int sum = 0;
		for(int i = 0; i < extendMoneys.size(); i++) {
			sum += extendMoneys.get(i).getMoney();
		}
		result.setText("列表共有"  + extendMoneys.size() + "项\n支出总计" + sum + "元");
		
		MoneyAdapter adapter = new MoneyAdapter(context, extendMoneys);
		moneyList.setAdapter(adapter);
		moneyList.setSelection(currentIndex);
	}

	
	/**
	 * 编辑或删除的对话框
	 */
	public void showEditOrRemoveDialog() {
		final SelectDialog dialog = new SelectDialog(context, R.style.dialog, R.layout.edit_remove_diary_dialog);
		dialog.show();
		Button editDiary = (Button) dialog.findViewById(R.id.editDiary);
		Button removeDiary = (Button) dialog.findViewById(R.id.removeDiary);
		editDiary.setText("修改数据");
		removeDiary.setText("删除数据");
		editDiary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				showMoneyDialog(EDIT_DIALOG);
			}
		});
		removeDiary.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				showMoneyDialog(REMOVE_DIALOG);
			}
		});
	}
	
	/**
	 * 收入(支出)对话框
	 * @param tag
	 */
	public void showMoneyDialog(final int tag) {
		moneyDialog = new SelectDialog(context, R.style.dialog, R.layout.money_dialog);
		moneyDialog.show();
		
		dateEdit = (EditText) moneyDialog.findViewById(R.id.date);
		moneyEdit = (EditText) moneyDialog.findViewById(R.id.money_edit);
		remarkEdit = (EditText) moneyDialog.findViewById(R.id.remark_edit);
		opButton = (Button) moneyDialog.findViewById(R.id.ok);
		incomeRadio = (RadioButton) moneyDialog.findViewById(R.id.income);
		extendRadio = (RadioButton) moneyDialog.findViewById(R.id.extend);
		
//		dateEdit.setText(MicroDiaryUtil.getCurrentDate());
		dateEdit.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					
					SelectDialog dateDialog = new SelectDialog(context, R.style.dialog,
							R.layout.datepicker_dialog);
					dateDialog.show();
					Calendar c = Calendar.getInstance();
					int year = c.get(Calendar.YEAR);
					int month = c.get(Calendar.MONTH);
					int day = c.get(Calendar.DAY_OF_MONTH);
					String[] dates = dateEdit.getText().toString().split("-");
					year = Integer.parseInt(dates[0]);
					month = Integer.parseInt(dates[1]);
					day = Integer.parseInt(dates[2]);
					month--;
					
					DatePicker datePicker = (DatePicker) dateDialog.findViewById(R.id.datePicker);
					
					datePicker.init(year, month, day, new OnDateChangedListener() {
						
						@Override
						public void onDateChanged(DatePicker view, int year, int monthOfYear,
								int dayOfMonth) {
							// TODO Auto-generated method stub
							monthOfYear++;
							dateEdit.setText(year + "-" + monthOfYear / 10 + monthOfYear % 10 +
									"-" + dayOfMonth / 10 + dayOfMonth % 10);
						}
					});
				}
				return true;
			}
		});
		
		incomeRadio.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked) {
					moneyEdit.setHint("收入");
				}
				else {
					moneyEdit.setHint("支出");
				}
			}
		});
		
		
		if(tag == WRITE_DIALOG) {
			writeDialog();
		}
		else if(tag == EDIT_DIALOG) {
			editDialog();
		}
		else if(tag == REMOVE_DIALOG) {
			removeDialog();
		}
		else if(tag == READ_DIALOG) {
			readDialog();
		}
	}

	/**
	 * 查看收入(支出)
	 */
	private void readDialog() {
		// TODO Auto-generated method stub
		dateEdit.setText(currentMoney.getDate());
		moneyEdit.setText(currentMoney.getMoney() + "");
		remarkEdit.setText(currentMoney.getRemark());
		dateEdit.setEnabled(false);
		moneyEdit.setEnabled(false);
		remarkEdit.setEnabled(false);
		incomeRadio.setEnabled(false);
		extendRadio.setEnabled(false);
		if(currList == 1)  {
			incomeRadio.setChecked(true);
			incomeRadio.setHint("收入");
		}
		else {
			extendRadio.setChecked(true);
			incomeRadio.setHint("支出");
		}
		opButton.setText("确定");
		opButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moneyDialog.dismiss();
			}
		});
	}

	
	/**
	 * 删除收入(支出)
	 */
	private void removeDialog() {
		// TODO Auto-generated method stub
		dateEdit.setText(currentMoney.getDate());
		moneyEdit.setText(currentMoney.getMoney() + "");
		remarkEdit.setText(currentMoney.getRemark());
		dateEdit.setEnabled(false);
		moneyEdit.setEnabled(false);
		remarkEdit.setEnabled(false);
		incomeRadio.setEnabled(false);
		extendRadio.setEnabled(false);
		if(currList == 1)  {
			incomeRadio.setChecked(true);
			incomeRadio.setHint("收入");
		}
		else {
			extendRadio.setChecked(true);
			incomeRadio.setHint("支出");
		}
		opButton.setText("删除");
		opButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currList == 1) {
					db.deleteIncome(currentMoney.getMid());
					incomeMoneys.remove(currentIndex);
					if(currentIndex == 0);
					else
						currentIndex--;
					
					refresh1();
				}
				else {
					db.deleteExtend(currentMoney.getMid());
					extendMoneys.remove(currentIndex);
					if(currentIndex == 0);
					else
						currentIndex--;
					
					refresh2();
				}
				Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
				moneyDialog.dismiss();
			}
		});
	}

	/**
	 * 修改收入(支出)
	 */
	private void editDialog() {
		// TODO Auto-generated method stub
		dateEdit.setText(currentMoney.getDate());
		moneyEdit.setText(currentMoney.getMoney() + "");
		remarkEdit.setText(currentMoney.getRemark());
		incomeRadio.setEnabled(false);
		extendRadio.setEnabled(false);
		if(currList == 1)  {
			incomeRadio.setChecked(true);
			incomeRadio.setHint("收入");
		}
		else {
			extendRadio.setChecked(true);
			incomeRadio.setHint("支出");
		}
		opButton.setText("修改");
		
		opButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String dateStr = dateEdit.getText().toString();
				String moneyStr = moneyEdit.getText().toString().trim();
				String remarkStr = remarkEdit.getText().toString().trim();
				
				if(moneyStr.equals("")) {
					if(incomeRadio.isChecked()){
						Toast.makeText(context, "收入不能为空", 3000).show();
					}
					else {
						Toast.makeText(context, "支出不能为空", 3000).show();
					}
					return;
				}
				
			
				
				if(incomeRadio.isChecked()) {
					currentMoney.setDate(dateStr);
					currentMoney.setMoney(Integer.parseInt(moneyStr));
					currentMoney.setRemark(remarkStr);
					currentMoney.setValue("收入：" + moneyStr + "元");
					db.updateIncome(currentMoney.getMid(), dateStr, "收入：" + moneyStr + "元",Integer.parseInt(moneyStr),
							remarkStr);
					incomeMoneys.set(currentIndex, currentMoney);
					refresh1();
					
				}
				else {
					currentMoney.setDate(dateStr);
					currentMoney.setMoney(Integer.parseInt(moneyStr));
					currentMoney.setRemark(remarkStr);
					currentMoney.setValue("支出：" + moneyStr + "元");
					db.updateExtend(currentMoney.getMid(), dateStr, "支出：" + moneyStr + "元",Integer.parseInt(moneyStr), 
							remarkStr);
					extendMoneys.set(currentIndex, currentMoney);
					refresh2();
				}
				Toast.makeText(context, "修改成功", 3000).show();
				moneyDialog.dismiss();
			}
		});
	}

	/**
	 * 新建收入(支出)
	 */
	private void writeDialog() {
		// TODO Auto-generated method stub
		currentMoney = new Money();
		dateEdit.setText(MicroDiaryUtil.getCurrentDate());
		if(currList == 1)  {
			incomeRadio.setChecked(true);
			incomeRadio.setHint("收入");
		}
		else {
			extendRadio.setChecked(true);
			incomeRadio.setHint("支出");
		}
		opButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String dateStr = dateEdit.getText().toString();
				String moneyStr = moneyEdit.getText().toString().trim();
				String remarkStr = remarkEdit.getText().toString().trim();
				
				if(moneyStr.equals("")) {
					if(incomeRadio.isChecked()){
						Toast.makeText(context, "收入不能为空", 3000).show();
					}
					else {
						Toast.makeText(context, "支出不能为空", 3000).show();
					}
					return;
				}
				
			
				
				currentIndex = 0;
				if(incomeRadio.isChecked()) {
					db.insertIncome(dateStr, "收入：" + moneyStr + "元",Integer.parseInt(moneyStr),
							remarkStr);
					incomeMoneys.add(0, db.selectIncomeFirst().get(0));
					refresh1();
				}
				else {
					db.insertExtend(dateStr, "支出：" + moneyStr + "元",Integer.parseInt(moneyStr), 
							remarkStr);
					extendMoneys.add(0, db.selectExtendFirst().get(0));
					refresh2();
				}
				Toast.makeText(context, "保存成功", 3000).show();
				moneyDialog.dismiss();
			}
		});
	}
	
	
	
}
