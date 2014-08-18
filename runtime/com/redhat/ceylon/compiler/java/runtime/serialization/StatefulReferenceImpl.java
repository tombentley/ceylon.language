package com.redhat.ceylon.compiler.java.runtime.serialization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedClass;
import com.redhat.ceylon.compiler.java.runtime.metamodel.AppliedMemberClass;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.AssertionError;
import ceylon.language.Empty;
import ceylon.language.Tuple;
import ceylon.language.impl.BaseIterable;
import ceylon.language.impl.BaseIterator;
import ceylon.language.impl.rethrow_;
import ceylon.language.meta.declaration.ValueDeclaration;
import ceylon.language.meta.model.ClassModel;
import ceylon.language.serialization.Reference;
import ceylon.language.serialization.StatefulReference;
import ceylon.language.serialization.Deconstructor;
import ceylon.language.serialization.Deconstructed;

class SerializingStatefulReference<Instance> 
        implements StatefulReference<Instance>, ReifiedType {
    private final TypeDescriptor reified$Instance;
    private final Object id;
    private final Instance instance;
    private final Deconstructor deconstructor;
    
    SerializingStatefulReference(TypeDescriptor reified$Instance, SerializationContextImpl context, Object id, Instance instance, Deconstructor deconstructor) {
        this.reified$Instance = reified$Instance;
        this.id = id;
        this.instance = instance;
        this.deconstructor = deconstructor;
    }
    
    public String toString() {
        return id +"=>" + instance;
    }
    
    @Override
    public /*Deconstructed<Instance>*/ Object serialize() {
        ((Serializable)this.instance).$serialize$(deconstructor);
        return null;
    }

    @Override
    public Instance instance() {
        return instance;
    }
    
    /** 
     * Construct the instance (by calling its constructor) and set 
     * its value-typed attributes. 
     * Does not set the reference-typed attributes
     */
    @Override
    public Object reconstruct() {
        // XXX no op because we're for serialization and this only makes sense for deserialization?
        return null;
    }
    

    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(SerializingStatefulReference.class, reified$Instance);
    }

    @Override
    public Object getId() {
        return id;
    }
}


class DeserializingStatefulReference<Instance> 
        implements StatefulReference<Instance>, ReifiedType {
    
    private final TypeDescriptor reified$Instance;
    private final DeserializationContextImpl context;
    private final Object id;
    private final Instance instance;
    
    private Deconstructed deconstructed;
    private ReconstructionState state = ReconstructionState.UNINITIALIZED;
    
    static enum ReconstructionState {
        /** 
         * {@link DeserializingStatefulReference#instance} is uninitialized 
         * ({@link Serializable#$deserialize$(Deconstructed)} has not yet 
         * been called).
         */
        UNINITIALIZED,
        /** 
         * {@link DeserializingStatefulReference#instance} is initialized 
         * ({@link Serializable#$deserialize$(Deconstructed)} has been 
         * called). Instances reachable from {@code instance} are in 
         * an unknown state.
         */
        UNINITIALIZED_REFS,
        /**
         * {@link DeserializingStatefulReference#instance} is initialized 
         * ({@link Serializable#$deserialize$(Deconstructed)} has been 
         * called) and we're in the process of ensuring the instances 
         * reachable from {@code instance} are also initialized.
         */
        INITIALIZING_REFS,
        /**
         * {@link DeserializingStatefulReference#instance} and everything 
         * reacahable from it is initialized.
         */
        INITIALIZED
    }
    
    /**
     * Create a stateful reference to the instance with 
     * the given {@code id} and {@code clazz}.
     * 
     * An instance of {@code clazz} is instantiated and associated with the 
     * {@code context}. The {@code deconstructed} is kept for use later by 
     * {@link #reconstruct()}.
     *  
     * @param reified$Instance
     * @param context
     * @param id
     * @param clazz
     * @param deconstructed
     */
    DeserializingStatefulReference(TypeDescriptor reified$Instance, 
            DeserializationContextImpl context, Object id, 
            ClassModel classModel, 
            Deconstructed deconstructed) {
        this.reified$Instance = reified$Instance;
        this.context = context;
        this.id = id;
        this.deconstructed = deconstructed;
        java.lang.Class<Instance> clazz;
        TypeDescriptor[] typeArguments = ((TypeDescriptor.Class)((ReifiedType)classModel).$getType$()).getTypeArguments();
        if (classModel instanceof AppliedClass) {
            clazz = (java.lang.Class)((TypeDescriptor.Class)typeArguments[0]).getKlass();
        } else if (classModel instanceof AppliedMemberClass) {
            clazz = (java.lang.Class)((TypeDescriptor.Class)typeArguments[1]).getKlass();
        } else {
            throw new AssertionError("unexpected class model: " 
                    + (classModel != null ? classModel.getClass().getName() : "null"));
        }
        
        try {
            Constructor<Instance> ctor = clazz.getDeclaredConstructor($Serialization$.class);
            ctor.setAccessible(true);
            instance = ctor.newInstance(new Object[]{null});// Pass a null $Serialization$
            context.put(id, (DeserializingStatefulReference)this);
        } catch (NoSuchMethodException e) {
            throw new AssertionError("class is not serializable " + classModel);
        } catch (InvocationTargetException e) {
            throw new AssertionError("error thrown during instantiation of " + classModel+ (e.getMessage() != null ? ": " + e.getMessage() : ""));
        } catch (SecurityException e) {
            // Should never happen
            throw new RuntimeException(e);
        } catch (InstantiationException|IllegalAccessException|IllegalArgumentException e) {
            // Should never happen
            throw new RuntimeException(e);
        }
    }
    
    public String toString() {
        if (state == ReconstructionState.INITIALIZED) {
            return id +"<=" + instance;
        } else {
            return id +"<=(" + deconstructed + ")";
        }
    }
    
    
    @Override
    public /*Deconstructed<Instance>*/ Object serialize() {
        // TODO What does this mean in the context of derserialization?
        throw new AssertionError("WTF?");
        //return DeconstructedImpl.forSer(context, id, instance);
    }

    @Override
    public Instance instance() {
        // !!!!! XXX MUST NOT LEAK PARTIALLY BUILT OBJECTS
        // XXX HERE we must ensure that this.instance has been 
        // reconstructed, but also that everything it references 
        // (transitively) has been reconstructed too.
        // We can do this by inspecting this.deconstructed finding the 
        // references (from this.context) and ensuring those are 
        // reconstructed.
        reconstruct();
        if (state == ReconstructionState.UNINITIALIZED_REFS) {
            state = ReconstructionState.INITIALIZING_REFS;
            try {
                // for each reference in deconstructed
                Reference ref = null;
                DeserializingStatefulReference<Object> sr = context.getReference(ref.getId());
                sr.instance();
            } catch (java.lang.Throwable t) {
                state = ReconstructionState.UNINITIALIZED_REFS;
                rethrow_.rethrow(t);
            }
            state = ReconstructionState.INITIALIZED;
            deconstructed = null;
        }
        return instance;
    }
    
    /**
     * Reconstruct the instance according to the {@link #deconstructed} 
     * passed to our constructor. 
     * 
     * The {@link Deconstructed} passed to 
     * {@link Serializable#$deserialize$(Deconstructed)} 
     * will replace references by their (possibly unreconstructed) 
     * instances. This means {@link Serializable#$deserialize$(Deconstructed)} 
     * can see unreconstructed instances, but since they're generated by 
     * the compiler they shouldn't leak to user code.
     */
    @Override
    public Object reconstruct() {
        if (state == ReconstructionState.UNINITIALIZED) {
            // TODO synchronization?
            class DereferencingDeconstructed 
                    extends BaseIterable<Tuple<Object,? extends ValueDeclaration,? extends Tuple<Object,? extends Object,? extends Empty>>, Object>
                    implements Deconstructed {
                DereferencingDeconstructed() {
                    super(null, null);// TODO TypeDescriptors
                }
                @Override
                public <Type> Object get(TypeDescriptor arg0, ValueDeclaration arg1) {
                    Object valueOrReference = deconstructed.get(arg0, arg1);
                    if (valueOrReference instanceof Reference) {
                        Object referredId = ((Reference<?>) valueOrReference).getId();
                        DeserializingStatefulReference<Object> reference = context.getReference(referredId);
                        if (reference == null) {
                            throw new AssertionError("reference to unregistered id: " + referredId);
                        }
                        return reference.instance;
                    } else {
                        return valueOrReference;
                    }
                }
                public ceylon.language.Iterator<Tuple<Object,? extends ValueDeclaration,? extends Tuple<Object,? extends Object,? extends Empty>>> iterator() {
                    return new BaseIterator<Tuple<Object,? extends ValueDeclaration,? extends Tuple<Object,? extends Object,? extends Empty>>>(null) {
                        ceylon.language.Iterator iter = deconstructed.iterator();
                        public Object next() {
                            return iter.next();
                        }
                    };
                }
            }
            ((Serializable)this.instance).$deserialize$(new DereferencingDeconstructed());
            state = ReconstructionState.UNINITIALIZED_REFS;
        }
        return null;
    }

    @Override
    public Object getId() {
        return id;
    }
    
    @Override
    public TypeDescriptor $getType$() {
        return TypeDescriptor.klass(DeserializingStatefulReference.class, reified$Instance);
    }
}