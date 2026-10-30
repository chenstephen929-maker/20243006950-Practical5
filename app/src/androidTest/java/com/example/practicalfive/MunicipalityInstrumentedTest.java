package com.example.practicalfive;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SdkSuppress;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressImeActionButton;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * End-to-end checks for the Practical Five app.
 * Exercises the real Activities; only external browser launch is intercepted.
 */
@RunWith(AndroidJUnit4.class)
public class MunicipalityInstrumentedTest {
    private static final Instrumentation INSTRUMENTATION =
            InstrumentationRegistry.getInstrumentation();

    // Array indices follow the assignment's explicit prose, not its contradictory example.
    private static final City[] CITIES = {
            new City("Beijing", 0, R.drawable.beijing, R.color.municipality_beijing,
                    "https://english.beijing.gov.cn/"),
            new City("Shanghai", 1, R.drawable.shanghai, R.color.municipality_shanghai,
                    "https://english.shanghai.gov.cn/"),
            new City("Chongqing", 2, R.drawable.chongqing, R.color.municipality_chongqing,
                    "https://english.cq.gov.cn/"),
            new City("Tianjin", 3, R.drawable.tianjin, R.color.municipality_tianjin,
                    "https://en.tj.gov.cn/")
    };

    @Test
    public void imeSubmissionOpensCorrectActivityWithCanonicalName() {
        String[][] examples = {
                {"Beijing", "Beijing"},
                {"beijing", "Beijing"},
                {"BEIJING", "Beijing"},
                {"Shanghai", "Shanghai"},
                {"Tianjin", "Tianjin"},
                {"Chongqing", "Chongqing"},
                {"ChOnGqInG", "Chongqing"},
                {"  shanghai  ", "Shanghai"}
        };
        for (String[] example : examples) {
            Instrumentation.ActivityMonitor monitor = INSTRUMENTATION.addMonitor(
                    Municipality.class.getName(), null, false);
            Activity detail = null;
            try (ActivityScenario<MainActivity> scenario =
                         ActivityScenario.launch(MainActivity.class)) {
                onView(withId(R.id.municipality_input)).perform(
                        scrollTo(), replaceText(example[0]), pressImeActionButton());
                detail = monitor.waitForActivityWithTimeout(5000);
                assertNotNull("No detail Activity for input: " + example[0], detail);
                assertEquals(example[1], detail.getIntent().getStringExtra(
                        Municipality.EXTRA_MUNICIPALITY));
                onView(withId(R.id.municipality_name))
                        .check(matches(withText(example[1])));
            } finally {
                if (detail != null) {
                    Activity openedDetail = detail;
                    INSTRUMENTATION.runOnMainSync(openedDetail::finish);
                    INSTRUMENTATION.waitForIdleSync();
                }
                INSTRUMENTATION.removeMonitor(monitor);
            }
        }
    }

    @Test
    public void invalidImeInputDisplaysErrorAndRemainsOnMainScreen() {
        assertInvalidMainInput("", R.string.input_required);
        assertInvalidMainInput("   ", R.string.input_required);
        assertInvalidMainInput("Guangzhou", R.string.invalid_municipality);
    }

    @Test
    public void everyMunicipalityDisplaysItsOwnSummaryImageAndColors() {
        for (City city : CITIES) {
            try (ActivityScenario<Municipality> scenario =
                         ActivityScenario.launch(detailIntent(city.name))) {
                assertDetail(scenario, city);
            }
        }
    }

    @Test
    public void detailActivityNormalizesItsOwnIntentInput() {
        try (ActivityScenario<Municipality> scenario =
                     ActivityScenario.launch(detailIntent("  tIaNjIn  "))) {
            assertDetail(scenario, CITIES[3]);
        }
    }

    @Test
    public void municipalitySelectionSurvivesActivityRecreation() {
        try (ActivityScenario<Municipality> scenario =
                     ActivityScenario.launch(detailIntent("Chongqing"))) {
            scenario.recreate();
            assertDetail(scenario, CITIES[2]);
        }
    }

    @Test
    public void missingOrInvalidIntentDataFinishesDetailSafely() {
        assertDetailFinishes(new Intent(context(), Municipality.class));
        assertDetailFinishes(detailIntent(null));
        assertDetailFinishes(detailIntent(""));
        assertDetailFinishes(detailIntent("   "));
        assertDetailFinishes(detailIntent("Guangzhou"));

        // getStringExtra must not cause a crash when an unexpected caller uses another type.
        Intent wrongType = new Intent(context(), Municipality.class);
        wrongType.putExtra(Municipality.EXTRA_MUNICIPALITY, 42);
        assertDetailFinishes(wrongType);
    }



    @Test
    @SdkSuppress(minSdkVersion = 26)
    public void visitSiteUsesImplicitActionViewAndEachCitysOfficialUrl() {
        for (City city : CITIES) {
            WebsiteMonitor monitor = new WebsiteMonitor(false);
            INSTRUMENTATION.addMonitor(monitor);
            try (ActivityScenario<Municipality> scenario =
                         ActivityScenario.launch(detailIntent(city.name))) {
                onView(withId(R.id.visit_site)).perform(click());
                Intent requested = monitor.requested.get();
                assertNotNull("Visit site did not launch an Intent for " + city.name, requested);
                assertEquals(Intent.ACTION_VIEW, requested.getAction());
                assertEquals(city.url, requested.getDataString());
                assertNull("Website Intent must be implicit", requested.getComponent());
                assertNull("Website Intent must allow any suitable application", requested.getPackage());
                assertEquals("One click must launch one URL", 1, monitor.calls.get());
                onView(withId(R.id.municipality_name)).check(matches(withText(city.name)));
            } finally {
                INSTRUMENTATION.removeMonitor(monitor);
            }
        }
    }

    @Test
    @SdkSuppress(minSdkVersion = 26)
    public void unavailableBrowserShowsErrorAndLeavesDetailUsable() {
        WebsiteMonitor monitor = new WebsiteMonitor(true);
        INSTRUMENTATION.addMonitor(monitor);
        try (ActivityScenario<Municipality> scenario =
                     ActivityScenario.launch(detailIntent("Beijing"))) {
            onView(withId(R.id.visit_site)).perform(click());
            assertEquals(1, monitor.calls.get());
            assertNotNull(monitor.requested.get());
            onView(withText(R.string.browser_unavailable)).check(matches(isDisplayed()));
            onView(withId(R.id.municipality_name)).check(matches(withText("Beijing")));
            onView(withId(R.id.visit_site)).check(matches(isDisplayed()));
            onView(withId(R.id.visit_site)).check(matches(isClickable()));
            assertEquals(Lifecycle.State.RESUMED, scenario.getState());
        } finally {
            INSTRUMENTATION.removeMonitor(monitor);
        }
    }

    private void assertInvalidMainInput(String input, int errorResource) {
        Instrumentation.ActivityMonitor monitor = INSTRUMENTATION.addMonitor(
                Municipality.class.getName(), null, false);
        try (ActivityScenario<MainActivity> scenario =
                     ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.municipality_input)).perform(
                    scrollTo(), replaceText(input), pressImeActionButton());
            onView(withId(R.id.municipality_input))
                    .check(matches(hasErrorText(context().getString(errorResource))));
            assertEquals("Invalid input unexpectedly opened a detail Activity", 0, monitor.getHits());
            assertEquals(Lifecycle.State.RESUMED, scenario.getState());
        } finally {
            INSTRUMENTATION.removeMonitor(monitor);
        }
    }

    private void assertDetail(ActivityScenario<Municipality> scenario, City city) {
        onView(withId(R.id.municipality_name)).check(matches(withText(city.name)));
        onView(withId(R.id.municipality_info))
                .check(matches(isDescendantOfA(withId(R.id.municipality_info_scroll))));
        onView(withId(R.id.visit_site)).check(matches(isDisplayed()));
        onView(withId(R.id.visit_site)).check(matches(isClickable()));
        scenario.onActivity(activity -> {
            TextView name = activity.findViewById(R.id.municipality_name);
            TextView information = activity.findViewById(R.id.municipality_info);
            ImageView image = activity.findViewById(R.id.municipality_image);
            Button visitSite = activity.findViewById(R.id.visit_site);
            ScrollView scroll = activity.findViewById(R.id.municipality_info_scroll);
            assertNotNull(scroll);

            String[] summaries = activity.getResources().getStringArray(R.array.municipalities_array);
            assertEquals("Exactly four municipality summaries are required", 4, summaries.length);
            String summary = summaries[city.arrayIndex];
            assertTrue("Summary at index " + city.arrayIndex + " must identify " + city.name,
                    summary.contains(city.name));
            int words = summary.trim().split("\\s+").length;
            assertTrue(city.name + " summary has " + words + " words", words >= 80 && words <= 120);
            assertEquals("Wrong summary for " + city.name, summary, information.getText().toString());

            int cityColor = ContextCompat.getColor(activity, city.colorResource);
            assertTrue("Name needs the municipality background", name.getBackground() instanceof ColorDrawable);
            assertEquals(cityColor, ((ColorDrawable) name.getBackground()).getColor());
            assertEquals(cityColor, information.getCurrentTextColor());
            assertTrue(scroll.getBackground() instanceof ColorDrawable);
            assertTrue("Information text must remain readable",
                    ColorUtils.calculateContrast(information.getCurrentTextColor(),
                            ((ColorDrawable) scroll.getBackground()).getColor()) >= 4.5);
            ColorStateList tint = visitSite.getBackgroundTintList();
            assertNotNull("Visit site requires its municipality color", tint);
            assertEquals(cityColor, tint.getColorForState(visitSite.getDrawableState(), tint.getDefaultColor()));
            assertTrue("Municipality heading text must remain readable",
                    ColorUtils.calculateContrast(name.getCurrentTextColor(), cityColor) >= 4.5);
            assertTrue("Visit site text must remain readable",
                    ColorUtils.calculateContrast(visitSite.getCurrentTextColor(), cityColor) >= 4.5);

            Drawable expectedImage = ContextCompat.getDrawable(activity, city.imageResource);
            assertTrue("Municipality JPG must load as a bitmap", image.getDrawable() instanceof BitmapDrawable);
            assertTrue(expectedImage instanceof BitmapDrawable);
            assertTrue("Wrong photograph for " + city.name,
                    ((BitmapDrawable) image.getDrawable()).getBitmap().sameAs(
                            ((BitmapDrawable) expectedImage).getBitmap()));
        });
    }

    private void assertDetailFinishes(Intent intent) {
        try (ActivityScenario<Municipality> scenario = ActivityScenario.launch(intent)) {
            assertEquals("Invalid extra must finish safely", Lifecycle.State.DESTROYED, scenario.getState());
        }
    }

    private static Context context() {
        return INSTRUMENTATION.getTargetContext();
    }

    private static Intent detailIntent(String name) {
        return new Intent(context(), Municipality.class)
                .putExtra(Municipality.EXTRA_MUNICIPALITY, name);
    }

    /** API 26+ interception runs inside startActivity, so an exception exercises the real catch. */
    private static final class WebsiteMonitor extends Instrumentation.ActivityMonitor {
        private final boolean throwNoBrowser;
        private final AtomicReference<Intent> requested = new AtomicReference<>();
        private final AtomicInteger calls = new AtomicInteger();

        WebsiteMonitor(boolean throwNoBrowser) {
            super();
            this.throwNoBrowser = throwNoBrowser;
        }

        @Override
        public Instrumentation.ActivityResult onStartActivity(Intent intent) {
            if (!Intent.ACTION_VIEW.equals(intent.getAction())) {
                return null;
            }
            requested.set(new Intent(intent));
            calls.incrementAndGet();
            if (throwNoBrowser) {
                throw new ActivityNotFoundException("No browser available (test fixture)");
            }
            return new Instrumentation.ActivityResult(Activity.RESULT_OK, null);
        }
    }

    private static final class City {
        final String name;
        final int arrayIndex;
        final int imageResource;
        final int colorResource;
        final String url;

        City(String name, int arrayIndex, int imageResource, int colorResource, String url) {
            this.name = name;
            this.arrayIndex = arrayIndex;
            this.imageResource = imageResource;
            this.colorResource = colorResource;
            this.url = url;
        }
    }
}
