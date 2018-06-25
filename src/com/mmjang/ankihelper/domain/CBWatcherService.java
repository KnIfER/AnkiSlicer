package com.mmjang.ankihelper.domain;


import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ClipboardManager.OnPrimaryClipChangedListener;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.knziha.ankislicer.ui.CMN;
import com.knziha.ankislicer.ui.MyApplication;
import com.knziha.ankislicer.ui.PopupActivity;
import com.mmjang.ankihelper.programData.Settings;

public class CBWatcherService extends Service {
    private OnPrimaryClipChangedListener listener = new OnPrimaryClipChangedListener() {
        public void onPrimaryClipChanged() {
            performClipboardCheck();
        }
    };
    private ClipboardManager pm;

    @Override
    public void onCreate() {
    	//CMN.show("started");
        pm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        pm.addPrimaryClipChangedListener(listener);
        
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        Log.d("CB", "onDestroy ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pm.addPrimaryClipChangedListener(listener);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void performClipboardCheck() {
        Log.d("clip", "clip_changed");
       // if (!Settings.getInstance(MyApplication.getContext()).getMoniteClipboardQ()) {
          //  return;
        
        ClipboardManager ClipMan = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData pc = ClipMan.getPrimaryClip();
		int i=0;
		while(i<pc.getItemCount()) {
			//CMN.show(pc.getItemAt(i++).getUri().getPath());
            if (/*isEnglish(text)*/true) {
                long[] vibList = new long[1];
                vibList[0] = 10L;
                Intent intent = new Intent(getApplicationContext(), PopupActivity.class);
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra(Intent.EXTRA_TEXT, pc.getItemAt(i).coerceToText(CBWatcherService.this));
                intent.putExtra(Intent.EXTRA_HTML_TEXT, pc.getItemAt(i).coerceToHtmlText(CBWatcherService.this));
                startActivity(intent);
            }
			
			i++;
		}


    }

    static boolean isEnglish(String content) {
        //String puncs = "!()_+{}|:<>?-=[]\;.,\\\"";
        double nonEnglishCharThreahold = 0.2;
        //�ж��Ƿ�����ַ
        String[] urlHeads = {
                "http://",
                "https://",
                "ftp://"
        };

        for (String s : urlHeads) {
            if (content.startsWith(s)) {
                return false;
            }
        }


        int len = content.length();
        if (len == 0) {
            return false;
        }
        int notEnglishCount = 0;
        for (int i = 0; i < len; i++) {
            char c = content.charAt(i);
            //isPunctuationOrBlank(c);
            if ((c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A') || (c <= '9' && c >= '0') || isPunctuationOrBlank(c)) {

            } else {
                notEnglishCount++;
            }
        }
        double ratio = ((double) notEnglishCount) / ((double) len);
        return ratio <= nonEnglishCharThreahold;
    }

    public static boolean isPunctuationOrBlank(char c) {
        return c == ' '
                || c == ','
                || c == '.'
                || c == '!'
                || c == '?'
                || c == ':'
                || c == ';'
                || c == '('
                || c == ')'
                || c == '~'
                || c == '"'
                || c == '“'
                || c == '”'
                ;
    }

}
