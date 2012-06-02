package com.redhat.ceylon.compiler.java;

import java.lang.invoke.CallSite;
import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class Bsms {

    private Bsms(){}
    
    public static CallSite bsm(MethodHandles.Lookup lookup, String methodName, MethodType methodType, String sa1) throws Exception {
        System.err.println("bsm");
        System.err.println(" lookup:   " +lookup);
        System.err.println(" methodName:  "+ methodName);
        System.err.println(" methodType:  "+ methodType);
        System.err.println(" sa1: "+ sa1);
        MethodHandle mh = lookup.findVirtual(lookup.lookupClass(), "m", MethodType.methodType(Void.TYPE, new Class[]{String.class}));
        
        mh = MethodHandles.insertArguments(mh, 1, sa1);
        mh = MethodHandles.dropArguments(mh, 1, String.class);
        mh = MethodHandles.dropArguments(mh, 2, Boolean.TYPE);
        final ConstantCallSite cs = new ConstantCallSite(mh);
        return cs;
    }
    
}
