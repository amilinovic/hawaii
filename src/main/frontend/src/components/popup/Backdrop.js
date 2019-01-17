import React from 'react';
import styled from 'styled-components';

const PopupBackdrop = styled.div`
  display: flex;
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  z-index: 10;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const PopupContainer = styled.div`
  border-radius: 20px;
  background-color: white;
`;

const PopupNavigation = styled.div`
  width: 100%;
  background-color: #e45052;
  border-radius: 20px 20px 0px 0px;
  display: flex;
  flex-direction: row-reverse;
  align-items: center;
  color: white;
  text-align: center;
  justify-content: space-between;
  p {
  }
  button {
    background-color: transparent;
    border: none;
    color: white;
    font-weight: 600;
    padding: 15px 20px;
  }
  span {
    padding: 15px 25px;
  }
`;

const PopupContent = styled.div``;

const Backdrop = props => {
  return (
    <PopupBackdrop>
      <PopupContainer>
        <PopupNavigation>
          <button>x</button>
          <p>{props.title}</p>
          <span />
        </PopupNavigation>
        <PopupContent>{props.children}</PopupContent>
      </PopupContainer>
    </PopupBackdrop>
  );
};

export default Backdrop;
