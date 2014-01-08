package com.example.msalary.activity;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.msalary.R;
import com.example.msalary.adapter.SelectCompanyAdapter;
import com.example.msalary.entity.CompanyEntity;
import com.example.msalary.entity.RequestEntity;
import com.example.msalary.entity.ResponseResult;
import com.example.msalary.entity.ShowResult;
import com.example.msalary.internet.InternetHelper;
import com.example.msalary.json.JsonSelectCompany;
import com.example.msalary.util.MConstant;

public class SelectCompanyActivity extends BaseActivity implements OnItemClickListener ,OnClickListener{
	private ListView listView;
	
	private SelectCompanyAdapter adapter = null;
	
	private ShowResult showResult = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	setContentView(R.layout.company_search_main);
		initView();
		request(getIntent().getStringExtra("key"));
		
	}

	protected void initView() {
		super.initView();
		tv_title.setText(getString(R.string.select_company_title));
		listView = (ListView) findViewById(R.id.company_list_lv);
		showResult = new  ShowResult();
		adapter = new SelectCompanyAdapter(this, showResult);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	
	/**
	 * ������������
	 * @param requestStr
	 */
	private void request(String requestStr){
		RequestEntity requestEntity =new RequestEntity(this,MConstant.URL_SELECT_COMPANY);
		HashMap<String,Object> map = new HashMap<String,Object>();
		map.put("key", requestStr);
		requestEntity.params = map;
		new InternetHelper(this).requestThread(requestEntity, this);
	}

	@Override
	public void requestSuccess(ResponseResult responseResult) {
		Log.d("tag","showResult"+responseResult);
		showResult = JsonSelectCompany.parse(responseResult,this);
		adapter.setData(showResult);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Log.d("tag",arg3+"]]"+arg2+"companyId-->"+ ((CompanyEntity)showResult.list.get((int)arg3)).getId());
		Intent i = new Intent(SelectCompanyActivity.this,CompanyAllPositionActivity.class);
		i.putExtra("companyId", ((CompanyEntity)showResult.list.get((int)arg3)).getId());
		i.putExtra("companyName", ((CompanyEntity)showResult.list.get((int)arg3)).getName());
		startActivity(i);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}

	
}