/**
 * 
 */
package com.example.msalary.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.msalary.entity.JobEntity;
import com.example.msalary.entity.ResponseResult;
import com.example.msalary.entity.ShowResult;
import com.example.msalary.internet.IRequestCallBack;
import com.example.msalary.util.ErrorCodeUtils;

/**
 * ���ߣ�@    <br>
 * ����ʱ�䣺2013/11/24 <br>
 * ��������: json����--��λ���� <br>
 */
public class JsonPositionDetail {
	
	public static ShowResult parse(ResponseResult responseResult,IRequestCallBack requestCallBack){
		ShowResult showResult = new ShowResult();
		try {
			JSONObject object = new JSONObject(responseResult.resultStr);
			int code = object.getInt("code");
			if(code!=0){//�������
				requestCallBack.requestFailedStr(ErrorCodeUtils.changeCodeToStr(1));
				return null;
			}
			JSONArray array = object.getJSONArray("list");
			List<JobEntity> list = new ArrayList<JobEntity>();
			JobEntity entity =  null;
			JSONObject item = null;
			for(int i =0;i<array.length();i++){
				item = array.getJSONObject(i);
				entity = new JobEntity();
				entity.setSalary(item.getInt("salary"));
				entity.setCount1(item.getInt("count1"));
				entity.setCount2(item.getInt("count2"));
				entity.setCount3(item.getInt("count3"));
				entity.setCount4(item.getInt("count4"));
				entity.setUserful_num(item.getInt("userful_num"));
				list.add(entity);
			}
			showResult.list = list;
			showResult.resultCode =code;
		} catch (JSONException e) {
			requestCallBack.requestFailedStr(ErrorCodeUtils.changeCodeToStr(1));
			e.printStackTrace();
			return null;
		}
		return showResult;
	}
	
}