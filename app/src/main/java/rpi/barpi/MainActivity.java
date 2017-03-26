package rpi.barpi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{
    ListView lvbars;
    ArrayAdapter<Bar> appAdapter=null;
    public int contextMenuItemSelected=-1;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!Data.initialized)
            Data.init(getApplicationContext().getSharedPreferences("SaveData", Context.MODE_PRIVATE));

        Data.mainAct=this;

        //set the gui objects
        lvbars=(ListView)findViewById(R.id.barListView);

        //set the lvs array
        appAdapter=new ArrayAdapter<Bar>(this, android.R.layout.simple_list_item_1, Data.bars);
        lvbars.setAdapter(appAdapter);

        //long click event
        registerForContextMenu(lvbars);

        //click event
        final MainActivity tempThis=this;
        lvbars.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                tempThis.contextMenuItemSelected=position;
                startActivity(new Intent(tempThis, BarActivity.class));
            }
        });

        //connect to the server
        ArrayList<String> data=new ArrayList<String>();
        data.add("CONNECT");

        Sockets.connect(data);

        //test data
        Data.bars.clear();
        Data.bars.add(new Bar(0, "Bar Troy", 0.0f, new Location()));
        Data.bars.add(new Bar(1, "The Ruck", 0.0f, new Location()));
        Data.bars.add(new Bar(2, "Olearys", 0.0f, new Location()));
        Data.bars.add(new Bar(3, "Union Pub", 0.0f, new Location()));
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.bar_menu, menu);

        //incase we open a new activity from this one
        AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo)menuInfo;
        contextMenuItemSelected=info.position;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        int id=item.getItemId();
        if(id == R.id.bar_main_open)
        {
            Toast.makeText(this, "Opening bar info...", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if(id == R.id.bar_main_report)
        {
            Toast.makeText(this, "Reporting bar...", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}