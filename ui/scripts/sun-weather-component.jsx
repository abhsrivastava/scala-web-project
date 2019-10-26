import React from 'react';
import axios from 'axios';

class SunWeatherComponent extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            sunrise: null,
            sunset: null,
            temprature: null,
            requests: null
        };
    }
    render() {
        return <div>
            <div>sunrise time: {this.state.sunrise} </div>
            <div>sunset time: {this.state.sunset}</div>
            <div>current temprature: {this.state.temprature}</div>
            <div>requests: {this.state.requests}</div>
        </div>
    }
    componentDidMount() {
        axios.get('/data').then((response) => {
            const json = response.data
            this.setState({
                sunrise: json.sunInfo.sunrise,
                sunset: json.sunInfo.sunset,
                temprature: json.temprature,
                requests: json.requests
            });
        })
    }
}

export default SunWeatherComponent;
