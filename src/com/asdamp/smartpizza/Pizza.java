package com.asdamp.smartpizza;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Random;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.asdamp.database.DBAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Pizza extends SherlockFragmentActivity {

	protected static final String ID_PIZZA_DA_VISUALIZZARE = "idPizza";
	protected static final String REQUEST_CODE = "RequestCode";
	 private ShareActionProvider myShareActionProvider;
	 private ArrayList<String> ingre;;		
	 private	String nome;	
		private	String des;
		private	String imm;
    private Animator mCurrentAnimator; 
	int id;
	Bundle b;
	protected static final int RANDOPIZZA = 0;
	protected static final int APRI_PIZZA = 1;
	private int req;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.pizza);
		/*AdLayout adview=(AdLayout) this.findViewById(R.id.adview);
		adview.loadAd(new AdTargetingOptions());*/
		b=this.getIntent().getExtras().getBundle("bundle");
		req=b.getInt(REQUEST_CODE);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		inizializza();
		
	}
	@Override
	public void onBackPressed() {
		Intent i=new Intent(this,MainActivity.class);    
		i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  
		this.startActivity(i);
	}
	private void inizializza() {
		switch (req){
		case RANDOPIZZA:
			id= randopizza();			
			break;
		case APRI_PIZZA:
			id=b.getInt(ID_PIZZA_DA_VISUALIZZARE);
			break;
		
	}

	}

	@Override
	protected void onResume() {
		super.onResume();
		visualizza(id);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.getSupportMenuInflater().inflate(R.menu.pizza, menu);
		switch (req){
		case RANDOPIZZA:
			menu.findItem(R.id.Rand).setVisible(true);		
			break;
		case APRI_PIZZA:
			break;
		}
	   MenuItem item = menu.findItem(R.id.share);
		myShareActionProvider = (ShareActionProvider) item.getActionProvider();
	     myShareActionProvider.setShareIntent(createShareIntent());
		return super.onCreateOptionsMenu(menu);
		
	}
	
				
	private Intent createShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
	    shareIntent.setType("text/plain");
	    String text=MessageFormat.format(getString(R.string.sto_mangiando_una_buonissima_pizza_con_),
				nome);
	    for(String i:ingre){
	    	text=text+"-"+i+"\n";
	    }
	    text=text+"[Smart Pizza: https://play.google.com/store/apps/details?id=com.asdamp.smartpizza]";
	    shareIntent.putExtra(Intent.EXTRA_TEXT, text);
	   // shareIntent.putExtra(Intent.EXTRA_STREAM, imm);
	    return shareIntent; 
	}
	private int randopizza() {
		ArrayList<Integer> id=Costanti.getDB().fetchIDPizze();
		if(id.size()==0) {
			Toast.makeText(this, R.string.nessuna_pizza_presente_in_database,Toast.LENGTH_LONG).show();
			finish();
			return -1;
		}
		else{
			Random r=new Random();
			int rNum=r.nextInt(id.size());
			return id.get(rNum);
		}
	}
	
	
	private void visualizza(int id) {
		DBAdapter db=Costanti.getDB();
		ingre=db.associaIdPizzaAIngredienti(id);		
		nome=db.associaIdPizzaANome(id);	
		des=db.associaIdPizzaADescrizione(id);
		imm=db.associaIdPizzaAImmagine(id);
		visualizza(nome, des, ingre, imm);
	}
	private void visualizza(String nome, String descrizione, ArrayList<String> ingredienti,String imm){
		TextView n=((TextView) this.findViewById(R.id.pizza_nome_pizza));
		n.setText(nome);
		TextView d=((TextView) this.findViewById(R.id.pizza_descrizione));
		if(descrizione==null) d.setVisibility(View.GONE);
		else d.setText(descrizione);
		final ImageView im=(ImageView) this.findViewById(R.id.pizza_immagine);

		if(imm!=null){
			final Uri immUri=Uri.parse(imm);
			im.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH)
					zoomImageFromThumb(im, immUri);
					return true;
				}});
			
			im.setVisibility(View.VISIBLE);
			im.setImageURI(immUri);
		}
		else im.setVisibility(View.GONE);
		ListView l=(ListView) this.findViewById(R.id.pizza_lista_ingredienti);
		IngredientiAdapter ia;
		ia=new IngredientiAdapter(this, ingredienti);
		l.setAdapter(ia);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
		case android.R.id.home:
			this.onBackPressed();
			break;
		case R.id.Rand:
			id=randopizza();
			visualizza(id);
			break;
		case R.id.modifica:
			Bundle bu=new Bundle();
			bu.putInt(ID_PIZZA_DA_VISUALIZZARE, id);
			bu.putInt(REQUEST_CODE, PizzaModifica.MODIFICA);
			Intent i=new Intent(this, PizzaModifica.class);
			i.putExtra("bundle", bu);
			this.startActivityForResult(i, PizzaModifica.MODIFICA);
		}
		return false;
	}
	
	private void zoomImageFromThumb(final View thumbView, Uri imm) {
	    // If there's an animation in progress, cancel it
	    // immediately and proceed with this one.
        final int mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

	    if (mCurrentAnimator != null) {
	        mCurrentAnimator.cancel();
	    }

	    // Load the high-resolution "zoomed-in" image.
	    final ImageView expandedImageView = (ImageView) findViewById(
	            R.id.expanded_image);
	    expandedImageView.setImageURI(imm);

	    // Calculate the starting and ending bounds for the zoomed-in image.
	    // This step involves lots of math. Yay, math.
	    final Rect startBounds = new Rect();
	    final Rect finalBounds = new Rect();
	    final Point globalOffset = new Point();

	    // The start bounds are the global visible rectangle of the thumbnail,
	    // and the final bounds are the global visible rectangle of the container
	    // view. Also set the container view's offset as the origin for the
	    // bounds, since that's the origin for the positioning animation
	    // properties (X, Y).
	    thumbView.getGlobalVisibleRect(startBounds);
	    findViewById(R.id.pizza_informazioni_layout)
	            .getGlobalVisibleRect(finalBounds, globalOffset);
	    startBounds.offset(-globalOffset.x, -globalOffset.y);
	    finalBounds.offset(-globalOffset.x, -globalOffset.y);

	    // Adjust the start bounds to be the same aspect ratio as the final
	    // bounds using the "center crop" technique. This prevents undesirable
	    // stretching during the animation. Also calculate the start scaling
	    // factor (the end scaling factor is always 1.0).
	    float startScale;
	    if ((float) finalBounds.width() / finalBounds.height()
	            > (float) startBounds.width() / startBounds.height()) {
	        // Extend start bounds horizontally
	        startScale = (float) startBounds.height() / finalBounds.height();
	        float startWidth = startScale * finalBounds.width();
	        float deltaWidth = (startWidth - startBounds.width()) / 2;
	        startBounds.left -= deltaWidth;
	        startBounds.right += deltaWidth;
	    } else {
	        // Extend start bounds vertically
	        startScale = (float) startBounds.width() / finalBounds.width();
	        float startHeight = startScale * finalBounds.height();
	        float deltaHeight = (startHeight - startBounds.height()) / 2;
	        startBounds.top -= deltaHeight;
	        startBounds.bottom += deltaHeight;
	    }

	    // Hide the thumbnail and show the zoomed-in view. When the animation
	    // begins, it will position the zoomed-in view in the place of the
	    // thumbnail.
	    ViewHelper.setAlpha(thumbView, 0f);
	    expandedImageView.setVisibility(View.VISIBLE);

	    // Set the pivot point for SCALE_X and SCALE_Y transformations
	    // to the top-left corner of the zoomed-in view (the default
	    // is the center of the view).
	    ViewHelper.setPivotX(expandedImageView, 0f);
	    ViewHelper.setPivotY(expandedImageView, 0f);

	    // Construct and run the parallel animation of the four translation and
	    // scale properties (X, Y, SCALE_X, and SCALE_Y).
	    AnimatorSet set = new AnimatorSet();
	    set.playTogether(
	            ObjectAnimator.ofFloat(expandedImageView, "translationX",
	                    startBounds.left, finalBounds.left),
	            ObjectAnimator.ofFloat(expandedImageView, "translationY",
	                    startBounds.top, finalBounds.top),
	            ObjectAnimator.ofFloat(expandedImageView, "scaleX",
	            startScale, 1f),
	            ObjectAnimator.ofFloat(expandedImageView,
	            		"scaleY", startScale, 1f));
	    		
	    set.setDuration(mShortAnimationDuration);
	    set.setInterpolator(new DecelerateInterpolator());
	    set.addListener(new AnimatorListenerAdapter() {
	        @Override
	        public void onAnimationEnd(Animator animation) {
	            mCurrentAnimator = null;
	        }

	        @Override
	        public void onAnimationCancel(Animator animation) {
	            mCurrentAnimator = null;
	        }
	    });
	    set.start();
	    mCurrentAnimator = set;

	    // Upon clicking the zoomed-in image, it should zoom back down
	    // to the original bounds and show the thumbnail instead of
	    // the expanded image.
	    final float startScaleFinal = startScale;
	    expandedImageView.setOnClickListener(new View.OnClickListener() {
	        @Override
	        public void onClick(View view) {
	            if (mCurrentAnimator != null) {
	                mCurrentAnimator.cancel();
	            }

	            // Animate the four positioning/sizing properties in parallel,
	            // back to their original values.
	            AnimatorSet set = new AnimatorSet();
	            set.playTogether(
	    	            ObjectAnimator.ofFloat(expandedImageView, "translationX",
	    	                    startBounds.left),
	    	            ObjectAnimator.ofFloat(expandedImageView, "translationY",
	    	                    startBounds.top),
	    	            ObjectAnimator.ofFloat(expandedImageView, "scaleX",
	    	            startScaleFinal),
	    	            ObjectAnimator.ofFloat(expandedImageView,
	    	            		"scaleY", startScaleFinal));
	            set.setDuration(mShortAnimationDuration);
	            set.setInterpolator(new DecelerateInterpolator());

	            set.addListener(new AnimatorListenerAdapter() {
	                @Override
	                public void onAnimationEnd(Animator animation) {
	                	ViewHelper.setAlpha(thumbView, 1f);
	                    expandedImageView.setVisibility(View.GONE);
	                    mCurrentAnimator = null;
	                }

	                @Override
	                public void onAnimationCancel(Animator animation) {
	                	ViewHelper.setAlpha(thumbView, 1f);
	                    expandedImageView.setVisibility(View.GONE);
	                    mCurrentAnimator = null;
	                }
	            });
	            set.start();
	            mCurrentAnimator = set;
	        }
	    });
	}
}
