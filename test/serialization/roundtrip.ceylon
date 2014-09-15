import ceylon.language.serialization{
    serialization,
    deserialization,
    SerializationContext,
    DeserializationContext
}

import json{
    JsonSerializer
}

import person{
    Person, 
    Address
}

@test
shared void jsonRoundTrip() {
    // very simple graph
    Address home = Address("121B Baker Street", "London");
    Person sherlock = Person("sherlock holmes", home);
    
    // serialize it to a String
    value StringBuilder sb = StringBuilder();
    value jsonSer = JsonSerializer(sb.append);
    jsonSer.serialize(sherlock);
    jsonSer.close();
    
    String json = sb.string;
    // {@type="person::Person", name="Sherlock Holmes", address={@type="person::Address", lines=["121B Baker Street", "London"]}}
    print(json);
    
    // deserialize it from the string
    value jsonDeser = JsonDeserializer(json);
    
    jsonDeser.parse(json);
    Person sherlock2 = jsonDeser.instance();
    
    // Check we read something equal (but different)
    assert(sherlock !== sherlock2);
    assert(sherlock.name = sherlock2.name);
    assert(sherlock.address.lines !== sherlock2.address.lines);
    assert(sherlock.address.lines == sherlock2.address.lines);
    
}
