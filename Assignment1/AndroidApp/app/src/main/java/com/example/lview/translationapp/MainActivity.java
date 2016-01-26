package com.example.lview.translationapp;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.hide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String onResultClick(String currText)
    {

        if (!(currText.trim().length()==0))
        {
            Toast.makeText(this, "Getting Results", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Empty Input", Toast.LENGTH_SHORT).show();
        }

        try
        {
            SaveTheFeed tempobj=new SaveTheFeed();
            System.out.println(currText);
            String result=tempobj.execute(currText).get();
            System.out.println(result);
            return  result;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "Invalid Input";
        } catch (ExecutionException e) {
            e.printStackTrace();
            return "Invalid Input";
        }

    }

    public void btnClickHandler(View view) {
        int Viewid=view.getId();
        TextView txtInput= (TextView) findViewById(R.id.txtInput);
        TextView txtSolution=(TextView) findViewById(R.id.txtSolution);
        String currText=txtInput.getText().toString();

        switch(Viewid)
        {
            case R.id.btnAdd:
                currText=currText+"+";
                break;
            case R.id.btnSubtract:
                currText=currText+"-";
                break;
            case R.id.btnDivide:
                currText=currText+"/";
                break;
            case R.id.btnMultiply:
                currText=currText+"*";
                break;
            case R.id.btnBrack1:
                currText=currText+"(";
                break;
            case R.id.btnBrack2:
                currText=currText+")";
                break;
            case R.id.btnClear:
                currText="";
                break;
            case R.id.btnEquals:
                String retVal=onResultClick(currText);
                if (retVal=="InvalidCode")
                {
                    Toast.makeText(MainActivity.this, "Error Connecting to server", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txtSolution.setText(retVal);
                }
                System.out.println(retVal);

                currText="";
                break;
            case R.id.btnOne:
                currText=currText+"1";
                break;
            case R.id.btnTwo:
                currText=currText+"2";
                break;
            case R.id.btnThree:
                currText=currText+"3";
                break;
            case R.id.btnFour:
                currText=currText+"4";
                break;
            case R.id.btnFive:
                currText=currText+"5";
                break;
            case R.id.btnSix:
                currText=currText+"6";
                break;
            case R.id.btnSeven:
                currText=currText+"7";
                break;
            case R.id.btnEight:
                currText=currText+"8";
                break;
            case R.id.btnNine:
                currText=currText+"9";
                break;
            case R.id.btnDecimal:
                currText=currText+".";
                break;
            case R.id.btnZero:
                currText=currText+"0";
                break;
            case R.id.btnBack:
                if (currText.length() > 0 )
                {
                    currText = currText.substring(0, currText.length()-1);
                }
                break;

        }
        txtInput.setText(currText);

    }

    public void exitHandler(MenuItem item)
    {
        finish();
    }

    class SaveTheFeed extends AsyncTask<String,String,String>
    {
        @Override
        protected String doInBackground(String... params) {

            System.out.println("Log written");
            String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
            String inputStr = params[0];
            String apiURL="http://192.168.0.21:8080/compute/getCachedResult/";
            String retVal="InvalidCode";

            try {
                inputStr = URLEncoder.encode(inputStr, charset);
            }
            catch(UnsupportedEncodingException e)
            {
            }

            try {
                String urlstr=apiURL+"?input="+inputStr;
                System.out.println(urlstr);
                URL url = new URL(urlstr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    JSONObject jsonObj;
                    try
                    {
                        jsonObj = new JSONObject(sb.toString());
                        System.out.println(jsonObj.get("result"));
                        retVal=jsonObj.get("result").toString();
                    }
                    catch(JSONException e)
                    {
                    }

                    return retVal;

                } else {

                    System.out.println("server error");
                    return  retVal;
                }
            } catch (MalformedURLException e) {
                System.out.println("malformed");
                return  retVal;
                // ...
            } catch (IOException e) {
                System.out.println("io exception" + e.getCause());
                return retVal;
            }
        }
    }
}
