package com.webinfrasolutions.ikarti;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.webinfrasolutions.ikarti.Adapters.CategorySpinnerAdapter;
import com.webinfrasolutions.ikarti.Pojo.Category;
import  com.webinfrasolutions.ikarti.Pojo.Storekeeper;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.gson.Gson;
import com.webinfrasolutions.ikarti.API.AddProductApi;
import com.webinfrasolutions.ikarti.API.UpdateProductApi;
import com.webinfrasolutions.ikarti.API.UploadImageApi;
import com.webinfrasolutions.ikarti.Adapters.ImagePreviewAdapter;
import com.webinfrasolutions.ikarti.Pojo.GetOtpPojo;
import com.webinfrasolutions.ikarti.Pojo.Product;
import com.webinfrasolutions.ikarti.Pojo.UploadIMagePojo;
import com.webinfrasolutions.ikarti.util.SessionManager;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import customfonts.MyEditText;
import customfonts.MyTextView;
import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Addproduct extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1,
            CAMERA_REQUEST=2;
    private ImageView actualImageView;
    private ImageView compressedImageView;
    private File actualImage;
    private File compressedImage;
    ArrayList<String> imageList=new ArrayList<>();
    Uri outputFileUri;
    String mCurrentPhotoPath;
    TextInputLayout titlelay,pricelay,deslay,no_of_pieces_lay;
    MyEditText title_et,price_et,des_et,no_of_pieces_et;
    boolean task=false;
    CheckBox delivery;
    String product_id,cat_id,store_id;
    Spinner catSpin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addproduct);
        actualImageView = (ImageView) findViewById(R.id.img);
        compressedImageView = (ImageView) findViewById(R.id.img);
        delivery=findViewById(R.id.home_delivery);
     //   actualSizeTextView = (TextView) findViewById(R.id.actual_size);
       // compressedSizeTextView = (TextView) findViewById(R.id.compressed_size);

        //actualImageView.setBackgroundColor(getRandomColor());
       // clearImage();
        initEts();
        intiSpin();
        try {
            String data=getIntent().getStringExtra("task");
            if (data.equals("edit"))
                task=true;
           // showError(task);
            Product product = (Product) getIntent().getSerializableExtra("data");
            product_id=product.getProductId();
            cat_id=product.getCatId();
            store_id=product.getStoreId()
                    ;
            loadData(product);
            //showError(product.getProductName());
        }catch (NullPointerException ne){
            ne.printStackTrace();
        }
    }
    private void intiSpin() {
         catSpin=findViewById(R.id.cat_spin);
        Resources res = getResources();
        //  categorySpinner.setBackground(getResources().getDrawable(R.drawable.error_rect));

        // Create custom adapter object ( see below CustomAdapter.java )
        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(Addproduct.this, R.layout.category_spin_row, new SessionManager().getList(Addproduct.this), res);
        // Set adapter to spinner
        catSpin.setAdapter(adapter);
    }

    private void loadData(Product product) {



        des_et.setText(product.getProductDescription());
        price_et.setText(product.getProductPrice());
        title_et.setText(product.getProductName());
        no_of_pieces_et.setText(product.getNoOfProducts());


    }

    private void initEts() {
MyTextView title=(MyTextView)findViewById(R.id.toolbar_title);
        title.setText("Add Product");
        title.setTextColor(getResources().getColor(R.color.light_green));
        //layouts
        titlelay=(TextInputLayout)findViewById(R.id.titlelay);
        pricelay=(TextInputLayout)findViewById(R.id.pricelay);
        deslay=(TextInputLayout)findViewById(R.id.deslay);
        no_of_pieces_lay=(TextInputLayout)findViewById(R.id.num_of_pieces_lay);

        //edit texts
        des_et=(MyEditText)findViewById(R.id.des_et);
        price_et=(MyEditText)findViewById(R.id.price_et);
        title_et=(MyEditText)findViewById(R.id.title_et);
        no_of_pieces_et=(MyEditText)findViewById(R.id.num_of_pieces_et);

        //textwatchers
        title_et.addTextChangedListener(new MyTextWatcher(title_et));
        price_et.addTextChangedListener(new MyTextWatcher(price_et));
        des_et.addTextChangedListener(new MyTextWatcher(des_et));
        no_of_pieces_et.addTextChangedListener(new MyTextWatcher(no_of_pieces_et));



    }


    public void chooseImage(View view) {
         popmenu();
    }

    public void compressImage() {
        if (actualImage == null) {
            showError("Please choose an image!");
        } else {

            // Compress image in main thread
            //compressedImage = new Compressor(this).compressToFile(actualImage);
            //setCompressedImage();

            // Compress image to bitmap in main thread
            //compressedImageView.setImageBitmap(new Compressor(this).compressToBitmap(actualImage));

            // Compress image using RxJava in background thread
            new Compressor(this)
                    .compressToFileAsFlowable(actualImage)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<File>() {
                        @Override
                        public void accept(File file) {
                            compressedImage = file;
                            uploadImage(compressedImage);
                           // setCompressedImage();
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) {
                            throwable.printStackTrace();
                            showError(throwable.getMessage());
                        }
                    });
        }
    }

    private void uploadImage(File compressedImage) {
        setCompressedImage();

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/jpeg"), compressedImage);

        MultipartBody.Part body = MultipartBody.Part.createFormData("userfile", compressedImage.getName(), reqFile);

        // Change base URL to your upload server URL.
        UploadImageApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).addConverterFactory(GsonConverterFactory.create()).build().create(UploadImageApi.class);


        retrofit2. Call<UploadIMagePojo> mService = service.upload_image(body);
        mService.enqueue(new Callback<UploadIMagePojo>() {
            @Override
            public void onResponse(Call<UploadIMagePojo> call, Response<UploadIMagePojo> response) {

                if (response.body().getStatus())
                    // clearFields();
                imageList.add(response.body().getFilename());
                initList(imageList);
                clearImage();
              //  Toast.makeText(Addproduct.this,""+response.body().getMessage(),Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<UploadIMagePojo> call, Throwable t) {
                Toast.makeText(Addproduct.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();
t.printStackTrace();
            }
        });

    }

    private void setCompressedImage() {

        compressedImageView.setImageBitmap(BitmapFactory.decodeFile(compressedImage.getAbsolutePath()));

       // Toast.makeText(this, "Compressed image save in " + compressedImage.getPath(), Toast.LENGTH_LONG).show();
        Log.d("Compressor", "Compressed image save in " + compressedImage.getPath());
    }
    public  void initList(List<String> urls){
    RecyclerView dlv=findViewById(R.id.images_rv);
    // compressedSizeTextView.setText(String.format("Size : %s", getReadableFileSize(compressedImage.length())));
    ImagePreviewAdapter adapter = new ImagePreviewAdapter(Addproduct.this,  urls);
    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Addproduct.this, LinearLayoutManager.HORIZONTAL, false);
    dlv.setLayoutManager(layoutManager); dlv.setHasFixedSize(true);
    dlv.setAdapter(adapter);

}
    private void clearImage() {

        compressedImageView.setImageResource(R.drawable.ic_camera_black_24dp);

          }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                showError("Failed to open picture!");
                return;
            }
            try {
                actualImage = FileUtil.from(this, data.getData());

compressImage();
                //actualImageView.setImageBitmap(BitmapFactory.decodeFile(actualImage.getAbsolutePath()));
                //actualSizeTextView.setText(String.format("Size : %s", getReadableFileSize(actualImage.length())));
             //   clearImage();
            } catch (IOException e) {
                showError("Failed to read picture data!");
                e.printStackTrace();
            }
        }

        /////////camera intent handling
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            // Show the thumbnail on ImageView
           outputFileUri= Uri.parse(mCurrentPhotoPath);
            try {
                actualImage = FileUtil.from(this, outputFileUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            compressImage();
        }



    }

    public void showError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
                return;
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(Addproduct.this,
                        BuildConfig.APPLICATION_ID + ".provider",
                        createImageFile());                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }
    @Override    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permission Granted Successfully. Write working code here.
                    try {
                        dispatchTakePictureIntent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    //You did not accept the request can not use the functionality.
                }
                break;
        }
    }
    void popmenu() {

        final Dialog dialog = new Dialog(Addproduct.this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
dialog.setCancelable(true);
        dialog.setContentView(R.layout.cameradialog);

        dialog.getWindow().setBackgroundDrawable(

                new ColorDrawable(android.graphics.Color.TRANSPARENT));

        MyTextView mCamerabtn = (MyTextView) dialog.findViewById(R.id.cameradialogbtn);

        MyTextView mGallerybtn = (MyTextView) dialog

                .findViewById(R.id.gallerydialogbtn);

        MyTextView btnCancel = (MyTextView) dialog.findViewById(R.id.canceldialogbtn);



        dialog.getWindow().setLayout(LinearLayoutCompat.LayoutParams.MATCH_PARENT,

                LinearLayoutCompat.LayoutParams.MATCH_PARENT);



        mCamerabtn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                    if (Build.VERSION.SDK_INT < 23) {
                        //Do not need to check the permission
                        try {
                            dispatchTakePictureIntent();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (checkAndRequestPermissions()) {
                            try {
                                dispatchTakePictureIntent();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //If you have already permitted the permission
                        }else {

                           // askpermission();
                        }
                    }








                dialog.cancel();

            }

        });



        mGallerybtn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                showError("hiiiiiiiiii");
  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                  intent.setType("image/*");
                 startActivityForResult(intent, PICK_IMAGE_REQUEST);


                dialog.cancel();

            }

        });



        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                dialog.cancel(); // dismissing the popup

            }

        });



        dialog.show();



    }

    private boolean checkAndRequestPermissions() {
        int permissionCAMERA = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int permissionStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int storagePermission = ContextCompat.checkSelfPermission(this,


                Manifest.permission.READ_EXTERNAL_STORAGE);



        List<String> listPermissionsNeeded = new ArrayList<>();
        if (storagePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), CAMERA_REQUEST);
            return false;
        }

        return true;
    }

    public void addProduct(View view) {
        if (task){
            if (validateTitle() && validatePrice() && validateNoOfPieces() && validateDes())
                updateProductCall(title_et.getText().toString(), price_et.getText().toString(), no_of_pieces_et.getText().toString(), des_et.getText().toString(),product_id);

            //updateProductCall();
        }else {
            if (validateTitle() && validatePrice() && validateNoOfPieces() && validateDes())
                addProductCall(title_et.getText().toString(), price_et.getText().toString(), no_of_pieces_et.getText().toString(), des_et.getText().toString());
            // showError("Submit product");
        }
    }

    private void updateProductCall(String stitle, String sprice, String snoOfPieces, String sdes,String id) {
        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("shopkeeper", "");
        Storekeeper obj = gson.fromJson(json, Storekeeper.class);

        RequestBody Title = RequestBody.create(MediaType.parse("text/plain"), stitle);
        RequestBody Price = RequestBody.create(MediaType.parse("text/plain"), sprice);
        RequestBody NoOfPieces = RequestBody.create(MediaType.parse("text/plain"), snoOfPieces);
        RequestBody Des = RequestBody.create(MediaType.parse("text/plain"), sdes);
        RequestBody StoreId = RequestBody.create(MediaType.parse("text/plain"), obj.getStoreId());
        RequestBody CatId = RequestBody.create(MediaType.parse("text/plain"), "1");
        RequestBody Id = RequestBody.create(MediaType.parse("text/plain"), id);


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.
        UpdateProductApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(UpdateProductApi.class);


        retrofit2. Call<GetOtpPojo> mService = service.add(Title,StoreId,Price,Des,CatId,Id);
        mService.enqueue(new Callback<GetOtpPojo>() {
            @Override
            public void onResponse(Call<GetOtpPojo> call, Response<GetOtpPojo> response) {
                GetOtpPojo obj=response.body();
                if (obj.getStatus())
                    clearFields();
                Toast.makeText(Addproduct.this,""+obj.getMessage(),Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onFailure(Call<GetOtpPojo> call, Throwable t) {
                Toast.makeText(Addproduct.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void addProductCall(String stitle, String sprice, String snoOfPieces, String sdes) {

int sId=catSpin.getSelectedItemPosition();
         Category category=(Category)new SessionManager().getList(Addproduct.this).get(sId);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        // Change base URL to your upload server URL.

     Storekeeper storekeeper= (Storekeeper)  new SessionManager().getStoreKeeperData(Addproduct.this);
        AddProductApi service = new Retrofit.Builder().baseUrl(getString(R.string.base_url)).client(client).addConverterFactory(GsonConverterFactory.create()).build().create(AddProductApi.class);
String images[]=new String[imageList.size()];
for (int i=0;i<imageList.size();i++)
{
    images[i]=imageList.get(i);

}
boolean bdelivery;
        if (delivery.isChecked())
        bdelivery=true;
        else
            bdelivery=false;

retrofit2. Call<GetOtpPojo> mService = service.add(stitle,storekeeper.getStoreId(),sprice,sdes,category.getCatId(),no_of_pieces_et.getText().toString(),bdelivery+"",images);
        mService.enqueue(new Callback<GetOtpPojo>() {
            @Override
            public void onResponse(Call<GetOtpPojo> call, Response<GetOtpPojo> response) {
                GetOtpPojo obj=response.body();
                if (obj.getStatus())
clearFields();
                Toast.makeText(Addproduct.this,""+obj.getMessage(),Toast.LENGTH_SHORT).show();

            }


            @Override
            public void onFailure(Call<GetOtpPojo> call, Throwable t) {
                Toast.makeText(Addproduct.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void goBack(View view) {
        super.onBackPressed();
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.title_et:
                {
                    validateTitle();
                    break;}
  case R.id.price_et:
                {
                    validatePrice();
                    break;}
  case R.id.des_et:
                {
                    validateDes();
                    break;}



            case R.id.num_of_pieces_et:
            {
                validateNoOfPieces();
                break;}


        }
        }
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private boolean validateTitle() {
        if (title_et.getText().toString().trim().isEmpty()) {
            titlelay.setError("Field Cannot Be Empty");
            requestFocus(title_et);
            return false;
        } else {
            titlelay.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatePrice() {
        if (price_et.getText().toString().trim().isEmpty()) {
            pricelay.setError("Field Cannot Be Empty");
            requestFocus(price_et);
            return false;
        } else {
            pricelay.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateNoOfPieces() {
        if (no_of_pieces_et.getText().toString().trim().isEmpty()) {
            no_of_pieces_lay.setError("Field Cannot Be Empty");
            requestFocus(no_of_pieces_et);
            return false;
        } else {
            no_of_pieces_lay.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateDes() {
        if (des_et.getText().toString().trim().isEmpty()) {
            deslay.setError("Field Cannot Be Empty");
            requestFocus(des_et);
            return false;
        } else {
            deslay.setErrorEnabled(false);
        }

        return true;
    }

public  void clearFields(){

    title_et.setText("");
    titlelay.setErrorEnabled(false);
    pricelay.setErrorEnabled(false);
    no_of_pieces_lay.setErrorEnabled(false);
    deslay.setErrorEnabled(false);
    price_et.setText("");
    no_of_pieces_et.setText("");
    des_et.setText("");
    imageList.clear();
    initList(imageList);

}
}
