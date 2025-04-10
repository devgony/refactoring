> Dealing with Inheritance

- 다른 강력한 메카니즘처럼 유용하면서도 오용하기 쉬움
- 백미러를 바라보기 전까지는 오용인줄 모름
- 종종 기능들은 상속 계층구조 에서 올라가거나 내려 가야한다
  - Pull Up Method, Pull Up Field, Pull Up Constructor Body, Push Down Method, and Push Down Field
  - 계층 구조에서 class 제거: Extract Superclass, Remove Subclass, and Collapse Hierarchy
  - field 를 subclass 로 변경: Replace Type Code with Subclasses
- 잘못된 위치에서 상속을 사용할 때: Replace Subclass with Delegate or Replace Superclass with Delegate

1. [Pull Up Method](./12-dealing-with-inheritance/01-pull-up-method.md)

2. [Pull Up Field](./12-dealing-with-inheritance/02-pull-up-field.md)

3. [Pull Up Constructor Body](./12-dealing-with-inheritance/03-pull-up-constructor-body.md)

4. [Push Down Method](./12-dealing-with-inheritance/04-push-down-method.md)

5. [Push Down Field](./12-dealing-with-inheritance/05-push-down-field.md)

6. [Replace Type Code with Subclasses](./12-dealing-with-inheritance/06-replace-type-code-with-subclasses.md)

   - Class 전체를 직접 상속하는 경우 간단하지만 변화에 취약하여 type code 가 mutable 이면 쓸 수 없다
   - Type 만 간접 상속하는 경우 type code 를 객체로 감싸고 해당 Type 에 대한 Subclass 로 분리

- superclass 에서 type code 와 type getter 를 제거
  - java 의 경우 super 에서 type getter 를 제거하면 super의 생성자에서 sub의 type() 을 호출 못한다?

```diff
class Employee…
  constructor(name, type){
    this.validateType(type);
    this._name = name;
-   this._type = type;
  }

- get type() {return this._type;}
  toString() {return `${this._name} (${this.type})`;}
```

7. [Remove Subclass](./12-dealing-with-inheritance/07-remove-subclass.md)

8. [Extract Superclass](./12-dealing-with-inheritance/08-extract-superclass.md)

9. [Collapse Hierarchy](./12-dealing-with-inheritance/09-collapse-hierarchy.md)

10. [Replace Subclass with Delegate](./12-dealing-with-inheritance/10-replace-subclass-with-delegate.md)
    > Favor a judicious mixture of composition and inheritance over either alone

- 그래도 저자는 일단 상속하고 필요할 때 위임을 하라는데, 최근에 나온 언어들은 상속을 아예 지원하지 않는다.

  - 그럼 Java 에서도 반대로 일단 Composition 으로 구성하고 지저분하면 나중에 상속을 하면 안되나?

- data 를 필드에 할당하지 않는데 생성자에서 받는 이유?

```diff
+class SpeciesDelegate {
+  constructor(data, bird) {
+    this._bird = bird;
+  }
+  get plumage() {
+    return this._bird._plumage || "average";
+  }

+class EuropeanSwallowDelegate extends SpeciesDelegate {

+class AfricanSwallowDelegate extends SpeciesDelegate {
  constructor(data, bird) {
+   super(data,bird);
    this._numberOfCoconuts = data.numberOfCoconuts;
  }

+class NorwegianBlueParrotDelegate extends SpeciesDelegate {
  constructor(data, bird) {
+   super(data, bird);
    this._voltage = data.voltage;
    this._isNailed = data.isNailed;
  }
```

11. [Replace Superclass with Delegate](./12-dealing-with-inheritance/11-replace-superclass-with-delegate.md)

- 함수의 superclass 가 subclass 에서 의미가 없다면 상속을 하지 말아야 한다는 신호
- 하지만 super 와 sub 모두에 forwarding 함수를 작성해야 한다는 단점이 있다
