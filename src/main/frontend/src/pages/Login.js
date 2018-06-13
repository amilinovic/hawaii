import React, { Component, Fragment } from 'react';
import { NavLink } from 'react-router-dom';
import { StyledLink } from '../components/StyledLink';

export default class Login extends Component {
  render() {
    return (
      <Fragment>
        <h1>Hello Hawaii</h1>
        <NavLink to="/leave">
          <StyledLink>Log in</StyledLink>
        </NavLink>
      </Fragment>
    );
  }
}
