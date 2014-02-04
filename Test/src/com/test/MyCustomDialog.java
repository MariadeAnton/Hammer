package com.test;

import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCustomDialog extends Dialog {
	 
    public MyCustomDialog(Context context, int theme) {
        super(context, theme);
    }
 
    public MyCustomDialog(Context context) {
        super(context);
    }
 
    /**
     * Helper class for creating a custom dialog
     */
    public static class Builder {
 
        private Context context;
        private String title;
        private Drawable tittleback;
        private String message;
        private String positiveButtonText;
        private Drawable posback;
        private String negativeButtonText;
        private Drawable negback;
        private View contentView;
        private Drawable contentback;
        private Drawable buttonback;
        private int icon;
 
        private DialogInterface.OnClickListener 
                        positiveButtonClickListener,
                        negativeButtonClickListener;
 
        public Builder(Context context) {
            this.context = context;
        }
 
        /**
         * Set the Dialog message from String
         * @param title
         * @return
         */
        public Builder setMessage(String message, Drawable background) {
            this.message = message;
            contentback=background;
            return this;
        }
 
        /**
         * Set the Dialog message from resource
         * @param title
         * @return
         */
        public Builder setMessage(int message,Drawable background) {
            this.message = (String) context.getText(message);
            contentback=background;
            return this;
        }
 
        /**
         * Set the Dialog title from resource
         * @param title
         * @return
         */
        public Builder setTitle(int title,Drawable back) {
            this.title = (String) context.getText(title);
            this.tittleback=back;
            return this;
        }
 
        /**
         * Set the Dialog title from String
         * @param title
         * @return
         */
        public Builder setTitle(String title,Drawable back) {
            this.title = title;
            this.tittleback=back;
            
            return this;
        }
 
        /**
         * Set a custom content view for the Dialog.
         * If a message is set, the contentView is not
         * added to the Dialog...
         * @param v
         * @return
         */
        public Builder setContentView(View v,Drawable background) {
            this.contentView = v;
            this.contentView.setBackground(background);
            return this;
        }
        
        public Builder setIcon(int icon) {
            this.icon = icon;
            return this;
        }
 
        /**
         * Set the positive button resource and it's listener
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText,Drawable buttonback,Drawable posback,
                DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) context
                    .getText(positiveButtonText);
            this.buttonback=buttonback;
            this.posback=posback;
            this.positiveButtonClickListener = listener;
            return this;
        }
 
        /**
         * Set the positive button text and it's listener
         * @param positiveButtonText
         * @param listener
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText,Drawable buttonback,Drawable posback,
                 DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.buttonback=buttonback;
            this.posback=posback;
            this.positiveButtonClickListener = listener;
            return this;
        }
 
        /**
         * Set the negative button resource and it's listener
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText,Drawable buttonback,Drawable negback,
                DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) context
                    .getText(negativeButtonText);
            this.buttonback=buttonback;
            this.negback=negback;
            this.negativeButtonClickListener = listener;
            return this;
        }
 
        /**
         * Set the negative button text and it's listener
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText,Drawable buttonback,Drawable negback,
                
                DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText;
            this.buttonback=buttonback;
            this.negback=negback;
            this.negativeButtonClickListener = listener;
            return this;
        }
 
        /**
         * Create the custom dialog
         */
        public MyCustomDialog create(Drawable dialogback) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final MyCustomDialog dialog = new MyCustomDialog(context, 
            		R.style.Dialog);
            View layout = inflater.inflate(R.layout.custom_dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ((LinearLayout)layout.findViewById(R.id.dialoback)).setBackground(dialogback);
            ((TextView) layout.findViewById(R.id.title)).setText(title);
            ((LinearLayout)layout.findViewById(R.id.tittleback)).setBackground(tittleback);   
            ((ImageView)layout.findViewById(R.id.icon)).setImageResource(icon);
            // set the confirm button
            if (positiveButtonText != null) {
                Button pos=(Button)layout.findViewById(R.id.positiveButton);
                       pos.setText(positiveButtonText);
                       pos.setBackground(posback);
                       ActivityRobot.changeCornersButtons(pos, 0, 0, 40, 0);
                
                if (positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.positiveButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    positiveButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_POSITIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.positiveButton).setVisibility(
                        View.GONE);
            }
            // set the cancel button
            if (negativeButtonText != null) {
            	Button neg=(Button)layout.findViewById(R.id.negativeButton);
                	neg.setText(negativeButtonText);
                	neg.setBackground(negback);
                	ActivityRobot.changeCornersButtons(neg, 0, 0, 0, 40);
                if (negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.negativeButton))
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    negativeButtonClickListener.onClick(
                                    		dialog, 
                                            DialogInterface.BUTTON_NEGATIVE);
                                }
                            });
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById(R.id.negativeButton).setVisibility(
                        View.GONE);
            }
            // set the content message
            if (message != null) {
                ((TextView)layout.findViewById(
                		R.id.message)).setText(message);
            }
            if (contentView != null) {
                // if no message set
                // add the contentView to the dialog body
                ((LinearLayout) layout.findViewById(R.id.view))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.view))
                	.setVisibility(View.VISIBLE);
                ((LinearLayout) layout.findViewById(R.id.view))
                        .addView(contentView, 
                                new LayoutParams(
                                        LayoutParams.MATCH_PARENT, 
                                        LayoutParams.MATCH_PARENT));
            }
            ((LinearLayout) layout.findViewById(R.id.buttonback)).setBackground(buttonback);
            ((LinearLayout) layout.findViewById(R.id.content)).setBackground(contentback);
            
            dialog.setContentView(layout);
            return dialog;
        }
 
    }
    
    
 
}
