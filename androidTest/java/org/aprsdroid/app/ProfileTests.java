package org.aprsdroid.app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assume.assumeTrue;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProfileTests {
    private static final String TAG = "APRSdroid.Test";

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testThatExportProfileOpensTheChooser() {
        assumeTrue(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()));
        Log.i(TAG, "Launch activity");
        ActivityScenario scenario = ActivityScenario.launch(PrefsAct.class);
        Intents.intending(hasAction(Intent.ACTION_CHOOSER)).respondWith(new Instrumentation.ActivityResult(0, null));
        Log.i(TAG, "Open overflow");
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().getTargetContext());
        Log.i(TAG, "Press export");
        onView(withText(R.string.profile_export))
                .perform(click());
        Intents.intended(hasAction(Intent.ACTION_MAIN));  // Validate the activity launch
        Log.i(TAG, "Wait for response");
        try { Thread.sleep(1000); } catch(InterruptedException ex) { Log.i(TAG, "Sleep interrupted: " + ex.toString()); }
        Log.i(TAG, "Validate response");
        Intents.intended(allOf(hasAction(Intent.ACTION_CHOOSER)));
        Log.i(TAG, "Response validated");
        Intents.assertNoUnverifiedIntents();
    }
}
