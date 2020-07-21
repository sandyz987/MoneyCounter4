package com.example.moneycounter4.utils.MyImageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.util.Pair;
import androidx.databinding.ObservableField;

import com.example.moneycounter4.utils.ThreadPool;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ImageItem {
    private String url;
    private Context context;
    private int loadingImageResource = 0;
    private int errorImageResource = 0;

    ImageItem (String url,Context context){
        this.url=url;
        this.context=context;
    }
    public void into(final ObservableField<Bitmap> bitmapObservableField){
        final Future<Bitmap> future= ImageController.get(url);
        Runnable task=new Runnable() {
            @Override
            public void run() {
                try{
                    Bitmap bitmap = future.get(5, TimeUnit.SECONDS);
                    bitmapObservableField.set(bitmap);
                    ImageLoader.with(context).saveImage(url,bitmap);
                }catch (TimeoutException e){
                    Log.e("ImageLoader","load a picture timeout! url:" + url);
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();

                }
            }
        };
        ThreadPool.getInstance().execute(task);
    }

    public void into(final ImageView imageView){
        if(imageView == null){
            return;
        }
        if(loadingImageResource != 0){
            imageView.setImageResource(loadingImageResource);
        }else{
            imageView.setImageBitmap(null);
        }
        final Future<Bitmap> future= ImageController.get(url);
        Runnable task=new Runnable() {
            @Override
            public void run() {
                try{
                    Bitmap bitmap = future.get(5, TimeUnit.SECONDS);
                    Message msg=new Message();
                    msg.what=1;
                    msg.obj=new Pair<>(imageView,bitmap);
                    if(bitmap == null){
                        Log.e("ImageLoader","can't load a null picture! url:" + url);
                        throw new Exception();
                    }
                    ThreadPool.getInstance().getImageBitmapHandler().sendMessage(msg);
                    ImageLoader.with(context).saveImage(url,bitmap);
                }catch (TimeoutException e){
                    Log.e("ImageLoader","load a picture timeout! url:" + url);
                    Message msg=new Message();
                    msg.what=1;

                    if(errorImageResource != 0){
                        msg.obj=new Pair<>(imageView,errorImageResource);
                        ThreadPool.getInstance().getImageRecourseHandler().sendMessage(msg);
                    }else{
                        msg.obj=new Pair<>(imageView,null);
                        ThreadPool.getInstance().getImageBitmapHandler().sendMessage(msg);
                    }
                    //e.printStackTrace();
                } catch (Exception e){
                    //e.printStackTrace();

                }
            }
        };
        ThreadPool.getInstance().execute(task);
        return;
    }

    public ImageItem setLoadingImage(int resourceId){
        loadingImageResource = resourceId;
        return this;
    }

    public ImageItem setErrorImage(int resourceId){
        errorImageResource = resourceId;
        return this;
    }

}
