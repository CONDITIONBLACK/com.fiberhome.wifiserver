package com.fenghuo.qzj;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Environment;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;
import com.fiberhome.wifiserver.C0171R;
import java.io.File;
import org.apache.http.cookie.ClientCookie;

public class VideoViewActivity extends Activity {
    private MediaController mediaController;
    String path = ("" + Environment.getExternalStorageDirectory() + "/test.3gp");
    private TextView tv;
    private VideoView view;

    /* renamed from: com.fenghuo.qzj.VideoViewActivity$1 */
    class C01551 implements OnCompletionListener {
        C01551() {
        }

        public void onCompletion(MediaPlayer mediaPlayer) {
            VideoViewActivity.this.finish();
        }
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(C0171R.layout.videoview);
        this.tv = (TextView) findViewById(C0171R.id.tv);
        this.tv.setText(getIntent().getStringExtra("name"));
        this.view = (VideoView) findViewById(C0171R.id.videoview);
        this.mediaController = new MediaController(this);
        File file = new File(getIntent().getStringExtra(ClientCookie.PATH_ATTR));
        if (file.exists()) {
            this.view.setVideoPath(file.getPath());
            this.view.setMediaController(this.mediaController);
            this.mediaController.setMediaPlayer(this.view);
            this.view.requestFocus();
            this.view.start();
            this.view.setOnCompletionListener(new C01551());
        }
    }
}
