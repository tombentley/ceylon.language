package ceylon.language.serialization;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.runtime.serialization.SerializationContextImpl;

import ceylon.language.serialization.SerializationContext;
import ceylon.language.serialization.Deconstructor;
import ceylon.language.serialization.Deconstructed;

@Ceylon(major=7, minor=0)
@Method
public class serialization_ {
    private serialization_(){}
    
    public static SerializationContext serialization(Deconstructor deconstructor) {
        return new SerializationContextImpl(deconstructor);
    }
}