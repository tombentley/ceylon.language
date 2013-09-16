
"An annotation that may occur multiple times
 at a single program element."
shared interface SequencedAnnotation<out Value, in ProgramElement=Annotated>
        of Value
        satisfies ConstrainedAnnotation<Value,Value[],ProgramElement>
        given Value satisfies Annotation<Value>
        given ProgramElement satisfies Annotated {}
