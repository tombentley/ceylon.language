package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Object;
import com.redhat.ceylon.compiler.java.metadata.Transient;
import com.redhat.ceylon.compiler.java.metadata.ValueType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 7) 
@Object
@Class(extendsType = "ceylon.language::Boolean")
@ValueType
public final class false_ extends Boolean {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(false_.class);
    
    private final static false_ value = new false_();

    public static false_ get_(){
        return value;
    }

    @Override
    @Ignore
    public boolean booleanValue() {
        return false;
    }
    
    @Override
    @Transient
    public java.lang.String toString() {
        return "false";
    }

    @Override
    @Ignore
    public TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
}
