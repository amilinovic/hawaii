import React, {Component} from 'react';
import './App.css';
import Login from './pages/Login';
import Dashboard from './pages/Control';
import {Switch, Route, BrowserRouter, Redirect} from 'react-router-dom';

class App extends Component {
  render() {
    return (
      <div className="App">
        <BrowserRouter>
          <Switch>
            <Redirect exact from="/" to="/leave" />
            <Route path="/login" component={Login} />
            <Route path="/" component={Dashboard} />
          </Switch>
        </BrowserRouter>
      </div>
    );
  }
}

export default App;
