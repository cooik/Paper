package com.example.sony.client;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sony.client.models.Paper;
import com.google.gson.Gson;

public class PaperDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String MESSAGE_PROGRESS = "message_progress";
    private static final int PERMISSION_REQUEST_CODE = 1;


    ProgressBar mProgressBar;
    TextView mProgressText,mTitleText,mCategoriesText,mAuthorText,mJournalText,mPaperPath;
    Button btn_download,btn_back;
    Paper paper;
    String filename,title,categories,author,journal;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        String bookJson=getIntent().getStringExtra("paper");
        paper=new Gson().fromJson(bookJson,Paper.class);
        setContentView(R.layout.activity_paper_detail);
        initView();
        registerReceiver();
        filename = paper.getFile_name();

    }

    private void initView() {
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressText = (TextView) findViewById(R.id.progress_text);
        btn_download = (Button) findViewById(R.id.btn_download);
        btn_download.setOnClickListener(this);
        mTitleText = (TextView) findViewById(R.id.tv_title);
        mCategoriesText = (TextView) findViewById(R.id.tv_categories);
        mAuthorText = (TextView) findViewById(R.id.tv_author);
        mJournalText = (TextView) findViewById(R.id.tv_journal);
        mTitleText.setText(paper.getTitle());
        mAuthorText.setText(paper.getAuthor());
        mPaperPath = (TextView) findViewById(R.id.tv_paperpath);
        mPaperPath.setText(paper.getFile_name());
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void back() {
        Intent intent=new Intent(context,MainActivity.class);
        startActivity(intent);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
    }

    private void startDownload() {
        Intent intent = new Intent(this,DownloadService.class);
        intent.putExtra("filename",filename);
        startService(intent);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

    private void registerReceiver() {
        LocalBroadcastManager bManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MESSAGE_PROGRESS);
        bManager.registerReceiver(broadcastReceiver, intentFilter);
    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(MESSAGE_PROGRESS)){

                Download download = intent.getParcelableExtra("download");
                mProgressBar.setProgress(download.getProgress());
                if(download.getProgress() == 100){

                    mProgressText.setText("File Download Complete");

                } else {

                    mProgressText.setText(String.format("Downloaded (%d/%d) MB",download.getCurrentFileSize(),download.getTotalFileSize()));

                }
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startDownload();
                } else {

                    Toast.makeText(context,"Permission Denied, Please allow to proceed !",Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if(checkPermission()){
            startDownload();
        } else {
            requestPermission();
        }
    }

}
