import React, { Component, Fragment } from 'react';
import { NavLink } from 'react-router-dom';
import { NavigationLink } from '../components/UI/NavigationLink';

export default class Login extends Component {
  render() {
    return (
      <Fragment>
        <h1>Hello Hawaii</h1>
        <NavLink to="/leave">
          <NavigationLink>Log in</NavigationLink>
        </NavLink>
      </Fragment>
    );
  }
}
