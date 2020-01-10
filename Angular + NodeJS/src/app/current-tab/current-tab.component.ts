import { Component, OnInit, Input, ViewEncapsulation, Output, EventEmitter } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-current-tab',
  templateUrl: './current-tab.component.html',
  encapsulation: ViewEncapsulation.None,
  styleUrls: ['./current-tab.component.css']
})
export class CurrentTabComponent implements OnInit {
  @Input('currentWeather') currentWeather: any;
  @Input('city') city: string;
  @Input('state') state: string;
  @Input('timezone') timezone: string;
  @Output() linkEmit = new EventEmitter<string>();
  temperature;
  summary;
  humidity;
  pressure;
  windSpeed;
  visibility;
  cloudCover;
  ozone;
  sealLink;
  load = false;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.temperature = Math.round(this.currentWeather.temperature);
    this.summary = this.currentWeather.summary;
    this.humidity = Math.round(this.currentWeather.humidity * 100) / 100;
    this.pressure = Math.round(this.currentWeather.pressure * 100) / 100;
    this.windSpeed = Math.round(this.currentWeather.windSpeed * 100) / 100;
    this.visibility = Math.round(this.currentWeather.visibility * 100) / 100;
    this.cloudCover = Math.round(this.currentWeather.cloudCover * 100) / 100;
    this.ozone = Math.round(this.currentWeather.ozone * 100) / 100;
    let param = new HttpParams();
    param = param.append('state', this.state);
    this.http
      .get('http://weather-nodejs-app.us-east-2.elasticbeanstalk.com/api/stateseal', { params : param })
      .subscribe(responseData => {
        this.sealLink = responseData['image_link'];
        this.load = true;
        this.linkEmit.emit(this.sealLink);
      });
  }

}
