import React from 'react';
import styled from 'styled-components';
import { ifProp } from 'styled-tools';

const HamburgerWrapper = styled.div`
  height: 50px;
  width: 50px;
  cursor: pointer;
`;

const ToggleButton = styled.div`
  height: 2px;
  width: ${ifProp('isMinimized', '25px', '12px')};
  background-color: #fff;
  position: relative;
  top: 50%;
  left: 0;
  margin: auto;
  transition: all 0.3s ease-in-out;
  &:before {
    content: '';
    height: 2px;
    width: ${ifProp('isMinimized', '25px', '6px')};
    box-shadow: 0 -10px 0 0 #fff;
    position: absolute;
    top: 0;
    left: 0;
    transition: all 0.3s ease-in-out;
  }
  &:after {
    content: '';
    height: 2px;
    width: 25px;
    box-shadow: 0 10px 0 0 #fff;
    position: absolute;
    top: 0;
    left: 0;
    transition: all 0.3s ease-in-out;
  }
`;

export const MinimizeIcon = props => {
  return (
    <HamburgerWrapper onClick={props.click}>
      <ToggleButton isMinimized={props.isMinimized} />
    </HamburgerWrapper>
  );
};
