package com.zhichen.parking.widget;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

public class MarqueeTextView extends TextView {

	public MarqueeTextView(Context context) {
		super(context);
		init();
	}

	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init()
	{
		setSingleLine(true);
		setMarqueeRepeatLimit(-1);
		setFocusable(true);
		setFocusableInTouchMode(true);
		setEllipsize(TruncateAt.MARQUEE);
	}

	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		return true;
	}
}
