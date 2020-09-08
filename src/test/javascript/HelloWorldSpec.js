describe("Hello world", function() {
    it("says Hello", function() {
        expect(helloWorld()).toEqual("Hello world!");
    });

     it("has world in it", function() {
        expect(helloWorld()).toContain("world");
    });
});

describe("Person", function() {
	// let's say we want to make sure it calls the sayHello() function when we call the helloSomeone() function
    it("calls the sayHello() function", function() {
        var fakePerson = new Person();
        spyOn(fakePerson, "sayHello");
        fakePerson.helloSomeone("Bobak");
        expect(fakePerson.sayHello).toHaveBeenCalled();
    });

    // to make sure that helloSomeone is called with "Bobak" as its argument
    it("greets Bobak", function() {
        var fakePerson = new Person();
        spyOn(fakePerson, "helloSomeone");
        fakePerson.helloSomeone("Bobak");
        expect(fakePerson.helloSomeone).toHaveBeenCalledWith("Bobak");
    });


});