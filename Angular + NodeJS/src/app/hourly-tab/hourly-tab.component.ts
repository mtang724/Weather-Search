/*
TOBE FIXED
  1. legend should be not be clickable -- Fixed
*/
import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { BaseChartDirective } from 'ng2-charts';


@Component({
  selector: 'app-hourly-tab',
  templateUrl: './hourly-tab.component.html',
  styleUrls: ['./hourly-tab.component.css']
})

export class HourlyTabComponent implements OnInit {
  @Input('hourlyWeather') hourlyWeather: any;
  @Input('temperatureData') temperatureData: any;
  @ViewChild(BaseChartDirective, {static: false}) chart: BaseChartDirective;
  data = new Array(24);
  label = new Array(24);
  ylabelStr = 'Fahrenheit';
  xlabelStr = 'Time difference from current hour';
  labelStr = 'temperature';
  niceUpperBound;
  chartOptions;

chartData = [
  {
    backgroundColor: 'rgba(165, 208, 238, 1)',
    hoverBackgroundColor: undefined,
    data: this.data,
    label: this.labelStr
  },
];

  chartLabels = this.label;

  onChartClick(event) {
    this.chart.chart.options.legend.onClick = null;
  }
  constructor() {
  }

  onOptionsSelected(category: string) {
    this.data.length = 0;
    for (let i = 0; i < 24; i++) {
      this.data[i] = this.hourlyWeather[i][category];
      this.label[i] = i;
    }
    const upperBound = Math.max.apply(null, this.data);
    const lowerBound = Math.min.apply(null, this.data);
    const range = this.niceNum(upperBound - lowerBound, false);
    const tickSpacing = this.niceNum(range / (10 - 1), true);
    const niceUpperBound = Math.ceil(upperBound / tickSpacing) * tickSpacing;
    this.chart.chart.options.scales.yAxes[0].ticks.max = niceUpperBound + tickSpacing;
    this.chart.chart.options.legend.onClick = null;
    if (category == 'pressure') {
      this.chart.chart.options.scales.yAxes[0].scaleLabel.labelString = 'Millibars';
    } else if (category == 'humidity') {
      this.chart.chart.options.scales.yAxes[0].scaleLabel.labelString = '% Humidility';
    } else if (category == 'ozone') {
      this.chart.chart.options.scales.yAxes[0].scaleLabel.labelString = 'Dobson Units';
    } else if (category == 'visibility') {
      this.chart.chart.options.scales.yAxes[0].scaleLabel.labelString = 'Miles (Maximum 10)';
    } else if (category == 'windSpeed') {
      this.chart.chart.options.scales.yAxes[0].scaleLabel.labelString = 'Miles per Hour';
    }
    this.chart.chart.data.datasets[0].label = category;
    this.chart.update();
  }

  ngOnInit() {
    for (let i = 0; i < 24; i++) {
      this.data[i] = this.hourlyWeather[i].temperature;
      this.label[i] = i;
    }
    const upperBound = Math.max.apply(null, this.temperatureData);
    const lowerBound = Math.min.apply(null, this.temperatureData);
    const range = this.niceNum(upperBound - lowerBound, false);
    const tickSpacing = this.niceNum(range / (10 - 1), true);
    const niceUpperBound = Math.ceil(upperBound / tickSpacing) * tickSpacing;
    this.niceUpperBound = niceUpperBound + tickSpacing;
    this.chartOptions = {
        scales: {
          yAxes: [{
            scaleLabel: {
              display: true,
              labelString: this.ylabelStr
            },
            ticks: {
              max: this.niceUpperBound,
            }
          }],
          xAxes: [{
            scaleLabel: {
              display: true,
              labelString: this.xlabelStr
            }
          }],
          legend: {
            onClick : null
          }
      }
    };
   }
    niceNum(range, round) {
      const exponent = Math.floor(Math.log10(range));
      const fraction = range / Math.pow(10, exponent);
      let niceFraction;

      if (round) {
        if (fraction < 1.5) {
          niceFraction = 1;
        } else if (fraction < 3) {
          niceFraction = 2;
        } else if (fraction < 7) {
          niceFraction = 5;
        } else {
          niceFraction = 10;
        }
      } else {
        if (fraction <= 1) {
          niceFraction = 1;
        } else if (fraction <= 2) {
          niceFraction = 2;
        } else if (fraction <= 5) {
          niceFraction = 5;
        } else {
          niceFraction = 10;
        }
      }
      return niceFraction * Math.pow(10, exponent);
    }
}
