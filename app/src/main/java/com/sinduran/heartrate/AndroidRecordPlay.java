package com.sinduran.heartrate;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by sinduran on 23/10/15.
 */
public class AndroidRecordPlay {


    private MediaPlayer mediaPlayer;
    private MediaPlayer.OnCompletionListener playerCompletion;

    private static String parentDir;
    private static final int recorder_bpp = 16;
    private static final String temp_file = "temp.raw";
    private static int[] sampleRates = new int[] {44100,22050,11025,8000};
    private static int sample_rate;
    private static final int channels = AudioFormat.CHANNEL_IN_MONO;
    private static final int encoding = AudioFormat.ENCODING_PCM_16BIT;
    private final int source;
    private final String wavFile;

    private AudioRecord audioRecord;
    private int bufferSize = 0;
    private Thread recordingThread;
    private boolean isRecording = false;

    public AndroidRecordPlay(int source, String parentDir, String wavFile){
        this.source = source;
        this.wavFile = wavFile;
        this.parentDir = parentDir;
    }

    private AudioRecord getAudioRecord(){
        AudioRecord ar = null;
        for(int sampleRate : sampleRates){
            bufferSize = AudioRecord.getMinBufferSize(sampleRate,channels,encoding);
            ar = new AudioRecord(source, sampleRate, channels, encoding, bufferSize);
            if(ar.getState() == AudioRecord.STATE_INITIALIZED){
                Log.d("DEBUG","sample rate is : " + sampleRate);
                sample_rate = sampleRate;
                return ar;
            }else{
                ar.release();
            }
        }
        return ar;
    }

    public void startRecording(){
        audioRecord = getAudioRecord();
        int i = audioRecord.getState();
        if(i == 1){
            audioRecord.startRecording();
        }
        isRecording = true;
        recordingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                writeDataToFile();
            }
        },"AudioRecorder Thread");
        recordingThread.start();
    }

    private void writeDataToFile() {
        byte data[] = new byte[bufferSize]; //init buffer
        FileOutputStream outputStream = null; //output stream to write data
        try{
            outputStream = new FileOutputStream(parentDir + temp_file);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }
        int read = 0;
        if(outputStream != null){
            while(isRecording){
                read = audioRecord.read(data,0,bufferSize); //get data from audiorecorder
                if(read != AudioRecord.ERROR_INVALID_OPERATION){
                    try{
                        outputStream.write(data); //if it is valid the write to stream
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
            try {
                outputStream.close(); //close stream
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void startPlaying(){
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(parentDir + wavFile);
            if (playerCompletion != null) {
                mediaPlayer.setOnCompletionListener(playerCompletion);
            }
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void stopRecording(){
        if(audioRecord != null){
            isRecording = false; //stop the running thread
            int state = audioRecord.getState();
            if(state == 1){
                audioRecord.stop();
            }
            audioRecord.release();
            audioRecord = null;
            recordingThread = null;
        }
        copyTempToWaveFile(parentDir + temp_file,parentDir + wavFile);
        deleteTempFile();
    }

    private void copyTempToWaveFile(String tempPath, String wavePath) {
        FileInputStream in = null;
        FileOutputStream out = null;
        long totalAudioLen = 0;
        long totalDataLen;
        long longSampleRate = sample_rate;
        int channels = this.channels/ 16; // 1 for mono, 2 for stereo
        long byteRate = recorder_bpp * sample_rate * channels/8;
        long blockAlign = channels *recorder_bpp/8;

        byte[] data = new byte[bufferSize];

        try {
            in = new FileInputStream(tempPath);
            out = new FileOutputStream(wavePath);
            totalAudioLen = in.getChannel().size();
            totalDataLen = totalAudioLen + 36;


            WriteWaveFileHeader(out, totalAudioLen, totalDataLen,
                    longSampleRate, channels, byteRate,blockAlign);

            while(in.read(data) != -1){
                out.write(data);
            }

            in.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void WriteWaveFileHeader(
            FileOutputStream out, long totalAudioLen,
            long totalDataLen, long longSampleRate, int channels,
            long byteRate, long blockAlign) throws IOException {

        byte[] header = new byte[44];

        header[0] = 'R'; // RIFF/WAVE header
        header[1] = 'I';
        header[2] = 'F';
        header[3] = 'F';
        header[4] = (byte) (totalDataLen & 0xff);
        header[5] = (byte) ((totalDataLen >> 8) & 0xff);
        header[6] = (byte) ((totalDataLen >> 16) & 0xff);
        header[7] = (byte) ((totalDataLen >> 24) & 0xff);
        header[8] = 'W';
        header[9] = 'A';
        header[10] = 'V';
        header[11] = 'E';
        header[12] = 'f'; // 'fmt ' chunk
        header[13] = 'm';
        header[14] = 't';
        header[15] = ' ';
        header[16] = 16; // 4 bytes: size of 'fmt ' chunk
        header[17] = 0;
        header[18] = 0;
        header[19] = 0;
        header[20] = 1; // format = 1
        header[21] = 0;
        header[22] = (byte) channels;
        header[23] = 0;
        header[24] = (byte) (longSampleRate & 0xff);
        header[25] = (byte) ((longSampleRate >> 8) & 0xff);
        header[26] = (byte) ((longSampleRate >> 16) & 0xff);
        header[27] = (byte) ((longSampleRate >> 24) & 0xff);
        header[28] = (byte) (byteRate & 0xff);
        header[29] = (byte) ((byteRate >> 8) & 0xff);
        header[30] = (byte) ((byteRate >> 16) & 0xff);
        header[31] = (byte) ((byteRate >> 24) & 0xff);
        header[32] = (byte) (blockAlign & 0xff); // block align
        header[33] = (byte) ((blockAlign >> 8) & 0xff);
        header[34] = recorder_bpp; // bits per sample
        header[35] = 0;
        header[36] = 'd';
        header[37] = 'a';
        header[38] = 't';
        header[39] = 'a';
        header[40] = (byte) (totalAudioLen & 0xff);
        header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
        header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
        header[43] = (byte) ((totalAudioLen >> 24) & 0xff);

        out.write(header, 0, 44);
    }


    private void deleteTempFile(){
        File file = new File(parentDir+temp_file);
        file.delete();
    }

    public void stopPlaying(){
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public void setOnPlayerCompletion(MediaPlayer.OnCompletionListener playerCompletion){
        this.playerCompletion = playerCompletion;
    }
}
