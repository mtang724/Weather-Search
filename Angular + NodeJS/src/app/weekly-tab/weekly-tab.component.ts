import { Component, Input, ViewChild, AfterViewInit } from '@angular/core';
import * as CanvasJS from '../../assets/canvasjs.min.js';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { HttpClient, HttpParams } from '@angular/common/http';


@Component({
  selector: 'app-weekly-tab',
  templateUrl: './weekly-tab.component.html',
  styleUrls: ['./weekly-tab.component.css'],
})
export class WeeklyTabComponent implements AfterViewInit {
  @Input('weeklyWeather') weeklyWeather: object;
  @Input('city') city: string;
  @Input('lat') lat: string;
  @Input('lon') lon: string;
  @ViewChild('template', {static: false}) template;
  datapoints = new Array();
  contentStr;
  dataTitle;
  temperature;
  summary;
  icon;
  precipIntensity;
  precipProbability;
  windSpeed;
  humidity;
  visibility;
  load = false;

  modalRef: BsModalRef;
  constructor(private modalService: BsModalService, private http: HttpClient) {}

  icon_to_url(icon) {
    let icon_url = '';
    switch(icon) {
      case 'clear-day':
      case 'clear-night':
        icon_url = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/sun-512.png';
        break;
      case 'rain':
        icon_url = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/rain-512.png';
        break;
      case 'snow':
        icon_url = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/snow-512.png';
        break;
      case 'sleet':
        icon_url = 'https://cdn3.iconfinder.com/data/icons/w eather-344/142/lightning-512.png';
        break;
      case 'wind':
        icon_url = 'https://cdn4.iconfinder.com/data/icons/the-weather-is-nice-today/64/weather_10512.png';
        break;
      case 'fog':
        icon_url = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/cloudy-512.png';
        break;
      case 'cloudy':
        icon_url = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/cloud-512.png';
        break;
      case 'partly-cloudy-day':
      case 'partly-cloudy-night':
        icon_url = 'https://cdn3.iconfinder.com/data/icons/weather-344/142/sunny-512.png';
        break;
      default:
        icon_url = ''
        break;
    }
    return icon_url;
  }

  ngAfterViewInit() {
    for (let i = 0; i < 8; i ++) {
      const date = new Date(this.weeklyWeather['data'][i]['time']*1000);
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();
      let data = {
        x : 8 - i,
        y : [this.weeklyWeather['data'][i]['temperatureLow'], this.weeklyWeather['data'][i]['temperatureHigh']],
        label : day + '/' + month + '/' + year,
      };
      this.datapoints.push(data);
    }
    console.log(this.datapoints);
    let chart = new CanvasJS.Chart("chartContainer", {
      animationEnabled: true,
      exportEnabled: false,
      title: {
        text: "Weekly Weather"
      },
      axisX: {
        title: "Days"
      },
      axisY: {
        gridThickness: 0,
        includeZero: false,
        title: "Temperature in Fahrenheit",
        interval: 10,
      },
      legend: {
        verticalAlign: "top"
      },
      dataPointWidth: 12,
      data: [{
        click: (e) => {
          this.contentStr = 8 - e.dataPoint.x;
          this.dataTitle = e.dataPoint.label;
          const time = this.weeklyWeather['data'][this.contentStr]['time'];
          let param = new HttpParams();
          param = param.append('time', time);
          param = param.append('lat', this.lat);
          param = param.append('lon', this.lon);
          this.http
          .get('http://weather-nodejs-app.us-east-2.elasticbeanstalk.com/api/weather_with_time', {params: param})
          .subscribe(resData => {
            console.log(resData);
            this.temperature = Math.round(resData['temperature']);
            this.summary = resData['summary'];
            this.icon = this.icon_to_url(resData['icon']);
            this.precipIntensity = Math.round(resData['precipIntensity'] * 100) / 100;
            this.precipProbability = (Math.round(resData['precipProbability'] * 100) / 100) * 100;
            this.windSpeed = Math.round(resData['windSpeed'] * 100) / 100;
            this.humidity = (Math.round(resData['humidity'] * 100) / 100) * 100;
            this.visibility = Math.round(resData['visibility'] * 100) / 100;
            this.load = true;
            this.modalRef = this.modalService.show(this.template, {class: 'modal-dialog-centered', ignoreBackdropClick: true, keyboard: false});
          });
        },
        type: "rangeBar",
        showInLegend: true,
        yValueFormatString: "#0",
        indexLabel: "{y[#index]}",
        legendText: "Day wise temperature range",
        toolTipContent: "<b>{label}</b>: {y[0]} to {y[1]}",
        color: 'rgba(165, 208, 238, 1)',
        dataPoints: this.datapoints
      }]
    });
    chart.render();
  }

}
