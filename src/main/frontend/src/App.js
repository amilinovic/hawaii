import React, { Component } from 'react';
import Login from './pages/Login';
import NavBar from './components/navigation/NavBar';
import { Switch, Route, BrowserRouter, Redirect } from 'react-router-dom';

export default class App extends Component {
  render() {
    return (
      <BrowserRouter>
        <Switch>
          <Redirect exact from="/" to="/leave" />
          <Route path="/login" component={Login} />
          <Route path="/" component={NavBar} />
        </Switch>
      </BrowserRouter>
    );
  }
}
