package com.microdiary;

/**
 * ΢���ҳ��
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
	public View view;               //΢�ռ�view
	
	TextView title;
    public ImageButton write;    //���Ͻǰ�Ť
	public Button income;      //���밴Ť
	public Button extend;   //֧����Ť
	public ListView moneyList;  //����(֧��)�б�
	public TextView result;   //�ܼ�
	
	
	EditText dateEdit;    //����(֧��)�Ի����е����ڱ༭��
	RadioButton incomeRadio;    //���밴Ť
	RadioButton extendRadio;   //֧����Ť
	EditText moneyEdit;    //���༭��
	EditText remarkEdit;    //��ע
	Button opButton;   //���½ǵĲ�����Ť
	
	
	SelectDialog moneyDialog = null;   //����(֧��)�Ի���
	
	/*
	 * ��������
	 */
	public final int WRITE_DIALOG = 1;    //�½� 
	public final int EDIT_DIALOG = 2;     //�༭
	public final int REMOVE_DIALOG = 3;   //ɾ��
	public final int READ_DIALOG = 4;     //�鿴
	
	MoneyDB db;
	public ArrayList<Money> incomeMoneys;   //�����б�
	public ArrayList<Money> extendMoneys;  //֧���б�
	
	public int currList = 1; //1�����б� 2֧���б�
	public Money currentMoney;  //��ǰ����
	public int currentIndex;   //��ǰ�������б��е�id
	
	/**
	 * ���캯��
	 * @param context
	 * @param view
	 */
	public MicroLicai(MainMicroDiary context, View view) {
		this.context = context;
		this.view = view;
		title = (TextView) view.findViewById(R.id.title);
		title.setText("΢���");
		
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
				System.out.println("�������");
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
	 * ˢ�������б�
	 */
	public void refresh1() {
		/*
		incomeMoneys = new ArrayList<Money>();
		for(int i = 0; i < 10; i++) {
			Money money = new Money();
			money.setDate("2012-12-12 12:12:12");
			money.setValue("���룺1000Ԫ");
			money.setRemark("��ע��ͨ���������Ŀ������");
			incomeMoneys.add(money);
		}*/
		income.setTextSize(20);
		extend.setTextSize(16);
		currList = 1;
		
		int sum = 0;
		for(int i = 0; i < incomeMoneys.size(); i++) {
			sum += incomeMoneys.get(i).getMoney();
		}
		result.setText("�б���"  + incomeMoneys.size() + "��\n�����ܼ�" + sum + "Ԫ");
		
		MoneyAdapter adapter = new MoneyAdapter(context, incomeMoneys);
		moneyList.setAdapter(adapter);
		moneyList.setSelection(currentIndex);
	}
	
	
	/**
	 * ˢ��֧���б�
	 */
	public void refresh2() {
		/*
		extendMoneys = new ArrayList<Money>();
		for(int i = 0; i < 10; i++) {
			Money money = new Money();
			money.setDate("2012-12-12 12:12:12");
			money.setValue("֧����500Ԫ");
			money.setRemark("��ע���䷹��");
			extendMoneys.add(money);
		}*/
		income.setTextSize(16);
		extend.setTextSize(20);
		currList = 2;
		int sum = 0;
		for(int i = 0; i < extendMoneys.size(); i++) {
			sum += extendMoneys.get(i).getMoney();
		}
		result.setText("�б���"  + extendMoneys.size() + "��\n֧���ܼ�" + sum + "Ԫ");
		
		MoneyAdapter adapter = new MoneyAdapter(context, extendMoneys);
		moneyList.setAdapter(adapter);
		moneyList.setSelection(currentIndex);
	}

	
	/**
	 * �༭��ɾ���ĶԻ���
	 */
	public void showEditOrRemoveDialog() {
		final SelectDialog dialog = new SelectDialog(context, R.style.dialog, R.layout.edit_remove_diary_dialog);
		dialog.show();
		Button editDiary = (Button) dialog.findViewById(R.id.editDiary);
		Button removeDiary = (Button) dialog.findViewById(R.id.removeDiary);
		editDiary.setText("�޸�����");
		removeDiary.setText("ɾ������");
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
	 * ����(֧��)�Ի���
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
					moneyEdit.setHint("����");
				}
				else {
					moneyEdit.setHint("֧��");
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
	 * �鿴����(֧��)
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
			incomeRadio.setHint("����");
		}
		else {
			extendRadio.setChecked(true);
			incomeRadio.setHint("֧��");
		}
		opButton.setText("ȷ��");
		opButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				moneyDialog.dismiss();
			}
		});
	}

	
	/**
	 * ɾ������(֧��)
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
			incomeRadio.setHint("����");
		}
		else {
			extendRadio.setChecked(true);
			incomeRadio.setHint("֧��");
		}
		opButton.setText("ɾ��");
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
				Toast.makeText(context, "ɾ���ɹ�", Toast.LENGTH_SHORT).show();
				moneyDialog.dismiss();
			}
		});
	}

	/**
	 * �޸�����(֧��)
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
			incomeRadio.setHint("����");
		}
		else {
			extendRadio.setChecked(true);
			incomeRadio.setHint("֧��");
		}
		opButton.setText("�޸�");
		
		opButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String dateStr = dateEdit.getText().toString();
				String moneyStr = moneyEdit.getText().toString().trim();
				String remarkStr = remarkEdit.getText().toString().trim();
				
				if(moneyStr.equals("")) {
					if(incomeRadio.isChecked()){
						Toast.makeText(context, "���벻��Ϊ��", 3000).show();
					}
					else {
						Toast.makeText(context, "֧������Ϊ��", 3000).show();
					}
					return;
				}
				
			
				
				if(incomeRadio.isChecked()) {
					currentMoney.setDate(dateStr);
					currentMoney.setMoney(Integer.parseInt(moneyStr));
					currentMoney.setRemark(remarkStr);
					currentMoney.setValue("���룺" + moneyStr + "Ԫ");
					db.updateIncome(currentMoney.getMid(), dateStr, "���룺" + moneyStr + "Ԫ",Integer.parseInt(moneyStr),
							remarkStr);
					incomeMoneys.set(currentIndex, currentMoney);
					refresh1();
					
				}
				else {
					currentMoney.setDate(dateStr);
					currentMoney.setMoney(Integer.parseInt(moneyStr));
					currentMoney.setRemark(remarkStr);
					currentMoney.setValue("֧����" + moneyStr + "Ԫ");
					db.updateExtend(currentMoney.getMid(), dateStr, "֧����" + moneyStr + "Ԫ",Integer.parseInt(moneyStr), 
							remarkStr);
					extendMoneys.set(currentIndex, currentMoney);
					refresh2();
				}
				Toast.makeText(context, "�޸ĳɹ�", 3000).show();
				moneyDialog.dismiss();
			}
		});
	}

	/**
	 * �½�����(֧��)
	 */
	private void writeDialog() {
		// TODO Auto-generated method stub
		currentMoney = new Money();
		dateEdit.setText(MicroDiaryUtil.getCurrentDate());
		if(currList == 1)  {
			incomeRadio.setChecked(true);
			incomeRadio.setHint("����");
		}
		else {
			extendRadio.setChecked(true);
			incomeRadio.setHint("֧��");
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
						Toast.makeText(context, "���벻��Ϊ��", 3000).show();
					}
					else {
						Toast.makeText(context, "֧������Ϊ��", 3000).show();
					}
					return;
				}
				
			
				
				currentIndex = 0;
				if(incomeRadio.isChecked()) {
					db.insertIncome(dateStr, "���룺" + moneyStr + "Ԫ",Integer.parseInt(moneyStr),
							remarkStr);
					incomeMoneys.add(0, db.selectIncomeFirst().get(0));
					refresh1();
				}
				else {
					db.insertExtend(dateStr, "֧����" + moneyStr + "Ԫ",Integer.parseInt(moneyStr), 
							remarkStr);
					extendMoneys.add(0, db.selectExtendFirst().get(0));
					refresh2();
				}
				Toast.makeText(context, "����ɹ�", 3000).show();
				moneyDialog.dismiss();
			}
		});
	}
	
	
	
}
