# Chapter01

- 아래와 같은 상황에서 별도 데이터 구조에 담으려고 했더니 partial move 를 체크해줘서 편했다. 다른 언어였으면 불필요한 parameter 를 넣고있는지 식별하기가 어려웠을듯함

```rust
pub fn statement(invoice: Invoice, plays: HashMap<&str, Play>) -> String {
│ let statement*data = StatementData {
│ │ customer: invoice.customer,
│ │ performances: invoice.performances, ● value partially moved here
│ };
│ render_plain_text(statement_data, invoice, plays) ● use of partially moved value: `invoice` partial move occurs because `invoice.performances` has type `std::vec::Vec<statement::Performance<'*>>`, which does not implement
}
```
