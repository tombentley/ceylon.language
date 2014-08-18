package ceylon.language.serialization;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.runtime.serialization.DeserializationContextImpl;

import ceylon.language.serialization.DeserializationContext;
import ceylon.language.serialization.SerializationContext;

@Ceylon(major=7, minor=0)
@Method
public class deserialization_ {
    private deserialization_(){}
    
    public static DeserializationContext deserialization() {
        return new DeserializationContextImpl();
    }
}