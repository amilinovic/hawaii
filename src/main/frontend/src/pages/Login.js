import React, { Component, Fragment } from 'react';
import styled from 'styled-components';
import Link from '../components/Link';

const Title = styled.h1`
  font-size: 1.5em;
  color: #fb4b4f;
`;

export default class Login extends Component {
  render() {
    return (
      <Fragment>
        <Title>Hello Hawaii</Title>
        <Link url="/" color="white">
          Log in
        </Link>
      </Fragment>
    );
  }
}
