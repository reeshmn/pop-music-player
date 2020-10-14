package com.skytailindia.popmusicplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class PopMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    public static final int REQUEST_CODE = 1;
    static ArrayList<MusicFiles> musicFiles;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popmain);


        permission();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        navigationView.setCheckedItem(R.id.nav_mainhome);

        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ImageView menuIcon = (ImageView) findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    public static class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();

        }

        void addFragments(Fragment fragment, String title) {

            fragments.add(fragment);
            titles.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(PopMain.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , REQUEST_CODE);

        } else {
            musicFiles = getAllAudio(this);
            initViewPager();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        

    }


    private void initViewPager() {
        ViewPager viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new SongsFragment(), "Songs");
        viewPager.setAdapter(viewPagerAdapter);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.nav_mainhome:
                break;
            case R.id.nav_playlist:
                Intent intent = new Intent(PopMain.this, PlayList.class);
                startActivity(intent);
                break;

            case R.id.nav_reward:
                Intent reward_intent = new Intent(PopMain.this, RewardScreen.class);
                startActivity(reward_intent);
                break;
            case R.id.nav_account:
                Intent account_intent = new Intent(PopMain.this, Account.class);
                startActivity(account_intent);
                break;
            case R.id.nav_singup:
                Intent singup_intent = new Intent(PopMain.this, Login_Singup.class);
                startActivity(singup_intent);
                break;
            case R.id.nav_ivite:
                Intent invite_intent = new Intent(PopMain.this, Invite.class);
                startActivity(invite_intent);
                break;
            case R.id.nav_FeedBack:
                Intent feedback_intent = new Intent(PopMain.this, FeedBack.class);
                startActivity(feedback_intent);
                break;


            case R.id.nav_settings:
                Intent setting_intent = new Intent(PopMain.this, Setting.class);
                startActivity(setting_intent);
                break;


        }


        return true;
    }

    public static ArrayList<MusicFiles> getAllAudio(Context context) {

        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST //for path
        };
        Cursor cursor = context.getContentResolver().query(uri, projection,
                null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {

                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);

                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration);
                Log.e("Path:" + path, "Album:" + album);
                tempAudioList.add(musicFiles);


            }
            cursor.close();
        }
        return tempAudioList;
    }


}
