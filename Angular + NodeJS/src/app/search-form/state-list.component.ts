import { Component, EventEmitter, Output, OnInit, Input } from '@angular/core';
@Component({
  selector: 'app-state-list',
  templateUrl: './state-list.component.html'
})
export class StateListComponent implements OnInit {
  state = '';
  @Output() foo = new EventEmitter<string>();
  @Input('inputDisabled') inputDisabled: string;
  output_state() {
    this.foo.emit(this.state);
  }
  ngOnInit(): void {
  }
}
