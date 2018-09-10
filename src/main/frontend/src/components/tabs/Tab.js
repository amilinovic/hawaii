import React, { Component } from 'react';
import styled from 'styled-components';

const TabButton = styled.button`
  flex: 1;
  height: 50px;
  padding: 0px 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 16px;
  cursor: pointer;
  background: transparent;
  outline: none;
  transition: border-color 0.2s ease-in;
  border: none;
  border-bottom: 4px solid ${props => (props.selected ? 'red' : '#eee')};
  &:focus,
  &:active {
    border-bottom: 4px solid ${props => (props.selected ? 'red' : '#dc3545')};
  }
`;

class Tab extends Component {
  handleClick = e => {
    e.preventDefault();
    this.props.handleClick();
  };
  render() {
    return <TabButton onClick={this.handleClick}>{this.props.name}</TabButton>;
  }
}

export default Tab;
