package com.example.prox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.prox.adapter.EbookAdapter;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.radaee.reader.R;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
 
public class BookSearch extends Activity implements OnNavigationListener{
 
    private TextView txtQuery;
    int res=0;
    Ebook results;
    Ebook ebooks;
    ArrayList<Ebook> resBook = new ArrayList<Ebook>();
    ArrayList<Ebook> bookresults = new ArrayList<Ebook>();
    final ArrayList<Ebook> ebook = new ArrayList<Ebook>();
    private EbookAdapter e_adapter;
    int selectedCategory=0;
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar, menu);
 
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
 
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookstore_search_results);
        ActionBar ab = getActionBar();
	    ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("Store Search");
        ab.setIcon(R.drawable.ebookstore);
    	
        ab.setDisplayShowTitleEnabled(false);
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        ArrayList<String> categoryList = new ArrayList<String>();
        categoryList.add("By Title");
        categoryList.add("By Author");
        categoryList.add("By Category");
        categoryList.add("By ISBN");
 
        ArrayAdapter<String> aAdpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, categoryList);
        ab.setListNavigationCallbacks(aAdpt,this);
        
        //Toast.makeText(getApplicationContext(), "Category: "+selectedCategory, Toast.LENGTH_LONG).show();
 
        txtQuery = (TextView) findViewById(R.id.txtQuery);
 
        //handleIntent(getIntent());
	        
    }
    
    public boolean onNavigationItemSelected(int position, long id) {
    	 
    	//Toast.makeText(getApplicationContext(), "Category" + position, Toast.LENGTH_LONG).show();
    	selectedCategory = position;
    	handleIntent(getIntent(), selectedCategory);
		return true;
    }
    
    protected void onNewIntent(Intent intent, int cat) {
        setIntent(intent);
        handleIntent(intent,cat);
    }
 
    /**
     * Handling intent data
     */
    private void handleIntent(Intent intent, int cat) {
    	 
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            final String searchquery = intent.getStringExtra(SearchManager.QUERY);
          
            
            if(TextUtils.isEmpty(searchquery)){
            	Toast.makeText(getApplicationContext(), "Please enter a valid search query.", Toast.LENGTH_LONG).show();
            }else{
		            /**
		             * Use this query to display search results like 
		             * 1. Getting the data from SQLite and showing in listview 
		             * 2. Making webrequest and displaying the data 
		             * For now we just display the query only
		             */
		            String filter = "";
		            switch(cat){
			            case 0 : filter = "title"; break;
			            case 1 : filter = "author"; break;
			            case 2 : filter = "categoryID"; break;
			            case 3 : filter = "ISBN"; break;
			            default: filter="title";
		            }
		            
		            txtQuery.setText("Searching: " + searchquery);
		            Log.d("Store Search","Searching..."+ searchquery);
		            Toast.makeText(getApplicationContext(),"Searching in..."+filter, Toast.LENGTH_LONG).show(); 
		            
		            Parse.initialize(this, "x9n6KdzqtROdKDXDYF1n5AEoZLZKOih8rIzcbPVP", "JkqOqaHmRCA35t9xTtyoiofgG3IO7E6b82QIIHbF");
		            
		            ParseQuery<ParseObject> query = ParseQuery.getQuery("ebook");
			    	query.whereContains(filter, searchquery);
			    	query.findInBackground(new FindCallback<ParseObject>() {
						@SuppressLint("NewApi")
						@Override
						public void done(List<ParseObject> ebookslist, ParseException e) {
							// TODO Auto-generated method stub
							res =  ebookslist.size();
							
							Log.d("ebooks", "Found " + res + " ebooks");
							txtQuery.setText( res +" result(s) found for " + searchquery);
							
							
							if (e == null) {
				    	    	
				    	    	if(res > 0)
				    	    	{
				    	    		//results = new 
				    	    		Toast.makeText(getApplicationContext(), "Displaying..", Toast.LENGTH_LONG).show();
				    	    		int i=0;
					    	       String mybooktitle, myebookID = null, cover, filename, author,ISBN, category,bookstatus, ebookID;
					    	       for(ParseObject userbooks : ebookslist) {
					    	    	  
					    	    	  
					    	    	  mybooktitle=(String) userbooks.get("title");
					    	    	  filename = (String) userbooks.get("filename");
					    	    	  cover =  (String) userbooks.get("cover");
					    	    	  author = (String) userbooks.get("author");	
					    	    	  ISBN =  (String) userbooks.get("ISBN");
					    	    	  bookstatus =  (String) userbooks.get("status");
					    	    	  ebookID =  userbooks.getObjectId();
					    	    	  category = (String) userbooks.get("category");
					    	    	 
					    	    	  ebooks = new Ebook();
					    	    	  ebooks.setFilename(filename);
					    	    	  ebooks.setAuthor(author);
					    	    	  ebooks.setCover(cover);
					    	    	  ebooks.setID(ebookID);
					    	    	  ebooks.setISBN(ISBN);
					    	    	  ebooks.setTitle(mybooktitle);
					    	    	  ebooks.setStatus(bookstatus);
					    	    	  ebooks.setCategory(category);
					    	    	   
					    	    	  //resBook.add(ebooks);
					    	    	  resBook.add(i, ebooks);
					    	    	  TextView mTitle = (TextView) findViewById(R.id.mTitle);
					    	    	  TextView mAuthor = (TextView) findViewById(R.id.mAuthor);
					    	    	  //mTitle.setText(mybooktitle);
					    	    	  //mAuthor.setText(author);
					    	    	  //results[i] =  new Ebook(object);
					    	    	  i++;
					    	    	  userbooks.put("ebookID", ebookID);
					    	    	  Log.d("Parse Data", "Retrieved ebook details " + cover + filename +author +mybooktitle+ ISBN +bookstatus);
					              }
					    	      
					    	      ListView listview = (ListView) findViewById(R.id.listresults);
					    	      //e_adapter = new EbookAdapter(BookSearch.this, R.layout.ebooks_row, ebook);
					    	      
					    	      
					    	      final EbookAdapter  adapter = new EbookAdapter(BookSearch.this, R.layout.ebooks_row, R.id.mTitle, resBook);
		
					              listview.setAdapter(adapter); 
					              
					              // And if you want selection feedback:
					              listview.setOnItemClickListener(new OnItemClickListener() {
					                  @Override
					                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					                      //Do whatever you want with the selected item
					                	  
					                	  Ebook storeebook = new Ebook();
					                	  	storeebook = resBook.get(position);
							        		Intent bookdetails = new Intent(getApplicationContext(), StoreBookDetails.class);
		
							        		bookdetails.putExtra("title", storeebook.getTitle());
							        		bookdetails.putExtra("filename", storeebook.getFilename());
							        		bookdetails.putExtra("author", storeebook.getAuthor());
							        		bookdetails.putExtra("cover", storeebook.getCover());
							    	    	bookdetails.putExtra("ebookID", storeebook.getID());
							    	    	bookdetails.putExtra("status", storeebook.getStatus());
							    	    	bookdetails.putExtra("ISBN", storeebook.getISBN());
							    	    	bookdetails.putExtra("category", storeebook.getCategory());
			
							        		bookdetails.putExtra("id",position);
							                startActivity(bookdetails);
							                
					                      Log.d("Test", storeebook.getTitle() + " has been selected!");
					                  }
					              });
					              
					              
				    	    	}else{
				    	    		txtQuery.setText("No results found for " + searchquery);
				    	    		//Toast.makeText(getApplicationContext(),"No results found... try another", Toast.LENGTH_LONG).show(); 
				    	    	}
				    	    	
				    	    	
				    	    } else {
				    	  
				    	    	 Log.d("ebooks error", "Error");
				    	    }
							
							 
						}
						
			    	});
	    	       	
            }
            
            
             
        }
 
  
    }
    
    
 
    
  
 
    
}
