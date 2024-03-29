import React from 'react';
import styled from 'styled-components';

const ButtonContainer = styled.button`
  background-color: #fb4b4f;
  color: white;
  padding: 5px 10px 5px 10px;
  border-radius: 5px;
  cursor: pointer;
  transition: font-size 100ms ease;
  border: 1px solid #fb4b4f;
  transition: 200ms ease;

  &:hover {
    border-color: #323234;
  }
`;

const Button = props => {
  return <ButtonContainer onClick={props.click}>{props.title}</ButtonContainer>;
};

export default Button;
