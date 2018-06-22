import React, {Component} from 'react';
import {NavLink} from 'react-router-dom';
import {NavigationLink} from '../components/common/NavigationLink';

export default class Login extends Component {
  render() {
    return (
        <div className="align-items-center justify-content-center d-flex flex-grow-1 flex-column">
        <h1>Hello Hawaii</h1>
        <NavLink to="/leave">
          <NavigationLink>Log in</NavigationLink>
        </NavLink>
        </div>
    );
  }
}
