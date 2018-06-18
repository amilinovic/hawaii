import React, { Component } from 'react';
import Login from './pages/Login';
import NavBar from './components/navigation/NavBar';
import { Switch, Route, BrowserRouter, Redirect } from 'react-router-dom';
import { Provider } from 'react-redux';
import store from './Store';

export default class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <BrowserRouter>
          <Switch>
            <Redirect exact from="/" to="/leave" />
            <Route path="/login" component={Login} />
            <Route path="/" component={NavBar} />
          </Switch>
        </BrowserRouter>
      </Provider>
    );
  }
}
