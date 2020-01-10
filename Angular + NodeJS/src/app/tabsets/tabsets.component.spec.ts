import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TabsetsComponent } from './tabsets.component';

describe('TabsetsComponent', () => {
  let component: TabsetsComponent;
  let fixture: ComponentFixture<TabsetsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TabsetsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TabsetsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
