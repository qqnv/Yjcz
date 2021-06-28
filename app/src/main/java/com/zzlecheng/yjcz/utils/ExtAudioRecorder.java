//package com.zzlecheng.yjcz.utils;
//
//import android.media.AudioRecord;
//import android.media.AudioRecord.OnRecordPositionUpdateListener;
//import android.media.MediaRecorder;
//import android.os.Handler;
//import android.os.Message;
//import android.os.SystemClock;
//import android.util.Log;
//import java.io.File;
//import java.io.IOException;
//import java.io.RandomAccessFile;
//import java.util.Date;
//
//public class ExtAudioRecorder
//{
//    private AudioRecord audioRecorder = null;
//    private byte[] buffer;
//    private int bufferSize;
//    private int cAmplitude = 0;
//    private short channels;
//    private AuditRecorderConfiguration configuration;
//    private String filePath;
//    private int framePeriod;
//    private MediaRecorder mediaRecorder = null;
//    private int payloadSize;
//    private RandomAccessFile randomAccessWriter;
//    private short samples;
//    private long startTime;
//    private State state;
//    private AudioRecord.OnRecordPositionUpdateListener updateListener = new AudioRecord.OnRecordPositionUpdateListener()
//    {
//        public void onMarkerReached(AudioRecord paramAnonymousAudioRecord) {}
//
//        public void onPeriodicNotification(AudioRecord paramAnonymousAudioRecord)
//        {
//            ExtAudioRecorder.this.audioRecorder.read(ExtAudioRecorder.this.buffer, 0, ExtAudioRecorder.this.buffer.length);
//            for (;;)
//            {
//                int j;
//                try
//                {
//                    ExtAudioRecorder.this.randomAccessWriter.write(ExtAudioRecorder.this.buffer);
//                    ExtAudioRecorder.access$302(ExtAudioRecorder.this, ExtAudioRecorder.this.payloadSize + ExtAudioRecorder.this.buffer.length);
//                    int i = ExtAudioRecorder.this.samples;
//                    j = 0;
//                    if (i == 16)
//                    {
//                        if (j < ExtAudioRecorder.this.buffer.length / 2)
//                        {
//                            ExtAudioRecorder localExtAudioRecorder = ExtAudioRecorder.this;
//                            byte[] arrayOfByte = ExtAudioRecorder.this.buffer;
//                            int k = j * 2;
//                            int m = localExtAudioRecorder.getShort(arrayOfByte[k], ExtAudioRecorder.this.buffer[(k + 1)]);
//                            if (m <= ExtAudioRecorder.this.cAmplitude) {
//                                break label246;
//                            }
//                            ExtAudioRecorder.access$602(ExtAudioRecorder.this, m);
//                            break label246;
//                        }
//                    }
//                    else if (j < ExtAudioRecorder.this.buffer.length)
//                    {
//                        if (ExtAudioRecorder.this.buffer[j] > ExtAudioRecorder.this.cAmplitude) {
//                            ExtAudioRecorder.access$602(ExtAudioRecorder.this, ExtAudioRecorder.this.buffer[j]);
//                        }
//                        j++;
//                        continue;
//                    }
//                    return;
//                }
//                catch (IOException localIOException)
//                {
//                    localIOException.printStackTrace();
//                    Log.e(ExtAudioRecorder.class.getName(), "Error occured in updateListener, recording is aborted");
//                }
//                label246:
//                j++;
//            }
//        }
//    };
//
//    public ExtAudioRecorder(AuditRecorderConfiguration paramAuditRecorderConfiguration)
//    {
//        this.configuration = paramAuditRecorderConfiguration;
//        if (paramAuditRecorderConfiguration.isUncompressed())
//        {
//            init(paramAuditRecorderConfiguration.isUncompressed(), paramAuditRecorderConfiguration.getSource(), paramAuditRecorderConfiguration.getRate(), paramAuditRecorderConfiguration.getChannelConfig(), paramAuditRecorderConfiguration.getFormat());
//            return;
//        }
//        int i = 0;
//        int j;
//        int k;
//        do
//        {
//            init(paramAuditRecorderConfiguration.isUncompressed(), paramAuditRecorderConfiguration.getSource(), AuditRecorderConfiguration.SAMPLE_RATES[i], paramAuditRecorderConfiguration.getChannelConfig(), paramAuditRecorderConfiguration.getFormat());
//            j = 1;
//            i += j;
//            if (i < AuditRecorderConfiguration.SAMPLE_RATES.length) {
//                k = j;
//            } else {
//                k = 0;
//            }
//            if (getState() == State.INITIALIZING) {
//                j = 0;
//            }
//        } while ((j & k) != 0);
//    }
//
//    private void fireFailEvent(final FailRecorder.FailType paramFailType, final Throwable paramThrowable)
//    {
//        if (this.configuration.getRecorderListener() != null) {
//            new Runnable()
//            {
//                public void run()
//                {
//                    ExtAudioRecorder.this.configuration.getRecorderListener().recordFailed(new FailRecorder(paramFailType, paramThrowable));
//                }
//            }.run();
//        }
//    }
//
//    private short getShort(byte paramByte1, byte paramByte2)
//    {
//        return (short)(paramByte1 | paramByte2 << 8);
//    }
//
//    private void startGetMaxAmplitudeThread()
//    {
//        if (this.configuration.getHandler() != null) {
//            new Thread(new Runnable()
//            {
//                public void run()
//                {
//                    while (ExtAudioRecorder.this.state == ExtAudioRecorder.State.RECORDING)
//                    {
//                        Message localMessage = new Message();
//                        localMessage.what = (13 * ExtAudioRecorder.this.getMaxAmplitude() / 32767);
//                        ExtAudioRecorder.this.configuration.getHandler().sendMessage(localMessage);
//                        SystemClock.sleep(100L);
//                    }
//                }
//            }).start();
//        }
//    }
//
//    public void discardRecording()
//    {
//        stop();
//        File localFile = new File(this.filePath);
//        if ((localFile.exists()) && (!localFile.isDirectory())) {
//            localFile.delete();
//        }
//    }
//
//    /* Error */
//    public int getMaxAmplitude()
//    {
//        // Byte code:
//        //   0: aload_0
//        //   1: getfield 118	com/fh/znky/utlis/ExtAudioRecorder:state	Lcom/fh/znky/utlis/ExtAudioRecorder$State;
//        //   4: getstatic 178	com/fh/znky/utlis/ExtAudioRecorder$State:RECORDING	Lcom/fh/znky/utlis/ExtAudioRecorder$State;
//        //   7: if_acmpne +37 -> 44
//        //   10: aload_0
//        //   11: getfield 50	com/fh/znky/utlis/ExtAudioRecorder:configuration	Lcom/fh/znky/utlis/AuditRecorderConfiguration;
//        //   14: invokevirtual 56	com/fh/znky/utlis/AuditRecorderConfiguration:isUncompressed	()Z
//        //   17: ifeq +15 -> 32
//        //   20: aload_0
//        //   21: getfield 41	com/fh/znky/utlis/ExtAudioRecorder:cAmplitude	I
//        //   24: istore_2
//        //   25: aload_0
//        //   26: iconst_0
//        //   27: putfield 41	com/fh/znky/utlis/ExtAudioRecorder:cAmplitude	I
//        //   30: iload_2
//        //   31: ireturn
//        //   32: aload_0
//        //   33: getfield 39	com/fh/znky/utlis/ExtAudioRecorder:mediaRecorder	Landroid/media/MediaRecorder;
//        //   36: invokevirtual 182	android/media/MediaRecorder:getMaxAmplitude	()I
//        //   39: istore_1
//        //   40: iload_1
//        //   41: ireturn
//        //   42: iconst_0
//        //   43: ireturn
//        //   44: iconst_0
//        //   45: ireturn
//        // Local variable table:
//        //   start	length	slot	name	signature
//        //   0	46	0	this	ExtAudioRecorder
//        //   39	2	1	i	int
//        //   24	7	2	j	int
//        //   42	1	3	localIllegalStateException	IllegalStateException
//        // Exception table:
//        //   from	to	target	type
//        //   32	40	42	java/lang/IllegalStateException
//    }
//
//    public State getState()
//    {
//        return this.state;
//    }
//
//    public void init(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
//    {
//        if ((!paramBoolean) || (paramInt4 == 2)) {}
//        try
//        {
//            this.samples = 16;
//            break label25;
//            this.samples = 8;
//            label25:
//            if (paramInt3 == 2) {
//                this.channels = 1;
//            } else {
//                this.channels = 2;
//            }
//            this.framePeriod = (paramInt2 * this.configuration.getTimerInterval() / 1000);
//            this.bufferSize = (2 * this.framePeriod * this.samples * this.channels / 8);
//            if (this.bufferSize < AudioRecord.getMinBufferSize(paramInt2, paramInt3, paramInt4))
//            {
//                this.bufferSize = AudioRecord.getMinBufferSize(paramInt2, paramInt3, paramInt4);
//                this.framePeriod = (this.bufferSize / (2 * this.samples * this.channels / 8));
//                String str = ExtAudioRecorder.class.getName();
//                StringBuilder localStringBuilder = new StringBuilder();
//                localStringBuilder.append("Increasing buffer size to ");
//                localStringBuilder.append(Integer.toString(this.bufferSize));
//                Log.w(str, localStringBuilder.toString());
//            }
//            AudioRecord localAudioRecord = new AudioRecord(paramInt1, paramInt2, paramInt3, paramInt4, this.bufferSize);
//            this.audioRecorder = localAudioRecord;
//            if (this.audioRecorder.getState() != 1) {
//                throw new Exception("AudioRecord initialization failed");
//            }
//            this.audioRecorder.setRecordPositionUpdateListener(this.updateListener);
//            this.audioRecorder.setPositionNotificationPeriod(this.framePeriod);
//            break label289;
//            this.mediaRecorder = new MediaRecorder();
//            this.mediaRecorder.setAudioSource(1);
//            this.mediaRecorder.setOutputFormat(1);
//            this.mediaRecorder.setAudioEncoder(1);
//            label289:
//            this.cAmplitude = 0;
//            this.filePath = null;
//            this.state = State.INITIALIZING;
//            return;
//        }
//        catch (Exception localException)
//        {
//            fireFailEvent(FailRecorder.FailType.NO_PERMISSION, localException);
//            if (localException.getMessage() != null) {
//                Log.e(ExtAudioRecorder.class.getName(), localException.getMessage());
//            } else {
//                Log.e(ExtAudioRecorder.class.getName(), "Unknown error occured while initializing recording");
//            }
//            this.state = State.ERROR;
//        }
//    }
//
//    public void prepare()
//    {
//        for (;;)
//        {
//            try
//            {
//                if (this.state == State.INITIALIZING)
//                {
//                    if (this.configuration.isUncompressed())
//                    {
//                        if (this.audioRecorder.getState() == 1)
//                        {
//                            i = 1;
//                            if (this.filePath == null) {
//                                break label425;
//                            }
//                            j = 1;
//                            if ((i & j) != 0)
//                            {
//                                this.randomAccessWriter = new RandomAccessFile(this.filePath, "rw");
//                                this.randomAccessWriter.setLength(0L);
//                                this.randomAccessWriter.writeBytes("RIFF");
//                                this.randomAccessWriter.writeInt(0);
//                                this.randomAccessWriter.writeBytes("WAVE");
//                                this.randomAccessWriter.writeBytes("fmt ");
//                                this.randomAccessWriter.writeInt(Integer.reverseBytes(16));
//                                this.randomAccessWriter.writeShort(Short.reverseBytes((short)1));
//                                this.randomAccessWriter.writeShort(Short.reverseBytes(this.channels));
//                                this.randomAccessWriter.writeInt(Integer.reverseBytes(this.configuration.getRate()));
//                                this.randomAccessWriter.writeInt(Integer.reverseBytes(this.configuration.getRate() * this.samples * this.channels / 8));
//                                this.randomAccessWriter.writeShort(Short.reverseBytes((short)(this.channels * this.samples / 8)));
//                                this.randomAccessWriter.writeShort(Short.reverseBytes(this.samples));
//                                this.randomAccessWriter.writeBytes("data");
//                                this.randomAccessWriter.writeInt(0);
//                                this.buffer = new byte[this.framePeriod * this.samples / 8 * this.channels];
//                                this.state = State.READY;
//                                return;
//                            }
//                            Log.e(ExtAudioRecorder.class.getName(), "prepare() method called on uninitialized recorder");
//                            this.state = State.ERROR;
//                            fireFailEvent(FailRecorder.FailType.UNKNOWN, null);
//                        }
//                    }
//                    else
//                    {
//                        this.mediaRecorder.prepare();
//                        this.state = State.READY;
//                    }
//                }
//                else
//                {
//                    Log.e(ExtAudioRecorder.class.getName(), "prepare() method called on illegal state");
//                    release();
//                    this.state = State.ERROR;
//                    fireFailEvent(FailRecorder.FailType.UNKNOWN, null);
//                    return;
//                }
//            }
//            catch (Exception localException)
//            {
//                if (localException.getMessage() != null) {
//                    Log.e(ExtAudioRecorder.class.getName(), localException.getMessage());
//                } else {
//                    Log.e(ExtAudioRecorder.class.getName(), "Unknown error occured in prepare()");
//                }
//                this.state = State.ERROR;
//                fireFailEvent(FailRecorder.FailType.UNKNOWN, localException);
//                return;
//            }
//            int i = 0;
//            continue;
//            label425:
//            int j = 0;
//        }
//    }
//
//    /* Error */
//    public void release()
//    {
//        // Byte code:
//        //   0: aload_0
//        //   1: getfield 118	com/fh/znky/utlis/ExtAudioRecorder:state	Lcom/fh/znky/utlis/ExtAudioRecorder$State;
//        //   4: getstatic 178	com/fh/znky/utlis/ExtAudioRecorder$State:RECORDING	Lcom/fh/znky/utlis/ExtAudioRecorder$State;
//        //   7: if_acmpne +11 -> 18
//        //   10: aload_0
//        //   11: invokevirtual 156	com/fh/znky/utlis/ExtAudioRecorder:stop	()I
//        //   14: pop
//        //   15: goto +69 -> 84
//        //   18: aload_0
//        //   19: getfield 118	com/fh/znky/utlis/ExtAudioRecorder:state	Lcom/fh/znky/utlis/ExtAudioRecorder$State;
//        //   22: getstatic 314	com/fh/znky/utlis/ExtAudioRecorder$State:READY	Lcom/fh/znky/utlis/ExtAudioRecorder$State;
//        //   25: if_acmpne +8 -> 33
//        //   28: iconst_1
//        //   29: istore_1
//        //   30: goto +5 -> 35
//        //   33: iconst_0
//        //   34: istore_1
//        //   35: iload_1
//        //   36: aload_0
//        //   37: getfield 50	com/fh/znky/utlis/ExtAudioRecorder:configuration	Lcom/fh/znky/utlis/AuditRecorderConfiguration;
//        //   40: invokevirtual 56	com/fh/znky/utlis/AuditRecorderConfiguration:isUncompressed	()Z
//        //   43: iand
//        //   44: ifeq +40 -> 84
//        //   47: aload_0
//        //   48: getfield 96	com/fh/znky/utlis/ExtAudioRecorder:randomAccessWriter	Ljava/io/RandomAccessFile;
//        //   51: invokevirtual 333	java/io/RandomAccessFile:close	()V
//        //   54: goto +15 -> 69
//        //   57: ldc 2
//        //   59: invokevirtual 205	java/lang/Class:getName	()Ljava/lang/String;
//        //   62: ldc_w 335
//        //   65: invokestatic 269	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
//        //   68: pop
//        //   69: new 158	java/io/File
//        //   72: dup
//        //   73: aload_0
//        //   74: getfield 160	com/fh/znky/utlis/ExtAudioRecorder:filePath	Ljava/lang/String;
//        //   77: invokespecial 163	java/io/File:<init>	(Ljava/lang/String;)V
//        //   80: invokevirtual 172	java/io/File:delete	()Z
//        //   83: pop
//        //   84: aload_0
//        //   85: getfield 50	com/fh/znky/utlis/ExtAudioRecorder:configuration	Lcom/fh/znky/utlis/AuditRecorderConfiguration;
//        //   88: invokevirtual 56	com/fh/znky/utlis/AuditRecorderConfiguration:isUncompressed	()Z
//        //   91: ifeq +18 -> 109
//        //   94: aload_0
//        //   95: getfield 37	com/fh/znky/utlis/ExtAudioRecorder:audioRecorder	Landroid/media/AudioRecord;
//        //   98: ifnull +25 -> 123
//        //   101: aload_0
//        //   102: getfield 37	com/fh/znky/utlis/ExtAudioRecorder:audioRecorder	Landroid/media/AudioRecord;
//        //   105: invokevirtual 336	android/media/AudioRecord:release	()V
//        //   108: return
//        //   109: aload_0
//        //   110: getfield 39	com/fh/znky/utlis/ExtAudioRecorder:mediaRecorder	Landroid/media/MediaRecorder;
//        //   113: ifnull +10 -> 123
//        //   116: aload_0
//        //   117: getfield 39	com/fh/znky/utlis/ExtAudioRecorder:mediaRecorder	Landroid/media/MediaRecorder;
//        //   120: invokevirtual 337	android/media/MediaRecorder:release	()V
//        //   123: return
//        // Local variable table:
//        //   start	length	slot	name	signature
//        //   0	124	0	this	ExtAudioRecorder
//        //   29	15	1	i	int
//        //   57	1	2	localIOException	IOException
//        // Exception table:
//        //   from	to	target	type
//        //   47	54	57	java/io/IOException
//    }
//
//    public void reset()
//    {
//        try
//        {
//            if (this.state != State.ERROR)
//            {
//                release();
//                this.filePath = null;
//                this.cAmplitude = 0;
//                if (this.configuration.isUncompressed())
//                {
//                    AudioRecord localAudioRecord = new AudioRecord(this.configuration.getSource(), this.configuration.getRate(), 1 + this.channels, this.configuration.getFormat(), this.bufferSize);
//                    this.audioRecorder = localAudioRecord;
//                    this.audioRecorder.setRecordPositionUpdateListener(this.updateListener);
//                    this.audioRecorder.setPositionNotificationPeriod(this.framePeriod);
//                }
//                else
//                {
//                    this.mediaRecorder = new MediaRecorder();
//                    this.mediaRecorder.setAudioSource(1);
//                    this.mediaRecorder.setOutputFormat(1);
//                    this.mediaRecorder.setAudioEncoder(1);
//                }
//                this.state = State.INITIALIZING;
//                return;
//            }
//        }
//        catch (Exception localException)
//        {
//            Log.e(ExtAudioRecorder.class.getName(), localException.getMessage());
//            this.state = State.ERROR;
//            fireFailEvent(FailRecorder.FailType.UNKNOWN, localException);
//        }
//    }
//
//    public void setOutputFile(String paramString)
//    {
//        try
//        {
//            if (this.state == State.INITIALIZING)
//            {
//                this.filePath = paramString;
//                if (!this.configuration.isUncompressed())
//                {
//                    this.mediaRecorder.setOutputFile(this.filePath);
//                    return;
//                }
//            }
//        }
//        catch (Exception localException)
//        {
//            if (localException.getMessage() != null) {
//                Log.e(ExtAudioRecorder.class.getName(), localException.getMessage());
//            } else {
//                Log.e(ExtAudioRecorder.class.getName(), "Unknown error occured while setting output path");
//            }
//            this.state = State.ERROR;
//            fireFailEvent(FailRecorder.FailType.UNKNOWN, localException);
//        }
//    }
//
//    public void start()
//    {
//        if (this.state == State.READY)
//        {
//            if (this.configuration.isUncompressed())
//            {
//                this.payloadSize = 0;
//                this.audioRecorder.startRecording();
//                this.audioRecorder.read(this.buffer, 0, this.buffer.length);
//            }
//            else
//            {
//                this.mediaRecorder.start();
//            }
//            this.state = State.RECORDING;
//            this.startTime = new Date().getTime();
//            startGetMaxAmplitudeThread();
//            return;
//        }
//        Log.e(ExtAudioRecorder.class.getName(), "start() called on illegal state");
//        this.state = State.ERROR;
//        fireFailEvent(FailRecorder.FailType.UNKNOWN, null);
//    }
//
//    public int stop()
//    {
//        if (this.state == State.RECORDING)
//        {
//            if (this.configuration.isUncompressed())
//            {
//                this.audioRecorder.stop();
//                try
//                {
//                    this.randomAccessWriter.seek(4L);
//                    this.randomAccessWriter.writeInt(Integer.reverseBytes(36 + this.payloadSize));
//                    this.randomAccessWriter.seek(40L);
//                    this.randomAccessWriter.writeInt(Integer.reverseBytes(this.payloadSize));
//                    this.randomAccessWriter.close();
//                }
//                catch (IOException localIOException)
//                {
//                    Log.e(ExtAudioRecorder.class.getName(), "I/O exception occured while closing output file");
//                    this.state = State.ERROR;
//                    tmpTernaryOp = localIOException;
//                }
//            }
//            try
//            {
//                this.mediaRecorder.stop();
//            }
//            catch (Exception localException) {}
//            this.state = State.STOPPED;
//            File localFile = new File(this.filePath);
//            if ((localFile.exists()) && (localFile.isFile()))
//            {
//                if (localFile.length() == 0L)
//                {
//                    localFile.delete();
//                    return 0;
//                }
//                return (int)(new Date().getTime() - this.startTime) / 1000;
//            }
//            return 0;
//        }
//        Log.e(ExtAudioRecorder.class.getName(), "stop() called on illegal state");
//        this.state = State.ERROR;
//        fireFailEvent(FailRecorder.FailType.UNKNOWN, null);
//        return 0;
//    }
//
//    public static abstract interface RecorderListener
//    {
//        public abstract void recordFailed(FailRecorder paramFailRecorder);
//    }
//
//    public static enum State
//    {
//        static
//        {
//            ERROR = new State("ERROR", 3);
//            STOPPED = new State("STOPPED", 4);
//            State[] arrayOfState = new State[5];
//            arrayOfState[0] = INITIALIZING;
//            arrayOfState[1] = READY;
//            arrayOfState[2] = RECORDING;
//            arrayOfState[3] = ERROR;
//            arrayOfState[4] = STOPPED;
//            $VALUES = arrayOfState;
//        }
//
//        private State() {}
//    }
//}
