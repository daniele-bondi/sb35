package it.meridian.spellbook35;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;
import java.util.regex.Pattern;

import it.meridian.spellbook35.utils.Utils;


public class FragmentSpellInfo extends android.support.v4.app.Fragment
{
	static public final String ARG_KEY_SPELL = "spell";
	
	
	
	private String spell_name;
	private View root;
	private TextView textview_source;
	private TextView textview_school;
	private TextView textview_descriptor;
	private TextView textview_components;
	private TextView textview_cast_time;
	private TextView textview_range;
	private TextView textview_effect_type;
	private TextView textview_effect;
	private TextView textview_duration;
	private TextView textview_save;
	private TextView textview_resistance;
	private TextView textview_desc_m;
	private TextView textview_desc_l;
	private WebView webview_desc;
	private EditText edittext_notes;
	
	private TableRow row_source;
	private TableRow row_school;
	private TableRow row_descriptor;
	private TableRow row_components;
	private TableRow row_cast_time;
	private TableRow row_range;
	private TableRow row_effect_type;
	private TableRow row_effect;
	private TableRow row_duration;
	private TableRow row_save;
	private TableRow row_resistance;
	
	private MenuItem menu_btn_add_spell;
	
	private String notes;
	
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
		
		String spell_name = (String) this.getArguments().get(ARG_KEY_SPELL);
		if(!Objects.equals(this.spell_name, spell_name))
		{
			this.spell_name = spell_name;
		}
	}
	
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		ScrollView scroll_view = (ScrollView)inflater.inflate(R.layout.fragment_spell_info, container, false);
		
		this.textview_source = (TextView) scroll_view.findViewById(R.id.spell_source);
		this.textview_school = (TextView) scroll_view.findViewById(R.id.spell_school);
		this.textview_descriptor = (TextView) scroll_view.findViewById(R.id.spell_descriptor);
		this.textview_components = (TextView) scroll_view.findViewById(R.id.spell_components);
		this.textview_cast_time = (TextView) scroll_view.findViewById(R.id.spell_cast_time);
		this.textview_range = (TextView) scroll_view.findViewById(R.id.spell_range);
		this.textview_effect_type = (TextView) scroll_view.findViewById(R.id.spell_effect_type);
		this.textview_effect = (TextView) scroll_view.findViewById(R.id.spell_effect);
		this.textview_duration = (TextView) scroll_view.findViewById(R.id.spell_duration);
		this.textview_save = (TextView) scroll_view.findViewById(R.id.spell_saving_throw);
		this.textview_resistance = (TextView) scroll_view.findViewById(R.id.spell_resistance);
		this.textview_desc_m = (TextView) scroll_view.findViewById(R.id.spell_description_medium);
		this.textview_desc_l = (TextView) scroll_view.findViewById(R.id.spell_description_long);
		this.webview_desc = (WebView) scroll_view.findViewById(R.id.spell_desc);
		this.edittext_notes = (EditText) scroll_view.findViewById(R.id.spell_notes);
		
		this.row_source = (TableRow) scroll_view.findViewById(R.id.row_spell_0);
		this.row_school = (TableRow) scroll_view.findViewById(R.id.row_spell_1);
		this.row_descriptor = (TableRow) scroll_view.findViewById(R.id.row_spell_2);
		this.row_components = (TableRow) scroll_view.findViewById(R.id.row_spell_3);
		this.row_cast_time = (TableRow) scroll_view.findViewById(R.id.row_spell_4);
		this.row_range = (TableRow) scroll_view.findViewById(R.id.row_spell_5);
		this.row_effect = (TableRow) scroll_view.findViewById(R.id.row_spell_7);
		this.row_duration = (TableRow) scroll_view.findViewById(R.id.row_spell_8);
		this.row_save = (TableRow) scroll_view.findViewById(R.id.row_spell_9);
		this.row_resistance = (TableRow) scroll_view.findViewById(R.id.row_spell_A);
		
		WebViewClient webview_client = new WebViewClient(this);
		this.webview_desc.setWebViewClient(webview_client);
		this.webview_desc.getSettings().setTextZoom(90);
		
		return scroll_view;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		if(this.spell_name != null)
		{
			this.getActivity().setTitle(this.spell_name);
			
			Cursor cursor = Application.query("SELECT * FROM spell_detail WHERE name = ?", this.spell_name);
			cursor.moveToFirst();
			
			{
				String source_book = Utils.CursorGetString(cursor, "source_book");
				String source_page = Utils.CursorGetString(cursor, "source_page");
				if(source_page != null)
					source_book += " " + source_page;
				this.row_source.setVisibility(source_book != null ? View.VISIBLE : View.GONE);
				this.textview_source.setText(source_book);
			}
			
			{
				String school = Utils.CursorGetString(cursor, "school");
				String subsch = Utils.CursorGetString(cursor, "subschool");
				if(subsch != null)
					school += " (" + subsch + ")";
				this.row_school.setVisibility(school != null ? View.VISIBLE : View.GONE);
				this.textview_school.setText(school);
			}
			
			{
				String descriptor = Utils.CursorGetString(cursor, "descriptor");
				this.row_descriptor.setVisibility(descriptor != null ? View.VISIBLE : View.GONE);
				this.textview_descriptor.setText(descriptor);
			}
			
			{
				String compenents = Utils.CursorGetString(cursor, "components");
				this.row_components.setVisibility(compenents != null ? View.VISIBLE : View.GONE);
				this.textview_components.setText(compenents);
			}
			
			{
				String cast_time = Utils.CursorGetString(cursor, "cast_time");
				this.row_cast_time.setVisibility(cast_time != null ? View.VISIBLE : View.GONE);
				this.textview_cast_time.setText(cast_time);
			}
			
			{
				String range = Utils.CursorGetString(cursor, "range");
				this.row_range.setVisibility(range != null ? View.VISIBLE : View.GONE);
				this.textview_range.setText(range);
			}

//				{
//					String eff_type = cursor.getString(cursor.getColumnIndex(Spell.COLUMN_EFFECT_TYPE));
//					this.row_effect_type.setVisibility(eff_type != null ? View.VISIBLE : View.GONE);
//					this.textview_effect_type.setText(eff_type);
//				}
			
			{
				String eff_type = Utils.CursorGetString(cursor, "effect_type");
				String effect = Utils.CursorGetString(cursor, "effect");
				this.row_effect.setVisibility(eff_type != null && effect != null ? View.VISIBLE : View.GONE);
				this.textview_effect.setText(effect);
				this.textview_effect_type.setText(eff_type);
			}
			
			{
				String duration = Utils.CursorGetString(cursor, "duration");
				this.row_duration.setVisibility(duration != null ? View.VISIBLE : View.GONE);
				this.textview_duration.setText(duration);
			}
			
			{
				String save = Utils.CursorGetString(cursor, "saving_throw");
				this.row_save.setVisibility(save != null ? View.VISIBLE : View.GONE);
				this.textview_save.setText(save);
			}
			
			{
				String resist = Utils.CursorGetString(cursor, "resistance");
				this.row_resistance.setVisibility(resist != null ? View.VISIBLE : View.GONE);
				this.textview_resistance.setText(resist);
			}
			
			{
				String desc = Utils.CursorGetString(cursor, "fluff");
				this.textview_desc_m.setVisibility(desc != null ? View.VISIBLE : View.GONE);
				this.textview_desc_m.setText(Html.fromHtml(desc != null ? desc : ""));
			}
			
			{
				String desc = Utils.CursorGetString(cursor, "description");
				if(desc == null || desc.length() == 0)
				{
					this.webview_desc.setVisibility(desc != null ? View.VISIBLE : View.GONE);
				}
				else
				{
					desc = desc.replace("\n", "<br/>");
					desc = String.format("<html><head></head><body>%s</body></html>", desc);
				}
				
				this.webview_desc.loadDataWithBaseURL("spellbook://0.0.0.0/", desc, null, "UTF-8", null);
			}
			
			{
				String notes = Utils.CursorGetString(cursor, "notes");
				this.edittext_notes.setText(notes);
				this.notes = this.edittext_notes.getText().toString();
			}
			
			cursor.close();
		}
		else // spell_name == null
		{
			this.root.setVisibility(View.GONE);
			
			this.spell_name = null;
			this.notes = null;
			
			ActionBar action_bar = this.getActivity().getActionBar();
			if(action_bar != null)
				action_bar.setTitle("ERROR");
			
			this.textview_source.setText("");
			this.textview_school.setText("");
			this.textview_descriptor.setText("");
			this.textview_components.setText("");
			this.textview_cast_time.setText("");
			this.textview_range.setText("");
			this.textview_effect_type.setText("");
			this.textview_effect.setText("");
			this.textview_duration.setText("");
			this.textview_save.setText("");
			this.textview_resistance.setText("");
			this.textview_desc_m.setText("");
//			this.textview_desc_l.setText("");
			this.webview_desc.loadData("", "text/html; charset=utf-8", "utf-8");
			this.edittext_notes.setText("");
			
			Toast.makeText(this.getContext(), "ERROR: spell is NULL", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	
	static private class WebViewClient extends android.webkit.WebViewClient
	{
		private FragmentSpellInfo fragment;
		
		WebViewClient(FragmentSpellInfo fragment)
		{
			this.fragment = fragment;
		}
		
		@SuppressWarnings("deprecation")
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			String spell_name = url.substring("spellbook://0.0.0.0/".length());
			try
			{
				spell_name = URLDecoder.decode(spell_name, "UTF-8");
			}
			catch(UnsupportedEncodingException ignored) {}
			
			Cursor spells = Application.query("SELECT 1 FROM spell WHERE name = ? LIMIT 1", spell_name);
			if(spells.getCount() > 0)
			{
				FragmentSpellInfo new_frag = new FragmentSpellInfo();
				Bundle args = new Bundle(1);
				args.putString(ARG_KEY_SPELL, spell_name);
				new_frag.setArguments(args);
				
				this.fragment.getFragmentManager().beginTransaction()
						.replace(R.id.activity_main_content, new_frag)
						.addToBackStack(null)
						.commit();
			}
			else
			{
				Toast.makeText(this.fragment.getContext(), "Spell " + spell_name + " does not exist", Toast.LENGTH_SHORT).show();
			}
			
			return true;
		}
		
		@TargetApi(Build.VERSION_CODES.LOLLIPOP)
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
		{
			return this.shouldOverrideUrlLoading(view, request.getUrl().toString());
		}
	}
}