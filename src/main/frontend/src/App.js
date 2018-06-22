import React, { Component } from 'react';
import Login from './pages/Login';
import NavBar from './components/navigation/NavBar';
import { Switch, Route, BrowserRouter, Redirect } from 'react-router-dom';
import request from 'superagent';

export default class App extends Component {
  state = {
    redirect: 'test'
  };

  componentDidMount() {
    request.get('/authentication').then(function(res) {
      this.setState({
        redirect:
          res.status !== 200 ? (
            <Redirect exact from="/" to="/login" />
          ) : (
            <Redirect from="/" to="/leave" />
          )
      });
    });
  }

  render() {
    return (
      <BrowserRouter>
        <Switch>
          {this.state.redirect}
          <Route path="/login" component={Login} />
          <Route path="/" component={NavBar} />
        </Switch>
      </BrowserRouter>
    );
  }
}
