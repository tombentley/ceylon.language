import ceylon.language.meta {
    type,
    optionalAnnotation
}
import ceylon.language.meta.model {
    ClassModel
}
import ceylon.language.meta.declaration {
    ValueDeclaration
}
import ceylon.language.serialization {
    serialization,
    Deconstructor,
    Deconstructed,
    Reference,
    SerializationContext
}

class JsonSerializer(void write(String s)) satisfies Deconstructor {
    
    SerializationContext context = serialization(this);
    
    variable value id = 0;
    
    "Serialize the given instance and the instances reachable from it"
    shared void serialize(Anything instance) {
        if (!(instance in context)) { // do nothing if it's already included
            ClassModel clazz = type(instance);
            //assert (optionalAnnotation(`SerializableAnnotation`, clazz.declaration) exists);
            // give it an id
            context.reference(id++, instance);
            // TODO we need to find the instances reachable from it, but that's equivalent to askinf for its state.
        }
    }
    
    void close() {
        for (reference in context) {
            reference.serialize();
        }
    }
    
    shared actual void put<Type>(ValueDeclaration attribute, Type|Reference<Type> valueOrReference) {
        /*if (is Reference<Type> ref = valueOrReference) {
            ref.serialize();
        }*/
        if (is Type v = valueOrReference) {
            attribute.name = nothing;
        } else {
            // it's a reference, but we want the instance so we can deep copy it
            v.instance
        }
    }
}

class JsonDehydrated() satisfies Deconstructed {
    shared actual Type|Reference<Type> get<Type>(ValueDeclaration attribute) {
        return nothing;
    }
}
