package com.webinfrasolutions.ikarti;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;
import com.viewpagerindicator.CirclePageIndicator;
import com.webinfrasolutions.ikarti.API.FetchDashApi;
import com.webinfrasolutions.ikarti.API.FetchcategoriesApi;
import com.webinfrasolutions.ikarti.API.SearchApi;
import com.webinfrasolutions.ikarti.Adapters.CatDialogGridAdapter;
import com.webinfrasolutions.ikarti.Adapters.MyAdapter;
import com.webinfrasolutions.ikarti.Adapters.RecyclerItemListener;
import com.webinfrasolutions.ikarti.Adapters.SlidingImage_Adapter;
import com.webinfrasolutions.ikarti.Pojo.Category;
import com.webinfrasolutions.ikarti.Pojo.CategoryListPojo;
import com.webinfrasolutions.ikarti.Pojo.DashPojo;
import com.webinfrasolutions.ikarti.Pojo.UserData;
import com.webinfrasolutions.ikarti.util.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Uhome extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    int POSITION=0;
    List<Category> list=new ArrayList<>();
    CategoryListPojo pojo;
    private DrawerLayout drawerLayout;
    private FrameLayout navList;
    MaterialSearchView searchView;
    RecyclerView recyclerView;
    Dialog catDialog;    static ImageView error;

    BottomBarTab cart,tab_fav,tab_notifications;
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uhome);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        navList = (FrameLayout) findViewById(R.id.nav_holder);
        fetchCategories();
        error=findViewById(R.id.emptycart);

        initToolbar();
        initSearch();
        //initSlider();
        fetchList();
        intiBottonBar();
        recyclerView    = (RecyclerView) findViewById(R.id.dash_recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void intiBottonBar() {
        bottomBar = (BottomBar)findViewById(R.id.bottomBar);

        bottomBar.setBadgeBackgroundColor(getResources().getColor(R.color.light_green));
         cart = bottomBar.getTabWithId(R.id.tab_cart);
        tab_fav = bottomBar.getTabWithId(R.id.tab_fav);
        tab_notifications = bottomBar.getTabWithId(R.id.tab_notifications);



        bottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                //Toast.makeText(Uhome.this, "tab RE Selected", Toast.LENGTH_LONG).show();
                switch (tabId){
                    case R.id.tab_demo:{
                        //openCatDialog();

                        break;
                    }
                    case R.id.tab_cat:{
                        openCatDialog();

                        break;
                    }
                    case R.id.tab_cart:{
                        Intent i=new Intent(Uhome.this,CartActivity.class);
                        startActivity(i);
                        //     Toast.makeText(getActivity(), "2", Toast.LENGTH_LONG).show();
                        break;
                    }case R.id.tab_fav:{
                        Intent i=new Intent(Uhome.this,FavouriteProductsActivity.class);
                        startActivity(i);
                        // Toast.makeText(getActivity(), "3", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case R.id.tab_notifications:{
                        Intent i=new Intent(Uhome.this,Orders.class);
                        startActivity(i);
                        // Toast.makeText(getActivity(), "3", Toast.LENGTH_LONG).show();
                        break;
                    }



                }
            }
        });
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId){
                    case R.id.tab_demo:{
                       // openCatDialog();

                        break;
                    }
                    case R.id.tab_cat:{
                        openCatDialog();

                       break;
                    }
                    case R.id.tab_cart:{
                        Intent i=new Intent(Uhome.this,CartActivity.class);
                        startActivity(i);
                        //     Toast.makeText(getActivity(), "2", Toast.LENGTH_LONG).show();
break;
                    }case R.id.tab_fav:{
                        Intent i=new Intent(Uhome.this,FavouriteProductsActivity.class);
                        startActivity(i);
                        // Toast.makeText(getActivity(), "3", Toast.LENGTH_LONG).show();
                       break;
                    }
                    case R.id.tab_notifications:{
                        Intent i=new Intent(Uhome.this,Orders.class);
                        startActivity(i);
                        // Toast.makeText(getActivity(), "3", Toast.LENGTH_LONG).show();
                        break;
                    }



                }
            }
        });
    }

    private void initSlider() {

int urls[]={
        R.drawable.food,
        R.drawable.iphone,
        R.drawable.cloth,
        R.drawable.food,
        R.drawable.iphone,
};

        mPager = (ViewPager) findViewById(R.id.pager);


      //  mPager.setAdapter(new SlidingImage_Adapter(Uhome.this,urls));


        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(3 * density);

        NUM_PAGES =urls.length;
        mPager.setCurrentItem(POSITION);
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 1500);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private void initSearch() {

        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        //searchView.setVoiceSearch(true); //or false
        searchView.setVoiceSearch(false);
        //searchView.setVoiceIcon(getResources().getDrawable(R.drawable.ic_mic_black_24dp));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
               // Toast.makeText(Uhome.this,""+query,Toast.LENGTH_SHORT).show();
                fetchSearch(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

    }

    private void fetchSearch(String query) {
        final UserData userData=new SessionManager().getUserData(Uhome.this);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        SearchApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(SearchApi.class);


        retrofit2. Call<DashPojo> mService = service.fetch(query);
        mService.enqueue(new Callback<DashPojo>() {
            @Override
            public void onResponse(Call<DashPojo> call, Response<DashPojo> response) {

                if (response.body().getStatus())
                // clearFields();
                {
                    if (response.body().getRecords().size()>0)
                    {
                        MyAdapter adapter = new MyAdapter(Uhome.this,Uhome.this, response.body().getRecords(),userData.getUid());
                        recyclerView.setAdapter(adapter);

                    }else {

                            error.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);

                    }

                }
                //Toast.makeText(Uhome.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<DashPojo> call, Throwable t) {
                Toast.makeText(Uhome.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void initNavDrawer() {
        Bundle bundle = new Bundle();

        bundle.putString("params", "bithch\nbithch\nbithch\nbithch\n");
        bundle.putSerializable("pojo",pojo);
        Fragment fragment = new UhomeNavigationFragment();
        fragment.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_holder, fragment).commit();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(navList)) {
                drawerLayout.closeDrawer(navList);
            } else {
                drawerLayout.openDrawer(navList);
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar()/* or getSupportActionBar() */.setTitle(fromHtml("<font color=\"grey\">" + "IkartI" + "</font>"));
        //getSupportActionBar().setLogo(R.drawable.menuicon);
        //  mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }
    public void open_drawer(View view) {
        drawerLayout.openDrawer(navList);
    }
    public void close_drawer(View view) {
        drawerLayout.closeDrawer(navList);
    }


    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }
    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Exit Application")
                    .setMessage("Are you sure you want to Exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            finish();
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
            //super.onBackPressed();
        }
    }

    public void goToCart(View view) {
        Intent i=new Intent(Uhome.this,CartActivity.class);
        startActivity(i);
    }


    public  void fetchCategories(){

       // RequestBody StoreID = RequestBody.create(MediaType.parse("text/plain"), "1");

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        FetchcategoriesApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(FetchcategoriesApi.class);


        retrofit2. Call<CategoryListPojo> mService = service.fetch("fhgjg");
        mService.enqueue(new Callback<CategoryListPojo>() {
            @Override
            public void onResponse(Call<CategoryListPojo> call, Response<CategoryListPojo> response) {

                if (response.body().getStatus())
                // clearFields();
                {
                    list=response.body().getCategory();
                    pojo=response.body();
                    initNavDrawer();
                }
                //Toast.makeText(Uhome.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<CategoryListPojo> call, Throwable t) {
                Toast.makeText(Uhome.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void fetchList(){

        RequestBody StoreID = RequestBody.create(MediaType.parse("text/plain"), "1");
        final UserData userData=new SessionManager().getUserData(Uhome.this);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        FetchDashApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(FetchDashApi.class);


        retrofit2. Call<DashPojo> mService = service.fetch("");
        mService.enqueue(new Callback<DashPojo>() {
            @Override
            public void onResponse(Call<DashPojo> call, Response<DashPojo> response) {

                if (response.body().getStatus())
                // clearFields();
                {
                    if (response.body().getRecords().size()>0)
                    {
                        MyAdapter adapter = new MyAdapter(Uhome.this,Uhome.this, response.body().getRecords(),"uid");

                        recyclerView.setAdapter(adapter);
                        if (response.body().getCartCount()>0)
                        cart.setBadgeCount(response.body().getCartCount());

                        if (response.body().getFavouriteCount()>0)
                        tab_fav.setBadgeCount(response.body().getFavouriteCount());
                       // tab_notifications.setBadgeCount(8);
                    }else
                    {
                        error.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    }

                }
                //Toast.makeText(Uhome.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<DashPojo> call, Throwable t) {
                Toast.makeText(Uhome.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }



    public void openCatDialog(View view) {
        catDialog = new BottomSheetDialog(Uhome.this);
        catDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        catDialog.setCancelable(true);
        catDialog.setContentView(R.layout.cat_bottom_dialog_lay);
        catDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        catDialog.show();
        RecyclerView rv=catDialog.findViewById(R.id.cat_dialog_rv);
        CatDialogGridAdapter adapter = new CatDialogGridAdapter(Uhome.this, list);
        rv.setLayoutManager(new GridLayoutManager(Uhome.this,3));
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(new RecyclerItemListener(Uhome.this,rv,new
                RecyclerItemListener.RecyclerTouchListener(){
                    @Override
                    public void onClickItem(View v, int position) {

                        Intent x=new Intent(Uhome.this,CategoryLIstActivity.class);
                        x.putExtra("cat",pojo.getCategory().get(position).getCatName());
                        x.putExtra("catId",pojo.getCategory().get(position).getCatId());

                        startActivity(x);
                    }

                    @Override
                    public void onLongClickItem(View v, int position) {

                    }
                }));
    }
    public void openCatDialog() {
        catDialog = new BottomSheetDialog(Uhome.this);
        catDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        catDialog.setCancelable(true);
        catDialog.setContentView(R.layout.cat_bottom_dialog_lay);
        catDialog.getWindow().findViewById(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent);
        catDialog.show();
        RecyclerView rv=catDialog.findViewById(R.id.cat_dialog_rv);
        CatDialogGridAdapter adapter = new CatDialogGridAdapter(Uhome.this, list);
        rv.setLayoutManager(new GridLayoutManager(Uhome.this,3));
        rv.setAdapter(adapter);
        rv.addOnItemTouchListener(new RecyclerItemListener(Uhome.this,rv,new
                RecyclerItemListener.RecyclerTouchListener(){
                    @Override
                    public void onClickItem(View v, int position) {

                        Intent x=new Intent(Uhome.this,CategoryLIstActivity.class);
                        x.putExtra("cat",pojo.getCategory().get(position).getCatName());
                        x.putExtra("catId",pojo.getCategory().get(position).getCatId());

                        startActivity(x);
                    }

                    @Override
                    public void onLongClickItem(View v, int position) {

                    }
                }));
    }


}
/**
 *     private void initRv() {
 RecyclerView rv1=(RecyclerView)findViewById(R.id.rv1);
 RecyclerView rv2=(RecyclerView)findViewById(R.id.rv2);
 RecyclerView rv3=(RecyclerView)findViewById(R.id.rv3);
 RecyclerView rv4=(RecyclerView)findViewById(R.id.rv4);
 RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(Uhome.this, LinearLayoutManager.HORIZONTAL, false);
 RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(Uhome.this, LinearLayoutManager.HORIZONTAL, false);
 RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(Uhome.this, LinearLayoutManager.HORIZONTAL, false);
 RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(Uhome.this, LinearLayoutManager.HORIZONTAL, false);

 rv1.setLayoutManager(layoutManager1);
 rv2.setLayoutManager(layoutManager2);
 rv3.setLayoutManager(layoutManager3);
 rv4.setLayoutManager(layoutManager4);

 // rv1.setAdapter(new HomeAddsRecycleAdapter(this,Uhome.this,mobile));

 /*  rv1.addOnItemTouchListener(new RecyclerItemListener(Uhome.this,rv1,new
 RecyclerItemListener.RecyclerTouchListener(){
@Override
public void onClickItem(View v, int position) {
Intent i=new Intent(Uhome.this,ProductDetailsActivity.class);
i.putExtra("pos",position);
startActivity(i);
}

@Override
public void onLongClickItem(View v, int position) {

}
}));
 rv2.setAdapter(new HomeAddsRecycleAdapter(this,Uhome.this,food));
        rv2.addOnItemTouchListener(new RecyclerItemListener(Uhome.this,rv1,new
                RecyclerItemListener.RecyclerTouchListener(){
@Override
public void onClickItem(View v, int position) {
        Intent i=new Intent(Uhome.this,ProductDetailsActivity.class);
        i.putExtra("pos",position);
        startActivity(i);
        }

@Override
public void onLongClickItem(View v, int position) {

        }
        }));
        //  rv3.setAdapter(new HomeAddsRecycleAdapter(this,Uhome.this,clothes));
        rv3.addOnItemTouchListener(new RecyclerItemListener(Uhome.this,rv1,new
        RecyclerItemListener.RecyclerTouchListener(){
@Override
public void onClickItem(View v, int position) {
        Intent i=new Intent(Uhome.this,ProductDetailsActivity.class);
        i.putExtra("pos",position);
        startActivity(i);
        }

@Override
public void onLongClickItem(View v, int position) {

        }
        }));
        //  rv4.setAdapter(new HomeAddsRecycleAdapter(this,Uhome.this,demourls));
        rv4.addOnItemTouchListener(new RecyclerItemListener(Uhome.this,rv1,new
        RecyclerItemListener.RecyclerTouchListener(){
@Override
public void onClickItem(View v, int position) {
        Intent i=new Intent(Uhome.this,ProductDetailsActivity.class);
        i.putExtra("pos",position);
        startActivity(i);
        }

@Override
public void onLongClickItem(View v, int position) {

        }
        }));

        }
        */
/*  @Override
      protected void onActivityResult(int requestCode, int resultCode, Intent data) {
          if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
              ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
              if (matches != null && matches.size() > 0) {
                  String searchWrd = matches.get(0);
                  if (!TextUtils.isEmpty(searchWrd)) {
                      searchView.setQuery(searchWrd, false);
                  }
              }

              return;
          }
          super.onActivityResult(requestCode, resultCode, data);
      }*/