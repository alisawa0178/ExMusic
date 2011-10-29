package k3.app.Exmuisc;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MakeKASHIView {
	private LinearLayout View = null;
	public WindowManager.LayoutParams SLayoutParams = null;
	public Context context;
	public MakeKASHIView(Context context) {
		SinitLayoutParams();
		this.context = context;
	}
	
	public void MakeView(final WindowManager wm,String text){
		// 前回使用した ImageView が残っている場合は除去（念のため？）
        if (View != null) {
            wm.removeView(View);
        }
        
        // ImageView を生成し WindowManager に addChild する
        View = new LinearLayout(context);
        TextView kashiView = new TextView(context);
        ScrollView scrollView = new ScrollView(context);
        scrollView.addView(kashiView);
        scrollView.setPadding(30, 40, 30, 40);
        
        kashiView.setText(text);
        kashiView.setTextColor(Color.WHITE);
        scrollView.setVerticalScrollBarEnabled(false);
       
        View.addView(scrollView);
        View.setBackgroundColor(Color.argb(200, 8, 10, 10));
        
        wm.addView(View, SLayoutParams);
        View.startLayoutAnimation();
        
        kashiView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {removeView(wm);}
		});
        View.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {removeView(wm);}
		});
	}
	
	public void removeView(WindowManager wm){
		if (View != null) {
//	        AlphaAnimation animation = new AlphaAnimation(1.0f, 0);
//			animation.setDuration(1000);
//			animation.setFillAfter(true);   //終了後を保持
//			animation.setFillEnabled(true);
//			View.setAnimation(animation);
			wm.removeView(View);
			View = null;
        }
	}
	
	/** ImageView 用 LayoutParams の初期化 */
    private void SinitLayoutParams() {
    	SLayoutParams = new WindowManager.LayoutParams();
    	SLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
    	SLayoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
    	SLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
    	SLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
    	SLayoutParams.format = PixelFormat.TRANSLUCENT;
    	SLayoutParams.windowAnimations = 0;
    }
}
