package com.BSLCommunity.CSN_student.Activities;

import androidx.core.content.ContextCompat;

import android.app.SearchManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.BSLCommunity.CSN_student.Managers.AnimationManager;
import com.BSLCommunity.CSN_student.Objects.AuditoriumsList;
import com.BSLCommunity.CSN_student.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.otaliastudios.zoom.ZoomImageView;

public class AuditoriumActivity extends BaseActivity {
    ZoomImageView selectedBuildingImage; //view выбранного корпуса(этажа)
    int[][] buildingsMaps; //карты корпусов(этажей), где [1][0] - 3 корпус 1 этаж (нету плана 2-ого корпуса)
    int selectedBuilding, selectedFloor; //индекс выбранного корпуса
    TabLayout building_tl, floor_tl; //лаяуты выбора коруса, этажа
    AuditoriumsList auditoriumsList;
    int audBuilding = 0;
    int audFloor = 0;

    class ImageScale {
        public ImageScale(int x_left, int x_right, int y_top, int y_bot) {
            this.x_left = x_left;
            this.x_right = x_right;
            this.y_top = y_top;
            this.y_bot = y_bot;
        }

        int x_left, x_right;
        int y_top, y_bot;
    }

    ImageScale[][] imageScales = {{new ImageScale(0, 400, 213, 387), new ImageScale(0, 400, 216, 384), new ImageScale(0, 400, 219, 382)},
            {new ImageScale(0, 400, 219, 381), new ImageScale(0, 400, 228, 372), new ImageScale(0, 400, 214, 385), new ImageScale(0, 400, 214, 385), new ImageScale(0, 400, 214, 385)},
            {new ImageScale(0, 400, 64, 536), new ImageScale(0, 400, 84, 517), new ImageScale(0, 400, 88, 511), new ImageScale(0, 400, 88, 511)},
            {new ImageScale(121, 278, 0, 600), new ImageScale(101, 299, 0, 600), new ImageScale(101, 299, 0, 600), new ImageScale(101, 299, 0, 600)}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnimationManager.setAnimation(getWindow(), this);
        setContentView(R.layout.activity_auditorium);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTitle(getString(R.string.auditoriums_search));

        //получаем необходимые объекты
        selectedBuildingImage = findViewById(R.id.selectedBuilding);
        building_tl = findViewById(R.id.building_tl);
        floor_tl = findViewById(R.id.floor_tl);
        buildingsMaps = new int[][]{
                {R.drawable.building1_1, R.drawable.building1_2, R.drawable.building1_3},
                {R.drawable.building3_1, R.drawable.building3_2, R.drawable.building3_3, R.drawable.building3_4, R.drawable.building3_5},
                {R.drawable.building4_1, R.drawable.building4_2, R.drawable.building4_3, R.drawable.building4_4},
                {R.drawable.building5_1, R.drawable.building5_2, R.drawable.building5_3, R.drawable.building5_4}};

        //устанавливаем максимальное приближение для зума 5 (стандартное 2.5)
        selectedBuildingImage.setMaxZoom(5);
        selectedBuildingImage.setMinZoom(1);
        selectedBuildingImage.setImageDrawable(getDrawable(R.drawable.building1_1));
        setFloorTabs();

        auditoriumsList = new AuditoriumsList(this);

        //ставим листенер на вкладки корпусов
        building_tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //устанавливаем изображение корпуса (1 этажа) и сохраняем индекс корпуса
                selectedBuildingImage.setImageDrawable(getDrawable(buildingsMaps[building_tl.getSelectedTabPosition()][0]));
                selectedBuilding = building_tl.getSelectedTabPosition();
                selectedBuildingImage.zoomTo(1, false);

                //очищаем этажи пред корпуса и устанавливаем вкладки этажей нового корпуса
                floor_tl.removeAllTabs();
                setFloorTabs();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        floor_tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //устанавливаем изображение этажа (по выбранному корпусу) и сохраняем индекс этажа
                selectedBuildingImage.setImageDrawable(getDrawable(buildingsMaps[selectedBuilding][floor_tl.getSelectedTabPosition()]));
                selectedFloor = floor_tl.getSelectedTabPosition();
                selectedBuildingImage.zoomTo(1, false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //создаем меню поиска
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        //устанавливаем конфигурацию для SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.aud_search_hint)); //hint
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSubmitButtonEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                final AuditoriumsList.Auditorium auditoriumInfo = auditoriumsList.getInfo(s.toLowerCase());
                //сравниваем набранным номер аудитории
                if (auditoriumInfo != null) {
                    //задаем параметры вью (высота, ширина), (коррдинаты x,y)
                    audBuilding = auditoriumInfo.building - 1;
                    audFloor = auditoriumInfo.floor - 1;

                    //получение индекса корпуса в массив, но т.к 2 отсуствует, то (3)2 -> 1, а (1)0 -> 0
                    if (audBuilding != 0) audBuilding--;

                    //устанавливаем вкладку нужного корпуса
                    TabLayout.Tab buildingTab = building_tl.getTabAt(audBuilding);
                    buildingTab.select();

                    //устанавливаем вкладки этажей нужного корпуса
                    floor_tl.removeAllTabs();
                    setFloorTabs();
                    TabLayout.Tab floorTab = floor_tl.getTabAt(audFloor);
                    floorTab.select();

                    final Drawable bottomDrawable = ContextCompat.getDrawable(getApplicationContext(), buildingsMaps[audBuilding][audFloor]);
                    Drawable topDrawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.auditoriumsquare);
                    topDrawable.setTint(getColor(R.color.main_color_3));
                    final LayerDrawable layer = new LayerDrawable(new Drawable[]{topDrawable, bottomDrawable});
                    selectedBuildingImage.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            int height = bottomDrawable.getIntrinsicHeight();
                            int width = bottomDrawable.getIntrinsicWidth();
                            double dx = (auditoriumInfo.x - imageScales[audBuilding][audFloor].x_left) / (double) (imageScales[audBuilding][audFloor].x_right - imageScales[audBuilding][audFloor].x_left);
                            double dy = (auditoriumInfo.y - imageScales[audBuilding][audFloor].y_top) / (double) (imageScales[audBuilding][audFloor].y_bot - imageScales[audBuilding][audFloor].y_top);

                            double newX = dx * width;
                            double newY = dy * height;

                            layer.setLayerInset(0, (int) newX, (int) newY, 0, 0);
                            layer.setLayerSize(0, (auditoriumInfo.width * width) / (imageScales[audBuilding][audFloor].x_right - imageScales[audBuilding][audFloor].x_left), (auditoriumInfo.height * height) / (imageScales[audBuilding][audFloor].y_bot - imageScales[audBuilding][audFloor].y_top));

                            selectedBuildingImage.setImageDrawable(layer);
                            selectedBuildingImage.removeOnLayoutChangeListener(this);
                        }
                    });
                    selectedBuildingImage.zoomTo(1, false);
                } else {
                    //убираем вью аудитории
                    try {
                        selectedBuildingImage.setImageDrawable(getDrawable(buildingsMaps[audBuilding][audFloor]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    //перевод dp  в пиксели
    private int getPxFromDp(int dp) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    //перевод пикселей  в dp
    private int getDpFromPx(int px) {
        final float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) ((px - 0.5f) / scale);
    }

    //считаем кол-во этажей и устанавливаем их вкладки
    private void setFloorTabs() {
        for (int i = 0; i < buildingsMaps[building_tl.getSelectedTabPosition()].length; ++i) {
            TabItem tabItem = new TabItem(AuditoriumActivity.this);
            tabItem.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            floor_tl.addView(tabItem);
            floor_tl.getTabAt(i).setText(getString(R.string.floor) + (i + 1));
        }
    }
}