import React, {Component} from 'react';
import './App.css';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import {Switch, Route, BrowserRouter} from 'react-router-dom';

class App extends Component {
  render() {
    return (
      <div className="App">
        <BrowserRouter>
          <Switch>
            <Route path="/login" component={Login} />
            <Route path="/" component={Dashboard} />
          </Switch>
        </BrowserRouter>
      </div>
    );
  }
}

export default App;
