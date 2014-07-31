	package cn.com.bjnews.thinker.act;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.bjnews.thinker.R;
import cn.com.bjnews.thinker.adapter.ViewPagerAdapterMedia;
import cn.com.bjnews.thinker.entity.MediaEntity;
import cn.com.bjnews.thinker.entity.NewsEntity;
import cn.com.bjnews.thinker.entity.RelatedEntity;
import cn.com.bjnews.thinker.img.CommonUtil;
import cn.com.bjnews.thinker.img.FileCache;
import cn.com.bjnews.thinker.img.ImageLoader;
import cn.com.bjnews.thinker.utils.FileDown;
import cn.com.bjnews.thinker.utils.Mconstant;
import cn.com.bjnews.thinker.utils.Utils;
import cn.com.bjnews.thinker.view.MViewPager;
import cn.com.bjnews.thinker.view.ViewPagerImageView;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.EmailHandler;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;


@SuppressLint("NewApi")
public class NewsDetailAct extends BaseAct implements OnClickListener, OnPageChangeListener {

	private LinearLayout llImages, llRelateds;

//	private View viewImage;

	private ViewPagerImageView image;

	private TextView tvViewpagerCaption;

	private TextView tvTitle;

	private TextView tvData;
	// 文章来源
	private TextView tvFrom;
	// 作者
	private TextView tvAuthor;

	private TextView tvContent;

	private MViewPager viewPagerTop;
	/**全屏模式下的图片浏览？？？？？*/
//	private ViewPager viewPagerBig;
	
	private RelativeLayout rlViewPagerTop;
	
	private LinearLayout llViewPagerTopIcons;
	
	private ViewPagerAdapterMedia viewPagerAdapterMedia;
	
	private NewsEntity newsEntity;

	private String newsId;
	
	/**本文章所属的 channelid*/
	private int channelId = 0;

	/**当前应用是否正在运行*/
	private boolean isRunning = false;
	
	private final static int RELATED_ID = 1;
	/**图片id*/
	private final static int VIEWPAGER_ITEM_ID = 2;
	
	private final static int IMG_ID = 3;
	
	private int selectedImg;
	
	private int selectedViewpager;
	
	private ImageLoader imgLoader;
	
	/***/
	private boolean IsPush = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_detail);
		setActionBarLayout(R.layout.act_detail_title);
		initView();
		initData();
//		Log.d("tag", "getcalling->"+(getCallingActivity()==null)+getIntent().getAction());
//		if(getCallingActivity()==null){
//			IsPush = true;
//		}else{	
//			IsPush = false;
//		}
	}

	private void initView() {
		newsId = getIntent().getStringExtra("id");
		ImageView imgBack = (ImageView) findViewById(R.id.back);
		llImages = (LinearLayout) findViewById(R.id.detail_act_fragment_pics_ll);
		llRelateds = (LinearLayout) findViewById(R.id.detail_act_fragment_related_ll);
		tvTitle = (TextView) findViewById(R.id.detail_act_fragment_title);
		tvData = (TextView) findViewById(R.id.detail_act_fragment_date);
		tvFrom = (TextView) findViewById(R.id.detail_act_fragment_from);
		tvAuthor = (TextView) findViewById(R.id.detail_act_fragment_author);
		tvContent = (TextView) findViewById(R.id.detail_act_fragment_content);
		viewPagerTop = (MViewPager) findViewById(R.id.detail_act_fragment_viewpager_small);
		tvViewpagerCaption = (TextView) findViewById(R.id.detail_act_fragment_viewpager_small_caption);
//		viewPagerBig = (ViewPager) findViewById(R.id.detail_act_fragment_viewpager_big);
		llViewPagerTopIcons = (LinearLayout) findViewById(R.id.detail_act_fragment_viewpager_small_ll);
		rlViewPagerTop = (RelativeLayout) findViewById(R.id.detail_act_fragment_viewpager_small_rl);
		
		
//		viewPagerTop = new MViewPager(this);
		viewPagerTop.setOnClickListener(this);
		viewPagerTop.setOnPageChangeListener(this);
		if(Utils.getSystemVersionNew()){
			imgBack.setVisibility(View.INVISIBLE);
		}else{
			imgBack.setVisibility(View.VISIBLE);
		}
		resizeViewpager();
	}
	 
	private void initData() {
		imgLoader = new ImageLoader(this, R.drawable.default_img,false);
		boolean temp = getIntent().getSerializableExtra("news") instanceof NewsEntity;
		//是否是广播
		
		if(getIntent().getAction()!=null && getIntent().getAction().equals("brocastreceiver")){
			IsPush = false;
		}else if(getIntent().getAction()!=null && getIntent().getAction().equals("pushReceiver")){
			IsPush = true;
		}
		
		if(getIntent().hasExtra("channelId")){
			channelId = getIntent().getIntExtra("channelId",0);
//			Log.d("tag","news_id>>"+ getIntent().getIntExtra("news_id",0));
		}
			
		if (temp) {
			newsEntity = (NewsEntity) getIntent().getSerializableExtra("news");
		}
//		Log.d("tag", temp+"news-==content>--" + newsEntity);
		if (newsEntity != null) {
//			Log.d("tag", "news-==content>" + newsEntity.medias.size()
//					+ "title->" + newsEntity.title);
			tvTitle.setText(newsEntity.title);
			tvData.setText(new Utils().formatDate(newsEntity.pubDate)+" "+newsEntity.source);
			tvContent.setText(newsEntity.content);
//			tvContent.setLetterSpacing(0);
			showViewPagerTop(newsEntity.medias);
			showPics(newsEntity.images);
			showRelated(newsEntity.relateds);
			// newsEntity.getMedias().size()
			
		}
		
	}


	private void resizeViewpager(){
		ViewGroup.LayoutParams params = viewPagerTop.getLayoutParams();
		params.width = CommonUtil.getScreenWidth(this);
		params.height = (int)(CommonUtil.getScreenWidth(this)-20)*2/3;
		viewPagerTop.setLayoutParams(params);
		
	}

	/**
	 * 
	 */
	private void showPics(List<MediaEntity> list) {
		if(list.size()==0){
			llImages.setVisibility(View.GONE);
			return ;
		}
		llImages.setVisibility(View.VISIBLE);
		llImages.removeAllViews();
		TextView imageText =null;
		for (int i = 0; i < list.size(); i++) {
			View viewImage = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.act_detail_pics, null);
			image = (ViewPagerImageView) viewImage.findViewById(R.id.act_detail_pics_img);
			imageText = (TextView) viewImage.findViewById(R.id.act_detail_pics_tv);
			image.setId(IMG_ID);
			imgLoader.setIsBg(true);
			imgLoader.setImgWidth(CommonUtil.getScreenWidth(this)-20);
			Utils.getMemory("detail");
			imgLoader.DisplayImage(list.get(i).pic , image,false);
			Utils.getMemory("detail-end");
//			image.setTag(i);
			image.setOnClickListener(this);
			// image.setImage
			imageText.setText(list.get(i).caption );
//			image.setTag(2,list.get(i).getPic());
			llImages.addView(viewImage);
			
		}
	}

	private FileDown down =new FileDown();
	
	/**
	 * 上部 媒体 
	 */
	private void showViewPagerTop(ArrayList<MediaEntity> medias) {
//		if(medias.size()==0){//如果当前没有大图，显示缩略图
//			MediaEntity tempEntity = new MediaEntity();
//			tempEntity.pic = newsEntity.thumbnail;
//			tempEntity.flag =1;//图片
//			tempEntity.caption = "";
//			medias.add(tempEntity);
//		}
		if(medias.size() ==0 ){
			rlViewPagerTop.setVisibility(View.GONE);
			return;
		}
		
		if(medias.size()==1){//只有一个不显示下部位置tab
			llViewPagerTopIcons.setVisibility(View.GONE);
		}else{
			llViewPagerTopIcons.setVisibility(View.VISIBLE);
		}
		//caption
		if(medias.get(0).caption==null||medias.get(0).caption.equals("")){
			tvViewpagerCaption.setVisibility(View.GONE);
		}else{
			tvViewpagerCaption.setVisibility(View.VISIBLE);
		}
		tvViewpagerCaption.setText(medias.get(0).caption);
		rlViewPagerTop.setVisibility(View.VISIBLE);
		ArrayList<View> views = new ArrayList<View>();
		View viewImage;
		ViewPagerImageView image = null;
//		TextView imageText = null;
		ImageView imageVideo = null;
		llViewPagerTopIcons.removeAllViews();
		ImageView ImgIcon;
		
		for(int i =0;i<medias.size();i++){
			viewImage = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.act_detail_pic, null);
			image = (ViewPagerImageView) viewImage.findViewById(R.id.act_detail_pic_img);
			image.setBackgroundResource(R.drawable.default_img);
			ScaleType scaleType = ScaleType.MATRIX;
			image.setScaleType(scaleType);
			imgLoader.DisplayImage(medias.get(i).pic ,image,false);
//			imgLoader.setImgWidth(new ImageUtils(this).getWidth());
//			imageText = (TextView) viewImage.findViewById(R.id.act_detail_pic_tv);
			imageVideo = (ImageView) viewImage.findViewById(R.id.act_detail_video);
			if(medias.get(i).flag  == 0 ){//视频
				imageVideo.setVisibility(View.VISIBLE);
			}else{
				imageVideo.setVisibility(View.GONE);
			}
			image.setId(VIEWPAGER_ITEM_ID);
			image.setOnClickListener(this);
			// image.setImage
//			imageText.setText(medias.get(i).caption);
			views.add(viewImage);
			
			/***/
			ImgIcon = new ImageView(this);
			ImgIcon.setImageResource(R.drawable.scroll_selected);
			llViewPagerTopIcons.addView(ImgIcon, i);
//			Log.d("tag","downFile-->"+medias.get(i).flag +"'''"+down.isExit(medias.get(i).video ));
			if(medias.get(i).flag  == 0 && down.isExit(medias.get(i).video) == null){
				down.down(this, null, medias.get(i).video );
			}
		}
		llViewPagerTopIcons.setPadding(0, 10, 0, 0);
		Utils.setViewPagerIcon(llViewPagerTopIcons, 0);
		viewPagerAdapterMedia = new ViewPagerAdapterMedia(views);
		viewPagerTop.setAdapter(viewPagerAdapterMedia);
	}

	/**
	 * 像是相关链接	
	 * @param relateds
	 */
	private void showRelated(ArrayList<RelatedEntity> relateds) {
		if (relateds.size() == 0) {
			llRelateds.setVisibility(View.GONE);
			return;
		}
		llRelateds.setVisibility(View.VISIBLE);
		llRelateds.setPadding(10, 8, 0, 8);
		Resources resource = (Resources) getResources();
		ColorStateList txtColor = (ColorStateList) resource
				.getColorStateList(R.color.related_txt);
		TextView tvTemp;
		for (int i = 0; i < relateds.size(); i++) {//
			tvTemp = new TextView(this);
			tvTemp.setText(relateds.get(i).getTitle());
			tvTemp.setTextColor(txtColor);
			tvTemp.setPadding(0, 10, 0, 10);
			tvTemp.setTextSize(18);
			tvTemp.setTag(relateds.get(i).getUrl());
			tvTemp.setId(RELATED_ID);
			tvTemp.setOnClickListener(this);
			llRelateds.addView(tvTemp);
		}
	}


	public void onClick(View view) {
//		Log.d("tag","click-->1"+view.getId());
		if(!Mconstant.isClickAble){
			return;
		}
//		Log.d("tag","click-->2"+Mconstant.isClickAble);
		new Utils().setViewUnable();
		switch (view.getId()) {
		case R.id.back:
//			Toast.makeText(this, "startAct"+IsPush, Toast.LENGTH_SHORT).show();
			if(IsPush){
				try{
						
					
					Mconstant.currentPageIndex = MainActivity.getPageIndex(channelId);
					if(!Utils.mainIsExit(this)){
						Intent i = new Intent(NewsDetailAct.this,MainActivity.class);
						i.putExtra("channelId",channelId);
						NewsDetailAct.this.startActivity(i);
					}
				}catch(Exception e){
					e.printStackTrace();
					Log.e("tag","error-->"+e);
					Intent i = new Intent(NewsDetailAct.this,MainActivity.class);
					i.putExtra("channelId",channelId);
					NewsDetailAct.this.startActivity(i);
					
				}
				
			}
			finish();
			
			break;
		case R.id.share:
//			share();
			shareTestInit();
			break;
		case R.id.act_detail_pic_tv:
			startActivity(new Intent(NewsDetailAct.this, ActPlay.class));
			break;
		case RELATED_ID:// 相关内容
			startActivity(new Intent(NewsDetailAct.this, ActWeb.class)
					.putExtra("url", view.getTag().toString()));
			break;
		case R.id.detail_act_fragment_viewpager_small://
			ArrayList<MediaEntity> temp1 =(ArrayList<MediaEntity>)  newsEntity.medias.clone();
			temp1.addAll(newsEntity.images);
			Intent i = new Intent(NewsDetailAct.this,ActPlay.class);
			i.putExtra("selectedId", selectedViewpager);
			i.putParcelableArrayListExtra("medias",temp1);
//			Log.d("tag","temp1————》"+temp1.size());
			startActivity(i);
			break;
		case IMG_ID://
			int selectedImg = 0;
			for(int j = 0 ;j<newsEntity.images.size();j++){
				if(llImages.getChildAt(j).equals(view)){
					selectedImg = j;
					break;
				}
			}
//			Log.d("tag","temp————1》"+selectedImg);
			selectedImg = newsEntity.medias.size() + selectedImg;
			ArrayList<MediaEntity> temp = (ArrayList<MediaEntity>) newsEntity.medias.clone();
//			Log.d("tag","temp————2》"+temp.size());
			temp.addAll(newsEntity.images);
			Intent i1 = new Intent(NewsDetailAct.this,ActPlay.class);
			i1.putExtra("selectedId", selectedImg);
			i1.putParcelableArrayListExtra("medias",temp);
//			Log.d("tag","temp————3》"+temp.size());
			startActivity(i1);
			break;
		}
	}
	

	/**
	 * 分享
	 */
	private void share() {
		Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);//
		intent.setType("*/*");//text/plain;image/*
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		
		StringBuilder sb = new StringBuilder(newsEntity.title);
		sb.append(newsEntity.picUrl).append(newsEntity.weburl);
		
		
		intent.putExtra(Intent.EXTRA_TEXT,sb.toString().replace("null", ""));
		
		// 添加图片
		ArrayList<Uri> picturesUriArrayList = new ArrayList<Uri>();
		FileCache fileCache = new FileCache(this);
		for(int i =0;i<newsEntity.medias.size();i++){
//			sb.append(newsEntity.medias.get(i).pic);
			picturesUriArrayList.add(Uri.fromFile(fileCache.getFile(newsEntity.medias.get(i).pic)));
		}
//		intent.setAction(Intent.ACTION_SEND_MULTIPLE);
		intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,
				picturesUriArrayList);
//		intent.putExtra(Intent.EXTRA_TITLE, "tile");
		if (hasApplication(intent)) {
			startActivity(Intent.createChooser(intent, getTitle()));
		} else {
			Toast.makeText(this, "当前没有应用可以分享", Toast.LENGTH_SHORT).show();
		}
		
		
	}

	private void shareTest(com.umeng.socialize.controller.UMSocialService mController){
		mController.openShare(this, false);
	}

	private void addWinXin() {
		//微信
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx967daebe835fbeac";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(this, appId);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(this, appId);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		
		//QQ
		//参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
//		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "100424468",
//		                "c7394704798a158208a74ab60104f0ba");
//		qqSsoHandler.addToSocialSDK();
//		
//		//qq空间
//		//参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
//		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "100424468",
//		                "c7394704798a158208a74ab60104f0ba");
//		qZoneSsoHandler.addToSocialSDK();
		
		// 添加短信
		SmsHandler smsHandler = new SmsHandler();
		smsHandler.addToSocialSDK();
		// 添加email
		EmailHandler emailHandler = new EmailHandler();
		emailHandler.addToSocialSDK();
		
	}

	private void shareTestInit(){
		addWinXin();
		
		// 首先在您的Activity中添加如下成员变量
		final com.umeng.socialize.controller.UMSocialService mController =com.umeng.socialize.controller.UMServiceFactory.getUMSocialService("com.umeng.share");
		mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN,SHARE_MEDIA.TENCENT);
		// 设置分享内容
		(mController).setShareContent(newsEntity.title+newsEntity.weburl);
		// 设置分享图片, 参数2为图片的url地址
		if(newsEntity.medias.size()>0)
			mController.setShareMedia(new UMImage(this, 
		                                      	newsEntity.medias.get(0).pic));
		// 设置分享图片，参数2为本地图片的资源引用
		//mController.setShareMedia(new UMImage(getActivity(), R.drawable.icon));
		// 设置分享图片，参数2为本地图片的路径(绝对路径)
		//mController.setShareMedia(new UMImage(getActivity(), 
//		                                BitmapFactory.decodeFile("/mnt/sdcard/icon.png")));

		// 设置分享音乐
		//UMusic uMusic = new UMusic("http://sns.whalecloud.com/test_music.mp3");
		//uMusic.setAuthor("GuGu");
		//uMusic.setTitle("天籁之音");
		// 设置音乐缩略图
		//uMusic.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		//mController.setShareMedia(uMusic);

		// 设置分享视频
		//UMVideo umVideo = new UMVideo(
//		          "http://v.youku.com/v_show/id_XNTE5ODAwMDM2.html?f=19001023");
		// 设置视频缩略图
		//umVideo.setThumb("http://www.umeng.com/images/pic/banner_module_social.png");
		//umVideo.setTitle("友盟社会化分享!");
		//mController.setShareMedia(umVideo);
		
		//新浪微博
		shareTest(mController);
//		shareSina(mController);
	}
	
	/**
	 * 底层 自助 分享面板
	 * @param mController
	 */
//	private void shareSina(
//			com.umeng.socialize.controller.UMSocialService mController) {
//		// 参数1为Context类型对象， 参数2为要分享到的目标平台， 参数3为分享操作的回调接口
//		mController.postShare(this, SHARE_MEDIA.QQ, new SnsPostListener() {
//			@Override
//			public void onStart() {
//				Toast.makeText(NewsDetailAct.this, "开始分享.", Toast.LENGTH_SHORT)
//						.show();
//			}
//
//			@Override
//			public void onComplete(SHARE_MEDIA platform, int eCode,
//					SocializeEntity entity) {
//				if (eCode == 200) {
//					Toast.makeText(NewsDetailAct.this, "分享成功.",
//							Toast.LENGTH_SHORT).show();
//				} else {
//					String eMsg = "";
//					if (eCode == -101) {
//						eMsg = "没有授权";
//					}
//					Toast.makeText(NewsDetailAct.this,
//							"分享失败[" + eCode + "] " + eMsg, Toast.LENGTH_SHORT)
//							.show();
//				}
//			}
//		});
//	}
	
	public boolean hasApplication(Intent intent){    
        PackageManager packageManager = getPackageManager();    
        //查询是否有该Intent的Activity    
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);    
        //activities里面不为空就有，否则就没有    
        return activities.size() > 0 ; 
	}
	
	@Override
	public void onTrimMemory(int level) {
//		Log.d("tag","trim---<>"+level);
		super.onTrimMemory(level);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
		
	}

	@Override
	public void onPageSelected(int arg0) {
		selectedViewpager = arg0; 
//		Log.d("tag","pageselected"+arg0+"size"+newsEntity.medias.size());
		if(newsEntity.medias.get(arg0).caption==null||newsEntity.medias.get(arg0).caption.equals("")){
			tvViewpagerCaption.setVisibility(View.GONE);
		}else{
			tvViewpagerCaption.setVisibility(View.VISIBLE);
		}
		tvViewpagerCaption.setText(newsEntity.medias.get(arg0).caption);
		Utils.setViewPagerIcon(llViewPagerTopIcons, arg0);
	}
	
	private void setActionBarLayout( int layoutId ){
	    ActionBar actionBar = getActionBar( );
	    if( null != actionBar ){
	        actionBar.setDisplayShowHomeEnabled( false );
	        actionBar.setDisplayHomeAsUpEnabled(Utils.getSystemVersionNew());
	        actionBar.setDisplayShowCustomEnabled(true);
	        LayoutInflater inflator = (LayoutInflater)   this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        View v = inflator.inflate(layoutId, null);
	        ActionBar.LayoutParams layout = new  ActionBar.LayoutParams(android.app.ActionBar.LayoutParams.FILL_PARENT, android.app.ActionBar.LayoutParams.FILL_PARENT);
	        actionBar.setCustomView(v,layout);
	    }
	}
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item)
	    {
	        // TODO Auto-generated method stub
	        if(item.getItemId() == android.R.id.home)
	        {
	        	if(IsPush){
					try{
						Mconstant.currentPageIndex = MainActivity.getPageIndex(channelId);
						if(!Utils.mainIsExit(this)){
							Intent i = new Intent(NewsDetailAct.this,MainActivity.class);
							i.putExtra("channelId",channelId);
							NewsDetailAct.this.startActivity(i);
						}
					}catch(Exception e){
						e.printStackTrace();
						Intent i = new Intent(NewsDetailAct.this,MainActivity.class);
						i.putExtra("channelId",channelId);
						NewsDetailAct.this.startActivity(i);
					}
					
				}
	            finish();
	            return true;
	        }
	        return super.onOptionsItemSelected(item);
	    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(IsPush){
				try{
					Mconstant.currentPageIndex = MainActivity.getPageIndex(channelId);
					if(!Utils.mainIsExit(this)){
						Intent i = new Intent(NewsDetailAct.this,MainActivity.class);
						i.putExtra("channelId",channelId);
						NewsDetailAct.this.startActivity(i);
					}
				}catch(Exception e){
					e.printStackTrace();
//					Log.e("tag","error-->"+e);
					Intent i = new Intent(NewsDetailAct.this,MainActivity.class);
					i.putExtra("channelId",channelId);
					NewsDetailAct.this.startActivity(i);
				}
				
			}
            finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	 
	 

}
