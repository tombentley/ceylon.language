doc "A fixed-size array of elements. An array may have zero
     size (an empty array). Arrays are mutable. Any element
     of an array may be set to a new value.
     
     This class is provided primarily to support interoperation 
     with Java, and for some performance-critical low-level 
     programming tasks."
shared abstract native class Array<Element>() 
        extends Object()
        satisfies List<Element> &
                  Cloneable<Array<Element>> &
                  Ranged<Integer, Array<Element>> {

    doc "Replace the existing element at the specified index 
         with the given element. Does nothing if the specified 
         index is negative or larger than the index of the 
         last element in the array."
    shared formal void set(
            doc "The index of the element to replace."
            Integer index, 
            doc "The new element."
            Element element);
    
    doc "Reverse this array, returning a new array."
    shared actual formal Array<Element> reversed;

    doc "The rest of the array, without the first element."
    shared actual formal Array<Element> rest;

    doc "Efficiently copy the elements in the segment 
         `sourcePosition:length` of this array to the segment 
         `destinationPosition:length` of the given array."
    shared formal void copyTo(
        doc "The array into which to copy the elements." 
        Array<Element> other,
        doc "The index of the first element in this array to copy."  
        Integer sourcePosition = 0, 
        doc "The index in the given array into which to 
             copy the first element."
        Integer destinationPosition = 0, 
        doc "The number of elements to copy."
        Integer length = size);
    
}

doc "Create an array containing the given elements. If no
     elements are provided, create an empty array of the
     given element type."
shared native Array<Element> array<Element>({Element*} elements);

doc "Create an array of the specified size, populating every 
     index with the given element. If the specified size is
     smaller than `1`, return an empty array of the given
     element type."
shared native Array<Element> arrayOfSize<Element>(
        doc "The size of the resulting array. If the size
             is non-positive, an empty array will be 
             created."
        Integer size,
        doc "The element value with which to populate the
             array. All elements of the resulting array 
             will have the same value." 
        Element element);

