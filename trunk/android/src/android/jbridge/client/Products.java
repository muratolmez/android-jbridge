package android.jbridge.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Products extends Activity{
	
	private static String TAG = "Products";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.products);
		
		final TableLayout table = (TableLayout) findViewById(R.id.tableLayout1);		
		table.removeAllViews();
		
		JSONObject json =RestEasy.connect("http://10.0.2.2:8080/JBridge/RestEasy/products");
		
		try
		{
			JSONArray products = json.getJSONArray("products");
			for(int i=0; i < products.length(); i ++)
			{
				String name = ((JSONObject) products.get(i)).getString("name");
				String price = ((JSONObject) products.get(i)).getString("price");
				String picture = ((JSONObject) products.get(i)).getString("picture");
				
				ImageView img = new ImageView(this);
				img.setScaleType(ScaleType.FIT_CENTER);
				img.setPadding(3,3,3,3);
				img.setImageBitmap(this.getImageBitmap("http://10.0.2.2:8080/JBridge/products/thumbnails/" + picture));
		                  
		        TextView txtName = new TextView(this);
            	txtName.setText(name);
            	txtName.setTextSize(16);
            	txtName.setTextColor(getResources().getColor(R.color.item_text));

            	TextView txtPrice = new TextView(this);
            	txtPrice.setText("�" + price);
            	txtPrice.setTextSize(18);
            	txtPrice.setPadding(15,3,3,3);
            	txtPrice.setTextColor(getResources().getColor(R.color.item_price));
            	txtPrice.setLayoutParams(new LayoutParams( LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            	
            	TableRow row = new TableRow(this);
            	row.setBaselineAligned(true);
            	row.setGravity(Gravity.CENTER_VERTICAL);
            	
            	if (i % 2 == 0)
            	{
            		row.setBackgroundColor(getResources().getColor(R.color.item_background));
            	}else{
            		row.setBackgroundColor(getResources().getColor(R.color.item_background_even));
            	}
            	 
            	 row.addView(img, new TableRow.LayoutParams());
                 row.addView(txtName, new TableRow.LayoutParams(1));
                 row.addView(txtPrice, new TableRow.LayoutParams());
                 table.addView(row, new TableLayout.LayoutParams());
			}
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            
            // resize the bitmap
            Matrix matrix = new Matrix();
            matrix.postScale(30, 30);
            
            bis.close();
            is.close();
       } catch (IOException e) {
           Log.e(TAG, "Error getting bitmap", e);
       }
       return bm;
    } 
}