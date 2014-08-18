package com.redhat.ceylon.compiler.java.runtime.serialization;

import java.lang.reflect.Constructor;

import com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedClass;
import com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedMemberClass;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.meta.model.ClassModel;
//import ceylon.language.serialization.Deconstructed;
import ceylon.language.serialization.StatefulReference;
import ceylon.language.serialization.StatelessReference;
import ceylon.language.serialization.Deconstructor;
import ceylon.language.serialization.Deconstructed;

class StatelessReferenceImpl<Instance> 
        implements StatelessReference<Instance>, ReifiedType {
    
    private final TypeDescriptor reified$Instance;
    private final DeserializationContextImpl context;
    private final Object id;
    private final ClassModel classModel;
    
    StatelessReferenceImpl(TypeDescriptor reified$Instance, 
            DeserializationContextImpl context, 
            Object id, 
            ClassModel classModel) {
        this.reified$Instance = reified$Instance;
        this.context = context;
        this.id = id;
        this.classModel = classModel;
        
    }
    
    @Override
    public Object getId() {
        return id;
    }
    
    /**
     * Instantiates and returns a {@link DeserializingStatefulReference}
     */
    @Override
    public StatefulReference<Instance> deserialize(Deconstructed deconstructed) {
        return new DeserializingStatefulReference<Instance>(reified$Instance, context, id, classModel, deconstructed);
    }
    
    public String toString() {
        return id +"<-"+classModel;
    }

    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(StatelessReferenceImpl.class, reified$Instance);
    }
    
}