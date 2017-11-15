# samples-weather-api

Sample applications for using weather api services.

## Getting Started

### Prepare Some API Keys

This project requires some cloud service API keys: OpenWeatherMap API, and Google API.

Create a file with the following contents, for example `secrets.xml`:

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="openweathermap_api_key">Your api key for OpenWeatherMap API</string>
    <string name="google_api_key">Your api key for Google API</string>
</resources>
```

then put it the following path:

```
lib-res/src/main/res/values/secrets.xml
```
