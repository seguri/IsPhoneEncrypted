package name.seguri.android.isphoneencrypted;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppCompatImageButton imageButton = (AppCompatImageButton) findViewById(R.id.imageButton);

        try {

            boolean isEncrypted = getSystemProperty("ro.crypto.state", "").toLowerCase().equals("encrypted");
            if (isEncrypted) {

                /* Positive icon */

                imageButton.setImageResource(R.drawable.ic_check_black_24dp);
                imageButton.setSupportBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green));
                imageButton.setOnClickListener(view -> snack(view, "Disk is fully encrypted :)"));

            } else {

                imageButton.setOnClickListener(view -> snack(view, "Disk is not encrypted :("));

            }

        } catch (Exception e) {

            Log.e(TAG, e.getMessage(), e);

            /* Unknown icon */

            imageButton.setImageResource(R.drawable.ic_warning_black_24dp1);
            // Foreground color
            imageButton.setColorFilter(ContextCompat.getColor(this, R.color.black));
            // Background color
            imageButton.setSupportBackgroundTintList(ContextCompat.getColorStateList(this, R.color.transparent));
            // Listener
            imageButton.setOnClickListener(view -> snack(view, e.getMessage()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                new AlertDialog.Builder(this)
                        .setTitle(R.string.app_name)
                        .setMessage("v1.0")
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void snack(View v, String s) {
        Snackbar.make(v, s, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    private String getSystemProperty(String key, String defValue)
            throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Types of parameters
        Class[] paramTypes = new Class[] { String.class, String.class };
        // Parameters
        Object[] params = new Object[] { key, defValue };
        // Target class
        Class<?> c = Class.forName("android.os.SystemProperties");
        // Target method
        Method m = c.getDeclaredMethod("get", paramTypes);
        // Invoke
        return (String) m.invoke(c, params);
    }
}
