package com.jiminham.myfirstapp

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import java.io.*

class MainActivity : AppCompatActivity() {

    lateinit var tess : TessBaseAPI // TESSERACT API 객체 생
    var dataPath : String = "" // 데이터 경로 변수 선언

    fun copyFile(long : String){
        try{
            // 언어 데이터 파일의 위치
            var filePath : String = dataPath + "/tessdata/" + long + ".traineddata"

            //AssetManager를 사용하기 위한 객체 생성
            var assetManager : AssetManager = getAssets();

            // byte 스트림을 읽기 쓰기용으로 열기
            var inputStream : InputStream = assetManager.open("tessdate." + long+".traineddata")

            var outStream : OutputStream = FileOutputStream(filePath)

            // 위에 적어둔 파일 경로쪽으로 해당 바이트 코드 파일을 복사한다.
            var buffer : ByteArray = ByteArray(1024)

            var read : Int = 0
            read = inputStream.read(buffer)
            while(read != -1){
                outStream.write(buffer,0,read)
                read - inputStream.read(buffer)
            }
            outStream.flush()
            outStream.close()
            inputStream.close()



        }catch(e : FileNotFoundException){
            Log.v("오류 발생" , e.toString())
        }catch (e : IOException){
            Log.v("오류 발생",e.toString())
        }
    }

    fun checkFile(dir : File, long : String){

        if(!dir.exists() && dir.mkdirs()){
            copyFile(long)
        }

        if(dir.exists()){
            var datafilepath : String = dataPath+"/tessdata/"+long+".traineddata"
            var dataFile : File = File(datafilepath)
            if(!dataFile.exists()){
                copyFile(long)
            }
        }
    }

    fun processImage(bitmap: Bitmap){
        Toast.makeText(applicationContext, " 잠시 기다려 주세요",Toast.LENGTH_LONG).show()
        var ocrResult : String? = null;
        tess.setImage(bitmap)
        ocrResult = tess.utF8Text
        activitymain_tv_result.text = ocrResult

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataPath = fileDir.toString()+"/tesseract/" // 언어데이터의 경로 미리 지정

        checkFile(File(dataPath+"tessdata/"),"kor") // 사용할 언어파일의 이름 지정
        checkFile(File(dataPath+"tessdata/"),"eng")

        var long : String = "kor + eng"
        tess = TessBaseAPI() // api 준
        tess.init(dataPath,long)

        processImage(BitmapFactory.decodeResource(resources,R.drawable.OcrTest)) // 이미지 가공 후 텍스트 뷰에 띄우기
    }
}