doc "Annotation to mark a type or member as shared. A `shared` 
     member is visible outside the block of code in which it
     is declared."
shared Null shared() { return null; }

doc "Annotation to mark an value as variable. A `variable` 
     value must be assigned multiple times." 
shared Null variable() { return null; }

doc "Annotation to mark a class as abstract. An `abstract` 
     class may not be directly instantiated. An `abstract`
     class may have enumerated cases."
shared Null abstract() { return null; }

doc "Annotation to mark a class as final. A `final` class 
     may not be extended."
shared Null final() { return null; }

doc "Annotation to mark a member of a type as refining a 
     member of a supertype."
shared Null actual() { return null; }

doc "Annotation to mark a member whose implementation must 
     be provided by subtypes."  
shared Null formal() { return null; }

doc "Annotation to mark a member whose implementation may be 
     refined by subtypes. Non-`default` declarations may not 
     be refined."
shared Null default() { return null; }

doc "Annotation to disable definite initialization analysis
     for a reference."  
shared Null late() { return null; }

doc "Annotation to mark a member whose implementation is 
     be provided by platform-native code."  
shared Null native() { return null; }

doc "Annotation to specify API documentation of a program
     element." 
shared Null doc(String description) { return null; }

doc "Annotation to specify API references to other related 
     program elements."
shared Null see(Anything* programElements) { return null; }

doc "Annotation to specify API authors."
shared Null by(String* authors) { return null; }

doc "Annotation to mark a program element that throws an 
     exception."
shared Null throws(Anything type, String? when=null) { return null; }

doc "Annotation to mark program elements which should not be 
     used anymore."
shared Null deprecated(String? reason=null) { return null; }

doc "Annotation to categorize the API by tag." 
shared Null tagged(String* tags) { return null; }

doc "Annotation to specify the URL of the license of a module 
     or package." 
shared Null license(String url) { return null; }

doc "Annotation to specify that a module can be executed 
     even if the annotated dependency is not available."
shared Null optional() { return null; }
