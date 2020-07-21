package com.example.moneycounter4.utils.MyImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.moneycounter4.utils.ThreadPool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class ImageLoader {
    private static ImageLoader imageLoader;
    private Context context;
    private MD5 md5=new MD5();
    public static int widthDefault=300;


    private ImageLoader(Context context){
        this.context=context;
    }

    public static ImageLoader with(Context context){
        if(imageLoader==null){
            synchronized (ImageLoader.class){
                if(imageLoader==null){
                    imageLoader=new ImageLoader(context);
                }
            }
        }
        return imageLoader;
    }

    public ImageItem load(final String url){

        if(ImageController.get(url)!=null){
            return new ImageItem(url,context);
        }
        if(imageLocalExist(url)){
            return new ImageItem(url,context);
        }
        Callable<Bitmap> callable=new Callable<Bitmap>() {
            @Override
            public Bitmap call() throws Exception {
                Log.d("ImageLoader","下载图片："+url);
                URL url1=new URL(url);
                HttpURLConnection httpURLConnection=(HttpURLConnection) url1.openConnection();
                httpURLConnection.setRequestMethod("GET");
                if(httpURLConnection.getResponseCode() != 200){
                    Log.d("ImageLoader","图片下载失败，" + httpURLConnection.getResponseCode() + "url:" + url);
                    return null;
                }
                ByteArrayOutputStream baos = cloneInputStream(httpURLConnection.getInputStream());
                InputStream inputStream1=new ByteArrayInputStream(baos.toByteArray());
                InputStream inputStream2=new ByteArrayInputStream(baos.toByteArray());
                BitmapFactory.Options ops=new BitmapFactory.Options();
                ops.inJustDecodeBounds=true;
                BitmapFactory.decodeStream(inputStream1,null,ops);
                Log.d("ImageLoader","图片尺寸："+ops.outWidth+"*"+ops.outHeight);
                ops.inSampleSize=(int)(ops.outWidth/widthDefault);
                ops.inJustDecodeBounds=false;
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream2,null,ops);
                Log.d("ImageLoader","压缩后图片尺寸："+ops.outWidth+"*"+ops.outHeight);
                httpURLConnection.getInputStream().close();
                inputStream1.close();
                inputStream2.close();
                httpURLConnection.disconnect();
                return bitmap;
            }
        };
        try{
            Future<Bitmap> future= ThreadPool.getInstance().submit(callable);
            ImageController.add(url,future);

            return new ImageItem(url,context);
        }catch (Exception e){
            //e.printStackTrace();
            return null;
        }
    }


    /**
     *复制一个input流
     * @param input
     * @return
     */
    private static ByteArrayOutputStream cloneInputStream(InputStream input) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
            return baos;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean imageLocalExist(String url){
        try{
            String name=md5.encode(url);
            final FileInputStream inputStream = context.openFileInput(name+".jpg");
            Callable callable = new Callable() {
                @Override
                public Object call() throws Exception {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            };
            Future future=ThreadPool.getInstance().submit(callable);
            ImageController.add(url,future);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void saveImage(String url,Bitmap bitmap){
        String name="";
        try{
            name = md5.encode(url);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(name.equals("")){
            return;
        }
        try{
            context.openFileInput(name+".jpg");
            Log.d("ImageLoader","imageExist！"+name+".jpg");
            return;
        }catch (IOException e){
        }
        try{

            FileOutputStream outputStream = context.openFileOutput(name+".jpg",Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();
            Log.d("ImageLoader","saveImage:"+name+".jpg");
        }catch (IOException e){
            Log.e("ImageLoader","saveImageError!");
        }
    }

    public class MD5 {
        public String encode(String string) throws Exception {
            byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        }
    }


}
