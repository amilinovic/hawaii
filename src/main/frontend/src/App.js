import React, { Component, Fragment } from 'react';
import Login from './pages/Login';
import Dashboard from './pages/Control';
import { Switch, Route, BrowserRouter, Redirect } from 'react-router-dom';

export default class App extends Component {
  render() {
    return (
      <Fragment>
        <BrowserRouter>
          <Switch>
            <Redirect exact from="/" to="/leave" />
            <Route path="/login" component={Login} />
            <Route path="/" component={Dashboard} />
          </Switch>
        </BrowserRouter>
      </Fragment>
    );
  }
}
