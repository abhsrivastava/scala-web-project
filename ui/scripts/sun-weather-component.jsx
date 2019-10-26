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
        return <table>
            <tbody>
                <tr>
                    <td>Sunrise Time:</td>
                    <td>{this.state.sunrise}</td>
                </tr>
                <tr>
                    <td>Sunset Time:</td>
                    <td>{this.state.sunset}</td>
                </tr>
                <tr>
                    <td>Current Temprature:</td>
                    <td>{this.state.temprature}</td>
                </tr>
                <tr>
                    <td>Number of requests: </td>
                    <td>{this.state.requests}</td>
                </tr>
            </tbody>
        </table>
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
