import ceylon.language.meta.model {
    Attribute
}
import ceylon.language.meta.declaration {
    ValueDeclaration
}

/*"The flattened state of an instance of [[Class]]."
shared interface Deconstructed<Instance>
/*satisfies {[Attribute<Instance>, Anything]*}*/ {
    /*
    "Get the value of the given attribute."
    throws (`class AssertionError`,
        "if the value is missing")
    shared formal
    Type|Reference<Type> get<Type>(
        Attribute<Instance,Type> attribute);
        */
}*/

"Contract for flattening the state of an instance of a class."
shared interface Deconstructor {
    "Adds the value the given attribute to the flattened state"
    throws (`class AssertionError`,
        "if there is already a vale for the given attribute")
    shared formal void put<Type>(ValueDeclaration attribute, Type|Reference<Type> valueOrReference);
}

"The flattened state of an instance of a class."
shared interface Deconstructed
        satisfies {[ValueDeclaration, Anything]*} {
    "Get the value of the given attribute."
    throws (`class AssertionError`,
        "if the value is missing")
    shared formal Type|Reference<Type> get<Type>(ValueDeclaration dec);
}
