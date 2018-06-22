import React, { Component } from 'react';
import Login from './pages/Login';
import NavBar from './components/navigation/NavBar';
import { Switch, Route, BrowserRouter, Redirect } from 'react-router-dom';
import request from 'superagent';

export default class App extends Component {
  componentDidMount() {
    request.get('/authentication').then(function(res) {
      console.log(res.body);
    });
  }

  render() {
    return (
      <BrowserRouter>
        <Switch>
          <Redirect exact from="/" to="/login" />
          <Route path="/login" component={Login} />
          <Route path="/" component={NavBar} />
        </Switch>
      </BrowserRouter>
    );
  }
}
