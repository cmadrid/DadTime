package ec.edu.espol.integradora.dadtime;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;


import layout.FragmentEntertainments;
import layout.FragmentMemories;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Activity activity;
    private Context context;
    private FloatingActionButton fab;
    FragmentEntertainments fragmentEntertainments = FragmentEntertainments.newInstance();
    FragmentMemories fragmentMemories = FragmentMemories.newInstance();

    private ViewPager.OnPageChangeListener onPageChangeListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position)
        {
            if(position==0) {//fab.show();
                fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(1)).start();
            }
            else {//fab.hide();
                fab.animate().translationY(fab.getHeight() + 640).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {}
    };

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = (FloatingActionButton)findViewById(R.id.fabFilterActivity);
        fab.hide();
        /*getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_dadtime);
        getSupportActionBar().setDisplayUseLogoEnabled(true);*/
        this.activity = this;
        this.context = getApplicationContext();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setOnPageChangeListener(onPageChangeListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        final Menu finalMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.camera_menu)
        {
            startActivity(new Intent(getApplicationContext(), CameraActivity.class));
            return true;
        }
        else if(id == R.id.shortcut_setting){
            openCamera();
        }
        else if (id == R.id.start_service) {
            startService(new Intent(getBaseContext(),ServiceBackground.class));
            //Toast.makeText(this,"inicia",Toast.LENGTH_LONG).show();
            return true;
        }
        else if (id == R.id.stop_service) {

            if(!stopService(new Intent(getBaseContext(),ServiceCollageBackground.class)))
                startService(new Intent(getBaseContext(),ServiceCollageBackground.class));
            stopService(new Intent(getBaseContext(),ServiceBackground.class));
            //Toast.makeText(this,"termina",Toast.LENGTH_LONG).show();
            return true;
        }
        else if (id == R.id.collage_setting) {
            Collage.createNotification(getBaseContext());
            //startActivity(new Intent(getApplicationContext(), Collage.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void openCamera(){
        Intent shortcutIntent = new Intent(getApplicationContext(), CameraActivity.class);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Camera DadTime");
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(getApplicationContext(),
                        R.mipmap.ic_launcher));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");

        getApplicationContext().sendBroadcast(addIntent);


    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment;
            switch (position)
            {
                case 0:
                    fragment = fragmentEntertainments;
                    break;
                case 1:
                    fragment = fragmentMemories;
                    break;
                default:
                    fragment = fragmentMemories;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Actividades";
                case 1:
                    return "Recuerdos";
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
