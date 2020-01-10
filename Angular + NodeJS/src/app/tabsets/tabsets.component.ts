import { Component, OnInit, Input } from '@angular/core';
import {NgbTabsetConfig, NgbTabChangeEvent} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-tabsets',
  templateUrl: './tabsets.component.html',
  styleUrls: ['./tabsets.component.css'],
})
export class TabsetsComponent implements OnInit {
  @Input('weatherDetail') weatherDetailStr: string;
  @Input('city') city: string;
  @Input('state') state: string;

  weatherDetail;
  currentWeather;
  timezone;
  hourlyWeather;
  temperatureData = new Array(24);
  weeklyWeather;
  lat;
  lon;
  currentJustify = 'end';
  sealLink;
  starState = 'star_border';
  starColor = 'black';
  constructor(config: NgbTabsetConfig) {
    // customize default values of tabsets used by this component tree
    // config.justify = 'center';
  }

  twitter() {
    let text = 'The current temperature at ' + this.city + ' is ' + this.weatherDetail.currently.temperature + '° F. The weather'
    + ' conditions are ' + this.weatherDetail.currently.summary + '.';
    text = encodeURI(text);
    window.open('https://twitter.com/intent/tweet?text=' + text + '&button_hashtag=CSCI571WeatherSearch');
  }

  accept(sealLink) {
    this.sealLink = sealLink;
  }

  public beforeChange($event: NgbTabChangeEvent) {
    if ($event.nextId === 'tab-preventchange2' || $event.nextId === 'tab-preventchange3') {
      $event.preventDefault();
    }
  }

  star() {
    if(this.starState != 'star'){
      let dataStr = localStorage.getItem('cityData');
      console.log(dataStr);
      if (dataStr == null || dataStr == '') {
        localStorage.setItem('cityData', JSON.stringify([]));
        dataStr = '[]';
      }
      let dataList = JSON.parse(dataStr);
      let data = {
        city : this.city,
        state : this.state,
        sealLink : this.sealLink
      }
      dataList.push(data);
      localStorage.setItem('cityData', JSON.stringify(dataList));
      this.starState = 'star';
      this.starColor = '#F9D556';
    } else {
      this.starState = 'star_border';
      this.starColor = 'black';
      let dataStr = localStorage.getItem('cityData');
      let dataList = JSON.parse(dataStr);
      for(let i = 0; i <dataList.length; i++){
        if (dataList[i]['city'] == this.city){
          dataList.splice(i,1);
          localStorage.setItem('cityData', JSON.stringify(dataList));
        }
      }
    }
}

  ngOnInit() {
    let dataStr = localStorage.getItem('cityData');
    if (dataStr != null && dataStr != '') {
      let dataList = JSON.parse(dataStr);
      for (let i =0; i < dataList.length; i++){
        if (dataList[i].city == this.city){
          this.starState = 'star';
          this.starColor = '#F9D556';
        }
      }
    }
    this.weatherDetail = JSON.parse(this.weatherDetailStr);
    this.hourlyWeather = this.weatherDetail.hourly.data;
    this.timezone = this.weatherDetail.timezone;
    this.currentWeather = this.weatherDetail.currently;
    for (let i = 0; i < 24; i++) {
      this.temperatureData[i] = this.hourlyWeather[i].temperature;
    }
    this.weeklyWeather = this.weatherDetail.daily;
    this.lat = this.weatherDetail.latitude;
    this.lon = this.weatherDetail.longitude;
  }

}
