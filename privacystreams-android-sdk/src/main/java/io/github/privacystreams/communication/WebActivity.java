package io.github.privacystreams.communication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.github.privacystreams.core.R;
import io.github.privacystreams.utils.Logging;

/**
 * Created by xiaobing1117 on 2017/8/21.
 */

public class WebActivity extends Activity {
    final TextView t = (TextView)findViewById(R.id.siftinfo);
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        Button button = (Button)findViewById(R.id.web);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SiftEmail sift = new SiftEmail(null,null);
                Logging.error("list sifts");
                for(int i=0;i<100;++i) {
                    String str = sift.listSifts("whatever", i);
                    if (str==null || str.equals(""))
                        break;
                }
            }
        });
        Uri uri = Uri.parse(url);
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
        browserIntent.addCategory(Intent.CATEGORY_BROWSABLE);
        startActivity(browserIntent);
    }


}