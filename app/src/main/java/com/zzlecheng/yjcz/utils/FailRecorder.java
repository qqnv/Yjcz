//package com.zzlecheng.yjcz.utils;
//
//public class FailRecorder
//{
//    private final Throwable cause;
//    private final FailType type;
//
//    public FailRecorder(FailType paramFailType, Throwable paramThrowable)
//    {
//        this.type = paramFailType;
//        this.cause = paramThrowable;
//    }
//
//    public Throwable getCause()
//    {
//        return this.cause;
//    }
//
//    public FailType getType()
//    {
//        return this.type;
//    }
//
//    public static enum FailType
//    {
//        static
//        {
//            FailType[] arrayOfFailType = new FailType[2];
//            arrayOfFailType[0] = NO_PERMISSION;
//            arrayOfFailType[1] = UNKNOWN;
//            $VALUES = arrayOfFailType;
//        }
//
//        private FailType() {}
//    }
//}
