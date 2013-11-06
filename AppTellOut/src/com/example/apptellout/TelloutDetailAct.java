package com.example.apptellout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adapter.CommentAdapter;
import com.example.entity.BaseEntity;
import com.example.entity.CommentEntity;
import com.example.entity.RequestEntity;
import com.example.entity.TellOutEntity;
import com.sun.constant.DbConstant;
import com.sun.constant.MConstant;

/**
 * 吐槽详细
 * @author sunqm
 *
 */
public class TelloutDetailAct extends BaseActivity implements OnClickListener{

	private ListView listView = null;
	/**当前吐槽的id*/
	private int TellOutId = 0;
	
	private int pageIndext =1;
	
	private CommentAdapter adapter = null;
	
	private List<CommentEntity> list = new ArrayList<CommentEntity>();
	
	private PopupWindow pop;
	
	private EditText etComment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tellout_detail_act);
		initView();
	}

	private void initView() {
		TellOutEntity entity = (TellOutEntity) getIntent().getSerializableExtra("tellout");
		listView = (ListView) findViewById(R.id.tellout_detail_listview);
		View headView = View.inflate(this, R.layout.tellout_item, null);
		TextView tvAuthor = (TextView) headView.findViewById(R.id.tellout_item_author_tv);
		TextView tvContent = (TextView) headView.findViewById(R.id.tellout_item_content_tv);
		TextView tvOk = (TextView) headView.findViewById(R.id.tellout_item_ok_tv);
		TextView tvNo = (TextView) headView.findViewById(R.id.tellout_item_no_tv);
		TextView tvComment = (TextView) headView.findViewById(R.id.tellout_item_comment_tv);
		listView.addHeaderView(headView);
		listView.addFooterView(footView());
		adapter = new CommentAdapter(this, list);
		TellOutId = entity.getTellOutId();
		tvAuthor.setText(entity.getAuthorName());
		tvContent.setText(entity.getContent());
		tvOk.setText(entity.getOkNum()+"");
		tvNo.setText(entity.getNoNum()+"");
		tvComment.setText(entity.getCommentNum()+"");
	}
	
	private View footView(){
		Button btn = new Button(this);
		btn.setText("添加评论");
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showCommentPop(v);
			}
		});
		return btn;
	}
	
	
	private void request(){
		RequestEntity requestEntity = new RequestEntity();
		requestEntity.setPost(false);
		requestEntity.setRequestType(MConstant.REQUEST_CODE_COMMENTS);
		Map<String,String> map = new HashMap<String,String>();
		map.put(DbConstant.DB_COMMENT_TELLOUT_ID,""+TellOutId);
		//当前页码
		map.put(MConstant.OTHER_PAGE_INDEXT, ""+pageIndext);
		requestEntity.setParams(map);
		request(requestEntity);
		
	}
	
	/**
	 * 添加评论请求
	 */
	private void requestAddComment(){
		RequestEntity requestEntity = new RequestEntity();
		requestEntity.setPost(false);
		requestEntity.setRequestType(MConstant.REQUEST_CODE_COMMENTS);
		Map<String,String> map = new HashMap<String,String>();
		map.put(DbConstant.DB_COMMENT_TELLOUT_ID,""+TellOutId);
		//评论内容
		map.put(DbConstant.DB_COMMENT_CONTENT,etComment.getText().toString());
		map.put(DbConstant.DB_USER_ID, MConstant.USER_ID_VALUE);
		requestEntity.setParams(map);
		request(requestEntity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void showResult(int type, BaseEntity baseEntity) {
		super.showResult(type, baseEntity);
		list = (List<CommentEntity>) baseEntity.getList();
		adapter.addData(list);
	}
	
	private void showCommentPop(View parentView){
		View view = View.inflate(this, R.layout.new_tell_out_act, null);
		view.findViewById(R.id.new_tell_out_back).setOnClickListener(this);
		view.findViewById(R.id.new_tell_out_save).setOnClickListener(this);
		etComment = (EditText) view.findViewById(R.id.new_tell_out_input_et);
		pop = new PopupWindow(view);
		pop.showAsDropDown(parentView);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.new_tell_out_back:
			pop.dismiss();
			break;
		case R.id.new_tell_out_save:
			String comment = etComment.getText().toString();
			if(comment!=null&&comment.length()>3){
				requestAddComment();
			}else{
				Toast.makeText(this, "您填的太少了", Toast.LENGTH_SHORT).show();
			}
			pop.dismiss();
			break;
		}
	}
	
	
	
}
