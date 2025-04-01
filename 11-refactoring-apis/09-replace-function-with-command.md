> Replace Function with Command

```js
function score(candidate, medicalExam, scoringGuide) {
  let result = 0;
  let healthLevel = 0;
  // long body code
}
```

👇

```js
class Scorer {
  constructor(candidate, medicalExam, scoringGuide) {
    this._candidate = candidate;
    this._medicalExam = medicalExam;
    this._scoringGuide = scoringGuide;
  }

  execute() {
    this._result = 0;
    this._healthLevel = 0;
    // long body code
  }
}
```

# Motivation

- 함수는 때로는 객체에 캐슐화 되어있을 때 유용하다
- Command object (or just Command): 하나의 메서드만을 가지며, 그 메서드의 실행이 유일한 목적인 객체
- 일반 함수보다 유연성을 제공

  - 실행 퀴소와 같은 보완적인 작업 가능
  - 풍부한 수명 주기를 위해 인자를 구축하는 메서드 제공 가능
  - 상속과 훅을 통해 사용자 정의 기능 구축
  - 일급함수가 없는 언어인 경우 Command 를 대신 사용 가능
  - 중첩함수가 없는 언어에서 메서드와 필드를 사용하여 복잡한 함수 분해 가능
  - 테스트와 디버깅 중에 해당 메서드를 직접 호출 가능

- 하지만 풍부한 유연성은 복잡성을 동반함
  - 95% 의 경우에는 일급함수를 쓰는게 나음
  - 훨씬 더 복잡한 기능을 제공해야 할 경우만 Command 를 사용

# Mechanics

- 기존 함수를 위한 빈 class 생성
- `Move Function`: 기존 함수를 새로운 class 로 이동
- 각 인자에 대해 필드와 생성자로 변경하는 것을 고려

# Example

```js
function score(candidate, medicalExam, scoringGuide) {
  let result = 0;
  let healthLevel = 0;
  let highMedicalRiskFlag = false;

  if (medicalExam.isSmoker) {
    healthLevel += 10;
    highMedicalRiskFlag = true;
  }
  let certificationGrade = "regular";
  if (scoringGuide.stateWithLowCertification(candidate.originState)) {
    certificationGrade = "low";
    result -= 5;
  }
  // lots more code like this
  result -= Math.max(healthLevel - 5, 0);
  return result;
}
```

- `Move Function`: 함수 전체를 새 class 내로 이동

```diff
function score(candidate, medicalExam, scoringGuide) {
+ return new Scorer().execute(candidate, medicalExam, scoringGuide);
}

+class Scorer {
+  execute(candidate, medicalExam, scoringGuide) {
    let result = 0;
    let healthLevel = 0;
    let highMedicalRiskFlag = false;

    if (medicalExam.isSmoker) {
      healthLevel += 10;
      highMedicalRiskFlag = true;
    }
    let certificationGrade = "regular";
    if (scoringGuide.stateWithLowCertification(candidate.originState)) {
      certificationGrade = "low";
      result -= 5;
    }
    // lots more code like this
    result -= Math.max(healthLevel - 5, 0);
    return result;
  }
}
```

- 인자를 필드와 생성자로 이동

```diff
  function score(candidate, medicalExam, scoringGuide) {
+   return new Scorer(candidate, medicalExam, scoringGuide).execute();
  }

class Scorer…
+ constructor(candidate, medicalExam, scoringGuide){
+   this._candidate = candidate;
+   this._medicalExam = medicalExam;
+   this._scoringGuide = scoringGuide;
  }
```

- 모든 로컬 변수들을 필드로 이동

```diff
class Scorer…
  ..
  execute () {
+   this._result = 0;
+   this._healthLevel = 0;
+   this._highMedicalRiskFlag = false;

    if (this._medicalExam.isSmoker) {
+     this._healthLevel += 10;
+     this._highMedicalRiskFlag = true;
    }
+   this._certificationGrade = "regular";
    if (this._scoringGuide.stateWithLowCertification(this._candidate.originState)) {
+     this._certificationGrade = "low";
+     this._result -= 5;
    }
    // lots more code like this
+   this._result -= Math.max(this._healthLevel - 5, 0);
+   return this._result;
  }
```

- 이제 변수의 범위에 얽매이지 않고 자유롭게 리팩터링 가능: `Extract Function`

```diff
class Scorer…
  execute () {
    this._result = 0;
    this._healthLevel = 0;
    this._highMedicalRiskFlag = false;

+   this.scoreSmoking();
    this._certificationGrade = "regular";
    if (this._scoringGuide.stateWithLowCertification(this._candidate.originState)) {
      this._certificationGrade = "low";
      this._result -= 5;
    }
    // lots more code like this
    this._result -= Math.max(this._healthLevel - 5, 0);
    return this._result;
    }

+ scoreSmoking() {
    if (this._medicalExam.isSmoker) {
      this._healthLevel += 10;
      this._highMedicalRiskFlag = true;
    }
  }
```

- nested function 도 유사한 동작을 하지만 Command 의 경우 테스트와 디버깅에 더 용이하다는 장점이 있다
