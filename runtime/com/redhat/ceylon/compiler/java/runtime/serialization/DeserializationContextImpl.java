package com.redhat.ceylon.compiler.java.runtime.serialization;

import java.util.HashMap;
import java.util.Map;

import ceylon.language.AssertionError;
import ceylon.language.Iterator;
import ceylon.language.Null;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterable;
import ceylon.language.meta.model.ClassModel;
import ceylon.language.serialization.DeserializationContext;
import ceylon.language.serialization.Reference;
import ceylon.language.serialization.StatelessReference;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major=7, minor=0)
@Class
@SatisfiedTypes("ceylon.language.serialization::DeserializationContext")
public class DeserializationContextImpl 
    extends BaseIterable<Reference<Object>, Object> 
    implements DeserializationContext, ReifiedType {

    private final Map<Object, DeserializingStatefulReference<Object>> idToReference = new HashMap<>();
    
    public DeserializationContextImpl() {
        super(ceylon.language.Object.$TypeDescriptor$, Null.$TypeDescriptor$);
    }
    
    @Override
    public <Instance> StatelessReference<Instance> reference(
            TypeDescriptor reified$Instance, 
            Object id,
            ClassModel classModel) {
        if (idToReference.containsKey(id)) {
            throw new ceylon.language.AssertionError("id already has a reference");
        }
        if (classModel.getDeclaration().getAbstract()) {
            throw new ceylon.language.AssertionError("class is abstract: " + classModel);
        }
        return new StatelessReferenceImpl<Instance>(reified$Instance, this, id, classModel);
    }

    /**
     * Registers a reference to against its id.
     */
    void put(Object id, DeserializingStatefulReference<Object> reference) {
        idToReference.put(id, reference);
    }

    /**
     * Returns the reference to the instance with the given id. 
     * Never returns null.
     */
    DeserializingStatefulReference<Object> getReference(Object id) {
        DeserializingStatefulReference<Object> reference = idToReference.get(id);
        if (reference == null) {
            throw new AssertionError("cannot obtain reference to unregistered id: " + id);
        }
        return reference;
    }

    @Override
    public Iterator<? extends Reference<Object>> iterator() {
        return new Iterator<Reference<Object>>() {
            private final java.util.Iterator<DeserializingStatefulReference<Object>> iter = idToReference.values().iterator();
            @Override
            public Object next() {
                if (iter.hasNext()) {
                    return iter.next();
                } else {
                    return finished_.get_();
                }
            }
        };
    }
    
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(DeserializationContextImpl.class);
    }

}