import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule, MatAutocompleteModule, MatIconModule} from '@angular/material';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { SearchFormComponent } from './search-form/search-form.component';
import { StateListComponent } from './search-form/state-list.component';
import { HttpClientModule } from '@angular/common/http';
import { NgbModule, NgbTabsetModule } from '@ng-bootstrap/ng-bootstrap';
import { CurrentTabComponent } from './current-tab/current-tab.component';
import { TabsetsComponent } from './tabsets/tabsets.component';
import { AppRoutingModule } from './app-routing.module';
import { HourlyTabComponent } from './hourly-tab/hourly-tab.component';
import {ChartsModule} from 'ng2-charts';
import { WeeklyTabComponent } from './weekly-tab/weekly-tab.component';
import { ModalModule } from 'ngx-bootstrap/modal';


@NgModule({
  declarations: [
    AppComponent,
    SearchFormComponent,
    StateListComponent,
    CurrentTabComponent,
    TabsetsComponent,
    HourlyTabComponent,
    WeeklyTabComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    MatInputModule,
    HttpClientModule,
    MatAutocompleteModule,
    ReactiveFormsModule,
    NgbModule,
    NgbTabsetModule,
    AppRoutingModule,
    MatIconModule,
    ChartsModule,
    ModalModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
