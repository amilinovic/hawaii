import React, { Component } from 'react';
import Login from './pages/Login';
import NavBar from './components/navigation/NavBar';
import { Switch, Route, BrowserRouter, Redirect } from 'react-router-dom';
import request from 'superagent';

export default class App extends Component {
  state = {
    redirect: null
  };

  componentDidMount() {
    request
      .get('/authentication')
      .then(res => {
        this.setState({
          redirect: <Redirect exact from="/" to="/leave" />
        });
      })
      .catch(err => {
        this.setState({
          redirect: <Redirect exact from="/" to="/login" />
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
