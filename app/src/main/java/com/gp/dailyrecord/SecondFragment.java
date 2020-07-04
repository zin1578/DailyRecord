package com.gp.dailyrecord;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;




public class SecondFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    HSSFSheet sheet;
    String filePath;
    String fileName;
    View view;

    // newInstance constructor for creating fragment with arguments
    public static SecondFragment newInstance(int page, String title) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_second, container, false);

        ExcelFileRead();
        return view;
    }

    void ExcelFileRead() {
        //excel file   // 같은 달 맨 마지막 감정 불러
        Date currentTime = Calendar.getInstance().getTime();
        String date_text = new SimpleDateFormat("yyyy-MM-", Locale.getDefault()).format(currentTime);
        //FileOutputStream 객체생성, 파일명 "data.txt", 새로운 텍스트 추가하기 모드
        int date_count = 1;
        int table_count = 0;
        int rowNum = 0;
        boolean lrowCheck = false;
        String dcStr ="";
        fileName ="save_file.xls";
        filePath = getActivity().getFilesDir().getPath().toString() + "/" + fileName;

            //파일 경로를 지정
            File files = new File(filePath);
            //파일 유무를 확인
            if (files.exists() == true) {
                //파일이 있을시
                String filePath = getActivity().getFilesDir().getPath().toString() + "/" + fileName;
                File excelFile = new File(filePath);
                try {
                    FileInputStream myInput = new FileInputStream(excelFile);
                    POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
                    HSSFWorkbook writer = new HSSFWorkbook(myFileSystem);
                    if (writer.getNumberOfSheets() != 0) {
                        sheet = writer.getSheetAt(0);
                        /** We now need something to iterate through the cells. **/
                        Iterator rowIter = sheet.rowIterator();
                        HSSFRow myRow = (HSSFRow) rowIter.next(); //헤더 한 줄 건너뛰기
                        myRow = (HSSFRow) rowIter.next();
                        while (date_count < 32&&lrowCheck==false) {//마지막 줄이면 그만하기
                            if(date_count<10){
                                dcStr = "0"+date_count;
                            }else{
                                dcStr = ""+date_count;
                            }
                            while(myRow.getCell(0).toString().contains(date_text+dcStr)&&lrowCheck==false) {
                                while (myRow.getCell(0).toString().contains(date_text+dcStr)&&rowIter.hasNext()) {
                                    myRow = (HSSFRow) rowIter.next(); // 한줄 데이터
                                }
                                if(!rowIter.hasNext()){ //마지막 줄이면
                                    rowNum = myRow.getRowNum();
                                    lrowCheck = true;
                                }else {
                                    rowNum = myRow.getRowNum();
                                    rowNum--;
                                }
                                myRow.setRowNum(rowNum);
                                // HSSFRow myRow = sheet.getRow(lrow);
                                HSSFCell myCell = myRow.getCell(2);
                                if (myCell.toString().equals("보통")) {
                                    String buttonID = "table_" + (table_count + 1);
                                    int resID = getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());
                                    Button button = ((Button) view.findViewById(resID));
                                    button.setBackgroundResource(R.drawable.ic_sentiment_satisfied_48px);
                                    table_count++;
                                    date_count++;
                                } else if (myCell.toString().equals("좋음")) {
                                    String buttonID = "table_" + (table_count + 1);
                                    int resID = getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());
                                    Button button = ((Button) view.findViewById(resID));
                                    button.setBackgroundResource(R.drawable.ic_sentiment_very_satisfied_48px);
                                    table_count++;
                                    date_count++;
                                } else if (myCell.toString().equals("나쁨")) {
                                    String buttonID = "table_" + (table_count + 1);
                                    int resID = getResources().getIdentifier(buttonID, "id", getActivity().getPackageName());
                                    Button button = ((Button) view.findViewById(resID));
                                    button.setBackgroundResource(R.drawable.ic_sentiment_very_dissatisfied_48px);
                                    table_count++;
                                    date_count++;
                                }
                            }
                            date_count++;
                    }

                }
                    writer.close();
            } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }else{

            }
        //((MainActivity)getActivity()).refresh();

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Refresh your fragment here

            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            Log.i("IsRefresh", "Yes");
        } else {

        }
    }

}
