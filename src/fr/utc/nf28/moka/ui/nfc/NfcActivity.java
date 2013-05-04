package fr.utc.nf28.moka.ui.nfc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import fr.utc.nf28.moka.MainActivity;
import fr.utc.nf28.moka.R;
import static fr.utc.nf28.moka.util.LogUtils.makeLogTag;

/**
 * Created with IntelliJ IDEA.
 * User: Hi-Bro
 * Date: 03/05/13
 * Time: 08:29
 * To change this template use File | Settings | File Templates.
 */
public class NfcActivity extends Activity {

    private static final String TAG = makeLogTag(NfcActivity.class);

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc);

        final Button buttonSkip = (Button)findViewById(R.id.skip);
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NfcActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}