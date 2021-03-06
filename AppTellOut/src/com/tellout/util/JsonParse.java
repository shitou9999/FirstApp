package com.tellout.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.tellout.constant.DbConstant;
import com.tellout.constant.ErrorCodeConstant;
import com.tellout.constant.MConstant;
import com.tellout.entity.BaseEntity;
import com.tellout.entity.CommentEntity;
import com.tellout.entity.TellOutEntity;
import com.tellout.entity.TypeEntity;
import com.tellout.entity.UserEntity;

public class JsonParse {
	
	public static BaseEntity JsonParse(int requestType, String responseStr) {
		BaseEntity baseEntity = null;
		try {
			baseEntity = ParseBase(responseStr);
			if(baseEntity.getCode()!=ErrorCodeConstant.SUCCESS){
				return baseEntity;
			}
			switch (requestType) {
			case MConstant.REQUEST_CODE_LOGIN_:
				baseEntity = ParseLogin_Regist(baseEntity);
				break;
			case MConstant.REQUEST_CODE_REGIST:
				baseEntity = ParseLogin_Regist(baseEntity);
				break;
			case MConstant.REQUEST_CODE_GET_SELF_INFOR://获得我的个人信息
				return ParseGetSelfInfor(baseEntity);
			case MConstant.REQUEST_CODE_EDIT_SELF_INFOR://编辑的信息,返回是否成功
//				ParseEditSelfInfor(baseEntity.getResultObject());
				break;
			case MConstant.REQUEST_CODE_GET_MY_RANK://我的个人排名信息
				return ParseGetSelfRankInfor(baseEntity);
			case MConstant.REQUEST_CODE_WORLD_RANK://世界排名，可以限制某一地区，或者某一 行业
				return ParseWorldRank(baseEntity);
			case MConstant.REQUEST_CODE_REGION_RANK://地区排名
				
				break;
			case MConstant.REQUEST_CODE_INDUSTRY_RANK://行业排名
				break;
			case MConstant.REQUEST_CODE_COMPANY_RANK://公司排名
				break;
				
			case MConstant.REQUEST_CODE_REGIONS://地区列表
				baseEntity = PaseTypes(baseEntity);
				break;
			case MConstant.REQUEST_CODE_INDUSTRYS://行业列表
				break;
			case MConstant.REQUEST_CODE_COMPANYS://公司列表
				
				break;
			case MConstant.REQUEST_CODE_TELLOUTS://吐槽列表
				return ParseTellOuts(baseEntity);
			case MConstant.REQUEST_CODE_COMMENTS://评论列表
				return ParseComments(baseEntity);
			case MConstant.REQUEST_CODE_NEW_TELLOUT://新建吐槽
				break;
			case MConstant.REQUEST_CODE_MY_HISTORY_INFOR://我的历史信息
				return ParseMyHistoryInfor(baseEntity);
			}
		} catch (JSONException e) {
			Log.e("tag","json-->"+e);
			e.printStackTrace();
		}
		
		return baseEntity;
	}
	

	private static BaseEntity ParseBase(String str) throws JSONException{
		BaseEntity entity = new BaseEntity();
		JSONObject object = new JSONObject(str);
		// 得到code值
		entity.setCode(object.getInt("code"));
		JSONObject result = object.getJSONObject("result");
		entity.setResultObject(result);
		return entity;
	}
	
	/**
	 * 解析登录返回信息
	 * 
	 * @param responseStr
	 * @return
	 * @throws JSONException
	 */
	private static BaseEntity ParseLogin_Regist(BaseEntity baseEntity)
			throws JSONException {
		Map<String,String> map = new HashMap<String,String>();
		map.put(DbConstant.DB_USER_ID, baseEntity.getResultObject().getString(DbConstant.DB_USER_ID));
		baseEntity.setMap(map);
		return baseEntity;

	}
	
	//>{"code":200,"result":{"welfarePer":"50","nickName":"test1",
//	"welfare":"0","salaryPer":"25","salary":"1234","industryId":"0",
//	"regionId":"0"}}

	
	/**
	 * 解析我的个人信息
	 * @param object
	 * @return  所有的个人信息
	 * @throws JSONException 
	 */
	private static UserEntity ParseGetSelfInfor(BaseEntity baseEntity) throws JSONException{
		UserEntity userEntity = new UserEntity();
		userEntity.setCode(baseEntity.getCode());
		JSONObject object = baseEntity.getResultObject();
		userEntity.setName(object.getString(DbConstant.DB_USER_NICK_NAME));
//		userEntity.setCompany_name(object.getString(DbConstant.DB_COMPANY_NAME));
//		userEntity.setRegion_name(object.getString(DbConstant.DB_REGION_NAME));
//		userEntity.setIndustry_name(""+object.getString(DbConstant.DB_INDUSTRY_ID));
		userEntity.setRegionId(object.getInt(DbConstant.DB_USER_REGION_ID));
		userEntity.setIndustryId(object.getInt(DbConstant.DB_USER_INDUSTRY_ID));
//		userEntity.setCompanyId(object.getInt(DbConstant.DB_USER_COMPANY_ID));
		userEntity.setSalary(object.getInt(DbConstant.DB_USER_SALARY));
//		userEntity.setSalaryPer(object.getInt(DbConstant.DB_USER_SALARY_PER));
		userEntity.setWelfare(object.getInt(DbConstant.DB_USER_WELFARE));
//		userEntity.setWelfarePer(object.getInt(DbConstant.DB_USER_WELFARE_PER));
		userEntity.setComment(object.getString(DbConstant.DB_USER_COMMENT));
		userEntity.setMyCompany(object.getString(DbConstant.DB_USER_MY_COMPANY_NAME));
		userEntity.setMyIndustry(object.getString(DbConstant.DB_USER_MY_INDUSTRY_NAME));
		userEntity.setStartWorkTime(object.getString(DbConstant.DB_USER_START_WORK_TIME));
		return userEntity;
		
	}
	
	/**
	 * 获得个人排名信息
	 * @param object
	 * @return
	 * @throws JSONException 
	 */
	private static UserEntity ParseGetSelfRankInfor(BaseEntity baseEntity) throws JSONException{
		UserEntity entity = new UserEntity();
		entity.setCode(baseEntity.getCode());
		JSONObject object = baseEntity.getResultObject();
		entity.setWorldRank(object.getInt("worldRank"));
		entity.setRegionRank(object.getInt("regionRank"));
		entity.setIndustryRank(object.getInt("industryRank"));
		entity.setTotalSize(object.getInt("totalSize"));
//		entity.setName(object.getString(DbConstant.DB_USER_NICK_NAME));
		entity.setScore(object.getInt(DbConstant.DB_USER_SCORE));
		return entity;
	}
	
	
	/**
	 * 编辑我的个人信息
	 * @param object
	 * @return  是否成功编辑
	 */
	private static UserEntity ParseEditSelfInfor(BaseEntity baseEntity){
		UserEntity entity = new UserEntity();
		
		return entity;
	}
	
	/**
	 * 世界排行
	 * @param baseEntity
	 * @return
	 * @throws JSONException
	 */
	private static BaseEntity ParseWorldRank(BaseEntity baseEntity) throws JSONException{
		List<UserEntity> list = new ArrayList<UserEntity>();
		JSONArray array = baseEntity.getResultObject().getJSONArray("worldRank");
		JSONObject object = new JSONObject();
		UserEntity userEntity = null;
		for(int i = 0;i<array.length();i++){
			object = array.getJSONObject(i);
			userEntity = new UserEntity();
			userEntity.setName(object.getString(DbConstant.DB_USER_NICK_NAME));
			userEntity.setScore(object.getInt(DbConstant.DB_USER_SCORE));
			userEntity.setSalary(object.getInt(DbConstant.DB_USER_SALARY));
			userEntity.setIndustry_name(object.getString(DbConstant.DB_INDUSTRY_NAME));
			list.add(userEntity);
		}
		baseEntity.setList(list);
		return baseEntity;
	}
	
	/**
	 * 地区，行业，公司列表
	 */
	private static BaseEntity PaseTypes(BaseEntity baseEntity) throws JSONException{
		List<TypeEntity> list  = new ArrayList<TypeEntity>();
		TypeEntity entity =null;
		JSONArray array =baseEntity.getResultObject().getJSONArray("list");
		JSONObject object = null;
		for(int i = 0;i<array.length();i++){
			entity = new TypeEntity();
			object =array.getJSONObject(i);
			entity.setName(object.getString("name"));
			list.add(entity);
		}
		baseEntity.setList(list);
		return baseEntity;
	}
	
	//-->{"code":0,"result":{"totalSize":3,"list":[
//	{"telloutContent":"test01","nickName":"test1","telloutOk":"1"},{"telloutContent":"test02","nickName":"test2","telloutOk":"2"},{"telloutContent":"null","nickName":"test1","telloutOk":"0"}]}}

	/**
	 * 吐槽列表
	 * @param baseEntity
	 * @return
	 * @throws JSONException 
	 */
	private static BaseEntity ParseTellOuts(BaseEntity baseEntity) throws JSONException{
		List<TellOutEntity> list = new ArrayList<TellOutEntity>();
		JSONArray array = baseEntity.getResultObject().getJSONArray("list");
		JSONObject object = null;
		TellOutEntity entity = null;
		for(int i = 0;i<array.length();i++){
			object = array.getJSONObject(i);
			entity =new TellOutEntity();
			entity.setAuthorName(object.getString(DbConstant.DB_USER_NICK_NAME));
			entity.setContent(object.getString(DbConstant.DB_TELLOUT_CONTENT));
			entity.setOkNum(object.getInt(DbConstant.DB_TELLOUT_OK));
//			entity.setNoNum(object.getInt(DbConstant.DB_TELLOUT_NO));
			entity.setCommentNum(object.getInt(DbConstant.COMMENT_NUM));
			entity.setTellOutId(object.getInt(DbConstant.DB_TELLOUT_ID));
			list.add(entity);
		}
		baseEntity.setList(list);
		Map<String,String> map = new HashMap<String,String>();
		/**总数*/
		map.put(MConstant.OTHER_TOTAL_SIZE, baseEntity.getResultObject().getString(MConstant.OTHER_TOTAL_SIZE));
		/**当前页码*/
//		map.put("pageIndex", baseEntity.getResultObject().getString(MConstant.OTHER_TOTAL_SIZE));
		
		baseEntity.setMap(map);
		return baseEntity;
	}
	
	/**
	 * 评论列表
	 * @param baseEntity
	 * @return
	 * @throws JSONException
	 */
	private static BaseEntity ParseComments(BaseEntity baseEntity) throws JSONException{
		List<CommentEntity> list = new ArrayList<CommentEntity>();
		JSONArray array = baseEntity.getResultObject().getJSONArray("list");
		JSONObject object = null;
		CommentEntity entity = null;
		for(int i = 0;i<array.length();i++){
			object = array.getJSONObject(i);
			entity =new CommentEntity();
//			entity.setTellOutId(object.getInt(DbConstant.DB_COMMENT_TELLOUT_ID));
			entity.setContent(object.getString(DbConstant.DB_COMMENT_CONTENT));
			entity.setAuthor(object.getString(DbConstant.DB_USER_NICK_NAME));
			list.add(entity);
		}
		baseEntity.setList(list);
		Map<String,String> map = new HashMap<String,String>();
		/**总数*/
		map.put(MConstant.OTHER_TOTAL_SIZE, baseEntity.getResultObject().getString(MConstant.OTHER_TOTAL_SIZE));
		/**当前页码*/
//		map.put("pageIndex", baseEntity.getResultObject().getString("pageIndex"));
		
		baseEntity.setMap(map);
		
		return baseEntity;
	}
	
	private static BaseEntity ParseMyHistoryInfor(BaseEntity baseEntity) throws JSONException{
		List<UserEntity> list = new ArrayList<UserEntity>();
		JSONArray array = baseEntity.getResultObject().getJSONArray("list");
		JSONObject object = null;
		UserEntity entity = null;
		for(int i = 0;i<array.length();i++){
			object = array.getJSONObject(i);
			entity =new UserEntity();
			entity.setSalary(Integer.parseInt(object.getString(DbConstant.DB_USER_SALARY)));
			entity.setComment(object.getString(DbConstant.DB_USER_COMMENT));
			entity.setOther(object.getString("createTime"));
			list.add(entity);
		}
		baseEntity.setList(list);
		return baseEntity;
	}

}
