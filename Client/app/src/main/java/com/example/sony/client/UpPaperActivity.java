package com.example.sony.client;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sony.client.models.Paper;
import com.example.sony.client.models.RequestForUploadPaper;
import com.example.sony.client.models.ServerResponse;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpPaperActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private ProgressDialog progressDialog;
    private Button btn_uploadfile,btn_choosefile,btn_save,btn_back;
    private EditText et_title,et_author,et_des,et_cag,et_journal,et_file;
    private String filePath;
    private Uri uri;
    final int ACTIVITY_CHOOSE_FILE = 1;
    private ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_paper);
        context=this;
        initView();
    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progress = (ProgressBar) findViewById(R.id.progress);
        et_title = (EditText) findViewById(R.id.et_title);
        et_author = (EditText) findViewById(R.id.et_author);
        et_cag = (EditText) findViewById(R.id.et_cag);
        et_journal = (EditText) findViewById(R.id.et_journal);
        et_des = (EditText) findViewById(R.id.et_des);
        et_file = (EditText) findViewById(R.id.et_file);
        btn_choosefile = (Button) findViewById(R.id.btn_choosefile);
        btn_uploadfile = (Button) findViewById(R.id.btn_uploadfile);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_choosefile.setOnClickListener(this);
        btn_uploadfile.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_choosefile:
                choosefile();
                break;
            case R.id.btn_uploadfile:
                uploadfile();
                break;
            case R.id.btn_save:
                progress.setVisibility(View.VISIBLE);
                save();
                break;
            case R.id.btn_back:
                back();
                break;
        }
    }

    private void back() {
        Intent intent = new Intent();

    }

    private void save() {
        Paper paper = new Paper();
        paper.setTitle(et_title.getText().toString());
        paper.setAuthor(et_author.getText().toString());
        paper.setCategories(et_cag.getText().toString());
        paper.setJournal_title(et_journal.getText().toString());
        paper.setDescription(et_des.getText().toString());
        paper.setFile_name(et_file.getText().toString());
        RequestForUploadPaper rfp = new RequestForUploadPaper();
        rfp.setPaper(paper);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ClientAPI requestInterface = retrofit.create(ClientAPI.class);
        Call<ServerResponse> call = requestInterface.uploadPaper(rfp);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    Toast.makeText(context, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.INVISIBLE);
                } else {
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });

    }

    private void uploadfile() {
        progressDialog.show();
        File file = new File(filePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ClientAPI requestInterface = retrofit.create(ClientAPI.class);
        Call<ServerResponse> call = requestInterface.upload(fileToUpload, filename);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toast.makeText(context, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

            }
        });
        progressDialog.dismiss();
    }


    private void choosefile() {
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("file/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, ACTIVITY_CHOOSE_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            uri = data.getData();
            filePath = uri.getPath();
            File file = new File(filePath);
            et_file.setText(file.getName());
        }
    }
}
