import React, { Component, Fragment } from 'react';
import { NavLink } from 'react-router-dom';
import styled from 'styled-components';
import { StyledLink } from '../components/StyledLink';

const Title = styled.h1`
  font-size: 1.5em;
  color: #fb4b4f;
`;

export default class Login extends Component {
  render() {
    return (
      <Fragment>
        <Title>Hello Hawaii</Title>
        <NavLink to="/leave">
          <StyledLink>Log in</StyledLink>
        </NavLink>
      </Fragment>
    );
  }
}
