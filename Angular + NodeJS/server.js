var http  = require('http');
const express = require("express");
const bodyParser = require("body-parser");
const request = require("request");
var path = require('path');

const app = express();
const GOOGLE_API_KEY = "AIzaSyAvc_3J4bHIjgakOKJMkSIpEB-Jo3sUjnI";
const DARK_SKY_API = "355d0440fea5d2f5b2c99379976251ed";
const GOOGLE_CUSTOM_SEARCH_KEY = "003141370008387654934:ww6sgfqjhok";

//Set the base path to the angular-test dist folder
app.use(express.static(path.join(__dirname, 'dist/weather-search')));

app.listen(8081, () => {
    console.log('Server started!');
    console.log('on port 8081');
});

app.use(bodyParser.json());

app.use((req, res, next) => {
  res.setHeader("Access-Control-Allow-Origin", "*");
  res.setHeader(
    "Access-Control-Allow-Headers",
    "Origin, X-Requested-With, Content-Type, Accept"
  );
  res.setHeader(
    "Access-Control-Allow-Methods",
    "GET, POST, PATCH, DELETE, OPTIONS"
  );
  next();
});


app.get("/api/weather", (req, res, next) => {
  var lat = req.query.lat;
  var lon = req.query.lng;
  const dark_sky_url =
    "https://api.forecast.io/forecast/" +
    DARK_SKY_API +
    "/" +
    lat +
    "," +
    lon;
  request(dark_sky_url, function(error, response, body) {
    var data = body;
    data = JSON.parse(data);
    res.json(data);
  });
});

app.get("/api/address", (req, res, next) => {
  const street = req.query.street;
  const city = req.query.city;
  const state = req.query.state;
  const google_geo_url =
    "https://maps.googleapis.com/maps/api/geocode/json?address=" +
    encodeURI(street) +
    "," +
    encodeURI(city) +
    "," +
    encodeURI(state) +
    "&key=" +
    GOOGLE_API_KEY;
  request(google_geo_url, function(error, response, body) {
    var data = body;
    data = JSON.parse(data);
    res.json(data);
  });
});

app.get("/api/current_address", (req, res, next) => {
  const lat = req.query.lat;
  const lng = req.query.lon;
  const dark_sky_url =
    "https://api.forecast.io/forecast/" + DARK_SKY_API + "/" + lat + "," + lng;
  request(dark_sky_url, function(error, response, body) {
    var data = body;
    data = JSON.parse(data);
    res.json(data);
  });
});
app.get("/api/autocomplete", (req, res, next) => {
  const input = req.query.input;
  const auto_complete_url =
    "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" +
    encodeURI(input) +
    "&types=geocode&language=en&key=" +
    GOOGLE_API_KEY;
  request(auto_complete_url, function(error, response, body) {
    var data = body;
    data = JSON.parse(data);
    var text_dic = new Array();
    var predictions = data.predictions;
    var length = 5;
    if (predictions.length < 5) {
      length = predictions.length;
    }
    for (i = 0; i < length; i++) {
      var prediction = predictions[i];
      var main_text = prediction.description;
      text_dic[i] = main_text;
    }
    res.json(text_dic);
  });
});

app.get("/api/stateseal", (req, res, next) => {
  state = req.query.state;
  const google_state_seal_search_url =
    "https://www.googleapis.com/customsearch/v1?q=" +
    state +
    "%20State%20Seal&cx=" +
    GOOGLE_CUSTOM_SEARCH_KEY +
    "&imgSize=huge&imgType=news&num=1&searchType=image&key=" +
    GOOGLE_API_KEY;
  request(google_state_seal_search_url, function(error, response, body) {
    var data = body;
    data = JSON.parse(data);
    const image_link = data.items[0].link;
    res.json({'image_link': image_link});
  });
});

app.get("/api/cityimg", (req, res, next) => {
  city = req.query.city;
  const google_state_seal_search_url =
    "https://www.googleapis.com/customsearch/v1?q=" +
    city +
    "&cx=" +
    GOOGLE_CUSTOM_SEARCH_KEY +
    "&imgSize=huge&imgType=photo&num=8&searchType=image&key=" +
    GOOGLE_API_KEY;
  request(google_state_seal_search_url, function(error, response, body) {
    var data = body;
    data = JSON.parse(data);
    // const image_link = data.items[0].link;
    var dic = new Array();
    for (var i = 0; i < 8; i++){
    	dic[i] = data.items[i].link;;
    }
    res.json(dic);
  });
});

app.get("/api/weather_with_time", (req, res, next) => {
  const lat = req.query.lat;
  const lon = req.query.lon;
  const time = req.query.time;
  const url = "https://api.darksky.net/forecast/"+ DARK_SKY_API+"/"+ lat + "," + lon + "," + time;
  request(url, function(error, response, body) {
    var data = body;
    data = JSON.parse(data);
    res.json(data.currently);
  });
});

// const port = 8081;
// app.set('port', port);
// const server = http.createServer(app);

// server.listen(port);
