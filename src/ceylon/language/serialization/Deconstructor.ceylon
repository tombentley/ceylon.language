import ceylon.language.meta.model {
    Attribute,
    Type
}
import ceylon.language.meta.declaration {
    ValueDeclaration,
    TypeParameter
}

"Contract for flattening the state of an instance of a class. 
 This interface is implemented by a serialization library."
shared interface Deconstructor {
    
    "Adds the given `outer` instance (for an instance of a member class) 
     to the flattened state."
    shared formal void putOuterInstance<Instance>(Instance outerInstance);
    
    "Adds the given type argument of the given type parameter to the flattened state."
    throws (`class AssertionError`,
        "if there is already a value for the given type argument")
    shared formal void putTypeArgument(TypeParameter typeParameter, Type typeArgument);
    
    "Adds the value of the given attribute to the flattened state."
    throws (`class AssertionError`,
        "if there is already a value for the given attribute")
    shared formal void putValue<Instance>(ValueDeclaration attribute, Instance referenced);
    
    shared formal void putElement<Instance>(Integer index, Instance referenced);
}

