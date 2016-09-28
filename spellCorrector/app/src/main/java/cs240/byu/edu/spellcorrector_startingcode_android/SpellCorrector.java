package cs240.byu.edu.spellcorrector_startingcode_android;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStreamReader;

import cs240.byu.edu.spellcorrector_startingcode_android.StudentPackage.ISpellCorrector;
import cs240.byu.edu.spellcorrector_startingcode_android.StudentPackage.MySpellCorrector;

public class SpellCorrector extends AppCompatActivity
{
    TextView resultDisplay;
    EditText searchInput;
    Button searchButton;
    ISpellCorrector studentController;
    LinearLayout progressBar;
    RelativeLayout mainSearchScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_corrector);

        resultDisplay = (TextView)findViewById(R.id.result_output);
        searchInput = (EditText)findViewById(R.id.inputWord);
        searchButton = (Button)findViewById(R.id.searchBtn);

        searchButton.setOnClickListener(new searchButtonListener());

        String file = getIntent().getExtras().getString(MainActivity.fileSelectedKey);

        /**
         * TODO set studentController to a new instance of your class that implements ISpellCorrector
         * E.G.: studentController = new MySpellCorrector();
         */
        studentController = new MySpellCorrector();

        mainSearchScreen = (RelativeLayout)findViewById(R.id.mainSearchPage);

        if(studentController == null)
        {
            //SHOW MESSAGE ON SCREEN
            ScrollView nullMessage = (ScrollView)findViewById(R.id.null_message);

            mainSearchScreen.setVisibility(View.INVISIBLE);
            nullMessage.setVisibility(View.VISIBLE);
        }
        else
        {
           progressBar = (LinearLayout)findViewById(R.id.progressBar);


            mainSearchScreen.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            //In case loading the file takes a long time we pass it off to another thread.
            //This will call the student's useDictionary method and swap a progress bar in and out
            //on the screen.
            new fileLoader(){
                @Override
                protected void onPostExecute(Void v)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    mainSearchScreen.setVisibility(View.VISIBLE);
                }
            }.execute(file);
        }

    }

    //When the "Search" button is pressed this baby is called! RIGHT HUR!
    class searchButtonListener implements View.OnClickListener
    {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v)
        {
            String input = searchInput.getText().toString();
            if(input.length() < 1)
            {
                Snackbar.make(findViewById(android.R.id.content), "You have to input a word first!", Snackbar.LENGTH_LONG).show();
            }
            else
            {
                try
                {
                    String result = studentController.suggestSimilarWord(input);
                    StringBuilder output = new StringBuilder("The word: '");
                    if (result.toLowerCase().equals(input.toLowerCase()))
                    {
                        //word spelled correctly
                        output.append(input + "' is spelled correctly!");
                    } else
                    {
                        //word not spelled correctly
                        output.append(input + "' was not found. However, this word was found: '" + result + "'.");
                    }
                    resultDisplay.setText(output.toString());


                } catch (ISpellCorrector.NoSimilarWordFoundException e)
                {
                    resultDisplay.setText("No similar word found!");
                }
            }
        }
    }

    //To show a progress bar when the file is loading, (So that the UI doesn't just sit there at a stand still)
    //we have this fancy AsyncTask! YAY!
    class fileLoader extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... params)
        {
            try{
                AssetManager am = getAssets();
                if(params.length > 0)
                    studentController.useDictionary(new InputStreamReader(am.open(params[0])));
                else
                    Toast.makeText(getBaseContext(), "Error. File not passed in to file loader", Toast.LENGTH_LONG).show();

            } catch (IOException e)
            {
                Toast.makeText(SpellCorrector.this, "Dictionary file not found", Toast.LENGTH_LONG).show();
            }

            return null;
        }
    }
}
