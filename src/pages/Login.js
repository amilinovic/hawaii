import React, {Component} from 'react';
import {NavLink} from 'react-router-dom';

export default class Login extends Component {
  render() {
    return (
      <div>
        <div className="background">
          <div className="container">
            <div className="gradient" />
            <div className="tree" />
            <div className="lines" />
          </div>
        </div>
        <h1 className="App-title">Hello Hawaii</h1>
        <div className="link__block">
          <NavLink className="link" to="/">
            Log in
          </NavLink>
        </div>
      </div>
    );
  }
}
