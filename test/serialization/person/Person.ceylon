shared serializable
class Person(name, address) {
    shared variable String name;
    shared variable Person? spouse;
    shared variable Address address;
}
shared serializable
class EmployedPerson() extends Person() {
    shared variable Organization employer;
}
shared serializable
class Organization(name) {
    shared String name;
}
shared serializable
class Address(lines) {
    shared String[] lines;
}
