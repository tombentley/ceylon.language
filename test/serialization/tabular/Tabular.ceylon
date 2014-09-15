import ceylon.collection {
    ArrayList,
    HashMap,
    StringBuilder
}

"A table made up of rows"
class Table(classDeclaration) satisfies Deconstructor&Deconstructed {
    shared ClassDeclaration classDeclaration;
    shared variable Table? superTable=null;
    shared String name => classDeclaration.qualifiedName;
    shared MutableMap<ValueDeclaration,Integer> indexes = HashMap<ValueDeclaration,Integer>();
    shared ArrayList<ArrayList<Object?>> rows = ArrayList<ArrayList<Object?>>();
}

"Quotes the characters in the given string. Does not enclose in double quotes."
String quoteString(String string) {
    StringBuilder sb = StringBuilder();
    for (char in string) {
        sb.append(quoteCharacter(char));
    }
    return sb.string;
}

"Quotes the given character. Does not enclose in single quotes."
String quoteCharacter(Character char) {
    switch(char)
    case ('\\', '"', '\'', ',', '\n', '\r') {
        return "\\{#``hex(char.codepoint)``}";// ceylon syntax
    }
    else {
        return char;
    }
}

"Unquotes the characters in the given string, which should be be enclosed 
 in double quotes."
String unquoteString(String string) {
    StringBuilder sb = StringBuilder();
    value iter = string.iterator();
    variable value ii = -1;
    while (true) {
        value char = iter.next();
        ii++;
        if (is Finished char) {
            break;
        } else {
            switch(char) 
            case ('\\') {
                if ('{' != iter.next()) {
                    throw Exception("expecting { following \\ ");
                }
                ii++;
                if ('#' != iter.next()) {
                    throw Exception("expecting # following \\{ ");
                }
                ii++;
                value start = ii;
                while (true) {
                    if (is Character hexDigit = iter.next()) {
                        ii++;
                        if (hexDigit == '}') {
                            sb.appendCharacter(unquoteCharacter(string, start, ii));
                            break;
                        } else if (!('0' <= hexDigit <= '9' || 'a' <= hexDigit <= 'f')) {
                            // TODO report IDE problem with brace matching here
                            throw Exception("expecting only hexadecimal digits following \\{#");
                        }
                    } else {
                        throw Exception("unterminated quoted character");
                    }
                }
            } 
            case ('"', '\'', ',', '\n', '\r') {
                throw Exception("unquoted quotable character in quoted string");
            } else {
                sb.append(char);
            }
        }
    }
    return sb.string;
}

Character unquoteCharacter(String hex, Integer start=0, Integer end=hex.size) {
    Integer? codepoint = parseHex(hex[start:end]);
    if (exists codepoint) {
        return codepoint.character;
    }
    throw Exception("");
}

"Formats a datum"
String formatDatum<Type>(Type|Reference<Type> v) {
    if (is Reference<Type> v) {
        return "@``v.id``";
    } else {
        if (is String v) {
            return "\"``quoteString(v)``\"";
        } else if (is Integer v) {
            return v.string;
        } else if (is Byte v) {
            return v.string;
        } else if (is Null v) {
            return "null";
        } else if (is Boolean v) {
            return v.string;
        } else if (is Character v) {
            return "'``quoteCharacter(v)``'";
        } else if (is Float v) {
            return v.string;
        } else {
            throw;
        }
    }
}

"Parses a datum."
Object? parseDatum(String datum) {
    if (nonempty datum) {
        value first = datum.first;
        if (first.letter) {
            if (datum=="true") {
                return true;
            } else if (datum=="false") {
                return false;
            } else if (datum=="null") {
                return null;
            } else {
                // FQ object declaration
                return findObject(datum);// TODO this function
            }
            
        } else if (first.digit) {
            // starts with number => Integer, Byte or Float
            if (datum.contains('.') || datum.contains('E')) {
                value float = parseFloat(datum)
                if (exists float) {
                    return float;
                } 
                throw Exception("invalid float: `datum`"); 
            } else {
                // TODO byte
                value i = parseInteger(datum);
                if (i exists) {
                    return i;
                }
                throw Exception("invalid float: `datum`");
            }
        } else if (first == '"') {
            if (!datum.endsWith("\"")) {
                throw Exception("unterminated string");
            }
            // starts with " => String
            return unquoteString(datum[1..datum.length-2]);
        } else if (first == '\'') {
            // starts with ' => character
            if (!datum.endsWith("\'")) {
                throw Exception("unterminated character: ``datum``");
            } else if (datum.size < 3) {
                throw Exception("invalid character: ``datum``");
            } else {
                String s = unquoteString(datum[1..datum.length-2]);
                if (s.size != 1) {
                    throw Exception("multiple characters: ``datum``");
                }
                return s;
            }
        } else {
            // TODO starts with [ => sequence ???
            // TODO starts with @ => key
            throw Exception("unhandled datum: ``datum``");
        }
    } else {
        //empty => null
        return null;
    }
}

class TableWriter(StringBuilder output) {
    shared void write(Table table) {
        writeHeader(table);
        for (Row in table) {
            writeRow(row);
        }
    }
    void writeHeader(Table table) {
        output.append("# ``table.name``").appendNewline();
        output.append("# ``table.indexes``").appendNewline();
    }
    void writeRow(Row row) {
        value iteration = row.iterator()
        variable value datum = iterator.next();
        while (true) {
            if (is Finished datum) {
                break;
            } else {
                output.append(format(datum));
            }
            datum = iterator.next();
            if (!(datum is Finished)) { 
                output.appendCharacter(',')
            }
        }
        output.appendNewline();
    }
}



class TableReader(Reader reader) {
    "Parses the first header row of a table, which is a hash (#) 
     followed by the FQ class name of the declaration the 
     table encodes
     
         # example::Person (extends superclass)?
    "
    Table parseHeader1() {
        String line = reader.readLine();
        if (!line.startsWith("#")) {
            throw Exception("Expected header row");
        }
        value classDeclarationName = line[1:].trim;
        ClassDeclaration classDeclaration;
        if (exists cd = classDeclarationFromName(classDeclarationName)) {// TODO a function to do this
            classDeclaration = cd;
        } else {
            throw Exception("class ``classDeclarationName`` cannot be found");
        }
        
        Table(classDeclaration);// TODO linking up super tables
    }
    "Parses the second header row of a table, which is a hash (#)
     followed by the names of the persisted attributes of 
     that class. 
     
         # <id>,name,spose,address
         
     attribute types are not encoded 
     (during deserialization they're obtained from the runtime metamodel)
    "
    void parseHeader2(Table table) {
        if (!line.startsWith("#")) {
            throw Exception("Expected header row");
        }
        value attributeToIndex = HashMap<ValueDeclaration, Integer>();
        value attributes = ArrayList<ValueDeclaration>();
        value attributeNames = line[1:].trim.split(",");
        variable value index = 0;
        for (attributeName in attributeNames) {
            if (exists vd=classDeclaration.getDeclaredMemberDeclaration<ValueDeclaration>(attributeName) ) {
                attributeToIndex.put(vd, index);
                attributes.add(vd);
            } else {
                throw Exception("class ``classDeclaration.qualifiedName`` lacks the attribute ``attributeName``");
            }
            index++;
        }
        table.indexes.putAll(attributeToIndex);
        table.attributes.addAll(attributes);
    }
    "Parses a row of data" 
    void parseRow() {
        String[] data = line.split(",");// this will only work if commas within datums are quoted
        if (data.size != table.attributes.size) {
            throw Exception("expected ``table.attributes.size`` values, found ``data.size``");
        }
        for (datum in data) {
            parseDatum(datum);
        }
    }
    
}

class DbReader() {
    HashMap<String, Table> tables = HashMap<String, Table>();
    // TODO read from a Reader parsing each table and adding it to tables
}

class TabularSerializer() {
    SerializationContext context = serialization();
    
    shared void add(Object instance) {
        context.reference(id, instance);
    }
    
    shared void write(Writer writer) {
        for (reference in context) {
            reference.serialize();
        }
    }
}

class TabluarDerserializer(Reader reader) {
    DeserializationContext context = deserialization();
    read(reader);
    
    void read(Reader reader) {
        // get id and class from the reader
        context.reference(id, classModel);
    }
    
    shared Object get() {
        for (reference in context) {
            reference.instance();
        }
    }
}