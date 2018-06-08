import React, { Component } from 'react';
import styled from 'styled-components';
import { NavLink } from 'react-router-dom';

const Linky = ({ className, children, url }) => (
  <NavLink to={url} className={className}>
    {children}
  </NavLink>
);

const StyledLink = styled(Linky)`
  color: ${props => props.color};
  padding: 10px;
  border-radius: 5px;
  background: #fb4b4f;
  border: 1px solid #3c3c46;
  display: inline-block;
`;

export default class Link extends Component {
  render() {
    return (
      <StyledLink
        color={this.props.color}
        url={this.props.url}
        className={this.props.className}
        children={this.props.children}
      />
    );
  }
}
