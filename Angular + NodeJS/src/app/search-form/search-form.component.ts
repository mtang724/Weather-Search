import { Component } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { StateListComponent } from './state-list.component';
import { FormControl, NgModel, NgControl } from '@angular/forms';

@Component({
  selector: 'app-search-form',
  templateUrl: './search-form.component.html'
})
export class SearchFormComponent {
  street = '';
  city = '';
  weatherDetail = '';
  progress = '50';
  state = '';
  latText = '';
  lonText = '';
  display = false;
  checked = false;
  result = true;
  favorite = false;
  myControl = new FormControl();
  options: object = new Array();
  submitted = false;
  localStorageLength;
  dataList;
  citySearch = false;
  streetValid = false;
  cityValid = false;
  isDisabled = true;
  inputDisabled = false;
  submitBackgroundColor = "#6E91AA";
  submitColor = "white";
  favoriteBackgroundColor = "white";
  favoriteColor = "grey";
  isError = false;
  constructor(private http: HttpClient) {}

  checkboxClick() {
    this.progress = '50';
    if (this.checked) {
      this.checked = false;
    } else {
      this.checked = true;
    }
    if (this.checked) {
      this.inputDisabled = true;
      this.myControl.disable();
    } else {
      this.inputDisabled = false;
      this.myControl.enable();
    }
    if ((this.street != '' && this.city != '' &&this.state != '') || this.checked){
      this.isDisabled = false;
    } else {
      this.isDisabled = true;
    }
    console.log(this.checked);
  }

  clearAll() {
    this.isDisabled = true;
    this.street = '';
    this.city = '';
    this.weatherDetail = '';
    this.progress = '50';
    this.state = '';
    this.latText = '';
    this.lonText = '';
    this.display = false;
    this.checked = false;
    this.result = true;
    this.favorite = false;
    this.myControl = new FormControl();
    this.options = new Array();
    this.submitted = false;
    this.localStorageLength;
    this.dataList;
    this.citySearch = false;
    this.streetValid = false;
    this.cityValid = false;
    this.isDisabled = true;
    this.inputDisabled = false;
    this.submitBackgroundColor = "#6E91AA";
    this.submitColor = "white";
    this.favoriteBackgroundColor = "white";
    this.favoriteColor = "grey";
    this.isError = false;
  }

  isValidStreet(){
    if (this.street == '') {
      this.streetValid = true;
    } else{
      this.streetValid = false;
    }
  }

  isCityValid() {
    if (this.city == '') {
      this.cityValid = true;
    } else {
      this.cityValid = false;
    }
  }


  onListen(data: any): void {
    this.state = data;
    this.submitted = false;
    this.progress = '50';
    this.result = false;
    if ((this.street != '' && this.city != '' &&this.state != '') || this.checked){
      this.isDisabled = false;
    } else {
      this.isDisabled = true;
    }
  }

  results() {
    this.isError = false;
    this.result = true;
    this.favorite = false;
    this.submitBackgroundColor = "#6E91AA";
    this.submitColor = "white";
    this.favoriteBackgroundColor = "white";
    this.favoriteColor = "grey";
  }

  deleteFavorite(i) {
    this.dataList.splice(i,1);
    localStorage.setItem('cityData', JSON.stringify(this.dataList));
    this.favorites();
  }

  favorites() {
    this.submitted = false;
    this.favorite = true;
    this.result = false;
    this.isError = false;
    this.submitBackgroundColor = "white";
    this.submitColor = "grey";
    this.favoriteBackgroundColor = "#6E91AA";
    this.favoriteColor = "white";
    let dataStr = localStorage.getItem('cityData');
    if (dataStr == null || dataStr == '') {
      this.localStorageLength = 0;
    } else {
      let dataList = JSON.parse(dataStr);
      this.dataList = dataList;
      this.localStorageLength = dataList.length;
    }
  }

  searchFavorite(data) {
    this.submitted = true;
    this.result = true;
    this.favorite = false;
    this.citySearch = true;
    this.progress = '50';
    this.city = data['city'];
    this.state = data['state'];
    this.onSubmit();
  }
  streetChange(){
    this.submitted = false;
    this.result = false;
    this.progress = '50';
    if ((this.street != '' && this.city != '' &&this.state != '') || this.checked){
      this.isDisabled = false;
    } else {
      this.isDisabled = true;
    }
  }
  onChange(inputText: any) {
    this.submitted = false;
    this.result = false;
    this.progress = '50';
    this.city = inputText;
    if ((this.street != '' && this.city != '' &&this.state != '') || this.checked){
      this.isDisabled = false;
    } else {
      this.isDisabled = true;
    }
    let param = new HttpParams();
    param = param.append('input', inputText);
    this.http
      .get('http://weather-nodejs-app.us-east-2.elasticbeanstalk.com/api/autocomplete', { params: param })
      .subscribe(responseData => {
        const autocompleteArray = responseData;
        this.options = autocompleteArray;
      });
  }

  onSubmit() {
    this.submitted = false;
    this.favorite = false;
    this.result = true;
    this.progress = '50';
    if (this.citySearch) {
      let param = new HttpParams();
      param = param.append('street', '');
      param = param.append('city', this.city);
      param = param.append('state', this.state);
      this.result = true;
      this.favorite = false;
      this.submitBackgroundColor = "#6E91AA";
      this.submitColor = "white";
      this.favoriteBackgroundColor = "white";
      this.favoriteColor = "grey";
      this.http
        .get('http://weather-nodejs-app.us-east-2.elasticbeanstalk.com/api/address', {
          params: param
        })
        .subscribe(responseData => {
          console.log(responseData);
          this.display = true;
          if (responseData['status'] == 'ZERO_RESULTS') {
            this.isError = true;
            this.submitted = false;
          } else {
            this.submitted = true;
            const location = responseData['results'][0]['geometry']['location'];
            let param = new HttpParams();
            param = param.append('lat', location.lat);
            param = param.append('lng', location.lng);
            this.http.get('http://weather-nodejs-app.us-east-2.elasticbeanstalk.com/api/weather', {params : param}).subscribe(resData => {
              this.isError = false;
              this.weatherDetail = JSON.stringify(resData);
              this.progress = '100';
              setTimeout("",1000);
              this.display = false;
            });
            }
          });
        this.citySearch = false;
    } else {
    if (this.checked) {
      this.display = true;
      this.progress = '50';
      this.submitBackgroundColor = "#6E91AA";
      this.submitColor = "white";
      this.favoriteBackgroundColor = "white";
      this.favoriteColor = "grey";
      this.http.get('http://ip-api.com/json').subscribe(responseData => {
        const resStr = JSON.stringify(responseData);
        const res = JSON.parse(resStr);
        this.latText = res.lat;
        this.lonText = res.lon;
        this.state = res.region;
        this.city = res.city;
        let param = new HttpParams();
        param = param.append('lat', this.latText);
        param = param.append('lon', this.lonText);
        this.http
          .get('http://weather-nodejs-app.us-east-2.elasticbeanstalk.com/api/current_address', {
            params: param
          })
          .subscribe(resData => {
            this.submitted = true;
            this.isError = false;
            this.weatherDetail = JSON.stringify(resData);
            // this.display = false;
            setTimeout(()=>{
              this.progress = '100';
              this.display=false;
            },500);
          });
      });
    } else {
      let param = new HttpParams();
      param = param.append('street', this.street);
      param = param.append('city', this.city);
      param = param.append('state', this.state);
      this.submitBackgroundColor = "#6E91AA";
      this.submitColor = "white";
      this.favoriteBackgroundColor = "white";
      this.favoriteColor = "grey";
      this.http
        .get('http://weather-nodejs-app.us-east-2.elasticbeanstalk.com/api/address', {
          params: param
        })
        .subscribe(responseData => {
          console.log(responseData);
          if (responseData['status'] == 'ZERO_RESULTS') {
            this.isError = true;
            this.submitted = false;
          } else {
            this.submitted = true;
            this.display = true;
            const location = responseData['results'][0]['geometry']['location'];
            let param = new HttpParams();
            param = param.append('lat', location.lat);
            param = param.append('lng', location.lng);
            this.http.get('http://weather-nodejs-app.us-east-2.elasticbeanstalk.com/api/weather', {params : param}).subscribe(resData => {
            this.isError = false;
            this.weatherDetail = JSON.stringify(resData);
            this.progress = '100';
            setTimeout("",1000);
            this.display = false;
            });
            }
          });
        }
    }
  }
}
