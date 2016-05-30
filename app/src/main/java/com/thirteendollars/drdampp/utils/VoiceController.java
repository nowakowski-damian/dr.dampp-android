package com.thirteendollars.drdampp.utils;

import android.content.Context;
import android.os.AsyncTask;
import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;


/**
 * Created by Damian Nowakowski on 27.04.16.
 */
public abstract class VoiceController extends AsyncTask<Void,Void,Exception> implements RecognitionListener {

    public static final String GO_AHEAD="goahead";
    public static final String GO_BACK="goback";
    public static final String TURN_LEFT="left";
    public static final String TURN_RIGHT="right";
    public static final String STOP="stop";

    private Context mContext;
    private SpeechRecognizer recognizer;


    public VoiceController(Context context) {
        mContext=context;
    }

    public abstract void initSuccess();
    public abstract void initFailed(Exception result);
    public abstract void onWordSaid(String text);



    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Exception doInBackground(Void... params) {
        try {
            Assets assets = new Assets(mContext);
            File assetDir = assets.syncAssets();
            setupRecognizer(assetDir);
        } catch (IOException e) {
            return e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Exception result) {

        if (result != null) {
            initFailed(result);
        } else {
            initSuccess();
            startListeningAgain();
        }
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        if (hypothesis == null) {
            return;
        }
        String text = hypothesis.getHypstr();
        int space = text.indexOf(' ');
        text = text.substring(0, space);
        onWordSaid(text);
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
    }

    @Override
    public void onBeginningOfSpeech() {
    }


    @Override
    public void onEndOfSpeech() {
            startListeningAgain();
    }


    private void startListeningAgain() {
        recognizer.stop();
        if( !isCancelled() ) {
            recognizer.startListening("menu");
        }
    }

    private void setupRecognizer(File assetsDir) throws IOException {

        recognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "8966.dict"))
                // Use context-independent phonetic search, context-dependent is too slow for mobile
                .setBoolean("-allphone_ci", true)
                .getRecognizer();
        recognizer.addListener(this);

        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addKeywordSearch("menu", menuGrammar);

    }


    @Override
    public void onTimeout() {
        startListeningAgain();
    }

}
