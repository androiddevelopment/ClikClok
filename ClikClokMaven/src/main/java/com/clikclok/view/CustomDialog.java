package com.clikclok.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clikclok.R;
import com.clikclok.util.UIUtilities;

/**
 * This is a custom implementation of a dialog to allow us to style our own buttons, background etc. The {@link AlertDialog} does not allow
 * us to style easily
 * @author David
 *
 */
public class CustomDialog extends Dialog {
	
	private Button leftButton;
	private Button rightButton;
	private TextView dialogText;
	private TextView clikTitleView;
	private TextView clokTitleView;
	private String dialogString;
	private String leftButtonText;
	private View.OnClickListener leftButtonClickListener;
	private String rightButtonText;
	private View.OnClickListener rightButtonClickListener;
	private int contentViewID;
	
	protected CustomDialog(Context context, Builder builder) {
		super(context, R.style.myAlertDialogStyle);
		this.dialogString = builder.dialogString;
		this.leftButtonText = builder.leftButtonText;
		this.leftButtonClickListener = builder.leftButtonClickListener;
		this.rightButtonText = builder.rightButtonText;
		this.rightButtonClickListener = builder.rightButtonClickListener;
		this.contentViewID = builder.contentViewID;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(contentViewID);
		dialogText = (TextView)findViewById(R.id.dialogText);
		leftButton = (Button)findViewById(R.id.leftButton);
		rightButton = (Button)findViewById(R.id.rightButton);
		clikTitleView = (TextView)findViewById(R.id.clikTitle);
		clokTitleView = (TextView)findViewById(R.id.clokTitle);
		
		clikTitleView.setTypeface(UIUtilities.setFont(R.string.clik_clok_font_type));
		clokTitleView.setTypeface(UIUtilities.setFont(R.string.clik_clok_font_type));
		dialogText.setTypeface(UIUtilities.setFont(R.string.dialog_text_font_type));
		
		dialogText.setText(new String(dialogString));
		leftButton.setText(new String(leftButtonText));
		leftButton.setOnClickListener(leftButtonClickListener);
				
		// If we only need one button then we will remove the right one completely
		if(rightButtonText != null && rightButtonText.length() >= 0)
		{
			rightButton.setText(new String(rightButtonText));
			rightButton.setOnClickListener(rightButtonClickListener);
		}
		else
		{
			rightButton.setVisibility(View.GONE);
		}	
	}
	
	public void setMessage(String text) {
		dialogText.setText(text);
	}
		
	/**
	 * Simple builder class to allow us to customize our {@link CustomDialog}
	 * @author David
	 *
	 */
	public static class Builder {
		private String dialogString;
		private String leftButtonText;
		private View.OnClickListener leftButtonClickListener;
		private String rightButtonText;
		private View.OnClickListener rightButtonClickListener;
		private int contentViewID;
		private Context context;	
		
		public Builder(Context c, int viewID)
		{
			this.context = c;
			this.contentViewID = viewID;	
		}
		
		public Builder setMessage(String messageString) {
			this.dialogString = messageString;
			return this;
		}
		
		public Builder createLeftButton(String buttonText, View.OnClickListener onClickListener) {
			this.leftButtonText = buttonText;
			this.leftButtonClickListener = onClickListener;
			return this;
		}
		
		public Builder createRightButton(String buttonText, View.OnClickListener onClickListener) {
			this.rightButtonText = buttonText;
			this.rightButtonClickListener = onClickListener;
			return this;
		}
				
		public CustomDialog create() {
			CustomDialog dialog = new CustomDialog(context, this);
			return dialog;
		}			
	}
}
