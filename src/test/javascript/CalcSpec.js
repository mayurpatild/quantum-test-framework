describe("Calculator", function() {
  describe("Addition function", function() {
    it("should add numbers", function() {
      expect(add(1, 1)).toBe(2);
      expect(add(2, 2)).toBeGreaterThan(3);
    });
  });
});