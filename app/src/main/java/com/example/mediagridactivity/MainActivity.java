package com.example.mediagridactivity;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Cursor videocursor;
    private int video_column_index;
    GridView videolist;
    int count;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init_phone_video_grid();
    }

    private void init_phone_video_grid() {
        System.gc();
        String selection=MediaStore.Video.Media.DATA +" like?";
        String[] selectionArgs=new String[]{"%Videos%"};

        String[] proj = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE };
        videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                proj, selection, selectionArgs, MediaStore.Video.Media.DATE_TAKEN + " DESC");
        count = videocursor.getCount();
        videolist = (GridView) findViewById(R.id.activity_media_grid_grid_view);
        videolist.setAdapter(new VideoAdapter(getApplicationContext()));
        videolist.setOnItemClickListener(videogridlistener);
    }

    private OnItemClickListener videogridlistener = new OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position,
                                long id) {
            System.gc();
            video_column_index = videocursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            videocursor.moveToPosition(position);
            String filename = videocursor.getString(video_column_index);
            /*   Intent intent = new Intent(MainActivity.this, ViewVideo.class);
                  intent.putExtra("videofilename", filename);
                  startActivity(intent);*/
            Toast.makeText(getApplicationContext(), filename, Toast.LENGTH_SHORT).show();
        }
    };

    public class VideoAdapter extends BaseAdapter {
        private Context vContext;

        public VideoAdapter(Context c) {
            vContext = c;
        }

        public int getCount() {
            return count;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            System.gc();
            TextView tv = new TextView(vContext.getApplicationContext());
            String id = null;
            if (convertView == null) {
                video_column_index = videocursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                videocursor.moveToPosition(position);
                id = videocursor.getString(video_column_index);
                video_column_index = videocursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
                videocursor.moveToPosition(position);
                id += " Size(KB):" + videocursor.getString(video_column_index);
                tv.setText(id);
            } else
                tv = (TextView) convertView;
            return tv;
        }
    }
}